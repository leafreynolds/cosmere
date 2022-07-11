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
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.client.gui.DrawUtils;
import leaf.cosmere.constants.Manifestations.ManifestationTypes;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyIronSteel;
import leaf.cosmere.network.Network;
import leaf.cosmere.network.packets.SyncPlayerSpiritwebMessage;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;

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
	final static ItemStack positiveActiveStack = Items.SOUL_TORCH.getDefaultInstance();
	final static ItemStack negativeActiveStack = Items.REDSTONE_TORCH.getDefaultInstance();
	final static ItemStack inactiveStack = Items.STICK.getDefaultInstance();
	//endregion


	//Injection
	public static final Capability<ISpiritweb> CAPABILITY = CapabilityManager.get(new CapabilityToken<>() { });

	//detect if capability has been set up yet
	private boolean didSetup = false;
	private boolean hasBeenInitialized = false;

	private final LivingEntity livingEntity;

	public final Map<AManifestation, Integer> MANIFESTATIONS_MODE = new HashMap<>();

	private AManifestation selectedManifestation = ManifestationRegistry.NONE.get();

	//biochromatic breaths stored.
	//todo, figure out how the passive buff works
	private int biochromaticBreathStored = 0;

	//stormlight stored

	private int stormlightStored = 0;
	private int eyeHeight = 0;

	//metals ingested
	public final Map<Metals.MetalType, Integer> METALS_INGESTED =
			Arrays.stream(Metals.MetalType.values())
					.collect(Collectors.toMap(Function.identity(), type -> 0));

	public List<BlockPos> pushBlocks = new ArrayList<>(4);
	public List<Integer> pushEntities = new ArrayList<>(4);

	public List<BlockPos> pullBlocks = new ArrayList<>(4);
	public List<Integer> pullEntities = new ArrayList<>(4);
	private CompoundTag nbt;


	public SpiritwebCapability(LivingEntity ent)
	{
		this.livingEntity = ent;
	}


	@Nonnull
	public static LazyOptional<ISpiritweb> get(LivingEntity entity)
	{
		return entity != null ? entity.getCapability(SpiritwebCapability.CAPABILITY, null)
		                      : LazyOptional.empty();
	}

	@Override
	public CompoundTag serializeNBT()
	{
		if (this.nbt == null)
		{
			this.nbt = new CompoundTag();
		}

		nbt.putBoolean("assigned_powers", hasBeenInitialized);
		nbt.putString("selected_power", selectedManifestation.getResourceLocation().toString());
		nbt.putInt("stored_breaths", biochromaticBreathStored);
		nbt.putInt("stored_stormlight", stormlightStored);
		nbt.putInt("eye_height", eyeHeight);

		final CompoundTag modeNBT = new CompoundTag();
		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			if (MANIFESTATIONS_MODE.containsKey(manifestation))
			{
				modeNBT.putInt(manifestation.getResourceLocation().toString(), MANIFESTATIONS_MODE.get(manifestation));
			}
		}
		nbt.put("manifestation_modes", modeNBT);

		final CompoundTag ingestedMetals = new CompoundTag();
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			final Integer ingestedMetalAmount = METALS_INGESTED.get(metalType);
			if (ingestedMetalAmount > 0)
			{
				ingestedMetals.putInt(metalType.getName() + "_ingested", ingestedMetalAmount);
			}
		}
		nbt.put("ingested_metals", ingestedMetals);

		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt)
	{
		this.nbt = nbt;

		hasBeenInitialized = nbt.getBoolean("assigned_powers");
		CompoundTag modeNBT = nbt.getCompound("manifestation_modes");

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			final String manifestationLoc = manifestation.getResourceLocation().toString();

			int oldManifestationMode = MANIFESTATIONS_MODE.getOrDefault(manifestation,0);
			int newManifestationMode = 0;

			if (modeNBT.contains(manifestationLoc))
			{
				newManifestationMode = modeNBT.getInt(manifestationLoc);
				MANIFESTATIONS_MODE.put(manifestation, newManifestationMode);
			}
			else
			{
				MANIFESTATIONS_MODE.remove(manifestation);
			}

			if (getLiving().level.isClientSide && oldManifestationMode != newManifestationMode)
			{
				manifestation.onModeChange(this);
			}
		}
		selectedManifestation = ManifestationRegistry.fromID(nbt.getString("selected_power"));

		biochromaticBreathStored = nbt.getInt("stored_breaths");
		stormlightStored = nbt.getInt("stored_stormlight");
		eyeHeight = nbt.getInt("eye_height");

		final CompoundTag ingestedMetals = nbt.getCompound("ingested_metals");
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			final String metalKey = metalType.getName() + "_ingested";
			if (ingestedMetals.contains(metalKey))
			{
				final int ingestedMetalAmount = ingestedMetals.getInt(metalKey);
				METALS_INGESTED.put(metalType, ingestedMetalAmount);
			}
			else
			{
				METALS_INGESTED.put(metalType, 0);
			}
		}
	}

	@Override
	public CompoundTag getNBT()
	{
		return nbt;
	}

	@Override
	public void tick()
	{
		//if server
		if (!livingEntity.level.isClientSide)
		{
			//Login sync
			if (!didSetup)
			{
				syncToClients(null);
				didSetup = true;
			}

			if (selectedManifestation != ManifestationRegistry.NONE.get() && !hasManifestation(selectedManifestation))
			{
				selectedManifestation = ManifestationRegistry.NONE.get();
				if (getLiving() instanceof ServerPlayer)
				{
					syncToClients((ServerPlayer) getLiving());
				}
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
			if (livingEntity.tickCount % 1200 == 0)
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
			if (stormlightStored > 0 && livingEntity.tickCount % 100 == 0)
			{
				//todo decide what's appropriate for reducing stormlight
				//maybe reducing cost based on how many ideals they have sworn?
				stormlightStored--;
			}

		}
		else//if client
		{
			//Iron allomancy
			{
				AllomancyIronSteel iron = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
				final boolean ironActive = iron.isActive(this);

				if (ironActive)
				{
					iron.applyEffectTick(this);
				}
			}

			//steel allomancy
			{
				AllomancyIronSteel steel = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();
				final boolean steelActive = steel.isActive(this);

				if (steelActive)
				{
					steel.applyEffectTick(this);
				}
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

		if (addingToInternalMetals || ingestedMetal >= amountToAdjust)
		{
			if (doAdjust)
			{
				METALS_INGESTED.put(metalType, ingestedMetal - amountToAdjust);
			}

			return true;
		}

		return false;
	}

	//Copy things from an old spiritweb into the new one.
	//Eg a player has died and we need to make sure they get their stormlight and breaths back.
	@Override
	public void transferFrom(ISpiritweb oldSpiritWeb)
	{
		var oldWeb = (SpiritwebCapability) oldSpiritWeb;

		//TODO config options that let you choose what can be transferred

		//transfer attributes
		var oldAttMap = oldWeb.getLiving().getAttributes();
		var newAttMap = getLiving().getAttributes();

		// A player's manifestations is now determined by attributes, which lets me do cool things like mess with strength in a power.
		// So if we've set a base value for an attribute on the player, copy it to the new one.
		for (RegistryObject<Attribute> attributeRegistryObject : AttributesRegistry.COSMERE_ATTRIBUTES.values())
		{
			if (attributeRegistryObject != null && attributeRegistryObject.isPresent())
			{
				AttributeInstance oldPlayerAttribute = oldAttMap.getInstance(attributeRegistryObject.get());
				AttributeInstance newPlayerAttribute = newAttMap.getInstance(attributeRegistryObject.get());

				if (newPlayerAttribute != null && oldPlayerAttribute != null)
				{
					// make sure that they match what the old player entity had.
					if (oldPlayerAttribute.getBaseValue() > 0)
					{
						newPlayerAttribute.setBaseValue(oldPlayerAttribute.getBaseValue());
					}
					//clear out the attributes that were placed on the newly cloned player at creation
					else if (newPlayerAttribute.getBaseValue() > 0)
					{
						newPlayerAttribute.setBaseValue(0);
					}
				}
			}
		}

		deserializeNBT(oldWeb.nbt.copy());
	}

	@Override
	public int getEyeHeight()
	{
		return eyeHeight;
	}

	@Override
	public void setEyeHeight(int eyeHeight)
	{
		this.eyeHeight = eyeHeight;
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderWorldEffects(RenderLevelLastEvent event)
	{
		Multimap<Color, List<Vec3>> linesToDrawByColor = LinkedHashMultimap.create();
		AllomancyIronSteel ironAllomancy = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
		AllomancyIronSteel steelAllomancy = (AllomancyIronSteel) ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();

		//if user has iron or steel manifestation
		if (hasManifestation(ironAllomancy) || hasManifestation(steelAllomancy))
		{
			//is zero if the manifestation is not active.
			int range = Math.max(ironAllomancy.getRange(this), steelAllomancy.getRange(this));

			if (range > 0)
			{
				Minecraft.getInstance().getProfiler().push("cosmere-getDrawLines");
				linesToDrawByColor.put(Color.BLUE, AllomancyIronSteel.getDrawLines(range));
				Minecraft.getInstance().getProfiler().pop();
			}
		}
		if (linesToDrawByColor.size() > 0)
		{
			Vec3 originPoint = getLiving().getLightProbePosition(Minecraft.getInstance().getFrameTime()).add(0, -1, 0);
			PoseStack matrixStack = event.getPoseStack();
			DrawUtils.drawLinesFromPoint(matrixStack,originPoint,linesToDrawByColor);
		}
	}


	public void renderSelectedHUD(PoseStack ms)
	{
		Minecraft mc = Minecraft.getInstance();
		Window mainWindow = mc.getWindow();
		int x = 10;
		int y = mainWindow.getGuiScaledHeight() / 5;

		//todo translations
		String stringToDraw = I18n.get(selectedManifestation.translation().getString());
		mc.font.drawShadow(ms, stringToDraw, x + 18, y, 0xFF4444);

		int mode = getMode(selectedManifestation);

		String stringToDraw2 = "";

		if (selectedManifestation.getManifestationType() == ManifestationTypes.FERUCHEMY)
		{
			stringToDraw2 = "Mode: " + (mode < 0 ? "Tapping " : "Storing ") + mode;
		}
		else if (selectedManifestation.getManifestationType() == ManifestationTypes.ALLOMANCY)
		{
			String rate;
			if (mode <=0)
			{
				rate = "Off";
			}
			else if (mode == 1)
			{
				rate = "Burning";
			}
			else// if (mode >= 3)
			{
				rate = "Flared!";
			}

			stringToDraw2 = "Mode: " + rate;
		}
		else
		{
			stringToDraw2 = "Mode: " + mode;
		}

		mc.font.drawShadow(ms, stringToDraw2, x + 18, y + 10, 0xFF4444);


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

		mc.getItemRenderer().renderAndDecorateItem(stack, x, y);

	}

	@Override
	public void setSelectedManifestation(AManifestation manifestation)
	{
		selectedManifestation = manifestation;
	}

	public boolean hasBeenInitialized()
	{
		return hasBeenInitialized;
	}

	public void setHasBeenInitialized()
	{
		hasBeenInitialized = true;
	}

	public boolean hasAnyPowers()
	{
		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			final RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
			if (attributeRegistryObject == null || !attributeRegistryObject.isPresent())
			{
				continue;
			}
			Attribute attribute = attributeRegistryObject.get();

			AttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);
			if (manifestationAttribute == null)
			{
				continue;
			}

			if (manifestationAttribute.getValue() > 5)
			{
				setHasBeenInitialized();
				return true;
			}
		}

		setHasBeenInitialized();
		return false;
	}

	@Override
	public boolean hasManifestation(AManifestation aManifestation)
	{
		return hasManifestation(aManifestation, false);
	}

	@Override
	public boolean hasManifestation(AManifestation manifestation, boolean ignoreTemporaryPower)
	{
		final RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
		if (attributeRegistryObject == null || !attributeRegistryObject.isPresent())
		{
			return false;
		}

		AttributeMap attributeManager = livingEntity.getAttributes();
		Attribute attribute = attributeRegistryObject.get();
		if (attributeManager.hasAttribute(attribute))
		{
			double manifestationStrength =
					ignoreTemporaryPower
					? attributeManager.getBaseValue(attribute)
					: attributeManager.getValue(attribute);
			return manifestationStrength > 3;
		}

		return false;
	}


	@Override
	public void giveManifestation(AManifestation manifestation, int i)
	{
		final RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
		if (attributeRegistryObject == null || !attributeRegistryObject.isPresent())
		{
			return;
		}
		Attribute attribute = attributeRegistryObject.get();
		AttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);

		if (manifestationAttribute != null)
		{
			manifestationAttribute.setBaseValue(10);
		}

		hasBeenInitialized = true;
	}

	@Override
	public void removeManifestation(AManifestation manifestation)
	{
		final RegistryObject<Attribute> attributeRegistryObject = manifestation.getAttribute();
		if (attributeRegistryObject == null || !attributeRegistryObject.isPresent())
		{
			return;
		}

		Attribute attribute = attributeRegistryObject.get();
		AttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);
		if (manifestationAttribute != null)
		{
			manifestationAttribute.setBaseValue(0);
		}
	}

	@Override
	public boolean canTickManifestation(AManifestation aManifestation)
	{
		if (!hasManifestation(aManifestation))
		{
			return false;
		}

		if (MANIFESTATIONS_MODE.containsKey(aManifestation))
		{
			return MANIFESTATIONS_MODE.get(aManifestation) != 0;
		}

		return false;
	}

	@Override
	public boolean canTickSelectedManifestation()
	{
		if (selectedManifestation == null)
		{
			return false;
		}

		return canTickManifestation(selectedManifestation);
	}

	@Override
	public void deactivateCurrentManifestation()
	{
		MANIFESTATIONS_MODE.remove(selectedManifestation);
	}

	@Override
	public void deactivateManifestations()
	{
		MANIFESTATIONS_MODE.clear();
	}

	@Override
	public void clearManifestations()
	{
		deactivateManifestations();

		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			removeManifestation(manifestation);
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
		for (AManifestation manifestation : ManifestationRegistry.MANIFESTATION_REGISTRY.get())
		{
			if (manifestation == ManifestationRegistry.NONE.get())
				continue;

			if (hasManifestation(manifestation, ignoreTemporaryPower))
			{
				list.add(manifestation);
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
		return selectedManifestation.translationKey();
	}

	@Override
	public void setMode(AManifestation aManifestation, int mode)
	{
		final int pMax = aManifestation.modeMax(this);
		final int pMin = aManifestation.modeMin(this);
		mode = Mth.clamp(mode, pMin, pMax);
		MANIFESTATIONS_MODE.put(aManifestation, mode);
		aManifestation.onModeChange(this);
	}

	@Override
	public int getMode(AManifestation aManifestation)
	{
		return MANIFESTATIONS_MODE.getOrDefault(aManifestation, 0);
	}

	@Override
	public int nextMode(AManifestation aim)
	{
		int currentMode = getMode(aim);

		if (aim.modeWraps(this) && currentMode == aim.modeMax(this))
		{
			int modeMin = aim.modeMin(this);
			this.setMode(aim, modeMin);
			return modeMin;
		}

		int mode = Math.min(currentMode + 1, aim.modeMax(this));
		this.setMode(aim, mode);

		return mode;
	}

	@Override
	public int previousMode(AManifestation aim)
	{
		int currentMode = getMode(aim);

		if (aim.modeWraps(this) && currentMode == aim.modeMin(this))
		{
			int modeMax = aim.modeMax(this);
			this.setMode(aim, modeMax);
			return modeMax;
		}

		int mode = Math.max(currentMode - 1, aim.modeMin(this));
		this.setMode(aim, mode);
		return mode;
	}

	@Override
	public void syncToClients(@Nullable ServerPlayer serverPlayerEntity)
	{
		if (livingEntity != null && livingEntity.level.isClientSide)
		{
			throw new IllegalStateException("Don't sync client -> server");
		}

		if (manifestation() == ManifestationRegistry.NONE.get())
		{
			//find first power
			Optional<AManifestation> first = getAvailableManifestations().stream().findFirst();
			first.ifPresent(this::setSelectedManifestation);
		}

		CompoundTag nbt = serializeNBT();

		if (serverPlayerEntity == null)
		{
			Network.sendToAllInWorld(new SyncPlayerSpiritwebMessage(this.livingEntity.getId(), nbt), (ServerLevel) livingEntity.level);
		}
		else
		{
			Network.sendTo(new SyncPlayerSpiritwebMessage(this.livingEntity.getId(), nbt), serverPlayerEntity);
		}
	}
}
