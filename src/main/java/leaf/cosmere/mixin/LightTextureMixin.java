/*
 * File created ~ 31 - 3 - 2022 ~ Leaf
 */

package leaf.cosmere.mixin;

import leaf.cosmere.cap.entity.ISpiritweb;
import leaf.cosmere.cap.entity.SpiritwebCapability;
import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.registry.AttributesRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.registries.RegistryObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import javax.xml.crypto.Data;

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
			final AllomancyBase tinAllomancy = ManifestationRegistry.ALLOMANCY_POWERS.get(Metals.MetalType.TIN).get();
			if (spiritweb.isPresent() && tinAllomancy.isMetalBurning(spiritweb.get()))
			{
				tinAlloVal = spiritweb.get().getMode(tinAllomancy) == 1 ? 0.25f : 0.75f;
			}
		}

		final RegistryObject<Attribute> attributeRegistryObject = AttributesRegistry.COSMERE_ATTRIBUTES.get(Metals.MetalType.TIN.getName());
		AttributeInstance attribute = clientPlayer.getAttribute(attributeRegistryObject.get());
		//return modded val
		final float v = attribute != null ? (float) attribute.getValue() : prev;
		return v + tinAlloVal;
	}
}
