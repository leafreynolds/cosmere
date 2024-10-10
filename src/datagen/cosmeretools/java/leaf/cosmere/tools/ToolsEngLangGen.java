/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.tools;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.tools.common.CosmereTools;
import leaf.cosmere.tools.common.registries.ToolsEntityTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class ToolsEngLangGen extends LanguageProvider
{
	public ToolsEngLangGen(PackOutput output)
	{
		super(output, CosmereTools.MODID, "en_us");
	}

	@Override
	protected void addTranslations()
	{
		addItemsAndBlocks();
		addEntities();
		addAdvancements();
		addManifestations();
		addAttributes();
		addPatchouli();
		addCreativeTabs();
		addTooltips();
		addDamageSources();
		addMobEffects();
		addCurioIdentifiers();
		addConfigs();
		addCommands();
		addKeybindings();
		addStats();

	}


	private void addItemsAndBlocks()
	{
		//Items and Blocks
		for (Item item : ForgeRegistries.ITEMS.getValues())
		{
			final ResourceLocation registryName = RegistryHelper.get(item);
			if (registryName.getNamespace().contentEquals(CosmereTools.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
				add(item.getDescriptionId(), localisedString);
			}
		}
	}

	private void addEntities()
	{
		//Entities
		for (IEntityTypeProvider type : ToolsEntityTypes.ENTITY_TYPES.getAllEntityTypes())
		{
			final ResourceLocation id = type.getRegistryName();
			add(type.getEntityType().getDescriptionId(), StringHelper.fixCapitalisation(id.getPath()));
		}
	}

	private void addAdvancements()
	{

	}

	private void addManifestations()
	{
	}

	private void addAttributes()
	{
	}

	private void addPatchouli()
	{

	}

	private void addCreativeTabs()
	{
		//ItemGroups/Tabs
		add("tabs.cosmeretools.items", "Cosmere Tools");

	}

	private void addTooltips()
	{

	}

	private void addDamageSources()
	{
	}

	private void addMobEffects()
	{
	}

	private void addCurioIdentifiers()
	{
	}

	private void addConfigs()
	{
	}

	private void addCommands()
	{
	}

	private void addKeybindings()
	{
	}

	private void addStats()
	{
	}
}
