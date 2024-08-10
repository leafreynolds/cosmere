/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.surgebinding.client.render.renderer.ArmorRenderer;
import leaf.cosmere.surgebinding.common.Surgebinding;
import leaf.cosmere.surgebinding.common.items.tiers.ShardplateArmorMaterial;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Optional;
import java.util.function.Consumer;

public class ShardplateItem extends ArmorItem
{
	public ShardplateItem(ShardplateArmorMaterial material, ArmorItem.Type pType, Properties properties)
	{
		super(material, pType, properties);
	}

	@Nonnull
	@Override
	public final String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type)
	{
		return Surgebinding.MODID + ":" + "textures/models/armor/shardplate.png";
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
					final boolean isHead = equipmentSlot == EquipmentSlot.HEAD;
					final boolean isChest = equipmentSlot == EquipmentSlot.CHEST;
					final boolean isLegs = equipmentSlot == EquipmentSlot.LEGS;
					final boolean isFeet = equipmentSlot == EquipmentSlot.FEET;

					var model = armorRenderer.model;
					model.hat.visible = false;
					model.head.visible = isHead;

					model.body.visible = isChest;
					//chestplate is done separately because pants have to set body visible for some reason
					model.Chestplate.visible = isChest;
					model.rightArm.visible = isChest;
					model.leftArm.visible = isChest;

					//set the parts on the base model visible
					model.rightLeg.visible = true;
					model.leftLeg.visible = true;

					//then set the actual child legs/boots visibility.
					//kinda janky but it works
					model.LeftLeg.visible = isLegs;
					model.RightLeg.visible = isLegs;
					model.LeftBoot.visible = isFeet;
					model.RightBoot.visible = isFeet;

					return model;
				}
				return null;
			}
		});
	}
}
