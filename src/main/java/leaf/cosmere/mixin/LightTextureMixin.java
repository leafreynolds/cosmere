/*
 * File created ~ 31 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.registry.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.entity.ai.attributes.Attribute;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.fml.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(LightTexture.class)
public class LightTextureMixin
{
	@ModifyConstant(method = "updateLightTexture", constant = @Constant(floatValue = 0f, ordinal = 1))
	private float updateLightTextureConstant(float prev)
	{
		//do stuff
		PlayerEntity clientPlayer = (PlayerEntity) Minecraft.getInstance().getCameraEntity();
		if (clientPlayer == null)
		{
			return prev;
		}

		final RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.TIN.getName());
		ModifiableAttributeInstance attribute = clientPlayer.getAttribute(attributeRegistryObject.get());
		//return modded val
		return attribute != null ? (float) attribute.getValue() : prev;
	}
}
