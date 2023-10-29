/*
 * File updated ~ 29 - 10 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.capabilities;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.allomancy.client.metalScanning.ScanResult;
import leaf.cosmere.allomancy.common.config.AllomancyConfigs;
import leaf.cosmere.allomancy.common.items.MetalVialItem;
import leaf.cosmere.allomancy.common.manifestation.AllomancyIronSteel;
import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.ISpiritwebSubmodule;
import leaf.cosmere.api.Metals;
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
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class AllomancySpiritwebSubmodule implements ISpiritwebSubmodule
{

	//metals ingested
	public final Map<Metals.MetalType, Integer> METALS_INGESTED =
			Arrays.stream(Metals.MetalType.values())
					.collect(Collectors.toMap(Function.identity(), type -> 0));


	@Override
	public void tickClient(ISpiritweb spiritweb)
	{
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
	}

	@Override
	public void tickServer(ISpiritweb spiritweb)
	{
		//tick metals
		if (spiritweb.getLiving().tickCount % 1200 == 0)
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
	}

	@Override
	public void deserialize(ISpiritweb spiritweb)
	{
		final CompoundTag ingestedMetals = spiritweb.getCompoundTag().getCompound("ingested_metals");
		for (Metals.MetalType metalType : Metals.MetalType.values())
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
	}

	@Override
	public void serialize(ISpiritweb spiritweb)
	{
		final CompoundTag ingestedMetals = new CompoundTag();
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			final Integer ingestedMetalAmount = METALS_INGESTED.get(metalType);
			if (ingestedMetalAmount > 0)
			{
				ingestedMetals.putInt(metalType.getName(), ingestedMetalAmount);
			}
		}
		spiritweb.getCompoundTag().put("ingested_metals", ingestedMetals);
	}

	@Override
	public void renderWorldEffects(ISpiritweb spiritweb, RenderLevelStageEvent event)
	{
		AllomancyIronSteel ironAllomancy = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.IRON).get();
		AllomancyIronSteel steelAllomancy = (AllomancyIronSteel) AllomancyManifestations.ALLOMANCY_POWERS.get(Metals.MetalType.STEEL).get();

		//if user has iron or steel manifestation
		if (spiritweb.hasManifestation(ironAllomancy) || spiritweb.hasManifestation(steelAllomancy))
		{
			//is zero if the manifestation is not active.
			int range = Math.max(ironAllomancy.getRange(spiritweb), steelAllomancy.getRange(spiritweb));

			if (range > 0)
			{
				Minecraft.getInstance().getProfiler().push("cosmere-getDrawLines");
				ScanResult scanResult = AllomancyIronSteel.getDrawLines(range);

				Vec3 originPoint = spiritweb.getLiving().getLightProbePosition(Minecraft.getInstance().getFrameTime()).add(0, -1, 0);

				PoseStack viewModelStack = new PoseStack();
				viewModelStack.last().pose().load(event.getPoseStack().last().pose());

				final Boolean drawMetalLines = AllomancyConfigs.CLIENT.drawMetalLines.get();
				if (drawMetalLines && !scanResult.foundEntities.isEmpty())
				{
					DrawHelper.drawLinesFromPoint(viewModelStack, originPoint, Color.BLUE, scanResult.foundEntities);
				}
				if (drawMetalLines && !scanResult.clusterResults.isEmpty())
				{
					DrawHelper.drawLinesFromPoint(viewModelStack, originPoint, Color.BLUE, scanResult.clusterCenters);
				}
				if (AllomancyConfigs.CLIENT.drawMetalBoxes.get() && !scanResult.foundBlocks.isEmpty())
				{
					DrawHelper.drawBlocksAtPoint(viewModelStack, Color.BLUE, scanResult.foundBlocks);
				}

				Minecraft.getInstance().getProfiler().pop();
			}
		}
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void collectMenuInfo(List<String> m_infoText)
	{
		for (Metals.MetalType metalType : Metals.MetalType.values())
		{
			int value = METALS_INGESTED.get(metalType);

			if (value > 0)
			{
				//todo localisation check
				final String text = metalType.getName() + ": " + value;
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
}
