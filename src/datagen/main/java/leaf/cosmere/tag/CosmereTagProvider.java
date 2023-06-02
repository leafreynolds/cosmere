/*
 * File updated ~ 1 - 6 - 2023 ~ Leaf
 */

package leaf.cosmere.tag;

import leaf.cosmere.api.providers.IBlockProvider;
import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registry.BlocksRegistry;
import leaf.cosmere.common.registry.GameEventRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.tags.GameEventTags;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CosmereTagProvider extends BaseTagProvider
{

	public CosmereTagProvider(DataGenerator gen, @Nullable ExistingFileHelper existingFileHelper)
	{
		super(gen, Cosmere.MODID, existingFileHelper);
	}

	@Override
	protected List<IBlockProvider> getAllBlocks()
	{
		return BlocksRegistry.BLOCKS.getAllBlocks();
	}

	@Override
	protected void registerTags()
	{
		addGameEvents();
	}

	private void addGameEvents()
	{
		addToTag(GameEventTags.VIBRATIONS, GameEventRegistry.KINETIC_INVESTITURE);
		addToTag(GameEventTags.WARDEN_CAN_LISTEN, GameEventRegistry.KINETIC_INVESTITURE);
	}

}