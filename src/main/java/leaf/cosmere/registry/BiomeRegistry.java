/*
 * File created ~ 3 - 7 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import com.mojang.serialization.Codec;
import leaf.cosmere.Cosmere;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomeRegistry
{
	public static final DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Cosmere.MODID);

}
