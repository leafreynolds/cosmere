/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 *
 * Special thank you to the Suff and their mod Regeneration.
 * That mod taught me how to add ticking capabilities to entities and have them sync
 * https://github.com/WhoCraft/Regeneration
 */

package leaf.cosmere.cap.entity;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.mojang.blaze3d.matrix.MatrixStack;
import leaf.cosmere.client.gui.DrawUtils;
import leaf.cosmere.constants.Manifestations.ManifestationTypes;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyIronSteel;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.SyncPlayerSpiritwebMessage;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.block.Blocks;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/*
    "The actual outlet of the power is not chosen by the practitioner, but instead is hardwritten into their Spiritweb"
    -Khriss
    https://coppermind.net/wiki/Ars_Arcanum#The_Alloy_of_Law
 */

public class SpiritwebCapability implements ISpiritweb
{
    //region Render stuff.
    final static ItemStack positiveActiveStack = new ItemStack(Blocks.SOUL_TORCH);
    final static ItemStack negativeActiveStack = new ItemStack(Blocks.REDSTONE_TORCH);
    final static ItemStack inactiveStack = new ItemStack(Items.STICK);
    //endregion


    //Injection
    @CapabilityInject(ISpiritweb.class)
    public static final Capability<ISpiritweb> CAPABILITY = null;

    //detect if capability has been set up yet
    private boolean didSetup = false;

    private final LivingEntity livingEntity;

    public final Map<ManifestationTypes, int[]> MANIFESTATIONS_MODE =
            Arrays.stream(ManifestationTypes.values())
                    .collect(Collectors.toMap(
                            Function.identity(),
                            type -> new int[Metals.MetalType.values().length]));//todo come back here eventually because not all power types will be the same as num metals. and some metals don't have powers >:(

    private AManifestation selectedManifestation = ManifestationRegistry.NONE.get();

    //biochromatic breaths stored.
    //todo, figure out how the passive buff works
    private int biochromaticBreathStored = 0;

    //stormlight stored

    private int stormlightStored = 0;

    //metals ingested
    public final Map<Metals.MetalType, Integer> METALS_INGESTED =
            Arrays.stream(Metals.MetalType.values())
                    .collect(Collectors.toMap(Function.identity(), type -> 0));

    public List<BlockPos> pushBlocks = new ArrayList<>(4);
    public List<Integer> pushEntities = new ArrayList<>(4);

    public List<BlockPos> pullBlocks = new ArrayList<>(4);
    public List<Integer> pullEntities = new ArrayList<>(4);


    public SpiritwebCapability(LivingEntity ent)
    {
        this.livingEntity = ent;
    }


    @Nonnull
    public static LazyOptional<ISpiritweb> get(LivingEntity entity)
    {
        return entity instanceof LivingEntity ? entity.getCapability(SpiritwebCapability.CAPABILITY, null)
                                              : LazyOptional.empty();
    }

    @Override
    public CompoundNBT serializeNBT()
    {
        CompoundNBT nbt = new CompoundNBT();

        nbt.putString("selected_power", selectedManifestation.getRegistryName().toString());
        nbt.putInt("stored_breaths", biochromaticBreathStored);
        nbt.putInt("stored_stormlight", stormlightStored);

        for (ManifestationTypes manifestationType : ManifestationTypes.values())
        {
            String manifestationTypeName = manifestationType.name().toLowerCase();
            nbt.putIntArray(manifestationTypeName + "_mode", MANIFESTATIONS_MODE.get(manifestationType));
        }

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            nbt.putInt(metalType.name().toLowerCase() + "_ingested", METALS_INGESTED.get(metalType));
        }

        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt)
    {
        for (ManifestationTypes manifestationType : ManifestationTypes.values())
        {
            String manifestationTypeName = manifestationType.name().toLowerCase();

            int[] storedIntArray = nbt.getIntArray(manifestationTypeName + "_mode");
            int length = storedIntArray.length;

            for (int i = 0; i < length; i++)
            {
                //doing it this way for backwards compatibility in cases where we add new powers.
                MANIFESTATIONS_MODE.get(manifestationType)[i] = storedIntArray[i];
            }
        }
        selectedManifestation = ManifestationRegistry.fromID(nbt.getString("selected_power"));

        biochromaticBreathStored = nbt.getInt("stored_breaths");
        stormlightStored = nbt.getInt("stored_stormlight");

        for (Metals.MetalType metalType : Metals.MetalType.values())
        {
            METALS_INGESTED.put(metalType, nbt.getInt(metalType.name().toLowerCase() + "_ingested"));
        }
    }

    @Override
    public void tick()
    {
        //if server
        if (!livingEntity.world.isRemote)
        {
            //Login setup
            if (!didSetup)
            {
                syncToClients(null);
                didSetup = true;
            }

            //Tick
            for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
            {
                //don't tick powers that the user doesn't have
                //don't tick powers that are not active
                if (manifestation.isActive(this))
                {
                    manifestation.tick(this);
                }
            }

            //tick metals
            if (livingEntity.ticksExisted % 1200 == 0)
            {
                //metals can't stay in your system forever, y'know?
                for (Metals.MetalType metalType : Metals.MetalType.values())
                {
                    Integer metalIngestAmount = METALS_INGESTED.get(metalType);
                    if (metalIngestAmount > 0)
                    {
                        //todo decide how and when we poison the user for eating metal, that sure ain't safe champ.


                        //todo, decide what's appropriate for reducing ingested metal amounts
                        METALS_INGESTED.put(metalType, metalIngestAmount - 1);
                    }
                }


            }

            //tick stormlight
            if (stormlightStored > 0 && livingEntity.ticksExisted % 100 == 0)
            {
                //todo decide what's appropriate for reducing stormlight
                //maybe reducing cost based on how many ideals they have sworn?
                stormlightStored--;
            }

        }
        else//if client
        {
            AManifestation iron = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
            if (iron.isActive(this))
            {
                ((AllomancyIronSteel) iron).performEffect(this);
            }

            AManifestation steel = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();
            if (steel.isActive(this))
            {
                ((AllomancyIronSteel) steel).performEffect(this);
            }
        }
    }

    @Override
    public LivingEntity getLiving()
    {
        return livingEntity;
    }

    @Override
    public int getIngestedMetal(Metals.MetalType metalType)
    {
        return METALS_INGESTED.get(metalType);
    }

    @Override
    public boolean adjustIngestedMetal(Metals.MetalType metalType, int amountToAdjust, boolean doAdjust)
    {
        int ingestedMetal = getIngestedMetal(metalType);
        boolean addingToInternalMetals = amountToAdjust < 0;

        if (addingToInternalMetals || ingestedMetal > amountToAdjust)
        {
            if (doAdjust)
            {
                METALS_INGESTED.put(metalType, ingestedMetal - amountToAdjust);
            }

            return true;
        }

        return false;
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void renderWorldEffects(RenderWorldLastEvent event)
    {
        Multimap<Color, List<Vector3d>> linesToDrawByColor = LinkedHashMultimap.create();

        //if user has iron or steel manifestation
        if (hasManifestation(ManifestationTypes.ALLOMANCY, Metals.MetalType.IRON.getID())
                || hasManifestation(ManifestationTypes.ALLOMANCY, Metals.MetalType.STEEL.getID()))
        {
            AllomancyIronSteel iron = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
            AllomancyIronSteel steel = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();

            //is zero if the manifestation is not active.
            int range = Math.max(iron.getRange(this), steel.getRange(this));

            if (range > 0)
            {
                linesToDrawByColor.put(Color.BLUE, AllomancyIronSteel.getDrawLines(range));
            }
        }
        if (linesToDrawByColor.size() > 0)
        {
            Vector3d originPoint = getLiving().getClientEyePosition(Minecraft.getInstance().getRenderPartialTicks()).add(0, -1, 0);
            for (Map.Entry<Color, List<Vector3d>> entry : linesToDrawByColor.entries())
            {
                //For all found things, draw the line
                DrawUtils.drawLinesFromPoint(event, originPoint, entry.getValue(), entry.getKey());
            }
        }
    }


    public void renderSelectedHUD(MatrixStack ms)
    {
        Minecraft mc = Minecraft.getInstance();
        MainWindow mainWindow = mc.getMainWindow();
        int x = 10;
        int y = mainWindow.getScaledHeight() / 5;

        //todo translations
        String stringToDraw = "Selected Power: " + I18n.format(selectedManifestation.translation().getString());
        mc.fontRenderer.drawStringWithShadow(ms, stringToDraw, x + 18, y, 0xFF4444);

        int mode = getMode(selectedManifestation.getManifestationType(), selectedManifestation.getPowerID());
        String stringToDraw2 = "Mode: " + mode;
        mc.fontRenderer.drawStringWithShadow(ms, stringToDraw2, x + 18, y + 10, 0xFF4444);


        ItemStack stack;

        if (mode > 0)
        {
            stack = positiveActiveStack;
        }
        else if (mode < 0)
        {
            stack = negativeActiveStack;
        }
        else
        {
            stack = inactiveStack;
        }

        mc.getItemRenderer().renderItemAndEffectIntoGUI(stack, x, y);

    }

    @Override
    public void setSelectedManifestation(AManifestation manifestation)
    {
        selectedManifestation = manifestation;
    }

    public boolean hasAnyPowers()
    {
        for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
        {
            String path = manifestation.getRegistryName().getPath();

            if (!AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.containsKey(path))
            {
                continue;
            }

            RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.get(path);
            Attribute attribute = attributeRegistryObject.get();

            ModifiableAttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);
            if (manifestationAttribute == null)
            {
                continue;
            }

            if (manifestationAttribute.getValue() > 5)
            {
                return true;
            }
        }


        return false;
    }

    @Override
    public boolean hasManifestation(ManifestationTypes manifestationTypeID, int powerID)
    {
        return hasManifestation(manifestationTypeID, powerID, false);
    }

    @Override
    public boolean hasManifestation(ManifestationTypes manifestationTypeID, int powerID, boolean ignoreTemporaryPower)
    {
        AManifestation manifestation = manifestationTypeID.getManifestation(powerID);
        String manifestationName = manifestation.getRegistryName().getPath();
        if (!AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.containsKey(manifestationName))
        {
            return false;
        }

        Attribute attribute = AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.get(manifestationName).get();
        ModifiableAttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);

        if (manifestationAttribute != null)
        {
            double manifestationStrength =
                    ignoreTemporaryPower ? manifestationAttribute.getBaseValue() : manifestationAttribute.getValue();

            return manifestationStrength > 5;
        }
        return false;
    }


    @Override
    public void giveManifestation(ManifestationTypes manifestationTypeID, int powerID)
    {
        AManifestation manifestation = manifestationTypeID.getManifestation(powerID);

        String manifestationName = manifestation.getRegistryName().getPath();
        if (!AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.containsKey(manifestationName))
        {
            return;
        }
        RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.get(manifestationName);

        Attribute attribute = attributeRegistryObject.get();
        ModifiableAttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);

        if (manifestationAttribute != null)
        {
            manifestationAttribute.setBaseValue(10);

                /*AttributeModifier attributeModifier = AttributeHelper.makeAttribute(manifestationName, "inherent ", 10, AttributeModifier.Operation.ADDITION);
                manifestationAttribute.removeModifier(attributeModifier);
                manifestationAttribute.applyNonPersistentModifier(attributeModifier);*/

        }
    }

    @Override
    public void removeManifestation(ManifestationTypes manifestationTypeID, int powerID)
    {
        AManifestation manifestation = manifestationTypeID.getManifestation(powerID);
        String path = manifestation.getRegistryName().getPath();

        if (!AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.containsKey(path))
        {
            return;
        }

        RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.MANIFESTATION_STRENGTH_ATTRIBUTES.get(path);

        Attribute attribute = attributeRegistryObject.get();
        ModifiableAttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);
        if (manifestationAttribute != null)
        {
            manifestationAttribute.setBaseValue(0);
        }
    }

    @Override
    public boolean canTickManifestation(ManifestationTypes manifestationType, int powerID)
    {
        if (!hasManifestation(manifestationType, powerID))
        {
            return false;
        }

        int[] manifestationPowersModes = MANIFESTATIONS_MODE.get(manifestationType);

        if (manifestationType == ManifestationTypes.NONE || manifestationPowersModes.length == 0)
        {
            return false;
        }

        return manifestationPowersModes[powerID] != 0;
    }

    @Override
    public boolean canTickSelectedManifestation()
    {
        if (selectedManifestation == null)
        {
            return false;
        }

        return canTickManifestation(selectedManifestation.getManifestationType(), selectedManifestation.getPowerID());
    }

    @Override
    public void deactivateCurrentManifestation()
    {
        MANIFESTATIONS_MODE.get(selectedManifestation.getManifestationType())[selectedManifestation.getPowerID()] = 0;
    }

    @Override
    public void deactivateManifestations()
    {
        for (ManifestationTypes manifestationTypes : ManifestationTypes.values())
        {
            Arrays.fill(MANIFESTATIONS_MODE.get(manifestationTypes), 0);
        }
    }

    @Override
    public void clearManifestations()
    {
        deactivateManifestations();

        for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
        {
            removeManifestation(manifestation.getManifestationType(), manifestation.getPowerID());
        }
    }

    @Override
    public List<AManifestation> getAvailableManifestations()
    {
        return getAvailableManifestations(false);
    }

    @Override
    public List<AManifestation> getAvailableManifestations(boolean ignoreTemporaryPower)
    {
        List<AManifestation> list = new ArrayList<AManifestation>();

        //todo intelligently handle multiple powers
        for (int i = 0; i < 16; i++)
        {
            for (ManifestationTypes manifestationTypes : ManifestationTypes.values())
            {
                if (hasManifestation(manifestationTypes, i, ignoreTemporaryPower))
                {
                    list.add(manifestationTypes.getManifestation(i));
                }
            }
        }

        return list;
    }

    @Override
    public AManifestation manifestation()
    {
        return selectedManifestation;
    }


    @Override
    public AManifestation manifestation(ManifestationTypes manifestationType, int powerID)
    {
        return manifestationType.getManifestation(powerID);
    }

    @Override
    public String changeManifestation(int dir)
    {
        List<AManifestation> unlockedManifestations = getAvailableManifestations();

        if (selectedManifestation == ManifestationRegistry.NONE.get())
        {
            selectedManifestation = unlockedManifestations.get(0);
        }
        else
        {
            for (int index = 0; index < unlockedManifestations.size(); index++)
            {
                AManifestation manifestation = unlockedManifestations.get(index);
                if (selectedManifestation == manifestation)
                {
                    //found a match,
                    int selection = index;

                    selection += (dir < 0) ? 1 : -1;
                    selection = (selection + unlockedManifestations.size()) % unlockedManifestations.size();

                    selectedManifestation = unlockedManifestations.get(selection);
                    break;
                }

            }
        }
        return selectedManifestation.translation().getKey();
    }

    @Override
    public void setMode(ManifestationTypes manifestationTypeID, int powerID, int mode)
    {
        MANIFESTATIONS_MODE.get(manifestationTypeID)[powerID] = mode;
    }

    @Override
    public int getMode(ManifestationTypes manifestationTypeID, int powerID)
    {
        return MANIFESTATIONS_MODE.get(manifestationTypeID)[powerID];
    }

    @Override
    public int nextMode(ManifestationTypes manifestationType, int powerID)
    {

        AManifestation aim = manifestationType.getManifestation(powerID);

        int currentMode = getMode(manifestationType, powerID);

        if (aim.modeWraps(this) && currentMode == aim.modeMax(this))
        {
            int modeMin = aim.modeMin(this);
            this.setMode(manifestationType, powerID, modeMin);
            return modeMin;
        }

        int mode = Math.min(currentMode + 1, aim.modeMax(this));
        this.setMode(manifestationType, powerID, mode);

        return mode;
    }

    @Override
    public int previousMode(ManifestationTypes manifestationType, int powerID)
    {
        AManifestation aim = manifestationType.getManifestation(powerID);

        int currentMode = getMode(manifestationType, powerID);

        if (aim.modeWraps(this) && currentMode == aim.modeMin(this))
        {
            int modeMax = aim.modeMax(this);
            this.setMode(manifestationType, powerID, modeMax);
            return modeMax;
        }

        int mode = Math.max(currentMode - 1, aim.modeMin(this));
        this.setMode(manifestationType, powerID, mode);
        return mode;
    }

    @Override
    public void syncToClients(@Nullable ServerPlayerEntity serverPlayerEntity)
    {
        if (livingEntity != null && livingEntity.world.isRemote)
        {
            throw new IllegalStateException("Don't sync client -> server");
        }

        if (manifestation() == ManifestationRegistry.NONE.get())
        {
            //find first power
            Optional<AManifestation> first = getAvailableManifestations().stream().findFirst();
            first.ifPresent(this::setSelectedManifestation);
        }

        CompoundNBT nbt = serializeNBT();

        if (serverPlayerEntity == null)
        {
            Network.sendToAllInWorld(new SyncPlayerSpiritwebMessage(this.livingEntity.getEntityId(), nbt), (ServerWorld) livingEntity.world);
        }
        else
        {
            Network.sendTo(new SyncPlayerSpiritwebMessage(this.livingEntity.getEntityId(), nbt), serverPlayerEntity);
        }
    }
}
