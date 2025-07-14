/*
* File created ~ 12 - 7 - 2025 ~ Soar
 */
package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.api.Roshar;
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;

import java.awt.*;

public class LivingplateCurioItem extends ShardplateCurioItem
{
	public Roshar.RadiantOrder order;
	public LivingplateCurioItem(Properties properties, Roshar.RadiantOrder order)
	{
		super(ShardplateArmorMaterial.LIVINGPLATE, properties);
		this.order = order;
	}

	@Override
	public Color getColour()
	{
		return order.getPlateColor();
	}

	@Override
	public boolean isLiving()
	{
		return true;
	}

	public Roshar.RadiantOrder getOrder()
	{
		return order;
	}

	@Override
	public boolean canUnequip(SlotContext slotContext, ItemStack stack)
	{
		if(slotContext.entity() instanceof Player player)
		{
			return player.isCreative();
		}
		return false;
	}


}
