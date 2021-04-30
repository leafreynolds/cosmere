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
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ItemsRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.monster.ZombieVillagerEntity;
import net.minecraft.entity.monster.piglin.PiglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CatEntity;
import net.minecraft.entity.passive.horse.LlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityAttributeModificationEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EntityEventHandler
{

    @SubscribeEvent
    public static void onEntityAttributeModificationEvent(EntityAttributeModificationEvent event)
    {
        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            if (metalType.hasAssociatedManifestation())
            {
                event.add(EntityType.PLAYER, AttributesRegistry.ALLOMANTIC_STRENGTH_ATTRIBUTES.get(metalType).get());
                event.add(EntityType.PLAYER, AttributesRegistry.FERUCHEMICAL_STRENGTH_ATTRIBUTES.get(metalType).get());
            }
        }

    }

    @SubscribeEvent
    public static void attachEntityCapabilities(AttachCapabilitiesEvent<Entity> event)
    {
        if (event.getObject() instanceof PlayerEntity
                || event.getObject() instanceof AnimalEntity
                || event.getObject() instanceof VillagerEntity
                || event.getObject() instanceof MonsterEntity)
        {
            SpiritwebCapability spiritwebCapability = new SpiritwebCapability((LivingEntity) event.getObject());

            //if player or villager
            if (event.getObject() instanceof PlayerEntity)
            {
                //from random powertype
                {
                    //give random power
                    giveRandomManifestation(event.getObject(), spiritwebCapability);
                }
            }
            else if (event.getObject() instanceof VillagerEntity
                    || event.getObject() instanceof ZombieVillagerEntity
                    || event.getObject() instanceof PiglinEntity)
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
        boolean isPlayerEntity = entity instanceof PlayerEntity;
        //low chance of having full powers of one type
        boolean isFullPowersFromOneType = MathHelper.randomInt(0, 16) % 16 == 0;

        //50/50 chance of of being twin born, but only if not having full powers above
        //players are guaranteed having at least two powers.
        boolean isTwinborn = MathHelper.randomBool() || isPlayerEntity;

        //randomise the given powers from allomancy and feruchemy
        int allomancyPower = MathHelper.randomInt(0, 15);
        int feruchemyPower = MathHelper.randomInt(0, 15);

        //if not twinborn, pick one power
        boolean isAllomancy = MathHelper.randomBool();
        Manifestations.ManifestationTypes powerType = isAllomancy
                                                      ? Manifestations.ManifestationTypes.ALLOMANCY
                                                      : Manifestations.ManifestationTypes.FERUCHEMY;

        if (isFullPowersFromOneType)
        {
            //ooh full powers
            for (int i = 0; i < 16; i++)
            {
                spiritwebCapability.giveManifestation(powerType, i);
            }

            if (!isPlayerEntity)
            {
                //todo translations
                //todo grant random name
                entity.setCustomName(TextHelper.createTranslatedText(isAllomancy
                                                                     ? "Mistborn"
                                                                     : "Feruchemist"));
            }
        }
        else if (isTwinborn)
        {
            spiritwebCapability.giveManifestation(Manifestations.ManifestationTypes.ALLOMANCY, allomancyPower);
            spiritwebCapability.giveManifestation(Manifestations.ManifestationTypes.FERUCHEMY, feruchemyPower);

            if (!isPlayerEntity)
            {
                //todo translations
                //todo grant random name
                entity.setCustomName(TextHelper.createTranslatedText("Twinborn"));
            }
        }
        else
        {
            int powerID = isAllomancy ? allomancyPower : feruchemyPower;
            spiritwebCapability.giveManifestation(powerType, powerID);
            //todo translations
            //todo grant random name
            entity.setCustomName(powerType.getManifestation(powerID).translation());
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
