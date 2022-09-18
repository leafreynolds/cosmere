/*
 * File created ~ 12 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.items;

import leaf.cosmere.Cosmere;
import leaf.cosmere.client.render.armor.ArmorRenderer;
import leaf.cosmere.client.render.armor.ShardplateModel;
import leaf.cosmere.client.render.curio.CosmereRenderers;
import leaf.cosmere.items.tiers.ShardplateArmorMaterial;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.jetbrains.annotations.NotNull;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class ShardplateItem extends ArmorItem
{
	public ShardplateItem(ShardplateArmorMaterial material, EquipmentSlot slot, Properties properties)
	{
		super(material, slot, properties);
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		return Cosmere.MODID + ":" + "textures/models/armor/shardplate.png";
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Nullable
			@Override
			public HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, HumanoidModel<?> original)
			{
				Optional<ICurioRenderer> armorModel = CosmereRenderers.getRenderer(itemStack.getItem());

				if (armorModel.isPresent() && armorModel.get() instanceof ArmorRenderer armorRenderer)
				{
					var model = armorRenderer.model;
					model.head.visible = equipmentSlot == EquipmentSlot.HEAD;
					model.hat.visible = false;

					model.body.visible = equipmentSlot == EquipmentSlot.CHEST;
					model.rightArm.visible = equipmentSlot == EquipmentSlot.CHEST;
					model.leftArm.visible = equipmentSlot == EquipmentSlot.CHEST;

					model.rightLeg.visible = equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.FEET;
					model.leftLeg.visible = equipmentSlot == EquipmentSlot.LEGS || equipmentSlot == EquipmentSlot.FEET;

					return model;
				}
				return null;
			}
		});
	}
}
