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

	//the layer count, rather than an item inv that has to convert between layers and full blocks
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> SAND_LAYERS =
			DATA_COMPONENTS.registerComponentType(
					"sand_layers",
					builder -> builder
							.persistent(Codec.INT)
							.networkSynchronized(ByteBufCodecs.VAR_INT));
}
