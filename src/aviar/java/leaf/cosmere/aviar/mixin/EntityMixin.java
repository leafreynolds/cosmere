/*
 * File updated ~ 21 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.mixin;

import leaf.cosmere.aviar.common.registries.AviarAttributes;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public class EntityMixin
{

	@Inject(method = "isCurrentlyGlowing", at = @At("RETURN"), cancellable = true)
	private void handleIsGlowing(CallbackInfoReturnable<Boolean> cir)
	{
		Entity e = (Entity) (Object) this;

		final boolean isServerSide = !(e.level.isClientSide);
		final boolean isInanimateEntity = !(e instanceof LivingEntity);
		Player clientPlayer = (Player) Minecraft.getInstance().player;
		if (isServerSide || isInanimateEntity || cir.getReturnValue() || clientPlayer == null)
		{
			return;
		}

		LivingEntity target = (LivingEntity) e;

		if (target instanceof Mob mob && mob.getTarget() == clientPlayer)
		{
			final Attribute attribute = AviarAttributes.HOSTILE_LIFE_SENSE.get();
			final AttributeInstance attributeInstance = clientPlayer.getAttribute(attribute);
			if (attributeInstance != null && attributeInstance.getValue() >= 1)
			{
				cir.setReturnValue(true);
			}
		}
	}
}

