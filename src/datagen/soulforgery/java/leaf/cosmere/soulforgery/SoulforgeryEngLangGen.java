/*
 * File updated ~ 10 - 10 - 2024 ~ Leaf
 */

package leaf.cosmere.soulforgery;

import leaf.cosmere.api.helpers.RegistryHelper;
import leaf.cosmere.api.providers.IEntityTypeProvider;
import leaf.cosmere.api.text.StringHelper;
import leaf.cosmere.soulforgery.common.Soulforgery;
import leaf.cosmere.soulforgery.common.registries.SoulforgeryEntityTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class SoulforgeryEngLangGen extends LanguageProvider
{
	public SoulforgeryEngLangGen(PackOutput output)
	{
		super(output, Soulforgery.MODID, "en_us");
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
		addTooltips();
		addItemGroups();
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
			if (registryName.getNamespace().contentEquals(Soulforgery.MODID))
			{
				String localisedString = StringHelper.fixCapitalisation(registryName.getPath());
				add(item.getDescriptionId(), localisedString);
			}
		}
	}

	private void addEntities()
	{
		//Entities
		for (IEntityTypeProvider type : SoulforgeryEntityTypes.ENTITY_TYPES.getAllEntityTypes())
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

	private void addTooltips()
	{

	}

	private void addItemGroups()
	{
		//ItemGroups/Tabs
		add("tabs.soulforgery.items", "Soulforgery");
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
