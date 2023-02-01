/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.manifestation;

import leaf.cosmere.api.Constants;
import leaf.cosmere.api.Manifestations;
import leaf.cosmere.api.Taldain;
import leaf.cosmere.api.helpers.StackNBTHelper;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.compat.curios.CuriosCompat;
import leaf.cosmere.sandmastery.common.capabilities.SandmasterySpiritwebSubmodule;
import leaf.cosmere.sandmastery.common.registries.SandmasteryAttributes;
import leaf.cosmere.sandmastery.common.registries.SandmasteryItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.CuriosCapability;
import top.theillusivec4.curios.api.SlotResult;

import java.util.List;

public class SandmasteryManifestation extends Manifestation
{
	protected final Taldain.Mastery mastery;

	public SandmasteryManifestation(Taldain.Mastery mastery)
	{
		super(Manifestations.ManifestationTypes.SANDMASTERY);
		this.mastery = mastery;
	}

	@Override
	public double getStrength(ISpiritweb data, boolean getBaseStrength)
	{
		AttributeInstance attribute = data.getLiving().getAttribute(getAttribute());
		if (attribute != null)
		{
			return getBaseStrength ? attribute.getBaseValue() : attribute.getValue();
		}
		return 0;
	}

	@Override
	public int getPowerID()
	{
		return mastery.getID();
	}

	@Override
	public int modeMin(ISpiritweb data) {
		return 0;
	};

	@Override
	public int modeMax(ISpiritweb data) {
		return (int) data.getSelectedManifestation().getStrength(data, false);
	};

	@Override
	public void onModeChange(ISpiritweb data, int lastMode) {
		SpiritwebCapability playerSpiritweb = (SpiritwebCapability) data;
		SandmasterySpiritwebSubmodule submodule = (SandmasterySpiritwebSubmodule) playerSpiritweb.spiritwebSubmodules.get(Manifestations.ManifestationTypes.SANDMASTERY);
		if(getMode(data) > lastMode) {
			submodule.useRibbon(data, this);
		} else if(getMode(data) < lastMode) {
			submodule.releaseRibbon(data, this);
		}
	}

	protected boolean enoughChargedSand(ISpiritweb data, int required) {
		List<SlotResult> resultList = CuriosApi.getCuriosHelper().findCurios(data.getLiving(), SandmasteryItems.SAND_POUCH_ITEM.asItem());

		int count = 0;
		for (SlotResult result : resultList) {
			ItemStack stack = result.stack();
			count += StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
			if(count > required) return true;
		}
		return false;
	}

	protected void useChargedSand(ISpiritweb data, int change) {
		List<SlotResult> resultList = CuriosApi.getCuriosHelper().findCurios(data.getLiving(), SandmasteryItems.SAND_POUCH_ITEM.asItem());
		int changeLeft = change;
		for (SlotResult result : resultList) {
			ItemStack stack = result.stack();
			int startingCharge = StackNBTHelper.getInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
			int amountLeft = (startingCharge - changeLeft);
			if(amountLeft >= 0)
				StackNBTHelper.setInt(stack, Constants.NBT.CHARGE_LEVEL, amountLeft);
			else {
				StackNBTHelper.setInt(stack, Constants.NBT.CHARGE_LEVEL, 0);
				changeLeft += amountLeft;
			}
			if(changeLeft <= 0) break;
		}
	}

	@Override
	public Attribute getAttribute() {
		return SandmasteryAttributes.RIBBONS.getAttribute();
	}

}
