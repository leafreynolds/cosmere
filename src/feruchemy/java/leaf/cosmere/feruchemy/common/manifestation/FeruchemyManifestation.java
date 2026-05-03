/*
 * File updated ~ 9 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.feruchemy.common.manifestation;

import leaf.cosmere.api.IHasMetalType;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.cosmereEffect.CosmereEffect;
import leaf.cosmere.api.cosmereEffect.CosmereEffectInstance;
import leaf.cosmere.api.helpers.EffectsHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.client.gui.GuiUtils;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.charge.MetalmindChargeHelper;
import leaf.cosmere.feruchemy.client.gui.FeruchemyInfoBlock;
import leaf.cosmere.feruchemy.client.utils.FeruchemyChargeThread;
import leaf.cosmere.feruchemy.common.registries.FeruchemyEffects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicReference;

public class FeruchemyManifestation extends Manifestation implements IHasMetalType
{
	private static final HashMap<Metals.MetalType, Double> metalmindChargesMap = new HashMap<>();
	private static final HashMap<Metals.MetalType, Double> metalmindMaxChargesMap = new HashMap<>();
	protected final Metals.MetalType metalType;

	public FeruchemyManifestation(Metals.MetalType metalType)
	{
		super(Manifestations.ManifestationTypes.FERUCHEMY);
		this.metalType = metalType;
	}

	@Override
	public int getPowerID()
	{
		return metalType.getID();
	}

	@Override
	public Metals.MetalType getMetalType()
	{
		return this.metalType;
	}

	@Override
	public boolean modeWraps(ISpiritweb data)
	{
		return false;
	}

	//storing is positive, eg adding to store
	@Override
	public int modeMax(ISpiritweb data)
	{
		final double strength = getStrength(data, false);
		return Mth.floor(strength / 3);
	}

	//tapping is negative, eg taking from store
	@Override
	public int modeMin(ISpiritweb data)
	{
		final double strength = getStrength(data, false);
		return -(Mth.floor(strength));
	}

	@Override
	public void onModeChange(ISpiritweb data, int lastMode)
	{
		super.onModeChange(data, lastMode);

		if (getMode(data) == 0)
		{
			//todo check if removing effects on mode change is wise. May be better to let them run out as they have already "paid" for them.
			final LivingEntity effectSource = data.getLiving();
			data.removeEffect(EffectsHelper.getEffectUUID(getStoringEffect(), effectSource));
			data.removeEffect(EffectsHelper.getEffectUUID(getTappingEffect(), effectSource));
		}
	}

	@OnlyIn(Dist.CLIENT)
	private void collectMenuInfo()
	{
		if (Minecraft.getInstance().player != null && Minecraft.getInstance().player.tickCount % 2 == 1)    // only do on odd tick
		{
			metalmindChargesMap.clear();
			metalmindChargesMap.putAll(FeruchemyChargeThread.getInstance().getCharges());

			metalmindMaxChargesMap.clear();
			metalmindMaxChargesMap.putAll(FeruchemyChargeThread.getInstance().getMaximumCharges());
		}
	}

	@Override
	public int getInvestitureRemaining(ISpiritweb spiritweb)
	{
		collectMenuInfo();
		return (int) Math.floor(metalmindChargesMap.getOrDefault(metalType, 0d));
	}

	@Override
	public float getInvestitureHud(ISpiritweb spiritweb)
	{
		collectMenuInfo();
		double maximum = metalmindMaxChargesMap.getOrDefault(metalType, 0d);
		double charge = metalmindChargesMap.getOrDefault(metalType, 0d);
		if (maximum <= 0) return 0.f;
		return (float) (charge/maximum);
	}

	protected CosmereEffect getTappingEffect()
	{
		return FeruchemyEffects.TAPPING_EFFECTS.get(this.metalType).get();
	}

	protected CosmereEffect getStoringEffect()
	{
		return FeruchemyEffects.STORING_EFFECTS.get(this.metalType).get();
	}

	public boolean isStoring(ISpiritweb data)
	{
		return getMode(data) > 0;
	}

	public boolean isTapping(ISpiritweb data)
	{
		return getMode(data) < 0;
	}

	public boolean canAfford(ISpiritweb data, boolean simulate)
	{
		int adjustAmount = getCost(data);
		final ItemStack metalmind = MetalmindChargeHelper.adjustMetalmindChargeExact(data, metalType, adjustAmount, !simulate, true);

		if (!metalmind.isEmpty())
		{
			return true;
		}

		if (!simulate)
		{
			final int mode = getMode(data);
			if (mode < 0)
			{
				//move towards turning off feruchemy.
				data.setMode(this, mode + 1);
			}
		}

		return false;
	}

	public int getCost(ISpiritweb data)
	{
		int mode = data.getMode(this);

		// if we are tapping
		//check if there is charges to tap
		if (mode < 0)
		{
			//wanting to tap
			//get cost
			return mode >= -modeMax(data) ? mode : -(Mth.abs(Mth.floor(Math.pow(Mth.abs(mode), 1.5d))));
		}
		//if we are storing
		//check if there is space to store
		else if (mode > 0)
		{
			return mode;
		}
		return 0;
	}


	@Override
	public boolean tick(ISpiritweb data)
	{
		//don't check every tick.
		LivingEntity livingEntity = data.getLiving();

		int mode = getMode(data);

		if ((!isActiveTick(data)) || mode == 0)
		{
			//if not active tick, or mode is off
			return false;
		}

		if (canAfford(data, false))//success
		{
			applyEffectTick(data);
			//todo, move to config so that players can choose how high tap rate needs to be to be picked up by sculk
			return isTapping(data) && mode < -4;
		}
		return false;
	}

	@Override
	public void applyEffectTick(ISpiritweb data)
	{
		int mode = getMode(data);
		CosmereEffect effect = getEffect(mode);
		CosmereEffectInstance currentEffect = EffectsHelper.getNewEffect(effect, data.getLiving(), Math.abs(mode));//todo check this strength

		data.addEffect(currentEffect);
	}

	@Override
	public AbstractWidget getInfoBlock()
	{
		AtomicReference<FeruchemyInfoBlock> retVal = new AtomicReference<>(null);

		SpiritwebCapability.get(Minecraft.getInstance().player).ifPresent( spiritweb -> {
			int x = 0;
			int y = 0;
			int screenWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
			int screenHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
			int width = GuiUtils.getInfoBoxWidth(Minecraft.getInstance());
			int height = GuiUtils.getInfoBoxHeight(Minecraft.getInstance());

			switch (this.metalType)
			{
				// top left
				case IRON:
				case PEWTER:
				case DURALUMIN:
				case CHROMIUM:
					x = 10;
					y = 10;
					break;
				// top right
				case COPPER:
				case ZINC:
				case STEEL:
				case TIN:
					x = screenWidth - width - 10;
					y = 10;
					break;
				// bottom left
				case ALUMINUM:
				case NICROSIL:
				case GOLD:
				case BENDALLOY:
				case ATIUM:
					x = 10;
					y = screenHeight - height - 10;
					break;
				// bottom right
				case CADMIUM:
				case ELECTRUM:
				case BRASS:
				case BRONZE:
					x = screenWidth - width - 10;
					y = screenHeight - height - 10;
					break;
				default:
					break;
			}

			retVal.set(new FeruchemyInfoBlock(x, y, width, height, spiritweb, this));
		});

		return retVal.get();
	}

	protected CosmereEffect getEffect(int mode)
	{
		if (mode == 0)
		{
			return null;
		}
		else if (mode < 0)
		{
			return getTappingEffect();
		}
		else
		{
			return getStoringEffect();
		}

	}

}
