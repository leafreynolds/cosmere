/*
 * File updated ~ 7 - 8 - 2023 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge; merged Mistcloak v2)
 */

package leaf.cosmere.allomancy.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class MistcloakItem extends Item implements ICurioItem
{
	private static final ResourceLocation MISTCLOAK_ARMOR_ID =
			ResourceLocation.fromNamespaceAndPath(Allomancy.MODID, "mistcloak_armor");
	private static final ResourceLocation MISTCLOAK_GLIDE_ID =
			ResourceLocation.fromNamespaceAndPath(Allomancy.MODID, "mistcloak_glide");
	private static final ResourceLocation MISTCLOAK_FLY_ID =
			ResourceLocation.fromNamespaceAndPath(Allomancy.MODID, "mistcloak_fly");

	public MistcloakItem(Properties properties)
	{
		super(properties);
	}

	//todo increase dodge chance while in the mists

	@Override
	public Multimap<Holder<Attribute>, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		ImmutableMultimap.Builder<Holder<Attribute>, AttributeModifier> builder = ImmutableMultimap.builder();

		builder.putAll(ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack));
		builder.put(
				Attributes.ARMOR,
				new AttributeModifier(MISTCLOAK_ARMOR_ID, 2, AttributeModifier.Operation.ADD_VALUE));
		// Forge's ENTITY_GRAVITY -> vanilla Attributes.GRAVITY in 1.21.1
		builder.put(
				Attributes.GRAVITY,
				new AttributeModifier(MISTCLOAK_GLIDE_ID, -0.02, AttributeModifier.Operation.ADD_VALUE));
		builder.put(
				Attributes.FLYING_SPEED,
				new AttributeModifier(MISTCLOAK_FLY_ID, 0.02, AttributeModifier.Operation.ADD_MULTIPLIED_BASE));
		return builder.build();
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
