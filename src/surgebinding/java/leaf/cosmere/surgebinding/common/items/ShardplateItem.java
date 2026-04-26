package leaf.cosmere.surgebinding.common.items;

import leaf.cosmere.client.render.CosmereRenderers;
import leaf.cosmere.surgebinding.client.render.renderer.ArmorRenderer;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import top.theillusivec4.curios.api.client.ICurioRenderer;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Consumer;

public class ShardplateItem extends ArmorItem
{
	public ShardplateItem(Holder<ArmorMaterial> material, ArmorItem.Type pType, Properties properties)
	{
		super(material, pType, properties);
	}

	@Nonnull
	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel)
	{
		return ResourceLocation.fromNamespaceAndPath(Surgebinding.MODID, "textures/models/armor/shardplate.png");
	}

	@OnlyIn(Dist.CLIENT)
	@Override
	public void initializeClient(Consumer<IClientItemExtensions> consumer)
	{
		consumer.accept(new IClientItemExtensions()
		{
			@Override
			public net.minecraft.client.model.HumanoidModel<?> getHumanoidArmorModel(LivingEntity livingEntity, ItemStack itemStack, EquipmentSlot equipmentSlot, net.minecraft.client.model.HumanoidModel<?> original)
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
					model.Chestplate.visible = isChest;
					model.rightArm.visible = isChest;
					model.leftArm.visible = isChest;

					model.rightLeg.visible = true;
					model.leftLeg.visible = true;

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
