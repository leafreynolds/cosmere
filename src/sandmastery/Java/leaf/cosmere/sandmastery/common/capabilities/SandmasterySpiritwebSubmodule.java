/*
 * File updated ~ 18 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.capabilities;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.effects.DehydratedEffect;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.network.packets.SyncMasteryBindsMessage;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEffects;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

public class SandmasterySpiritwebSubmodule implements ISpiritwebSubmodule
{
	private CompoundTag sandmasteryTag = null;
	private int hydrationLevel = SandmasteryConfigs.SERVER.STARTING_HYDRATION.get();
	private int projectileCooldown = 0;
	private final LinkedList<SandmasteryManifestation> ribbonsInUse = new LinkedList<>();

	private int hotkeyFlags = 0;

	public static SandmasterySpiritwebSubmodule get(ISpiritweb data)
	{
		return (SandmasterySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.SANDMASTERY);
	}

	@Override
	public void tickClient(ISpiritweb spiritweb)
	{
		if (spiritweb.getSelectedManifestation() instanceof SandmasteryManifestation sandmasteryManifestation)
		{
			final int isActivatedAndActive =
					Keybindings.MANIFESTATION_USE_ACTIVE.isDown()
					? 1
					: 0;

			final int elevateFlag =
					SandmasteryKeybindings.SANDMASTERY_ELEVATE.isDown()
					? SandmasteryConstants.ELEVATE_HOTKEY_FLAG
					: 0;

			final int launchFlag =
					SandmasteryKeybindings.SANDMASTERY_LAUNCH.isDown()
					? SandmasteryConstants.LAUNCH_HOTKEY_FLAG
					: 0;

			final int projectileFlag =
					SandmasteryKeybindings.SANDMASTERY_PROJECTILE.isDown()
					? SandmasteryConstants.PROJECTILE_HOTKEY_FLAG
					: 0;

			final int platformFlag =
					SandmasteryKeybindings.SANDMASTERY_PLATFORM.isDown()
					? SandmasteryConstants.PLATFORM_HOTKEY_FLAG
					: 0;

			int currentFlags = 0;
			currentFlags = currentFlags + isActivatedAndActive;
			currentFlags = currentFlags + elevateFlag;
			currentFlags = currentFlags + launchFlag;
			currentFlags = currentFlags + projectileFlag;
			currentFlags = currentFlags + platformFlag;

			if (hotkeyFlags != currentFlags)
			{
				sandmasteryTag.putInt(SandmasteryConstants.HOTKEY_TAG, currentFlags);
				hotkeyFlags = currentFlags;
				Sandmastery.packetHandler().sendToServer(new SyncMasteryBindsMessage(currentFlags));
			}
		}
		//if we are only tracking hotkeys when a sandmastery manifestation is selected
		//then things turn off when not selecting one. Would that be correct behaviour?
		//Todo more elegant way of checking if the user is wanting to use sandmastery?
		else if (hotkeyFlags != 0)
		{
			//don't create references unless needed
			//final CompoundTag dataTag = spiritweb.getCompoundTag();
			//reset flag
			hotkeyFlags = 0;
			//save
			sandmasteryTag.putInt(SandmasteryConstants.HOTKEY_TAG, hotkeyFlags);
			//update server
			Sandmastery.packetHandler().sendToServer(new SyncMasteryBindsMessage(hotkeyFlags));
		}
	}

	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		//offload to tick 19 out of 20
		if ((spiritweb.getLiving().tickCount - 1) % 20 != 0)
		{
			return;
		}

		//let the spiritweb submodule check for dehydration and handle the effect
		double percentage = (((double) this.getHydrationLevel()) / ((double) SandmasteryConfigs.SERVER.MAX_HYDRATION.get())) * 100;
		if (percentage <= SandmasteryConfigs.SERVER.DEHYDRATION_THRESHOLD.get())
		{
			spiritweb.addEffect(EffectsHelper.getNewEffect(SandmasteryEffects.DEHYDRATED_EFFECT.get(), spiritweb.getLiving(), 1, 33));
		}
	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();
		//save a reference to the tag
		sandmasteryTag = CompoundNBTHelper.getOrCreate(compoundTag, Sandmastery.MODID);
		//unload the player specific fields
		hydrationLevel = sandmasteryTag.getInt(SandmasteryConstants.HYDRATION_TAG);
		projectileCooldown = sandmasteryTag.getInt(SandmasteryConstants.PROJECTILE_COOLDOWN_TAG);
		hotkeyFlags = sandmasteryTag.getInt(SandmasteryConstants.HOTKEY_TAG);
	}

	@Override
	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();

		if (sandmasteryTag == null)
		{
			sandmasteryTag = CompoundNBTHelper.getOrCreate(compoundTag, Sandmastery.MODID);
		}

		sandmasteryTag.putInt(SandmasteryConstants.HYDRATION_TAG, hydrationLevel);
		sandmasteryTag.putInt(SandmasteryConstants.PROJECTILE_COOLDOWN_TAG, projectileCooldown);
		sandmasteryTag.putInt(SandmasteryConstants.HOTKEY_TAG, hotkeyFlags);

		//this shouldn't be necessary, as the spiritweb tag should already have the reference
		//but we are hunting a null ref, so maybe something gets unassigned somewhere
		compoundTag.put(Sandmastery.MODID, sandmasteryTag);
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void collectMenuInfo(List<String> m_infoText)
	{
		//todo Localization
		final String text = "Hydration: " + getHydrationLevel();
		m_infoText.add(text);
	}

	@Override
	public void GiveStartingItem(Player player)
	{
	}

	@Override
	public void GiveStartingItem(Player player, Manifestation manifestation)
	{
	}

	public int getHydrationLevel()
	{
		return hydrationLevel;
	}

	public void adjustHydration(int amountToAdjust, boolean allowOvermastery, ISpiritweb data)
	{
		int hydration = getHydrationLevel();
		final int newHydrationValue = hydration + amountToAdjust;
		hydrationLevel = Mth.clamp(newHydrationValue, 0, SandmasteryConfigs.SERVER.MAX_HYDRATION.get());

		if (allowOvermastery && newHydrationValue < 0)
		{
			overmaster(data);
		}
	}

	public void adjustHydration(int amountToAdjust)
	{
		int hydration = hydrationLevel;
		hydrationLevel = Mth.clamp(hydration + amountToAdjust, 0, SandmasteryConfigs.SERVER.MAX_HYDRATION.get());
	}

	private static void overmaster(ISpiritweb data)
	{
		AttributeInstance availableRibbons = data.getLiving().getAttribute(SandmasteryAttributes.RIBBONS.getAttribute());

		if (availableRibbons == null)
		{
			return;
		}

		int ribbons = (int) availableRibbons.getBaseValue();
		int gainedRibbons;

		if (availableRibbons.getModifier(SandmasteryAttributes.OVERMASTERY_UUID) == null)
		{
			if (ribbons < 5)
			{
				gainedRibbons = 3;
			}
			else if (ribbons < 10)
			{
				gainedRibbons = 4;
			}
			else if (ribbons < 15)
			{
				gainedRibbons = 5;
			}
			else
			{
				gainedRibbons = 6;
			}

			final AttributeModifier overmasteryAttributeModifier = getOvermasteryAttributeModifier(gainedRibbons, SandmasteryAttributes.OVERMASTERY_UUID);

			availableRibbons.addPermanentModifier(overmasteryAttributeModifier);
		}
		else if (availableRibbons.getModifier(SandmasteryAttributes.OVERMASTERY_SECONDARY_UUID) == null)
		{
			if (ribbons < 5)
			{
				gainedRibbons = 1;
			}
			else if (ribbons < 10)
			{
				gainedRibbons = 2;
			}
			else if (ribbons < 15)
			{
				gainedRibbons = 3;
			}
			else
			{
				gainedRibbons = 4;
			}

			final AttributeModifier overmasteryAttributeModifier = getOvermasteryAttributeModifier(gainedRibbons, SandmasteryAttributes.OVERMASTERY_SECONDARY_UUID);
			availableRibbons.addPermanentModifier(overmasteryAttributeModifier);
		}

		//damage and disable powers regardless
		data.getLiving().hurt(DehydratedEffect.DEHYDRATED, 10F);
		data.addEffect(EffectsHelper.getNewEffect(SandmasteryEffects.OVERMASTERED_EFFECT.get(), data.getLiving(), 1, SandmasteryConfigs.SERVER.OVERMASTERY_DURATION.get() * 20 * 60)); //  * 20 * 60 to convert minutes to ticks
	}

	@NotNull
	private static AttributeModifier getOvermasteryAttributeModifier(int gainedRibbons, UUID uuid)
	{
		final AttributeModifier overmasteryAttributeModifier = new AttributeModifier(
				SandmasteryAttributes.OVERMASTERY_SECONDARY_UUID,
				String.format("%s - gained %s ribbons: %s", "Overmastery", gainedRibbons, uuid),
				gainedRibbons,
				AttributeModifier.Operation.ADDITION
		);
		return overmasteryAttributeModifier;
	}

	public void tickProjectileCooldown()
	{
		this.projectileCooldown -= this.projectileCooldown < 1 ? 0 : 1;
	}

	public void setProjectileCooldown(int cooldown)
	{
		this.projectileCooldown = cooldown;
	}

	public boolean projectileReady()
	{
		return this.projectileCooldown <= 0;
	}


	public void useRibbon(ISpiritweb data, SandmasteryManifestation manifestation)
	{
		int maxRibbons = (int) manifestation.getStrength(data, false);
		if (ribbonsInUse.size() >= maxRibbons)
		{
			SandmasteryManifestation ribbon = ribbonsInUse.getLast();
			data.setMode(ribbon, data.getMode(ribbon) - 1);
		}
		ribbonsInUse.addFirst(manifestation);
		data.syncToClients(null);
	}

	public void releaseRibbon(ISpiritweb data, SandmasteryManifestation manifestation)
	{
		int index = ribbonsInUse.indexOf(manifestation);
		if (index > -1)
		{
			ribbonsInUse.remove(index);
		}
		data.syncToClients(null);
	}

	public void debugRibbonUsage()
	{
		CosmereAPI.logger.info("Ribbons in use ");
		CosmereAPI.logger.info(ribbonsInUse.toString());
	}

	public void updateFlags(int flags)
	{
		this.hotkeyFlags = flags;
		//update the tag value for later serialization.
		this.sandmasteryTag.putInt("hotkeys", hotkeyFlags);
	}
}
