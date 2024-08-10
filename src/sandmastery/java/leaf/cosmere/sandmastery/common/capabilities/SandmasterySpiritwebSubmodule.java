/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.capabilities;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.Keybindings;
import leaf.cosmere.sandmastery.client.SandmasteryKeybindings;
import leaf.cosmere.sandmastery.common.Sandmastery;
import leaf.cosmere.sandmastery.common.config.SandmasteryConfigs;
import leaf.cosmere.sandmastery.common.manifestation.SandmasteryManifestation;
import leaf.cosmere.sandmastery.common.network.packets.SyncMasteryBindsMessage;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryEffects;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import leaf.cosmere.sandmastery.common.utils.SandmasteryConstants;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class SandmasterySpiritwebSubmodule implements ISpiritwebSubmodule
{
	private CompoundTag sandmasteryTag = null;
	private int hydrationLevel = SandmasteryConfigs.SERVER.STARTING_HYDRATION.get();
	private int projectileCooldown = 0;
	private int launchCooldown = 0;
	private int launchesSinceLeftGround = 0;
	private int numRibbonsInUse = 0;

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
		launchCooldown = sandmasteryTag.getInt(SandmasteryConstants.LAUNCH_COOLDOWN_TAG);
		launchesSinceLeftGround = sandmasteryTag.getInt(SandmasteryConstants.LAUNCHES_SINCE_FLOOR_TAG);
		hotkeyFlags = sandmasteryTag.getInt(SandmasteryConstants.HOTKEY_TAG);
		numRibbonsInUse = sandmasteryTag.getInt(SandmasteryConstants.RIBBONS_IN_USE_TAG);
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
		sandmasteryTag.putInt(SandmasteryConstants.LAUNCH_COOLDOWN_TAG, launchCooldown);
		sandmasteryTag.putInt(SandmasteryConstants.LAUNCHES_SINCE_FLOOR_TAG, launchesSinceLeftGround);
		sandmasteryTag.putInt(SandmasteryConstants.HOTKEY_TAG, hotkeyFlags);
		sandmasteryTag.putInt(SandmasteryConstants.RIBBONS_IN_USE_TAG, numRibbonsInUse);

		//this shouldn't be necessary, as the spiritweb tag should already have the reference
		//but we are hunting a null ref, so maybe something gets unassigned somewhere
		compoundTag.put(Sandmastery.MODID, sandmasteryTag);
	}

	@Override
	public void resetOnDeath(ISpiritweb spiritweb)
	{
		hydrationLevel = SandmasteryConfigs.SERVER.STARTING_HYDRATION.get();
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
		if (SandmasteryConfigs.SERVER.GIVE_QIDO_ON_FIRST_LOGIN.get())
		{
			Random r = new Random();
			ItemStack qido = SandmasteryItems.QIDO_ITEM.asItem().getChargedQido(r.nextFloat());
			PlayerHelper.addItem(player, qido);
		}
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
		final LivingEntity living = data.getLiving();
		AttributeInstance availableRibbons = living.getAttribute(SandmasteryAttributes.RIBBONS.getAttribute());

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
		//todo replace dryout
		living.hurt(living.damageSources().dryOut(), 10F);
		data.addEffect(EffectsHelper.getNewEffect(SandmasteryEffects.OVERMASTERED_EFFECT.get(), living, 1, SandmasteryConfigs.SERVER.OVERMASTERY_DURATION.get() * 20 * 60)); //  * 20 * 60 to convert minutes to ticks
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

	public void tickLaunchCooldown()
	{
		this.launchCooldown -= this.launchCooldown < 1 ? 0 : 1;
	}

	public void setProjectileCooldown(int cooldown)
	{
		this.projectileCooldown = cooldown;
	}

	public void setLaunchCooldown(int cooldown)
	{
		this.launchCooldown = cooldown;
	}

	public boolean projectileReady()
	{
		return this.projectileCooldown <= 0;
	}

	public boolean launchReady()
	{
		return this.launchCooldown <= 0;
	}

	public void addLaunch()
	{
		this.launchesSinceLeftGround += 1;
	}

	public int getLaunches()
	{
		return this.launchesSinceLeftGround;
	}

	public void resetLaunches()
	{
		this.launchesSinceLeftGround = 0;
	}

	public int requstRibbons(ISpiritweb data, SandmasteryManifestation manifestation, int requestedRibbons)
	{
		int change = 0;
		int maxRibbons = (int) manifestation.getStrength(data, false);

		if (numRibbonsInUse >= maxRibbons)
		{
			change = 0;
			this.numRibbonsInUse = maxRibbons;
		}
		else
		{
			int changeAttempt = requestedRibbons;
			if ((this.numRibbonsInUse + changeAttempt) <= maxRibbons)
			{
				change = changeAttempt;
			}
		}
		this.numRibbonsInUse += change;
		return change;
	}

	public int returnRibbons(ISpiritweb data, SandmasteryManifestation manifestation, int returnedRibbons)
	{
		int oldRibbons = this.numRibbonsInUse;
		int newRibbons = Math.max(0, this.numRibbonsInUse - returnedRibbons);
		this.numRibbonsInUse = newRibbons;
		return oldRibbons - newRibbons;
	}

	public int getUsedRibbons()
	{
		return this.numRibbonsInUse;
	}

	public void setUsedRibbons(int ribbons)
	{
		this.numRibbonsInUse = ribbons;
	}

	public void debugRibbonUsage()
	{
		CosmereAPI.logger.info("Ribbons in use: " + numRibbonsInUse);
	}

	public void updateFlags(int flags)
	{
		this.hotkeyFlags = flags;
		//update the tag value for later serialization.
		this.sandmasteryTag.putInt("hotkeys", hotkeyFlags);
	}
}
