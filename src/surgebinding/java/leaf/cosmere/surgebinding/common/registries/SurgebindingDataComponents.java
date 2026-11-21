/*
 * File updated ~ 23 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.surgebinding.common.registries;

import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class SurgebindingDataComponents
{
	public static final DeferredRegister.DataComponents DATA_COMPONENTS =
			DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Surgebinding.MODID);

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SHARD_DATA =
			DATA_COMPONENTS.registerComponentType(
					"shard_data",
					builder -> builder
							.persistent(CompoundTag.CODEC)
							.networkSynchronized(ByteBufCodecs.TRUSTED_COMPOUND_TAG));

	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> BOND_DATA =
			DATA_COMPONENTS.registerComponentType(
					"bond_data",
					builder -> builder
							.persistent(CompoundTag.CODEC)
							.networkSynchronized(ByteBufCodecs.TRUSTED_COMPOUND_TAG));
}
