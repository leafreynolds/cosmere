/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.handlers;

import leaf.cosmere.Cosmere;
import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.charge.ItemChargeHelper;
import leaf.cosmere.constants.Constants;
import leaf.cosmere.constants.Manifestations;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.utils.helpers.MathHelper;
import leaf.cosmere.utils.helpers.TextHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.AbstractIllagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.WitchEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Arrays;

import static leaf.cosmere.utils.helpers.EntityHelper.giveEntityStartingManifestation;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

    @SubscribeEvent
    public static void onEntityJoinWorldEvent(EntityJoinWorldEvent event)
    {
        Entity eventEntity = event.getEntity();

        if (eventEntity.level.isClientSide || !(eventEntity instanceof LivingEntity))
        {
            return;
        }

        LivingEntity livingEntity = (LivingEntity) eventEntity;
        SpiritwebCapability.get(livingEntity).ifPresent(iSpiritweb ->
        {
            SpiritwebCapability spiritweb = (SpiritwebCapability) iSpiritweb;

            //find out if any innate powers exist on the entity first
            //if they do
            if (spiritweb.hasAnyPowers())
            {
                //then skip
                //no need to give them extras just for rejoining the world.
                return;
            }

            //if player or villager
            if (eventEntity instanceof PlayerEntity)
            {
                //from random powertype
                {
                    //give random power
                    giveEntityStartingManifestation(livingEntity, spiritweb);
                }
            }
            else if (eventEntity instanceof VillagerEntity
                    || eventEntity instanceof ZombieVillagerEntity
                    || eventEntity instanceof AbstractIllagerEntity
                    || eventEntity instanceof WitchEntity
                    || eventEntity instanceof PiglinEntity)
            {
                //random 1/16
                // only 1 in 16 will have the gene
                if (MathHelper.randomInt(1, 16) % 16 == 0)
                {
                    giveEntityStartingManifestation(livingEntity, spiritweb);
                }

            }
        });

    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        Entity eventEntity = event.getObject();

        if (eventEntity instanceof PlayerEntity
                || eventEntity instanceof AnimalEntity
                || eventEntity instanceof VillagerEntity
                || eventEntity instanceof MonsterEntity)
        {
            LivingEntity livingEntity = (LivingEntity) eventEntity;
            SpiritwebCapability spiritwebCapability = new SpiritwebCapability(livingEntity);

            event.addCapability(Constants.Resources.SPIRITWEB_CAP, new ISpiritweb.Provider(spiritwebCapability));
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
        //seriously, get item three times is stupid, I know it.
        //but entity item, itemstack and then the actual item is needed.
        if (event.getItem().getItem().getItem() == ItemsRegistry.INVESTITURE.get())
        {
            event.getItem().getItem().shrink(1);

            ItemChargeHelper.dispatchCharge(event.getPlayer(), 1000, true);
        }
    }

    @SubscribeEvent
    public static void onRightClickItem(PlayerInteractEvent.RightClickItem event)
    {
        final PlayerEntity player = event.getPlayer();
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
            /*else if (handItem == Items.COPPER_NUGGET)
            {
                metalType = Metals.MetalType.COPPER;
            }*/

            MetalNuggetItem.consumeNugget(player, metalType, itemInHand);
        }

    }


    @SubscribeEvent
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
    {
        if (!(event.getTarget() instanceof LivingEntity))
        {
            return;
        }

        ItemStack stack = event.getPlayer().getItemInHand(Hand.MAIN_HAND);
        LivingEntity target = (LivingEntity) event.getTarget();

        SpiritwebCapability.get(target).ifPresent(cap ->
        {
            if (stack.getItem() instanceof MetalNuggetItem)
            {
                MetalNuggetItem beadItem = (MetalNuggetItem) stack.getItem();
                Metals.MetalType metalType = beadItem.getMetalType();

                if (metalType != Metals.MetalType.LERASATIUM && metalType != Metals.MetalType.LERASIUM)
                {
                    return;
                }

                MetalNuggetItem.consumeNugget(target, metalType, stack);
            }
            else if (stack.getItem() instanceof HemalurgicSpikeItem)
            {
                //https://www.theoryland.com/intvmain.php?i=977#43
                if (!(event.getTarget() instanceof CatEntity))
                {
                    return;
                }

                HemalurgicSpikeItem spike = (HemalurgicSpikeItem) stack.getItem();

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
