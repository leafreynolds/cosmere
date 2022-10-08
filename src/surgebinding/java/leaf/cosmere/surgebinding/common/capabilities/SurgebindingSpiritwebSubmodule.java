/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;

import java.util.stream.Stream;

public class SurgebindingSpiritwebSubmodule implements ISpiritwebSubmodule
{
	//stormlight stored

	private int stormlightStored = 0;


	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		//tick stormlight
		if (stormlightStored > 0)
		{
			final LivingEntity livingEntity = spiritweb.getLiving();
			if (livingEntity.tickCount % 100 == 0)
			{
				//todo decide what's appropriate for reducing stormlight
				//maybe reducing cost based on how many ideals they have sworn?
				//todo config drain rate
				adjustStormlight(-1, true);
			}

			//special effects for wearing shardplate.
			if (livingEntity.tickCount % 20 == 0)
			{
				ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
				ItemStack breastplate = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
				ItemStack leggings = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
				ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);

				//check wearing full suit of armor
				if (Stream.of(helmet, breastplate, leggings, boots).allMatch(armorStack -> !armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem))
				{
					//check armor matches same material
					for (ShardplateArmorMaterial material : ShardplateArmorMaterial.values())
					{
						if (Stream.of(helmet, breastplate, leggings, boots).allMatch((armorStack -> ((ArmorItem) armorStack.getItem()).getMaterial() == material)))
						{
							int amplifier = material == ShardplateArmorMaterial.DEADPLATE ? 0 : 1;

							livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, amplifier));
							livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DIG_SPEED, amplifier));
							livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DAMAGE_BOOST, amplifier));
							livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, amplifier));

							stormlightStored--;
							break;
						}
					}
				}
			}
		}
	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{
		stormlightStored = spiritweb.getCompoundTag().getInt("stored_stormlight");
	}

	@Override
	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();

		compoundTag.putInt("stored_stormlight", stormlightStored);
	}

	public int getStormlight()
	{
		return stormlightStored;
	}

	public boolean adjustStormlight(int amountToAdjust, boolean doAdjust)
	{
		int stormlight = getStormlight();

		final int newSLValue = stormlight + amountToAdjust;
		if (newSLValue >= 0)
		{
			if (doAdjust)
			{
				stormlightStored = newSLValue;
			}

			return true;
		}

		return false;
	}
}
