package leaf.cosmere.sandmastery.common.effects;

import leaf.cosmere.common.effects.MobEffectBase;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class OvermasteredEffect extends MobEffectBase
{
	public OvermasteredEffect(MobEffectCategory mobEffectCategory, int colorValue)
	{
		super(mobEffectCategory, colorValue);
	}

	@Override
	public void applyEffectTick(LivingEntity livingEntity, int amplifier)
	{

	}

	@Override
	public List<ItemStack> getCurativeItems()
	{
		// Create and return an empty cure list, the user shouldn't be able to remove this effect without commands
		ArrayList<ItemStack> ret = new ArrayList<>();
		return ret;
	}
}
