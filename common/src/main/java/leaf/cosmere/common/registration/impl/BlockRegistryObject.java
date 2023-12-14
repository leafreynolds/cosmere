package leaf.cosmere.common.registration.impl;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.registration.DoubleWrappedRegistryObject;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class BlockRegistryObject<BLOCK extends Block, ITEM extends Item> extends DoubleWrappedRegistryObject<BLOCK, ITEM> implements IBlockProvider
{

	public BlockRegistryObject(RegistryObject<BLOCK> blockRegistryObject, RegistryObject<ITEM> itemRegistryObject)
	{
		super(blockRegistryObject, itemRegistryObject);
	}

	@NotNull
	@Override
	public BLOCK getBlock()
	{
		return getPrimary();
	}

	@NotNull
	@Override
	public ITEM asItem()
	{
		return getSecondary();
	}
}