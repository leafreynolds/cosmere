/*
 * File updated ~ 10 - 8 - 2024 ~ Leaf
 */

package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.api.providers.IItemProvider;
import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.eventbus.api.IEventBus;

import java.util.function.Consumer;
import java.util.function.UnaryOperator;

public class CreativeTabDeferredRegister extends WrappedDeferredRegister<CreativeModeTab>
{

	private final Consumer<BuildCreativeModeTabContentsEvent> addToExistingTabs;
	private final String modid;

	public CreativeTabDeferredRegister(String modid)
	{
		this(modid, event ->
		{
		});
	}

	public CreativeTabDeferredRegister(String modid, Consumer<BuildCreativeModeTabContentsEvent> addToExistingTabs)
	{
		super(modid, Registries.CREATIVE_MODE_TAB);
		this.modid = modid;
		this.addToExistingTabs = addToExistingTabs;
	}

	@Override
	public void register(IEventBus bus)
	{
		super.register(bus);
		bus.addListener(addToExistingTabs);
	}

	/**
	 * @apiNote We manually require the title and icon to be passed so that we ensure all tabs have one.
	 */
	public CreativeTabRegistryObject registerMain(Component title, IItemProvider icon, UnaryOperator<CreativeModeTab.Builder> operator)
	{
		return register(modid, title, icon, operator);
	}

	/**
	 * @apiNote We manually require the title and icon to be passed so that we ensure all tabs have one.
	 */
	public CreativeTabRegistryObject register(String name, Component title, IItemProvider icon, UnaryOperator<CreativeModeTab.Builder> operator)
	{
		return register(name, () ->
		{
			CreativeModeTab.Builder builder = CreativeModeTab.builder()
					.title(title)
					.icon(icon::getItemStack)
					.withTabFactory(CosmereCreativeTab::new);
			return operator.apply(builder).build();
		}, CreativeTabRegistryObject::new);
	}

	public static void addToDisplay(CreativeModeTab.Output output, ItemLike... items)
	{
		for (ItemLike item : items)
		{
			addToDisplay(output, item);
		}
	}

	public static void addToDisplay(CreativeModeTab.Output output, ItemLike itemLike)
	{
		if (itemLike.asItem() instanceof ICustomCreativeTabContents contents)
		{
			if (contents.addDefault())
			{
				output.accept(itemLike);
			}
			contents.addItems(output);
		}
		else
		{
			output.accept(itemLike);
		}
	}

	public static void addToDisplay(ItemDeferredRegister register, CreativeModeTab.Output output)
	{
		for (IItemProvider itemProvider : register.getAllItems())
		{
			addToDisplay(output, itemProvider);
		}
	}

	public static void addToDisplay(BlockDeferredRegister register, CreativeModeTab.Output output)
	{
		for (IBlockProvider itemProvider : register.getAllBlocks())
		{
			addToDisplay(output, itemProvider);
		}
	}

	public interface ICustomCreativeTabContents
	{

		void addItems(CreativeModeTab.Output tabOutput);

		default boolean addDefault()
		{
			return true;
		}
	}

	public static class CosmereCreativeTab extends CreativeModeTab
	{

		protected CosmereCreativeTab(Builder builder)
		{
			super(builder);
		}

		@Override
		public int getLabelColor()
		{
			//todo better colours references
			return 0xFF404040;
		}
	}
}