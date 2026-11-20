/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.sandmastery.common.registries;

import com.mojang.serialization.Codec;
import leaf.cosmere.sandmastery.common.Sandmastery;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SandmasteryDataComponents
{
	public static final DeferredRegister.DataComponents DATA_COMPONENTS =
			DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Sandmastery.MODID);

	// the total layer count, rather than an item inv that has to convert between layers and full blocks
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SAND_LAYERS =
			DATA_COMPONENTS.registerComponentType(
					"sand_layers",
					builder -> builder
							.persistent(Codec.INT)
							.networkSynchronized(ByteBufCodecs.VAR_INT));
	
	// the charged layer count
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> CHARGED_SAND_LAYERS =
			DATA_COMPONENTS.registerComponentType(
					"charged_sand_layers",
					builder -> builder
							.persistent(Codec.INT)
							.networkSynchronized(ByteBufCodecs.VAR_INT));

	// Should the pouch display black sand in the output slots first? If false, white sand is prioritized.
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Boolean>> DISPLAYING_BLACK_SAND =
			DATA_COMPONENTS.registerComponentType(
				"displaying_black_sand",
				builder -> builder
					.persistent(Codec.BOOL)
					.networkSynchronized(ByteBufCodecs.BOOL));
}
