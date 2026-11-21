/*
 * File created ~ 22 - 8 - 2026 ~ Leaf
 */

package leaf.cosmere.hemalurgy.common.registries;

import leaf.cosmere.hemalurgy.common.Hemalurgy;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.UUID;

public class HemalurgyDataComponents
{
	public static final DeferredRegister.DataComponents DATA_COMPONENTS =
			DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Hemalurgy.MODID);

	//stolen powers: name -> strength. charge/attunement stay on the shared CUSTOM_DATA keys
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<CompoundTag>> SPIKE_POWERS =
			DATA_COMPONENTS.registerComponentType(
					"spike_powers",
					builder -> builder
							.persistent(CompoundTag.CODEC)
							.networkSynchronized(ByteBufCodecs.TRUSTED_COMPOUND_TAG));

	//identity of the entity the powers were stolen from
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<UUID>> STOLEN_IDENTITY =
			DATA_COMPONENTS.registerComponentType(
					"stolen_identity",
					builder -> builder
							.persistent(UUIDUtil.CODEC)
							.networkSynchronized(UUIDUtil.STREAM_CODEC));
}
