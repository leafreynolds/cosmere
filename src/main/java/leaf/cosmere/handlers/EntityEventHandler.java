/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.CoinPouchItem;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Ravager;
import net.minecraft.world.entity.monster.ZombieVillager;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.raid.Raider;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Arrays;

import static leaf.cosmere.utils.helpers.EntityHelper.giveEntityStartingManifestation;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

	@SubscribeEvent
	public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
	{
		Entity eventEntity = event.getEntity();

		if (eventEntity.level.isClientSide || !(eventEntity instanceof LivingEntity livingEntity))
		{
			return;
		}

		SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
		{
			SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

			//find out if any innate powers exist on the entity first
			//if they do
			if (spiritweb.hasBeenInitialized() || spiritweb.hasAnyPowers())
			{
				//then skip
				//no need to give them extras just for rejoining the world.
				return;
			}

			//if player or villager
			if (eventEntity instanceof Player)
			{
				//from random powertype
				{
					//give random power
					giveEntityStartingManifestation(livingEntity, spiritweb);
				}
			}
			else if (eventEntity instanceof AbstractVillager
					|| eventEntity instanceof ZombieVillager
					|| (eventEntity instanceof Raider && !(eventEntity instanceof Ravager))
					|| eventEntity instanceof AbstractPiglin)
			{
				//random 1/16
				// only 1 in 16 will have the gene


				final int chance = eventEntity instanceof Raider ? 50 : 16;
				if (MathHelper.randomInt(1, chance) % chance == 0)
				{
					giveEntityStartingManifestation(livingEntity, spiritweb);
				}

			}

			spiritweb.setHasBeenInitialized();
		});

	}

	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity eventEntity = event.getObject();

		if (eventEntity instanceof Player
				|| eventEntity instanceof Animal
				|| eventEntity instanceof AbstractVillager
				|| eventEntity instanceof Monster)
		{
			LivingEntity livingEntity = (LivingEntity) eventEntity;
			event.addCapability(Constants.Resources.SPIRITWEB_CAP, new ICapabilitySerializable<CompoundTag>()
			{
				final SpiritwebCapability spiritweb = new SpiritwebCapability(livingEntity);
				final LazyOptional<ISpiritweb> spiritwebInstance = LazyOptional.of(() -> spiritweb);

				@Nonnull
				@Override
				public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @javax.annotation.Nullable Direction side)
				{
					return cap == SpiritwebCapability.CAPABILITY ? (LazyOptional<T>) spiritwebInstance
					                                             : LazyOptional.empty();
				}

				@Override
				public CompoundTag serializeNBT()
				{
					return spiritweb.serializeNBT();
				}

				@Override
				public void deserializeNBT(CompoundTag nbt)
				{
					spiritweb.deserializeNBT(nbt);
				}
			});
		}
	}

	@SubscribeEvent
	public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
	{
		SpiritwebCapability.get(event.getEntityLiving()).ifPresent(ISpiritweb::tick);
	}

	@SubscribeEvent
	public static void onEntityItemPickUp(EntityItemPickupEvent event)
	{
		if (CoinPouchItem.onPickupItem(event.getItem(), event.getPlayer()))
		{
			event.setCanceled(true);
		}
		//seriously, get item three times is stupid, I know it.
		//but entity item, itemstack and then the actual item is needed.
/*      else if (event.getItem().getItem().getItem() == ItemsRegistry.INVESTITURE.get())
        {
            event.getItem().getItem().shrink(1);

            ItemChargeHelper.dispatchCharge(event.getPlayer(), 1000, true);
        }*/
	}

	@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		final Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand(event.getHand());

		final Item[] allowedItems = {Items.IRON_NUGGET, Items.GOLD_NUGGET/*,Items.COPPER_NUGGET*/};

		final Item handItem = itemInHand.getItem();

		if (!itemInHand.isEmpty() && Arrays.asList(allowedItems).contains(handItem))
		{
			Metals.MetalType metalType = null;

			if (handItem == Items.IRON_NUGGET)
			{
				metalType = Metals.MetalType.IRON;
			}
			else if (handItem == Items.GOLD_NUGGET)
			{
				metalType = Metals.MetalType.GOLD;
			}
            /*else if (handItem == Items.COPPER_NUGGET)//todo if copper ever has nuggets
            {
                metalType = Metals.MetalType.COPPER;
            }*/
			player.startUsingItem(event.getHand());

			MetalNuggetItem.consumeNugget(player, metalType, itemInHand);
		}

	}


	@SubscribeEvent
	public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
	{
		if (!(event.getTarget() instanceof LivingEntity target))
		{
			return;
		}

		ItemStack stack = event.getPlayer().getItemInHand(InteractionHand.MAIN_HAND);

		SpiritwebCapability.get(target).ifPresent(cap ->
		{
			if (stack.getItem() instanceof MetalNuggetItem beadItem)
			{
				Metals.MetalType metalType = beadItem.getMetalType();

				if (metalType != Metals.MetalType.LERASATIUM && metalType != Metals.MetalType.LERASIUM)
				{
					return;
				}

				MetalNuggetItem.consumeNugget(target, metalType, stack);
			}
			else if (stack.getItem() instanceof HemalurgicSpikeItem spike)
			{
				//https://www.theoryland.com/intvmain.php?i=977#43
				if (!(event.getTarget() instanceof Cat))
				{
					return;
				}

				//only apply spike if it has a power
				//no accidentally losing spikes
				if (!spike.hemalurgicIdentityExists(stack))
				{
					return;
				}


				//todo random list of catquisitor names
				target.setCustomName(TextHelper.createTranslatedText("Catquisitor "));

				boolean spikeApplied = false;

				//todo catquisitor
                /*
                switch (spike.getMetalType())
                {
                    case STEEL:
                    case BRONZE:
                    case CADMIUM:
                    case ELECTRUM:
                        cap.giveManifestation(Manifestations.ManifestationTypes.ALLOMANCY, spike.getMetalType().getID());
                        spikeApplied = true;
                        break;
                    case PEWTER:
                    case BRASS:
                    case BENDALLOY:
                    case GOLD:
                        cap.giveManifestation(Manifestations.ManifestationTypes.FERUCHEMY, spike.getMetalType().getID());
                        spikeApplied = true;
                        break;
                    case ATIUM:
                        //Steals any power
                        //todo decide if we just pick a random power
                        break;
                    case LERASIUM:
                        //Steals all powers
                        break;
                }*/

				if (spikeApplied && !event.getPlayer().isCreative())
				{
					stack.shrink(1);
				}
			}

		});
	}
}
