/*
 * File updated ~ 7 - 8 - 2023 ~ Leaf
 * File updated ~ 12 - 7 - 2025 ~ Soar
 */

package leaf.cosmere.allomancy.common.items;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.ForgeMod;
import top.theillusivec4.curios.api.SlotContext;
import top.theillusivec4.curios.api.type.capability.ICurioItem;

import java.util.UUID;

public class MistcloakItem extends Item implements ICurioItem
{
	public MistcloakItem(Properties properties)
	{
		super( properties);
	}

	//todo increase dodge chance while in the mists
	

	@Override
	public boolean isDamageable(ItemStack stack)
	{
		return true;
	}

	public Multimap<Attribute, AttributeModifier> getAttributeModifiers(SlotContext slotContext, UUID uuid, ItemStack stack)
	{
		ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();

		builder.putAll(ICurioItem.super.getAttributeModifiers(slotContext, uuid, stack));
		builder.put(Attributes.ARMOR, new AttributeModifier(uuid, "Mistcloak modifier", 2, AttributeModifier.Operation.ADDITION));
		builder.put(ForgeMod.ENTITY_GRAVITY.get(), new AttributeModifier(uuid, "Mistcloak glide", -0.02, AttributeModifier.Operation.ADDITION));
		builder.put(Attributes.FLYING_SPEED, new AttributeModifier(uuid, "Mistcloak fly", 0.02, AttributeModifier.Operation.MULTIPLY_BASE));
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
