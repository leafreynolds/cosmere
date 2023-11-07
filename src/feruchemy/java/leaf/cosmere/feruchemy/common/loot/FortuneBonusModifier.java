/*
 * File updated ~ 23 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import leaf.cosmere.api.helpers.EntityHelper;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.function.Supplier;


//Thank you Curios for the example !
// modified to work with the chromium feruchemy effects.

public class FortuneBonusModifier extends LootModifier
{
	protected FortuneBonusModifier(LootItemCondition[] conditions)
	{
		super(conditions);
	}

	public static final Supplier<Codec<FortuneBonusModifier>> CODEC = Suppliers.memoize(() ->
			RecordCodecBuilder.create(inst -> codecStart(inst).apply(inst, FortuneBonusModifier::new)));

	@Override
	public Codec<? extends IGlobalLootModifier> codec()
	{
		return CODEC.get();
	}

	@Override
	protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context)
	{
		final String hasCosmereFortuneBonus = "HasCosmereFortuneBonus";

		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

		if (tool != null && (!tool.hasTag() || tool.getTag() == null || !tool.getTag().getBoolean(hasCosmereFortuneBonus)))
		{
			Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
			BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);

			if (blockState != null && entity instanceof LivingEntity livingEntity)
			{
				int totalFortuneBonus = (int) EntityHelper.getAttributeValue(livingEntity, AttributesRegistry.COSMERE_FORTUNE.getAttribute());

				//bonus for tapping amplifier.

				if (totalFortuneBonus != 0)
				{
					ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();

					fakeTool.getOrCreateTag().putBoolean(hasCosmereFortuneBonus, true);

					Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(fakeTool);
					enchantments.put(Enchantments.BLOCK_FORTUNE, EnchantmentHelper.getTagEnchantmentLevel(Enchantments.BLOCK_FORTUNE, fakeTool) + totalFortuneBonus);

					EnchantmentHelper.setEnchantments(enchantments, fakeTool);

					LootContext.Builder builder = new LootContext.Builder(context);
					builder.withParameter(LootContextParams.TOOL, fakeTool);

					LootContext newContext = builder.create(LootContextParamSets.BLOCK);
					LootTable lootTable = context.getLevel().getServer().getLootTables().get(blockState.getBlock().getLootTable());

					return lootTable.getRandomItems(newContext);
				}
			}
		}

		//otherwise return the context that was passed in. no modification needed.
		return generatedLoot;
	}
}
