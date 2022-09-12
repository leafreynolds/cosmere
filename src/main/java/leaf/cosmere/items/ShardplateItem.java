/*
 * File created ~ 12 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.items.tiers.ShardplateArmorMaterial;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;

public class ShardplateItem extends ArmorItem
{
	public ShardplateItem(ShardplateArmorMaterial material, EquipmentSlot slot, Properties properties)
	{
		super(material, slot, properties);
	}

}
