/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.allomancy.advancements;

import leaf.cosmere.allomancy.common.manifestation.AllomancyManifestation;
import leaf.cosmere.allomancy.common.registries.AllomancyItems;
import leaf.cosmere.allomancy.common.registries.AllomancyManifestations;
import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.commands.CommandFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;

import java.util.function.Consumer;

public class AllomancyAdvancements implements Consumer<Consumer<Advancement>>
{
	public AllomancyAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "allomancy";

		final String titleFormat = "advancements.allomancy.%s.title";
		final String descriptionFormat = "advancements.allomancy.%s.description";
		final String achievementPathFormat = "allomancy:%s/%s";

		Advancement root = Advancement.Builder.advancement()
				.display(AllomancyItems.METAL_VIAL.get(),
						Component.translatable(String.format(titleFormat, tabName)),
						Component.translatable(String.format(descriptionFormat, tabName)),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,//showToast
						false,//announceChat
						false)//hidden
				.addCriterion("tick", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.ANY))
				.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));


		for (ManifestationRegistryObject<AllomancyManifestation> manifestation : AllomancyManifestations.ALLOMANCY_POWERS.values())
		{
			AllomancyManifestation allomancyManifestation = (AllomancyManifestation) manifestation.get();

			Metals.MetalType metalType = allomancyManifestation.getMetalType();
			String metalName = metalType.getName();

			final Item item = metalType.getNugget();
			final ItemPredicate itemPredicate = ItemPredicate.Builder.item().of(CosmereTags.Items.METAL_NUGGET_TAGS.get(metalType)).build();
			Advancement advancement1 = Advancement.Builder.advancement()
					.parent(root)
					.display(
							item,
							Component.translatable(String.format(titleFormat, (tabName + "." + metalName))),
							Component.translatable(String.format(descriptionFormat, tabName + "." + metalName)),
							(ResourceLocation) null,
							FrameType.TASK,
							true, //showToast
							false, //announce
							false)//hidden
					.addCriterion("has_item", InventoryChangeTrigger.TriggerInstance.hasItems(itemPredicate))
					.rewards(new AdvancementRewards(50, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE))
					.save(advancementConsumer, String.format(achievementPathFormat, tabName, metalName));

		}


	}
}