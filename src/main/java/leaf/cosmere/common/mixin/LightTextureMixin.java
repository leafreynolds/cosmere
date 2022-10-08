/*
 * File updated ~ 31 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.common.mixin;

import leaf.cosmere.api.CosmereAPI;
import leaf.cosmere.api.Metals;
import leaf.cosmere.api.manifestation.Manifestation;
import leaf.cosmere.api.spiritweb.ISpiritweb;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.registration.impl.AttributeRegistryObject;
import leaf.cosmere.common.registry.AttributesRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
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
		Player clientPlayer = (Player) Minecraft.getInstance().getCameraEntity();
		if (clientPlayer == null)
		{
			return prev;
		}

		final LazyOptional<ISpiritweb> iSpiritwebLazyOptional = SpiritwebCapability.get(clientPlayer);
		float tinAlloVal = 0;

		if (iSpiritwebLazyOptional.isPresent())
		{
			var spiritweb = iSpiritwebLazyOptional.resolve();
			final Manifestation tinAllomancy = CosmereAPI.manifestationRegistry().getValue(new ResourceLocation(Metals.MetalType.TIN.getName()));
			if (spiritweb.isPresent() && tinAllomancy.isActive(spiritweb.get()))
			{
				tinAlloVal = spiritweb.get().getMode(tinAllomancy) == 1 ? 0.25f : 0.75f;
			}
		}

		final AttributeRegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.NIGHT_VISION_ATTRIBUTE;
		AttributeInstance attribute = clientPlayer.getAttribute(attributeRegistryObject.get());
		//return modded val
		final float v = attribute != null ? (float) attribute.getValue() : prev;
		return v + tinAlloVal;
	}
}
