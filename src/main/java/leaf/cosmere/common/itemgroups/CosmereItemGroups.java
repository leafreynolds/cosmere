/*
 * File updated ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.common.itemgroups;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.ItemsRegistry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.CreativeModeTabRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class CosmereItemGroups
{
	public static final DeferredRegister<CreativeModeTab> CREATIVE_TABS = DeferredRegister.create(new ResourceLocation(Cosmere.MODID + ".creative_tabs"), Cosmere.MODID);

	public static RegistryObject<CreativeModeTab> ITEMS = CREATIVE_TABS.register("items", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroups." + Cosmere.MODID + ".items"))
			.icon(() -> new ItemStack(ItemsRegistry.GUIDE.asItem()))
			.displayItems((params, output) -> {
				// todo: cosmere items here
			})
			.build());

	public static RegistryObject<CreativeModeTab> BLOCKS = CREATIVE_TABS.register("blocks", () -> CreativeModeTab.builder()
			.title(Component.translatable("itemGroups." + Cosmere.MODID + ".blocks"))
			.icon(() -> new ItemStack(BlocksRegistry.METAL_ORE.entrySet().stream().findAny().get().getValue().getBlock()))
			.displayItems((params, output) -> {
				// todo: cosmere blocks here
			})
			.build());

}
