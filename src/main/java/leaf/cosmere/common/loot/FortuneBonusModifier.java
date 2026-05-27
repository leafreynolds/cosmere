/*
 * File updated ~ 28 - 3 - 2026 ~ Leaf
 */

package leaf.cosmere.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;


//Thank you Curios for the example !
// modified to work with the chromium feruchemy effects.

public class FortuneBonusModifier extends LootModifier
{
	protected FortuneBonusModifier(LootItemCondition[] conditions)
	{
		super(conditions);
	}

	public static final Supplier<MapCodec<FortuneBonusModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.mapCodec(inst -> codecStart(inst).apply(inst, FortuneBonusModifier::new)));

	@Override
	public MapCodec<? extends IGlobalLootModifier> codec()
	{
		return CODEC.get();
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		final String hasCosmereFortuneBonus = "HasCosmereFortuneBonus";
		MinecraftServer server = context.getLevel().getServer();
		Registry<Enchantment> enchantmentRegistry = server.registryAccess().registryOrThrow(Registries.ENCHANTMENT);
		Vec3 origin = context.getParamOrNull(LootContextParams.ORIGIN);

		// Block entities (e.g. shulker boxes) copy their contents via CopyNbtFunction
		// at the time the original loot context is built — before the block entity is
		// invalidated. Re-running the loot table here would read a stale/removed block
		// entity and produce an empty drop. Since fortune doesn't affect block-entity
		// blocks in vanilla, skip early.

		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
		BlockEntity blockEntity = context.getParamOrNull(LootContextParams.BLOCK_ENTITY);

		CustomData toolData = tool == null ? null : tool.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);
		boolean alreadyMarked = toolData != null && toolData.copyTag().getBoolean(hasCosmereFortuneBonus);

		if (blockEntity == null && tool != null && !alreadyMarked)
		{
			Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
			BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);

			if (blockState != null && entity instanceof LivingEntity livingEntity)
			{
				int totalFortuneBonus = (int) EntityHelper.getAttributeValue(livingEntity, AttributesRegistry.COSMERE_FORTUNE.getHolder());

				//bonus for tapping amplifier.

				if (totalFortuneBonus != 0)
				{
					ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();

					CustomData.update(DataComponents.CUSTOM_DATA, fakeTool, t -> t.putBoolean(hasCosmereFortuneBonus, true));

					Holder<Enchantment> fortune = enchantmentRegistry.getHolderOrThrow(Enchantments.FORTUNE);

					int existingLevel = fakeTool.getEnchantmentLevel(fortune);
					fakeTool.enchant(fortune, existingLevel + totalFortuneBonus);

					LootParams lootparams = (new LootParams.Builder(context.getLevel()))
							.withParameter(LootContextParams.ORIGIN, origin)
							.withParameter(LootContextParams.THIS_ENTITY, entity)
							.withParameter(LootContextParams.BLOCK_STATE, blockState)
							.withParameter(LootContextParams.TOOL, fakeTool)
							.create(LootContextParamSets.BLOCK);


					LootTable lootTable = server.reloadableRegistries().getLootTable(blockState.getBlock().getLootTable());

					return lootTable.getRandomItems(lootparams);
				}
			}
		}

		// hopefully this takes care of entity loot
		Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
		DamageSource damageSource = context.getParamOrNull(LootContextParams.DAMAGE_SOURCE);
		if (damageSource != null && damageSource.getEntity() instanceof LivingEntity sourceLiving && entity instanceof Mob target)
		{
			ItemStack weapon = sourceLiving.getWeaponItem();
			CustomData weaponData = weapon.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY);

			if (!weaponData.copyTag().getBoolean(hasCosmereFortuneBonus))
			{
				int totalLootingBonus = (int) EntityHelper.getAttributeValue(sourceLiving, AttributesRegistry.COSMERE_FORTUNE.getHolder());

				if (totalLootingBonus > 0)
				{
					ItemStack fakeWeapon = weapon.isEmpty() ? new ItemStack(Items.STICK) : weapon.copy(); // why a stick? because... it could be fire (but really it's arbitrary)
					CustomData.update(DataComponents.CUSTOM_DATA, fakeWeapon, t -> t.putBoolean(hasCosmereFortuneBonus, true));

					Holder<Enchantment> looting = enchantmentRegistry.getHolderOrThrow(Enchantments.LOOTING);
					int existingLevel = fakeWeapon.getEnchantmentLevel(looting);
					fakeWeapon.enchant(looting, existingLevel + totalLootingBonus); // todo: might wanna check this in case it's too much

					LootParams lootParams = (new LootParams.Builder(context.getLevel()))
							.withParameter(LootContextParams.THIS_ENTITY, entity)
							.withParameter(LootContextParams.ORIGIN, origin != null ? origin : entity.position())
							.withParameter(LootContextParams.DAMAGE_SOURCE, damageSource)
							.withOptionalParameter(LootContextParams.ATTACKING_ENTITY, sourceLiving)
							.withOptionalParameter(LootContextParams.DIRECT_ATTACKING_ENTITY, damageSource.getDirectEntity())
							.withOptionalParameter(LootContextParams.TOOL, fakeWeapon)
							.create(LootContextParamSets.ENTITY);

					target.getLootTable();
					LootTable lootTable = server.reloadableRegistries().getLootTable(target.getLootTable());
					return lootTable.getRandomItems(lootParams);
				}
			}
		}

		//otherwise return the context that was passed in. no modification needed.
		return generatedLoot;
	}
}
