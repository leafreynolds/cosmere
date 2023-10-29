/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.common.cap.entity;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.config.CosmereConfigs;
import leaf.cosmere.common.network.packets.SyncPlayerSpiritwebMessage;
import leaf.cosmere.common.registry.AttributesRegistry;
import leaf.cosmere.common.registry.GameEventRegistry;
import leaf.cosmere.common.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/*
    "The actual outlet of the power is not chosen by the practitioner, but instead is hardwritten into their Spiritweb"
    -Khriss
    https://coppermind.net/wiki/Ars_Arcanum#The_Alloy_of_Law
 */

public class SpiritwebCapability implements ISpiritweb
{
	//Injection
	public static final Capability<ISpiritweb> CAPABILITY = CapabilityManager.get(new CapabilityToken<>()
	{
	});

	//detect if capability has been set up yet
	private boolean didSetup = false;
	private boolean hasBeenInitialized = false;

	private final LivingEntity livingEntity;

	public final Map<Manifestation, Integer> MANIFESTATIONS_MODE = new HashMap<>();

	private Manifestation selectedManifestation = ManifestationRegistry.NONE.get();


	public List<BlockPos> pushBlocks = new ArrayList<>(4);
	public List<Integer> pushEntities = new ArrayList<>(4);

	public List<BlockPos> pullBlocks = new ArrayList<>(4);
	public List<Integer> pullEntities = new ArrayList<>(4);
	private CompoundTag nbt;

	private final Map<UUID, CosmereEffectInstance> activeEffects = Maps.newHashMap();

	private final Map<Manifestations.ManifestationTypes, ISpiritwebSubmodule> spiritwebSubmodules;


	public SpiritwebCapability(LivingEntity ent)
	{
		this.livingEntity = ent;
		spiritwebSubmodules = Cosmere.makeSpiritwebSubmodules();
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
		nbt.putString("selected_power", selectedManifestation.getRegistryName().toString());

		final CompoundTag modeNBT = new CompoundTag();
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			if (MANIFESTATIONS_MODE.containsKey(manifestation))
			{
				modeNBT.putInt(manifestation.getRegistryName().toString(), MANIFESTATIONS_MODE.get(manifestation));
			}
		}
		nbt.put("manifestation_modes", modeNBT);

		if (this.activeEffects.isEmpty())
		{
			nbt.remove("ActiveEffects");
		}
		else
		{
			ListTag listtag = new ListTag();

			for (CosmereEffectInstance cosmereEffectInstance : this.activeEffects.values())
			{
				listtag.add(cosmereEffectInstance.save(new CompoundTag()));
			}

			nbt.put("ActiveEffects", listtag);
		}

		for (ISpiritwebSubmodule spiritwebSubmodule : spiritwebSubmodules.values())
		{
			spiritwebSubmodule.serialize(this);
		}


		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag compoundTag)
	{
		this.nbt = compoundTag;

		hasBeenInitialized = compoundTag.getBoolean("assigned_powers");
		CompoundTag modeNBT = compoundTag.getCompound("manifestation_modes");

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			final String manifestationLoc = manifestation.getRegistryName().toString();

			int oldManifestationMode = MANIFESTATIONS_MODE.getOrDefault(manifestation, 0);
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
				manifestation.onModeChange(this, oldManifestationMode);
			}
		}
		selectedManifestation = ManifestationRegistry.fromID(compoundTag.getString("selected_power"));

		if (compoundTag.contains("ActiveEffects"))
		{
			//I think when you use 9 as a tag type, it means list of compound
			ListTag listtag = compoundTag.getList("ActiveEffects", 9);

			for (int i = 0; i < listtag.size(); ++i)
			{
				CompoundTag compoundtag = listtag.getCompound(i);
				CosmereEffectInstance cosmereEffectInstance = CosmereEffectInstance.load(compoundtag);
				if (cosmereEffectInstance != null)
				{
					this.activeEffects.put(cosmereEffectInstance.getUUID(), cosmereEffectInstance);
				}
			}
		}

		for (ISpiritwebSubmodule spiritwebSubmodule : spiritwebSubmodules.values())
		{
			spiritwebSubmodule.deserialize(this);
		}
	}

	@Override
	public CompoundTag getCompoundTag()
	{
		return nbt;
	}

	@Override
	public ISpiritwebSubmodule getSubmodule(Manifestations.ManifestationTypes manifestationType)
	{
		return spiritwebSubmodules.get(manifestationType);
	}

	@Override
	public Map<Manifestations.ManifestationTypes, ISpiritwebSubmodule> getSubmodules()
	{
		return spiritwebSubmodules;
	}

	@Override
	public void tickEffects()
	{
		Iterator<UUID> iterator = this.activeEffects.keySet().iterator();

		try
		{
			while (iterator.hasNext())
			{
				UUID uuidOfEffedInstance = iterator.next();
				CosmereEffectInstance cosmereEffectInstance = this.activeEffects.get(uuidOfEffedInstance);
				if (!cosmereEffectInstance.tick(this.getLiving()))
				{
					if (!this.getLiving().level.isClientSide)
					{
						iterator.remove();
						this.onEffectRemoved(cosmereEffectInstance);
					}
				}
				else if (cosmereEffectInstance.getDuration() % 600 == 0)
				{
					//this was copied from mob effect code, serverplayer
					// overrides the section to send effect packet updates
					//we don't do that, as everything on spiritweb gets synced at once
					this.onEffectUpdated(cosmereEffectInstance, false, (Entity) null);
				}
			}
		}
		catch (ConcurrentModificationException concurrentmodificationexception)
		{
			//ignore
		}
	}

	@Override
	public void addEffect(CosmereEffectInstance newEffect)
	{
		this.addEffect(newEffect, (Entity) null);
	}

	@Override
	public void addEffect(CosmereEffectInstance newEffect, @Nullable Entity sourceEntity)
	{
		CosmereEffectInstance cosmereEffectInstance = this.activeEffects.get(newEffect.getUUID());

		if (cosmereEffectInstance != null)
		{
			//remove old copy
			removeEffect(cosmereEffectInstance.getUUID());
		}

		this.activeEffects.put(newEffect.getUUID(), newEffect);
		this.onEffectAdded(newEffect, sourceEntity);
	}

	@Override
	public void onEffectAdded(CosmereEffectInstance effectInstance, Entity sourceEntity)
	{
		if (!this.getLiving().level.isClientSide)
		{
			effectInstance.applyAttributeModifiers(this.getLiving(), this.getLiving().getAttributes());
		}

		//todo do something with source entity here
	}

	@Override
	public void onEffectUpdated(CosmereEffectInstance cosmereEffectInstance, boolean forced, Entity entity)
	{
		final LivingEntity livingEntity = this.getLiving();
		if (forced && !livingEntity.level.isClientSide)
		{
			cosmereEffectInstance.removeAttributeModifiers(livingEntity.getAttributes());
			cosmereEffectInstance.applyAttributeModifiers(livingEntity, livingEntity.getAttributes());
		}
	}

	@Override
	public void removeEffect(UUID uuid)
	{
		if (!this.getLiving().level.isClientSide)
		{
			final CosmereEffectInstance effectInstance = this.activeEffects.remove(uuid);
			onEffectRemoved(effectInstance);
		}
	}

	@Override
	public void onEffectRemoved(CosmereEffectInstance cosmereEffectInstance)
	{
		if (!this.getLiving().level.isClientSide && cosmereEffectInstance != null)
		{
			cosmereEffectInstance.removeAttributeModifiers(this.getLiving().getAttributes());
		}
	}

	@Override
	public CosmereEffectInstance getEffect(UUID uuid)
	{
		return this.activeEffects.get(uuid);
	}

	//get the sum total strength of all matching effects in list of effects affecting target
	@Override
	public int totalStrengthOfEffect(CosmereEffect cosmereEffect)
	{
		return this.activeEffects.values().stream().filter(effectInstance -> effectInstance.getEffect() == cosmereEffect).mapToInt(o -> (int) o.getStrength()).sum();
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

			final LivingEntity spiritWebEntity = getLiving();
			if (selectedManifestation != ManifestationRegistry.NONE.get() && !hasManifestation(selectedManifestation))
			{
				selectedManifestation = ManifestationRegistry.NONE.get();
				if (spiritWebEntity instanceof ServerPlayer serverPlayer)
				{
					syncToClients(serverPlayer);
				}
			}

			boolean shouldTriggerSculkEvent = false;

			//Tick
			for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
			{
				//don't tick powers that the user doesn't have
				//don't tick powers that are not active
				if (manifestation.isActive(this))
				{
					//ordering here matters (: tick must always run
					shouldTriggerSculkEvent = manifestation.tick(this) || shouldTriggerSculkEvent;
				}
			}

			//An example from mekanism had the event triggering every two seconds
			if (shouldTriggerSculkEvent && (spiritWebEntity.tickCount % 40 == 0) && CosmereConfigs.SERVER_CONFIG.SCULK_CAN_HEAR_KINETIC_INVESTITURE.get())
			{
				// if the target is properly concealed, we don't trigger investiture game events
				final AttributeMap targetAttributes = spiritWebEntity.getAttributes();
				double concealmentStrength = 0;
				final Attribute cognitiveConcealmentAttr = AttributesRegistry.COGNITIVE_CONCEALMENT.get();
				if (targetAttributes.hasAttribute(cognitiveConcealmentAttr))
				{
					concealmentStrength = targetAttributes.getValue(cognitiveConcealmentAttr);
				}
				//but the concealment needs to be strong enough?
				//todo move this to a config so people can define how strong the concealment needs to be
				if (concealmentStrength < 2)
				{
					spiritWebEntity.gameEvent(GameEventRegistry.KINETIC_INVESTITURE.get());
				}
			}

			for (ISpiritwebSubmodule spiritwebSubmodule : spiritwebSubmodules.values())
			{
				spiritwebSubmodule.tickServer(this);
			}

			this.tickEffects();
		}
		else//if client
		{

			for (ISpiritwebSubmodule spiritwebSubmodule : spiritwebSubmodules.values())
			{
				spiritwebSubmodule.tickClient(this);
			}
		}
	}

	@Override
	public LivingEntity getLiving()
	{
		return livingEntity;
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
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			//attribute registry name is now the same as the manifestation registry name, so this function
			//doesn't need to be able to access the attribute registries of sub mods :)
			Attribute attribute = manifestation.getAttribute();
			if (attribute != null)
			{
				AttributeInstance oldAttr = oldAttMap.getInstance(attribute);
				AttributeInstance newAttr = newAttMap.getInstance(attribute);

				if (newAttr != null && oldAttr != null)
				{
					// make sure that they match what the old player entity had.
					if (oldAttr.getBaseValue() > 0)
					{
						newAttr.setBaseValue(oldAttr.getBaseValue());
					}
					//clear out the attributes that were placed on the newly cloned player at creation
					else if (newAttr.getBaseValue() > 0)
					{
						newAttr.setBaseValue(0);
					}
				}
			}
		}

		deserializeNBT(oldWeb.nbt.copy());
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void renderWorldEffects(RenderLevelStageEvent event)
	{
		for (ISpiritwebSubmodule spiritwebSubmodule : spiritwebSubmodules.values())
		{
			spiritwebSubmodule.renderWorldEffects(this, event);
		}
	}


	public void renderSelectedHUD(PoseStack ms)
	{
		Minecraft mc = Minecraft.getInstance();
		Window mainWindow = mc.getWindow();
		int x = 10;
		int y = mainWindow.getGuiScaledHeight() / 5;

		String stringToDraw = I18n.get(selectedManifestation.getTextComponent().getString());
		mc.font.drawShadow(ms, stringToDraw, x + 18, y, 0xFF4444);

		int mode = getMode(selectedManifestation);

		String stringToDraw2 = "";


		//todo migrate drawing text to manifestation, this shouldn't be in main module.
		if (selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.FERUCHEMY)
		{
			//todo translations
			if (mode < 0)
			{
				stringToDraw2 = "Mode: " + "Tapping " + mode;
			}
			else if (mode > 0)
			{
				stringToDraw2 = "Mode: " + "Storing " + mode;
			}
			else
			{
				//don't draw
				//stringToDraw2 = "";
			}
		}
		else if (selectedManifestation.getManifestationType() == Manifestations.ManifestationTypes.ALLOMANCY)
		{
			String rate;

			switch (mode)
			{
				case -2 -> rate = "Flared Compounding!";
				case -1 -> rate = "Compounding";
				default -> rate = "Off";
				case 1 -> rate = "Burning";
				case 2, 3 -> rate = "Flared!";//copper has a 3rd mode for only smoking self
			}
			stringToDraw2 = "Mode: " + rate;
		}

		//todo translations
		if (!stringToDraw2.isEmpty())
		{
			mc.font.drawShadow(ms, stringToDraw2, x + 18, y + 10, 0xFF4444);
		}
	}

	@Override
	public void setSelectedManifestation(Manifestation manifestation)
	{
		selectedManifestation = manifestation;
	}

	@Override
	public Manifestation getSelectedManifestation()
	{
		return selectedManifestation;
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
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			final Attribute attribute = manifestation.getAttribute();
			if (attribute == null)
			{
				continue;
			}

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
	public boolean hasManifestation(Manifestation manifestation)
	{
		return hasManifestation(manifestation, false);
	}

	@Override
	public boolean hasManifestation(Manifestation manifestation, boolean ignoreTemporaryPower)
	{
		final Attribute attribute = manifestation.getAttribute();
		if (attribute == null)
		{
			return false;
		}

		AttributeMap attributeManager = livingEntity.getAttributes();
		if (attributeManager.hasAttribute(attribute))
		{
			double manifestationStrength =
					ignoreTemporaryPower
					? attributeManager.getBaseValue(attribute)
					: attributeManager.getValue(attribute);
			return manifestationStrength > 2;
		}

		return false;
	}


	@Override
	public void giveManifestation(Manifestation manifestation, int baseValue)
	{
		final Attribute attribute = manifestation.getAttribute();
		if (attribute == null)
		{
			return;
		}
		AttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);

		if (manifestationAttribute != null)
		{
			manifestationAttribute.setBaseValue(baseValue);
		}

		hasBeenInitialized = true;
	}

	@Override
	public void removeManifestation(Manifestation manifestation)
	{
		final Attribute attribute = manifestation.getAttribute();
		if (attribute == null)
		{
			return;
		}

		AttributeInstance manifestationAttribute = livingEntity.getAttribute(attribute);
		if (manifestationAttribute != null)
		{
			manifestationAttribute.setBaseValue(0);
		}
	}

	@Override
	public boolean canTickManifestation(Manifestation manifestation)
	{
		if (!hasManifestation(manifestation))
		{
			return false;
		}

		if (MANIFESTATIONS_MODE.containsKey(manifestation))
		{
			return MANIFESTATIONS_MODE.get(manifestation) != 0;
		}

		return false;
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

		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			removeManifestation(manifestation);
		}
	}

	@Override
	public List<Manifestation> getAvailableManifestations()
	{
		return getAvailableManifestations(false);
	}

	@Override
	public List<Manifestation> getAvailableManifestations(boolean ignoreTemporaryPower)
	{
		List<Manifestation> list = new ArrayList<Manifestation>();

		//todo intelligently handle multiple powers
		for (Manifestation manifestation : CosmereAPI.manifestationRegistry())
		{
			if (manifestation == ManifestationRegistry.NONE.getManifestation())
			{
				continue;
			}

			if (hasManifestation(manifestation, ignoreTemporaryPower))
			{
				list.add(manifestation);
			}
		}

		return list;
	}

	@Override
	public String changeManifestation(int dir)
	{
		List<Manifestation> unlockedManifestations = getAvailableManifestations();

		if (selectedManifestation == ManifestationRegistry.NONE.get())
		{
			selectedManifestation = unlockedManifestations.get(0);
		}
		else
		{
			for (int index = 0; index < unlockedManifestations.size(); index++)
			{
				Manifestation manifestation = unlockedManifestations.get(index);
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
		return selectedManifestation.getTranslationKey();
	}

	@Override
	public void setMode(Manifestation manifestation, int mode)
	{
		final int pMax = manifestation.modeMax(this);
		final int pMin = manifestation.modeMin(this);
		mode = Mth.clamp(mode, pMin, pMax);
		int lastMode = MANIFESTATIONS_MODE.put(manifestation, mode);
		manifestation.onModeChange(this, lastMode);
	}

	@Override
	public int getMode(Manifestation manifestation)
	{
		if (manifestation != null)
		{
			return MANIFESTATIONS_MODE.getOrDefault(manifestation, 0);
		}
		return 0;
	}

	@Override
	public int nextMode(Manifestation aim)
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
	public int previousMode(Manifestation aim)
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

		if (getSelectedManifestation() == ManifestationRegistry.NONE.get())
		{
			//find first power
			Optional<Manifestation> first = getAvailableManifestations().stream().findFirst();
			first.ifPresent(this::setSelectedManifestation);
		}

		CompoundTag nbt = serializeNBT();

		if (serverPlayerEntity == null)
		{
			Cosmere.packetHandler().sendToAllInWorld(new SyncPlayerSpiritwebMessage(this.livingEntity.getId(), nbt), (ServerLevel) livingEntity.level);
		}
		else
		{
			Cosmere.packetHandler().sendTo(new SyncPlayerSpiritwebMessage(this.livingEntity.getId(), nbt), serverPlayerEntity);
		}
	}
}
