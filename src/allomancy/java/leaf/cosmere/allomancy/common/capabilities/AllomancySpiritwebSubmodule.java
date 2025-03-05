/*
 * File updated ~ 5 - 3 - 2025 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.allomancy.client.metalScanning.IronSteelLinesThread;
import leaf.cosmere.allomancy.client.metalScanning.ScanResult;
import leaf.cosmere.allomancy.common.Allomancy;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.items.MetalVialItem;
import leaf.cosmere.allomancy.common.manifestation.*;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.EnumUtils;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.helpers.CompoundNBTHelper;
import leaf.cosmere.api.helpers.DrawHelper;
import leaf.cosmere.api.helpers.PlayerHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;

import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AllomancySpiritwebSubmodule implements ISpiritwebSubmodule
{
	final String INGESTED_KEY = "ingested_metals";
	final String PEWTER_DELAYED_DAMAGE_KEY = "pewter_delayed_damage";
	private CompoundTag moduleTag = null;

	//metals ingested
	public final Map<Metals.MetalType, Integer> METALS_INGESTED =
			Arrays.stream(EnumUtils.METAL_TYPES)
					.collect(Collectors.toMap(Function.identity(), type -> 0));

	private float pewterDelayedDamage = 0;

	public static AllomancySpiritwebSubmodule getSubmodule(ISpiritweb data)
	{
		return (AllomancySpiritwebSubmodule) data.getSubmodule(Manifestations.ManifestationTypes.ALLOMANCY);
	}

	@Override
	public void tickClient(ISpiritweb spiritweb)
	{
		//todo fix this, why am I only calling apply effect tick on the client for iron/steel?

		//Iron allomancy
		{
			AllomancyIronSteel iron = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
			final boolean ironActive = iron.isActive(spiritweb);

			if (ironActive && !iron.isCompounding(spiritweb))
			{
				iron.applyEffectTick(spiritweb);
			}
		}

		//steel allomancy
		{
			AllomancyIronSteel steel = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();
			final boolean steelActive = steel.isActive(spiritweb);

			if (steelActive && !steel.isCompounding(spiritweb))
			{
				steel.applyEffectTick(spiritweb);
			}
		}

		//tin allomancy
		{
			AllomancyTin tin = (AllomancyTin) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.TIN).get();
			final boolean tinActive = tin.isActive(spiritweb);

			if (tinActive && !tin.isCompounding(spiritweb))
			{
				tin.applyEffectTick(spiritweb);
			}
		}

		//brass allomancy
		{
			AllomancyBrass brass = (AllomancyBrass) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.BRASS).get();
			final boolean brassActive = brass.isActive(spiritweb);

			if (brassActive && !brass.isCompounding(spiritweb))
			{
				brass.applyEffectTick(spiritweb);
			}
		}

		//zinc allomancy
		{
			AllomancyZinc zinc = (AllomancyZinc) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.ZINC).get();
			final boolean zincActive = zinc.isActive(spiritweb);

			if (zincActive && !zinc.isCompounding(spiritweb))
			{
				zinc.applyEffectTick(spiritweb);
			}
		}
	}

	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		//tick metals
		if (spiritweb.getLiving().tickCount % 1200 == 0)
		{
			//metals can't stay in your system forever, y'know?
			for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
			{
				Integer metalIngestAmount = METALS_INGESTED.get(metalType);
				if (metalIngestAmount > 0)
				{
					//todo decide how and when we poison the user for eating large amounts of metal, that sure ain't safe champ.
					//even if the user is immune to metal poisoning, it's still not safe to eat a bunch of metal.
					//IIRC, it's safe until you start to digest it, that's why Kelsier recommends Vin burns off all her metals before sleeping. -Rose
					//^^ To that effect, maybe have a counter that increments up when you have a metal ingested and increments slightly slower down over time when burning or faster when empty. When it's too high the player gets worse and worse? -Rose

					//todo, decide what's appropriate for reducing ingested metal amounts
					METALS_INGESTED.put(metalType, metalIngestAmount - 1);
				}
			}
		}
	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();

		CompoundTag ingestedMetals;// = compoundTag.getCompound("ingested_metals");

		moduleTag = CompoundNBTHelper.getOrCreate(compoundTag, Allomancy.MODID);

		//backwards compat, todo remove later in 1.20+ port
		if (compoundTag.contains(INGESTED_KEY))
		{
			ingestedMetals = compoundTag.getCompound(INGESTED_KEY);
		}
		else
		{
			ingestedMetals = CompoundNBTHelper.getOrCreate(moduleTag, INGESTED_KEY);
		}

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			final String metalKey = metalType.getName();
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

		pewterDelayedDamage = CompoundNBTHelper.getFloat(moduleTag, PEWTER_DELAYED_DAMAGE_KEY, 0f);
	}

	@Override
	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag compoundTag = spiritweb.getCompoundTag();
		moduleTag = CompoundNBTHelper.getOrCreate(compoundTag, Allomancy.MODID);

		//replace old ingested tag with new one
		final CompoundTag ingestedMetals = new CompoundTag();
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			final Integer ingestedMetalAmount = METALS_INGESTED.get(metalType);
			if (ingestedMetalAmount > 0)
			{
				ingestedMetals.putInt(metalType.getName(), ingestedMetalAmount);
			}
		}

		//todo remove in 1.20+ port
		//remove old ingested tag if exists
		if (compoundTag.contains(INGESTED_KEY))
		{
			compoundTag.remove(INGESTED_KEY);
		}

		//put it in the new place
		moduleTag.put(INGESTED_KEY, ingestedMetals);
		moduleTag.putFloat(PEWTER_DELAYED_DAMAGE_KEY, pewterDelayedDamage);

		compoundTag.put(Allomancy.MODID, moduleTag);
	}

	@Override
	public void resetOnDeath(ISpiritweb spiritweb)
	{
		pewterDelayedDamage = 0f;
	}

	@Override
	public void drainInvestiture(ISpiritweb data, double strength)
	{
		//for the purpose of allomantic aluminum and chromium, we're going to drain all the metals from the user.
		// I realise it doesn't quite fit for a-duralumin

		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			int ingestedMetalAmount = getIngestedMetal(metalType);

			//if metal exists
			if (ingestedMetalAmount > 0)
			{
				//drain metals that are actively being burned
				if (data.canTickManifestation(Manifestations.ManifestationTypes.ALLOMANCY.getManifestation(metalType.getID())))
				{
					final int amountToAdjust =
							ingestedMetalAmount > 30 ? (ingestedMetalAmount / 2) : ingestedMetalAmount;
					adjustIngestedMetal(
							metalType,
							-amountToAdjust, //take amount away
							true);

				}
			}
		}
	}

	@Override
	public void renderWorldEffects(ISpiritweb spiritweb, RenderLevelStageEvent event)
	{
		AllomancyIronSteel ironAllomancy = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
		AllomancyIronSteel steelAllomancy = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();
		AllomancyTin tinAllomancy = (AllomancyTin) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.TIN).get();

		PoseStack viewModelStack = event.getPoseStack();

		//if user has iron or steel manifestation
		if (spiritweb.hasManifestation(ironAllomancy) || spiritweb.hasManifestation(steelAllomancy))
		{
			//is zero if the manifestation is not active.
			int range = Math.max(ironAllomancy.getRange(spiritweb), steelAllomancy.getRange(spiritweb));

			if (range > 0)
			{
				Minecraft.getInstance().getProfiler().push("cosmere-getDrawLines");
				//todo - does this mean it's wrong on the first check? probably doesn't matter
				IronSteelLinesThread.getInstance().setScanRange(range);
				ScanResult scanResult = IronSteelLinesThread.getInstance().requestScanResult();
				Vec3 closestMetalObject = IronSteelLinesThread.getInstance().getClosestMetalObject();

				Vec3 originPoint = spiritweb.getLiving().getLightProbePosition(Minecraft.getInstance().getFrameTime()).add(0, -1, 0);

				final Boolean drawMetalLines = AllomancyConfigs.CLIENT.drawMetalLines.get();
				if (drawMetalLines && !scanResult.foundEntities.isEmpty())
				{
					DrawHelper.drawLinesFromPoint(viewModelStack, originPoint, range, Color.BLUE, scanResult.foundEntities, closestMetalObject);
				}
				if (drawMetalLines && !scanResult.clusterResults.isEmpty())
				{
					DrawHelper.drawLinesFromPoint(viewModelStack, originPoint, range, Color.BLUE, scanResult.clusterCenters, closestMetalObject);
				}
				if (AllomancyConfigs.CLIENT.drawMetalBoxes.get() && !scanResult.foundBlocks.isEmpty())
				{
					if (scanResult.hasTargetedCluster)
					{
						DrawHelper.drawBlocksAtPoint(viewModelStack, Color.BLUE, scanResult.foundBlocks, range, closestMetalObject, scanResult.targetedCluster.getBlocks());
					}
					else
					{
						DrawHelper.drawBlocksAtPoint(viewModelStack, Color.BLUE, scanResult.foundBlocks, range, closestMetalObject, new ArrayList<>());
					}
				}

				Minecraft.getInstance().getProfiler().pop();

				IronSteelLinesThread.getInstance().releaseScanResult();
			}
		}

		if (spiritweb.hasManifestation(tinAllomancy))
		{
			viewModelStack.last().pose().get(event.getPoseStack().last().pose());   // not sure that get() is right here

			Minecraft.getInstance().getProfiler().push("cosmere-getDrawSoundIndicator");
			DrawHelper.drawSquareAtPoint(viewModelStack, Color.WHITE, AllomancyTin.getTinSoundList(), spiritweb.getLiving().getEyePosition());
			Minecraft.getInstance().getProfiler().pop();
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void collectMenuInfo(List<String> m_infoText)
	{
		for (Metals.MetalType metalType : EnumUtils.METAL_TYPES)
		{
			int value = METALS_INGESTED.get(metalType);

			if (value > 0)
			{
				//todo localisation check
				final String text = "A. " + metalType.getName() + ": " + value;
				m_infoText.add(text);
			}
		}
	}

	@Override
	public void GiveStartingItem(Player player)
	{
		ItemStack itemStack = new ItemStack(AllomancyItems.METAL_VIAL.get());
		for (int i = 0; i < 16; i++)
		{
			MetalVialItem.addMetals(itemStack, i, 1);
		}
		PlayerHelper.addItem(player, itemStack);
	}

	@Override
	public void GiveStartingItem(Player player, Manifestation manifestation)
	{
		if (manifestation instanceof AllomancyManifestation allomancyManifestation)
		{
			ItemStack itemStack = new ItemStack(AllomancyItems.METAL_VIAL.get());
			MetalVialItem.addMetals(itemStack, allomancyManifestation.getMetalType().getID(), 16);
			PlayerHelper.addItem(player, itemStack);
		}
	}

	public int getIngestedMetal(Metals.MetalType metalType)
	{
		return METALS_INGESTED.get(metalType);
	}

	public boolean adjustIngestedMetal(Metals.MetalType metalType, int amountToAdjust, boolean doAdjust)
	{
		int ingestedMetal = getIngestedMetal(metalType);

		final int newValue = ingestedMetal + amountToAdjust;
		if (newValue >= 0)
		{
			if (doAdjust)
			{
				METALS_INGESTED.put(metalType, newValue);
			}

			return true;
		}

		return false;
	}

	public float getPewterDelayedDamage()
	{
		return pewterDelayedDamage;
	}

	public void setPewterDelayedDamage(float pewterDelayedDamage)
	{
		this.pewterDelayedDamage = pewterDelayedDamage;
	}
}
