/*
 * File updated ~ 8 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.feruchemy.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyAtium;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.LevelReader;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin
{

	@ModifyArg(method = "render", index = 6, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/entity/EntityRenderDispatcher;renderShadow(Lcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/entity/Entity;FFLnet/minecraft/world/level/LevelReader;F)V"))
	private float renderShadowSizeProxy(PoseStack pMatrixStack, MultiBufferSource pBuffer, Entity pEntity, float pWeight, float pPartialTicks, LevelReader pLevel, float pSize)
	{
		if (pEntity instanceof LivingEntity livingEntity)
		{
			return pSize * FeruchemyAtium.getScale(livingEntity);
		}

		return pSize;
	}
}
