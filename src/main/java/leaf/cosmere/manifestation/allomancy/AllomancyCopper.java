/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.manifestation.allomancy;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.EffectsRegistry;
import leaf.cosmere.utils.helpers.EffectsHelper;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.LootingEnchantFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.predicates.LootItemKilledByPlayerCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceWithLootingCondition;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;

import static leaf.cosmere.utils.helpers.EntityHelper.getLivingEntitiesInRange;

public class AllomancyCopper extends AllomancyBase
{
	public AllomancyCopper(Metals.MetalType metalType)
	{
		super(metalType);
	}

	@Override
	protected void performEffect(ISpiritweb data)
	{
		LivingEntity livingEntity = data.getLiving();
		boolean isActiveTick = livingEntity.tickCount % 20 == 0;

		//Hides Allomantic Pulses
		if (isActiveTick)
		{
			MobEffectInstance newEffect = EffectsHelper.getNewEffect(
					EffectsRegistry.ALLOMANTIC_COPPER.get(),
					Mth.fastFloor(
							getStrength(data, false)
					)
			);

			List<LivingEntity> entitiesToApplyEffect = getLivingEntitiesInRange(livingEntity, getRange(data), true);

			for (LivingEntity e : entitiesToApplyEffect)
			{
				e.addEffect(newEffect);
			}
		}
	}
}
