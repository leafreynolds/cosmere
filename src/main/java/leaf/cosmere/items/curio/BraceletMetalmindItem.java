/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.items.curio;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.itemgroups.CosmereItemGroups;
import leaf.cosmere.items.MetalmindItem;
import net.minecraft.client.model.HumanoidModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import top.theillusivec4.curios.api.SlotTypePreset;
import top.theillusivec4.curios.api.type.capability.ICurio;

import java.awt.*;
import java.util.Optional;

public class BraceletMetalmindItem extends MetalmindItem
{
	public BraceletMetalmindItem(Metals.MetalType metalType)
	{
		super(metalType, CosmereItemGroups.METALMINDS);
	}

	@Override
	public float getMaxChargeModifier()
	{
		return (5f / 9f);
	}

}
