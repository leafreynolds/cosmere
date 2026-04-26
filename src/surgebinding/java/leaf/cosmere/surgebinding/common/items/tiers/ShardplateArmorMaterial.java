package leaf.cosmere.surgebinding.common.items.tiers;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.Holder;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.List;
import java.util.Map;

public final class ShardplateArmorMaterial
{
	public static final Holder<ArmorMaterial> DEADPLATE = Holder.direct(new ArmorMaterial(
			Map.of(
					ArmorItem.Type.HELMET, 3,
					ArmorItem.Type.CHESTPLATE, 6,
					ArmorItem.Type.LEGGINGS, 8,
					ArmorItem.Type.BOOTS, 3,
					ArmorItem.Type.BODY, 0
			),
			10,
			SoundEvents.ARMOR_EQUIP_DIAMOND,
			() -> Ingredient.of(Items.DIAMOND),
			List.of(new ArmorMaterial.Layer(Surgebinding.rl("shardplate"))),
			2.0F,
			0.0F
	));

	public static final Holder<ArmorMaterial> LIVINGPLATE = Holder.direct(new ArmorMaterial(
			Map.of(
					ArmorItem.Type.HELMET, 3,
					ArmorItem.Type.CHESTPLATE, 6,
					ArmorItem.Type.LEGGINGS, 8,
					ArmorItem.Type.BOOTS, 3,
					ArmorItem.Type.BODY, 0
			),
			15,
			SoundEvents.ARMOR_EQUIP_NETHERITE,
			() -> Ingredient.of(Items.NETHERITE_INGOT),
			List.of(new ArmorMaterial.Layer(Surgebinding.rl("shardplate"))),
			3.0F,
			0.1F
	));

	public static final Holder<ArmorMaterial>[] ALL = new Holder[]{DEADPLATE, LIVINGPLATE};

	private ShardplateArmorMaterial()
	{
	}
}
