/*
 * File created ~ 26 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.loot;

import com.google.gson.JsonObject;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.LogHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
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
import net.minecraftforge.common.loot.GlobalLootModifierSerializer;
import net.minecraftforge.common.loot.LootModifier;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Map;


//Thank you Curios for the example !
// modified to work with the chromium feruchemy effects.

public class FortuneBonusModifier extends LootModifier
{
	protected FortuneBonusModifier(LootItemCondition[] conditions)
	{
		super(conditions);
	}

	@Nonnull
	protected List<ItemStack> doApply(List<ItemStack> generatedLoot, LootContext context)
	{
		final String hasCosmereFortuneBonus = "HasCosmereFortuneBonus";

		ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);

		if (tool != null && (!tool.hasTag() || tool.getTag() == null || !tool.getTag().getBoolean(hasCosmereFortuneBonus)))
		{
			Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
			BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);

			if (blockState != null && entity instanceof LivingEntity player)
			{

				MobEffectInstance storingLuckEffect = player.getEffect(EffectsRegistry.STORING_EFFECTS.get(Metals.MetalType.CHROMIUM).get());
				MobEffectInstance tappingLuckEffect = player.getEffect(EffectsRegistry.TAPPING_EFFECTS.get(Metals.MetalType.CHROMIUM).get());

				//bonus for tapping amplifier.
				int totalFortuneBonus = 0;
				if (tappingLuckEffect != null)
				{
					totalFortuneBonus = tappingLuckEffect.getAmplifier() + 1;
				}

				//minus off the unlucky effect amplifier

				if (storingLuckEffect != null)
				{
					totalFortuneBonus -= (storingLuckEffect.getAmplifier() + 1);
				}

				if (totalFortuneBonus != 0)
				{
					ItemStack fakeTool = tool.isEmpty() ? new ItemStack(Items.BARRIER) : tool.copy();

					fakeTool.getOrCreateTag().putBoolean(hasCosmereFortuneBonus, true);

					Map<Enchantment, Integer> enchantments = EnchantmentHelper.getEnchantments(fakeTool);
					enchantments.put(Enchantments.BLOCK_FORTUNE, EnchantmentHelper.getItemEnchantmentLevel(Enchantments.BLOCK_FORTUNE, fakeTool) + totalFortuneBonus);

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

	public static class Serializer extends GlobalLootModifierSerializer<FortuneBonusModifier>
	{
		public Serializer()
		{
			LogHelper.info("Fortune bonus modifier setting up");
		}

		public FortuneBonusModifier read(ResourceLocation location, JsonObject object, LootItemCondition[] conditions)
		{
			return new FortuneBonusModifier(conditions);
		}

		public JsonObject write(FortuneBonusModifier instance)
		{
			return this.makeConditions(instance.conditions);
		}
	}
}
