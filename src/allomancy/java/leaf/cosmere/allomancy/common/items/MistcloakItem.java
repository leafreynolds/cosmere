/*
 * File updated ~ 7 - 8 - 2023 ~ Leaf
 */

package leaf.cosmere.allomancy.common.items;

import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public class MistcloakItem extends ArmorItem
{
	private static final ResourceLocation MISTCLOAK_TEXTURE =
			ResourceLocation.fromNamespaceAndPath(Allomancy.MODID, "textures/models/armor/mistcloak.png");

	public MistcloakItem(Holder<ArmorMaterial> material, Type type, Properties properties)
	{
		super(material, type, properties);
	}

	//todo increase dodge chance while in the mists

	@Nonnull
	@Override
	public ResourceLocation getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, ArmorMaterial.Layer layer, boolean innerModel)
	{
		return MISTCLOAK_TEXTURE;
	}

/* If we were to not use curios, this is what we would attempt to use.
	There's a weird interaction with some vanilla code that gets run afterward, resetting some of the values we set
	Ideally don't deal with it.
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

				if (armorModel.isPresent() && armorModel.get() instanceof MistcloakRenderer armorRenderer)
				{
					var model = armorRenderer.model;
					model.hat.visible = false;
					model.head.visible = true;
					model.body.visible = true;

					model.rightArm.visible = false;
					model.leftArm.visible = false;
					model.rightLeg.visible = false;
					model.leftLeg.visible = false;

					return model;
				}
				return null;
			}
		});
	}*/
}
