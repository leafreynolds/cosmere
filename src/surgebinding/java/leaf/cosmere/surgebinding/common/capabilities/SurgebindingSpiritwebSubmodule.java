/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.capabilities;

import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.items.CapWrapper;
import leaf.cosmere.surgebinding.common.capabilities.ideals.RadiantStateManager;
import leaf.cosmere.surgebinding.common.config.SurgebindingConfigs;
import leaf.cosmere.surgebinding.common.items.GemstoneItem;
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import leaf.cosmere.surgebinding.common.manifestation.SurgeProgression;
import leaf.cosmere.surgebinding.common.registries.SurgebindingDimensions;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.items.wrapper.PlayerInvWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class SurgebindingSpiritwebSubmodule implements ISpiritwebSubmodule
{

	//stormlight stored
	private int stormlightStored = 0;

	private boolean herald = false;

	//Since I'm referencing it so often. For readability if nothing else
	int maxPlayerStormlight = SurgebindingConfigs.SERVER.PLAYER_MAX_STORMLIGHT.get();

	//Somewhat temporary value. How fast stormlight is drawn in/breathed out. Maybe make this a config value?
	int drawSpeed = SurgebindingConfigs.SERVER.PLAYER_DRAW_SPEED.get();

	//a little ew, I'd rather this in an enum utils, but it's the only place that needs it
	public static final ShardplateArmorMaterial[] ARMOR_MATERIALS = ShardplateArmorMaterial.values();
	RadiantStateManager idealsManager = new RadiantStateManager();
	private ISpiritweb spiritweb;

	public static SurgebindingSpiritwebSubmodule getSubmodule(ISpiritweb data)
	{
		return (SurgebindingSpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SURGEBINDING);
	}

	public boolean isHerald()
	{
		return herald;
	}

	public boolean isOathed()
	{
		//boolean anySurges = SurgebindingManifestations.SURGEBINDING_POWERS.values().stream().anyMatch((manifestation -> spiritweb.hasManifestation(manifestation.getManifestation())));
		return idealsManager.getOrder() != null;
	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{
		this.spiritweb = spiritweb;
		final CompoundTag compoundTag = spiritweb.getCompoundTag();
		stormlightStored = compoundTag.getInt("stored_stormlight");
		idealsManager.deserialize(spiritweb);

		this.herald = compoundTag.contains("herald") && compoundTag.getBoolean("herald");
	}

	@Override
	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();

		compoundTag.putInt("stored_stormlight", stormlightStored);
		idealsManager.serialize(spiritweb);

		//don't store extra data if we don't need to
		if (herald)
		{
			compoundTag.putBoolean("herald", true);
		}
		else if (compoundTag.contains("herald"))
		{
			compoundTag.remove("herald");
		}
	}


	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		final LivingEntity livingEntity = spiritweb.getLiving();
		final boolean surgebindingActiveTick = (livingEntity.tickCount + Manifestations.ManifestationTypes.SURGEBINDING.getID()) % 20 == 0;

		//tick stormlight
		if (isOathed())
		{

			//Moved the highstorm check into it's own method that's called by draw stormlight, so the player can choose to draw in stormlight from a highstorm.

			if (stormlightStored > 0 && surgebindingActiveTick)
			{
				//being hurt takes priority
				if (livingEntity.getHealth() < livingEntity.getMaxHealth())
				{
					//todo healing stormlight config
					final int stormlightHealingCostMultiplier = 20;

					if (adjustStormlight(-stormlightHealingCostMultiplier, true))
					{
						//todo stormlight healing better
						SurgeProgression.heal(livingEntity, livingEntity.getHealth() + 1);
					}
				}
				//otherwise conditional effects
				else
				{
					if (livingEntity.getCombatTracker().inCombat)
					{
						//todo combat effect cost
						//todo replace with non-vanilla effects
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DAMAGE_BOOST, 0));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, 0));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, 0));
						adjustStormlight(-30, true);
					}
					else if (livingEntity.isUnderWater())
					{
						//todo replace with non-vanilla effects
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.WATER_BREATHING, 0));
						//todo waterbreathing stormlight cost
						adjustStormlight(-10, true);
					}
					else
					{
						//todo detect better based on what the player is doing? mining means haste,
						//travelling means movement etc. Not sure if that's really feasible though
						//todo replace with non-vanilla effects
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DIG_SPEED, 0));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, 0));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, 0));
						adjustStormlight(-30, true);
					}
				}

				int drainRate = SurgebindingConfigs.SERVER.STORMLIGHT_DRAIN_RATE.get();
				//todo maybe reducing cost based on how many ideals they have sworn?
				//int idealsSworn = idealsManager.getIdeal();

				adjustStormlight(-(drainRate), true);
			}
		}

		//special effects for wearing shardplate.

		if (surgebindingActiveTick)
		{
			ItemStack helmet = livingEntity.getItemBySlot(EquipmentSlot.HEAD);
			ItemStack breastplate = livingEntity.getItemBySlot(EquipmentSlot.CHEST);
			ItemStack leggings = livingEntity.getItemBySlot(EquipmentSlot.LEGS);
			ItemStack boots = livingEntity.getItemBySlot(EquipmentSlot.FEET);

			//check wearing full suit of armor
			if (Stream.of(helmet, breastplate, leggings, boots).allMatch(armorStack -> !armorStack.isEmpty() && armorStack.getItem() instanceof ArmorItem))
			{
				//check armor matches same material
				for (ShardplateArmorMaterial material : ARMOR_MATERIALS)
				{
					if (Stream.of(helmet, breastplate, leggings, boots).allMatch((armorStack -> ((ArmorItem) armorStack.getItem()).getMaterial() == material)))
					{
						int amplifier = material == ShardplateArmorMaterial.DEADPLATE ? 0 : 1;

						//todo make our own effect for wearing shardplate
						//todo replace with non-vanilla effects
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.MOVEMENT_SPEED, amplifier));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DIG_SPEED, amplifier));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.DAMAGE_BOOST, amplifier));
						livingEntity.addEffect(EffectsHelper.getNewEffect(MobEffects.JUMP, amplifier));

						//todo oathed radiant shardplate stormlight cost
						//todo deadplate stormlight cost
						//Didn't deadplate draw reallyy quickly from Kaladin when he was using the 'gauntlet' in the duel?
						stormlightStored--;
						break;
					}
				}
			}
		}
	}

	@Override
	public void drainInvestiture(ISpiritweb data, double strength)
	{
		//todo drain based on strength
		stormlightStored = (int) (stormlightStored * 0.1f);
	}


	private void requestGemStormlight(ItemStack item, int amountDrawn)
	{
		GemstoneItem gemstoneItem = (GemstoneItem) item.getItem();

		int availableSpace = maxPlayerStormlight - stormlightStored;

		if (availableSpace < amountDrawn)
		{
			amountDrawn = availableSpace;
		}

		if (gemstoneItem.getCharge(item) >= amountDrawn)
		{
			gemstoneItem.adjustCharge(item, -amountDrawn);
			stormlightStored += amountDrawn;
		}
		else
		{
			stormlightStored += gemstoneItem.getCharge(item);
			gemstoneItem.setCharge(item, 0);
		}
	}

	//Called by RequestStormlight packet.
	public void requestStormlight()
	{

		final LivingEntity entity = spiritweb.getLiving();


		if (isInHighstorm(entity))
		{
			if (maxPlayerStormlight - stormlightStored >= drawSpeed)
			{
				stormlightStored += drawSpeed;
			}
			else
			{
				stormlightStored += maxPlayerStormlight - stormlightStored;
			}
		}
		else if (entity instanceof Player player)
		{

			ItemStack item = entity.getItemInHand(InteractionHand.MAIN_HAND);

			if (item.getItem() instanceof GemstoneItem gemstoneItem && gemstoneItem.getCharge(item) != 0)
			{
				requestGemStormlight(item, drawSpeed);
			}
			else
			{
				List<ItemStack> invItems = getGemItems(player);

				List<ItemStack> chargedGems = invItems.stream()
						.filter(gem -> ((GemstoneItem) gem.getItem()).getCharge(gem) > 0)
						.toList();

				int gemAmount = chargedGems.size();

				if (!chargedGems.isEmpty())
				{

					int requestAmount = drawSpeed / gemAmount;

					for (ItemStack gem : chargedGems)
					{

						requestGemStormlight(gem, requestAmount);

					}
				}

			}

		}

	}


	private void dispatchGemStormlight(ItemStack item, int amountDrawn)
	{
		GemstoneItem gemstoneItem = (GemstoneItem) item.getItem();

		int availableSpace = gemstoneItem.getMaxCharge(item) - gemstoneItem.getCharge(item);

		if (availableSpace < amountDrawn)
		{
			amountDrawn = availableSpace;
		}

		if (stormlightStored >= amountDrawn)
		{
			gemstoneItem.adjustCharge(item, amountDrawn);
			adjustStormlight(-amountDrawn, true);
		}
		else
		{
			gemstoneItem.adjustCharge(item, stormlightStored);
			stormlightStored = 0;
		}
	}

	//Called by DispatchStormlight packet.
	public void dispatchStormlight()
	{

		final LivingEntity entity = spiritweb.getLiving();

		if (entity instanceof Player player)
		{

			ItemStack handItem = player.getItemInHand(InteractionHand.MAIN_HAND);

			//If the hand item is a gemstone focus on that one.
			if (handItem.getItem() instanceof GemstoneItem)
			{
				dispatchGemStormlight(handItem, drawSpeed);
			}
			//Otherwise affect all gemstoneItems in inventory.
			else
			{
				List<ItemStack> invItems = getGemItems(player);

				//Get gems that are not fully charged.
				List<ItemStack> unchargedGems = invItems.stream()
						.filter(gem ->
						{
							GemstoneItem gemstoneItem = (GemstoneItem) gem.getItem();
							return (gemstoneItem.getMaxCharge(gem) - gemstoneItem.getCharge(gem)) > 0;
						}).toList();

				//If there are none, just breathe out into air.
				if (unchargedGems.isEmpty())
				{
					adjustStormlight(-drawSpeed, true);
				}
				else
				{
					int dispatchAmount = drawSpeed / unchargedGems.size();

					//For every gem that is not fully charged.
					for (ItemStack gem : unchargedGems)
					{
						dispatchGemStormlight(gem, dispatchAmount);
					}
				}

			}

		}
	}

	//TODO Replace with proper method once highstorms have been added.
	public static boolean isInHighstorm(LivingEntity entity)
	{
		return entity.level().isThundering()
				&& entity.level().dimension().equals(SurgebindingDimensions.ROSHAR_DIM_KEY)
				&& entity.level().isRainingAt(entity.blockPosition());
	}

	public static List<ItemStack> getGemItems(Player player)
	{
		if (player == null)
		{
			return Collections.emptyList();
		}

		Container acc = new CapWrapper(new PlayerInvWrapper(player.getInventory()));

		List<ItemStack> toReturn = new ArrayList<>(acc.getContainerSize());

		for (int slot = 0; slot < acc.getContainerSize(); slot++)
		{
			ItemStack stackInSlot = acc.getItem(slot);

			if (!stackInSlot.isEmpty() && stackInSlot.getItem() instanceof GemstoneItem)
			{
				toReturn.add(stackInSlot);
			}
		}
		return toReturn;
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
				stormlightStored = Mth.clamp(newSLValue, 0, maxPlayerStormlight);
			}

			return true;
		}

		return false;
	}

	public void setStormlight(int amount)
	{
		stormlightStored = Mth.clamp(amount, 0, maxPlayerStormlight);
	}

	public void onChatMessageReceived(ServerChatEvent event)
	{
		idealsManager.onChatMessageReceived(event);
	}

	public void setHerald(boolean isHerald)
	{
		herald = isHerald;
	}
}