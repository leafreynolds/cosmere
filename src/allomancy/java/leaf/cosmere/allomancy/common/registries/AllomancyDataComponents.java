/*
 * File updated ~ 2026-08-21 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 */

package leaf.cosmere.allomancy.common.registries;

import leaf.cosmere.allomancy.common.Allomancy;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.component.ItemContainerContents;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class AllomancyDataComponents
{
	public static final DeferredRegister.DataComponents DATA_COMPONENTS =
			DeferredRegister.createDataComponents(Registries.DATA_COMPONENT_TYPE, Allomancy.MODID);

	//item caps resolve per stack, so the contents live on the stack
	public static final DeferredHolder<DataComponentType<?>, DataComponentType<ItemContainerContents>> COIN_POUCH_CONTENTS =
			DATA_COMPONENTS.registerComponentType(
					"coin_pouch_contents",
					builder -> builder
							.persistent(ItemContainerContents.CODEC)
							.networkSynchronized(ItemContainerContents.STREAM_CODEC));
}
