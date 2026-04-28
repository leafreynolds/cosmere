/*
 * File updated ~ 20 - 12 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.manifestation;

import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Roshar;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.surgebinding.common.capabilities.SurgebindingSpiritwebSubmodule;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.registries.SurgebindingManifestations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

import java.util.HashMap;

public class SurgeProgression extends SurgebindingManifestation
{
	public SurgeProgression(Roshar.Surges surge)
	{
		super(surge);
	}

	private static final HashMap<Block, Block> progressionBlockMap = new HashMap<>();


	//alter growth and healing
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (event.getTarget() instanceof LivingEntity eventTarget)
		{
			final float eventTargetHealth = eventTarget.getHealth();
			final float eventTargetMaxHealth = eventTarget.getMaxHealth();
			int healthMissing = Mth.floor(eventTargetMaxHealth - eventTargetHealth);

			if (healthMissing > 0)
			{
				SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
				{
					if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).get()) &&
							SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).getManifestation().isActive(iSpiritweb))
					{
						SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
						SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

						final int stormlightHealingCostMultiplier = SurgebindingConfigs.SERVER.PROGRESSION_HEAL_COST.get();
						if (submodule.adjustStormlight(-(healthMissing * stormlightHealingCostMultiplier), true))
						{
							heal(eventTarget, eventTargetMaxHealth);
						}
						else
						{
							final int affordableHealth = submodule.getStormlight() / stormlightHealingCostMultiplier;
							if (affordableHealth > 0 && submodule.adjustStormlight(-submodule.getStormlight(), true))
							{
								heal(eventTarget, eventTargetHealth + affordableHealth);
							}
						}
					}
				});
			}
		}

		if (event.getTarget() instanceof AgeableMob ageableMob && ageableMob.level() instanceof ServerLevel)
		{
			if (ageableMob.isBaby())
			{
				SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
				{
					if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).get()) &&
							SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).getManifestation().isActive(iSpiritweb))
					{
						int ageUpAmount = (int) Math.floor(-(ageableMob.getAge() / 20D) * 0.1);       // get age in seconds, then 10% of that
						SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
						SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

						final int stormlightAgeUpCostMultiplier = SurgebindingConfigs.SERVER.PROGRESSION_AGE_UP_COST.get();
						if (submodule.adjustStormlight(-(stormlightAgeUpCostMultiplier), true))
						{
							ageUp(ageableMob, ageUpAmount);
						}
						else
						{
							final int affordableAge = (int) ((float) submodule.getStormlight() / (float) (stormlightAgeUpCostMultiplier)) * ageUpAmount;
							if (affordableAge > 0 && submodule.adjustStormlight(-submodule.getStormlight(), true))
							{
								ageUp(ageableMob, affordableAge);
							}
						}
					}
				});
			}
		}
	}

	public static void heal(LivingEntity livingEntity, float setHealthTo)
	{
		livingEntity.setHealth(setHealthTo);

		if (livingEntity.level() instanceof ServerLevel serverLevel)
		{
			serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
					livingEntity.getX(),
					livingEntity.getY() + livingEntity.getBbHeight() / 2.0D,
					livingEntity.getZ(),
					20,
					livingEntity.getBbWidth() / 2.0D,
					livingEntity.getBbHeight() / 2.0D,
					livingEntity.getBbWidth() / 2.0D,
					0.02D);
		}

		//this gets very annoying quick
		/*livingEntity.level.playSound(
				(Player) null,
				livingEntity.getX(),
				livingEntity.getY(),
				livingEntity.getZ(),
				SoundEvents.PLAYER_LEVELUP,
				livingEntity.getSoundSource(),
				0.25F,
				1.0F);*/
	}

	public static void ageUp(AgeableMob ageableMob, int ageUpAmount)
	{
		ageableMob.ageUp(ageUpAmount);

		if (ageableMob.level() instanceof ServerLevel serverLevel)
		{
			serverLevel.sendParticles(ParticleTypes.HAPPY_VILLAGER,
					ageableMob.getX(),
					ageableMob.getY() + ageableMob.getBbHeight() / 2.0D,
					ageableMob.getZ(),
					20,
					ageableMob.getBbWidth() / 2.0D,
					ageableMob.getBbHeight() / 2.0D,
					ageableMob.getBbWidth() / 2.0D,
					0.02D);
		}
	}

	//bonemeal crops
	public static void onBlockInteract(PlayerInteractEvent.RightClickBlock event)
	{
		if (!event.getEntity().getMainHandItem().isEmpty())
		{
			return;
		}

		final BlockPos blockPos = event.getHitVec().getBlockPos();
		final BlockState blockState = event.getLevel().getBlockState(blockPos);
		if (blockState.getBlock() instanceof BonemealableBlock bonemealableBlock)
		{
			SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
			{
				if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).get()) && SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).getManifestation().isActive(iSpiritweb))
				{
					SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
					SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

					final int stormlightHealingCostMultiplier = SurgebindingConfigs.SERVER.PROGRESSION_BONEMEAL_COST.get();
					if (submodule.adjustStormlight(-stormlightHealingCostMultiplier, true))
					{
						if (event.getLevel() instanceof ServerLevel serverLevel)
						{
							bonemealableBlock.performBonemeal(serverLevel, serverLevel.random, blockPos, blockState);
						}
						else
						{
							BoneMealItem.addGrowthParticles(event.getLevel(), blockPos, 0);
						}
					}
				}
			});
		}
		else
		{
			if (progressionBlockMap.isEmpty())
			{
				progressionBlockMap.put(Blocks.DIRT, Blocks.GRASS_BLOCK);
				progressionBlockMap.put(Blocks.COARSE_DIRT, Blocks.DIRT);
				progressionBlockMap.put(Blocks.COBBLESTONE, Blocks.MOSSY_COBBLESTONE);
				progressionBlockMap.put(Blocks.COBBLESTONE_SLAB, Blocks.MOSSY_COBBLESTONE_SLAB);
				progressionBlockMap.put(Blocks.COBBLESTONE_STAIRS, Blocks.MOSSY_COBBLESTONE_STAIRS);
				progressionBlockMap.put(Blocks.COBBLESTONE_WALL, Blocks.MOSSY_COBBLESTONE_WALL);
			}

			final Block targetBlockType = progressionBlockMap.getOrDefault(blockState.getBlock(), null);

			if (targetBlockType != null)
			{
				SpiritwebCapability.get(event.getEntity()).ifPresent(iSpiritweb ->
				{
					if (iSpiritweb.hasManifestation(SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).get()) && SurgebindingManifestations.SURGEBINDING_POWERS.get(Roshar.Surges.PROGRESSION).getManifestation().isActive(iSpiritweb))
					{
						SpiritwebCapability playerSpiritweb = (SpiritwebCapability) iSpiritweb;
						SurgebindingSpiritwebSubmodule submodule = (SurgebindingSpiritwebSubmodule) playerSpiritweb.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);

						final int stormlightBonemealCostMultiplier = SurgebindingConfigs.SERVER.PROGRESSION_BONEMEAL_COST.get();
						if (submodule.adjustStormlight(-stormlightBonemealCostMultiplier, true))
						{
							if (event.getLevel() instanceof ServerLevel)
							{
								BlockState newState = targetBlockType.defaultBlockState();

								// copy over all block properties
								for (Property<?> prop : blockState.getProperties())
								{
									if (newState.hasProperty(prop))
									{
										newState = copyProperty(blockState, newState, prop);
									}
								}

								event.getLevel().setBlock(blockPos, newState, 3);
							}
							else
							{
								BoneMealItem.addGrowthParticles(event.getLevel(), blockPos, 0);
							}
						}
					}
				});
			}
		}
	}

	// forge forums were useful for once
	// borrowed from https://forums.minecraftforge.net/topic/117047-copy-all-property-values-from-one-blockstate-to-another/
	// required because the compiler can't otherwise be sure ? and ? are the same type
	private static <T extends Comparable<T>> BlockState copyProperty(BlockState from, BlockState to, Property<T> property)
	{
		return to.setValue(property, from.getValue(property));
	}
}
