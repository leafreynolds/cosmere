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
import leaf.cosmere.helpers.MathHelper;
import leaf.cosmere.helpers.TextHelper;
import leaf.cosmere.items.MetalNuggetItem;
import leaf.cosmere.items.curio.HemalurgicSpikeItem;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof PlayerEntity
                || event.getObject() instanceof AnimalEntity
                || event.getObject() instanceof VillagerEntity
                || event.getObject() instanceof MonsterEntity
                || event.getObject() instanceof PlayerEntity)
        {
            SpiritwebCapability spiritwebCapability = new SpiritwebCapability((LivingEntity) event.getObject());

            if (event.getObject() instanceof PlayerEntity)
            {
                //from random powertype
                {
                    //give random power
                    giveRandomManifestation(event.getObject(), spiritwebCapability);
                }
            }
            else if (event.getObject() instanceof VillagerEntity)
            {
                //random 1/16
                // only 1 in 16 villagers will have the gene
                if (MathHelper.randomInt(1, 16) % 16 == 0)
                {
                    giveRandomManifestation(event.getObject(), spiritwebCapability);
                }

            }

            event.addCapability(Constants.Resources.SPIRITWEB_CAP, new ISpiritweb.Provider(spiritwebCapability));
        }
    }

    private static void giveRandomManifestation(Entity entity, SpiritwebCapability spiritwebCapability)
    {
        //give random power
        boolean isAllomancy = MathHelper.randomBool();

        // and only 1 in 16 of those will have the full powers
        Manifestations.ManifestationTypes powerType = isAllomancy
                                              ? Manifestations.ManifestationTypes.ALLOMANCY
                                              : Manifestations.ManifestationTypes.FERUCHEMY;

        //if 17, then give them all the powers from either allomancy or feruchemy
        if (MathHelper.randomInt(0, 16) % 16 == 0)
        {
            //ooh full powers
            for (int i = 0; i < 16; i++)
            {
                spiritwebCapability.giveManifestation(powerType, i);
            }

            if (!(entity instanceof PlayerEntity))
            {
                entity.setCustomName(TextHelper.createTranslatedText(isAllomancy
                                                                     ? "Mistborn"
                                                                     : "Feruchemist"));
            }
        }
        else
        {
            int powerID = MathHelper.randomInt(1, 16);
            spiritwebCapability.giveManifestation(powerType, powerID);

            String post = isAllomancy
                          ? " Misting"
                          : " Ferring";
            if (!(entity instanceof PlayerEntity))
            {
                entity.setCustomName(powerType.getManifestation(powerID).translation());
            }
        }
    }

    @SubscribeEvent
    public static void onLivingTick(LivingEvent.LivingUpdateEvent event)
    {
        SpiritwebCapability.get(event.getEntityLiving()).ifPresent(ISpiritweb::tick);
    }

    @SubscribeEvent
    public static void onEntityDeath(LivingDeathEvent event)
    {
        if (event.getSource().getTrueSource() instanceof PlayerEntity)
        {
            PlayerEntity playerEntity = (PlayerEntity) event.getSource().getTrueSource();
            SpiritwebCapability.get(playerEntity).ifPresent(iSpiritweb ->
            {
                ItemStack itemstack = playerEntity.getHeldItemMainhand();
                if (itemstack.getItem() instanceof HemalurgicSpikeItem)
                {
                    //entity was killed by a spike
                    HemalurgicSpikeItem spikeItem = (HemalurgicSpikeItem) itemstack.getItem();
                    //pass in killed entity for the item to figure out what to do
                    spikeItem.killedEntity(itemstack, event.getEntityLiving());
                }

            });
        }
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
    public static void onEntityInteract(PlayerInteractEvent.EntityInteract event)
    {
        if (!(event.getTarget() instanceof LivingEntity))
        {
            return;
        }

        ItemStack stack = event.getPlayer().getHeldItem(Hand.MAIN_HAND);
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

                if (!event.getPlayer().isCreative())
                {
                    stack.shrink(1);
                }

                for (int i = 0; i < 16; i++)
                {
                    switch (metalType)
                    {
                        case LERASIUM:
                            //give allomancy
                            cap.giveManifestation(Manifestations.ManifestationTypes.ALLOMANCY, i);

                            //https://www.theoryland.com/intvmain.php?i=977#43
                            if (target instanceof LlamaEntity && !target.hasCustomName())
                            {
                                target.setCustomName(TextHelper.createTranslatedText("Mistborn Llama"));
                            }

                            break;
                        case LERASATIUM:
                            //give feruchemy
                            cap.giveManifestation(Manifestations.ManifestationTypes.FERUCHEMY, i);
                            break;
                    }
                }


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
                }

                if (spikeApplied && !event.getPlayer().isCreative())
                {
                    stack.shrink(1);
                }
            }

        });
    }
}
