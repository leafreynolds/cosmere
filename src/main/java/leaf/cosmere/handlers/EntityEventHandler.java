/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.effects.feruchemy.store.BrassStoreEffect;
import leaf.cosmere.items.CoinPouchItem;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyAtium;
import leaf.cosmere.manifestation.allomancy.AllomancyNicrosil;
import leaf.cosmere.manifestation.feruchemy.FeruchemyAtium;
import leaf.cosmere.manifestation.feruchemy.FeruchemyElectrum;
import leaf.cosmere.registry.ManifestationRegistry;
import leaf.cosmere.registry.TagsRegistry;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.animal.horse.Llama;
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
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

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
			else if (canStartWithPowers(eventEntity))
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

	public static boolean isValidSpiritWebEntity(Entity entity)
	{
		return entity instanceof Player
				|| entity instanceof AbstractVillager
				|| entity instanceof ZombieVillager
				|| (entity instanceof Raider && !(entity instanceof Ravager))
				|| entity instanceof AbstractPiglin
				|| entity instanceof Llama
				|| entity instanceof Cat;
	}

	public static boolean canStartWithPowers(Entity entity)
	{
		return entity instanceof Player
				|| entity instanceof AbstractVillager
				|| entity instanceof ZombieVillager
				|| (entity instanceof Raider && !(entity instanceof Ravager))
				|| entity instanceof AbstractPiglin;
	}

	@SubscribeEvent
	public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
	{
		Entity eventEntity = event.getObject();

		if (isValidSpiritWebEntity(eventEntity))
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

	/*@SubscribeEvent
	public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
	{
		final Player player = event.getPlayer();
		ItemStack itemInHand = player.getItemInHand(event.getHand());

		final Item[] allowedItems = {Items.IRON_NUGGET, Items.GOLD_NUGGET};

		final Item handItem = itemInHand.getItem();

		if (!itemInHand.isEmpty() && Arrays.asList(allowedItems).contains(handItem))
		{
			for (Metals.MetalType metalType : Metals.MetalType.values())
			{
				if (itemInHand.is(TagsRegistry.Items.METAL_NUGGET_TAGS.get(metalType)))
				{
					player.startUsingItem(event.getHand());
					MetalNuggetItem.consumeNugget(player, metalType, itemInHand, 1);
					break;
				}
			}
		}
	}*/


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

				MetalNuggetItem.consumeNugget(target, metalType, stack, 1);
			}
			else if (stack.getItem() instanceof HemalurgicSpikeItem spike)
			{
				//https://www.theoryland.com/intvmain.php?i=977#43
				if (!(event.getTarget() instanceof Cat cat))
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
				target.setCustomName(TextHelper.createTranslatedText("Catquisitor"));

				boolean spikeApplied = false;

				try
				{
					for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
					{
						final double hemalurgicStrength = spike.getHemalurgicStrength(stack, manifestation);
						if (hemalurgicStrength > 0)
						{
							final RegistryObject<Attribute> regAttribute = manifestation.getAttribute();
							if (regAttribute == null || !regAttribute.isPresent())
							{
								continue;
							}
							spikeApplied = true;

							final AttributeMap catAttributes = cat.getAttributes();
							final AttributeInstance instance = catAttributes.getInstance(regAttribute.get());

							if (instance != null)
							{
								instance.setBaseValue(hemalurgicStrength);
							}
						}
					}
				}
				catch (Exception e)
				{

				}

				if (spikeApplied && !event.getPlayer().isCreative())
				{
					stack.shrink(1);
				}
			}

		});
	}


	@SubscribeEvent
	public static void changeSize(EntityEvent.Size event)
	{
		final Entity entity = event.getEntity();
		if (entity != null && entity instanceof LivingEntity livingEntity)
		{
			float scale = FeruchemyAtium.getScale(livingEntity);

			//only change if scale not 1, else we let the change size event do it's thing unimpeded
			if (scale != 1)
			{
				event.setNewSize(event.getNewSize().scale(scale));
				event.setNewEyeHeight(event.getNewEyeHeight() * scale);
			}
		}
	}

	//Attack event happens first
	@SubscribeEvent
	public static void onLivingAttackEvent(LivingAttackEvent event)
	{
		BrassStoreEffect.onLivingAttackEvent(event);
		AllomancyAtium.onLivingAttackEvent(event);
	}

	//then living hurt event
	@SubscribeEvent
	public static void onLivingHurtEvent(LivingHurtEvent event)
	{
		BrassStoreEffect.onLivingHurtEvent(event);
		FeruchemyElectrum.onLivingHurtEvent(event);
		AllomancyNicrosil.onLivingHurtEvent(event);
	}


}
