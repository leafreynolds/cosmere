package leaf.cosmere.feruchemy.common.utils;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.items.ChargeableMetalCurioItem;
import leaf.cosmere.feruchemy.common.itemgroups.FeruchemyItemGroups;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class FeruchemyChargeThread implements Runnable {
    private static FeruchemyChargeThread INSTANCE;
    private static final FeruchemyChargeTracker feruchemyChargeTracker = new FeruchemyChargeTracker();
    static Thread t;
    static boolean isStopping = false;

    public static FeruchemyChargeThread getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new FeruchemyChargeThread();
        }

        return INSTANCE;
    }

    public float getCharge(Metals.MetalType metalType)
    {
        return feruchemyChargeTracker.getCharge(metalType);
    }

    public void start()
    {
        if (t == null || !isStopping)
        {
            CosmereAPI.logger.info("Feruchemy thread started");
            t = new Thread(this, "feruchemy_enumerator_thread");
            isStopping = false;
            t.start();
        }
    }

    public void stop()
    {
        if (t != null && !isStopping)
        {
            isStopping = true;
        }
    }

    @Override
    public void run() {
        Minecraft mc = Minecraft.getInstance();

        while (!isStopping)
        {
            // no serverside action, unloaded levels, or non-existent players allowed >:(
            if (mc.level == null || mc.player == null || !mc.level.isClientSide)
            {
                stop();
                break;
            }

            if (mc.player.tickCount % 2 != 0)
            {
                try {
                    Thread.sleep(50);       // 20 ticks per 1000ms, 1000/20 = 50, rest for 1 tick
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                continue;
            }

            // floats to keep track of each metal's f-charge in the inventory
            float ironCharge = 0F, steelCharge = 0F, tinCharge = 0F, pewterCharge = 0F,
                    copperCharge = 0F, bronzeCharge = 0F, zincCharge = 0F, brassCharge = 0F,
                    aluminumCharge = 0F, duraluminCharge = 0F, chromiumCharge = 0F, nicrosilCharge = 0F,
                    cadmiumCharge = 0F, bendalloyCharge = 0F, goldCharge = 0F, electrumCharge = 0F,
                    atiumCharge = 0F;

            for (ItemStack stack : mc.player.getInventory().items)
            {
                if (stack.getItem() instanceof ChargeableMetalCurioItem item)
                {
                    // is either f-item or h-item
                    if (item.getItemCategory() == FeruchemyItemGroups.METALMINDS)
                    {
                        float chargeToAdd = item.getCharge(stack);
                        switch (item.getMetalType()) {
                            case IRON:
                                ironCharge += chargeToAdd;
                                break;
                            case STEEL:
                                steelCharge += chargeToAdd;
                                break;
                            case TIN:
                                tinCharge += chargeToAdd;
                                break;
                            case PEWTER:
                                pewterCharge += chargeToAdd;
                                break;
                            case ZINC:
                                zincCharge += chargeToAdd;
                                break;
                            case BRASS:
                                brassCharge += chargeToAdd;
                                break;
                            case COPPER:
                                copperCharge += chargeToAdd;
                                break;
                            case BRONZE:
                                bronzeCharge += chargeToAdd;
                                break;
                            case ALUMINUM:
                                aluminumCharge += chargeToAdd;
                                break;
                            case DURALUMIN:
                                duraluminCharge += chargeToAdd;
                                break;
                            case CHROMIUM:
                                chromiumCharge += chargeToAdd;
                                break;
                            case NICROSIL:
                                nicrosilCharge += chargeToAdd;
                                break;
                            case CADMIUM:
                                cadmiumCharge += chargeToAdd;
                                break;
                            case BENDALLOY:
                                bendalloyCharge += chargeToAdd;
                                break;
                            case GOLD:
                                goldCharge += chargeToAdd;
                                break;
                            case ELECTRUM:
                                electrumCharge += chargeToAdd;
                                break;
                            case ATIUM:
                                atiumCharge += chargeToAdd;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            Optional<List<ItemStack>> metalmindCuriosList = CuriosApi.getCuriosHelper().getEquippedCurios(mc.player)
                    .map(mapper ->
                    {
                        List<ItemStack> metalmindList = new ArrayList<>();
                        for (int i = 0; i < mapper.getSlots(); i++)
                        {
                            if (mapper.getStackInSlot(i).getItem() instanceof ChargeableMetalCurioItem item)
                            {
                                metalmindList.add(mapper.getStackInSlot(i));
                            }
                        }
                        return metalmindList;
                    });

            if (metalmindCuriosList.isPresent())
            {
                for (ItemStack stack : metalmindCuriosList.get())
                {
                    ChargeableMetalCurioItem item = (ChargeableMetalCurioItem) stack.getItem();

                    // is either f-item or h-item
                    if (item.getItemCategory() == FeruchemyItemGroups.METALMINDS)
                    {
                        CosmereAPI.logger.debug("Item: " + stack.getHoverName());

                        float chargeToAdd = item.getCharge(stack);
                        switch (item.getMetalType()) {
                            case IRON:
                                ironCharge += chargeToAdd;
                                break;
                            case STEEL:
                                steelCharge += chargeToAdd;
                                break;
                            case TIN:
                                tinCharge += chargeToAdd;
                                break;
                            case PEWTER:
                                pewterCharge += chargeToAdd;
                                break;
                            case ZINC:
                                zincCharge += chargeToAdd;
                                break;
                            case BRASS:
                                brassCharge += chargeToAdd;
                                break;
                            case COPPER:
                                copperCharge += chargeToAdd;
                                break;
                            case BRONZE:
                                bronzeCharge += chargeToAdd;
                                break;
                            case ALUMINUM:
                                aluminumCharge += chargeToAdd;
                                break;
                            case DURALUMIN:
                                duraluminCharge += chargeToAdd;
                                break;
                            case CHROMIUM:
                                chromiumCharge += chargeToAdd;
                                break;
                            case NICROSIL:
                                nicrosilCharge += chargeToAdd;
                                break;
                            case CADMIUM:
                                cadmiumCharge += chargeToAdd;
                                break;
                            case BENDALLOY:
                                bendalloyCharge += chargeToAdd;
                                break;
                            case GOLD:
                                goldCharge += chargeToAdd;
                                break;
                            case ELECTRUM:
                                electrumCharge += chargeToAdd;
                                break;
                            case ATIUM:
                                atiumCharge += chargeToAdd;
                                break;
                            default:
                                break;
                        }
                    }
                }
            }

            feruchemyChargeTracker.setCharge(Metals.MetalType.IRON, ironCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.STEEL, steelCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.TIN, tinCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.PEWTER, pewterCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.ZINC, zincCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.BRASS, brassCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.COPPER, copperCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.BRONZE, bronzeCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.ALUMINUM, aluminumCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.DURALUMIN, duraluminCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.CHROMIUM, chromiumCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.NICROSIL, nicrosilCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.CADMIUM, cadmiumCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.BENDALLOY, bendalloyCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.GOLD, goldCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.ELECTRUM, electrumCharge);
            feruchemyChargeTracker.setCharge(Metals.MetalType.ATIUM, atiumCharge);
        }
    }

    private static class FeruchemyChargeTracker
    {
        private float ironCharge = 0F, steelCharge = 0F, tinCharge = 0F, pewterCharge = 0F,
                copperCharge = 0F, bronzeCharge = 0F, zincCharge = 0F, brassCharge = 0F,
                aluminumCharge = 0F, duraluminCharge = 0F, chromiumCharge = 0F, nicrosilCharge = 0F,
                cadmiumCharge = 0F, bendalloyCharge = 0F, goldCharge = 0F, electrumCharge = 0F,
                atiumCharge = 0F;

        public void setCharge(Metals.MetalType metalType, float chargeToSet)
        {
            switch (metalType)
            {
                case IRON:
                    ironCharge = chargeToSet;
                    break;
                case STEEL:
                    steelCharge = chargeToSet;
                    break;
                case TIN:
                    tinCharge = chargeToSet;
                    break;
                case PEWTER:
                    pewterCharge = chargeToSet;
                    break;
                case ZINC:
                    zincCharge = chargeToSet;
                    break;
                case BRASS:
                    brassCharge = chargeToSet;
                    break;
                case COPPER:
                    copperCharge = chargeToSet;
                    break;
                case BRONZE:
                    bronzeCharge = chargeToSet;
                    break;
                case ALUMINUM:
                    aluminumCharge = chargeToSet;
                    break;
                case DURALUMIN:
                    duraluminCharge = chargeToSet;
                    break;
                case CHROMIUM:
                    chromiumCharge = chargeToSet;
                    break;
                case NICROSIL:
                    nicrosilCharge = chargeToSet;
                    break;
                case CADMIUM:
                    cadmiumCharge = chargeToSet;
                    break;
                case BENDALLOY:
                    bendalloyCharge = chargeToSet;
                    break;
                case GOLD:
                    goldCharge = chargeToSet;
                    break;
                case ELECTRUM:
                    electrumCharge = chargeToSet;
                    break;
                case ATIUM:
                    atiumCharge = chargeToSet;
                    break;
                default:
                    break;
            }
        }

        public float getCharge(Metals.MetalType metalType)
        {
            switch (metalType)
            {
                case IRON:
                    return ironCharge;
                case STEEL:
                    return steelCharge;
                case TIN:
                    return tinCharge;
                case PEWTER:
                    return pewterCharge;
                case ZINC:
                    return zincCharge;
                case BRASS:
                    return brassCharge;
                case COPPER:
                    return copperCharge;
                case BRONZE:
                    return bronzeCharge;
                case ALUMINUM:
                    return aluminumCharge;
                case DURALUMIN:
                    return duraluminCharge;
                case CHROMIUM:
                    return chromiumCharge;
                case NICROSIL:
                    return nicrosilCharge;
                case CADMIUM:
                    return cadmiumCharge;
                case BENDALLOY:
                    return bendalloyCharge;
                case GOLD:
                    return goldCharge;
                case ELECTRUM:
                    return electrumCharge;
                case ATIUM:
                    return atiumCharge;
                default:
                    return 0F;
            }
        }
    }
}
