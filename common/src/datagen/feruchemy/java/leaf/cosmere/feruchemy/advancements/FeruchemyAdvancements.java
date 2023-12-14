/*
 * File updated ~ 4 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.feruchemy.advancements;

import leaf.cosmere.api.Metals;
import leaf.cosmere.common.registration.impl.ManifestationRegistryObject;
import leaf.cosmere.feruchemy.common.manifestation.FeruchemyManifestation;
import leaf.cosmere.feruchemy.common.registries.FeruchemyItems;
import leaf.cosmere.feruchemy.common.registries.FeruchemyManifestations;
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

public class FeruchemyAdvancements implements Consumer<Consumer<Advancement>>
{
	public FeruchemyAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "feruchemy";

		final String titleFormat = "advancements.feruchemy.%s.title";
		final String descriptionFormat = "advancements.feruchemy.%s.description";
		final String achievementPathFormat = "feruchemy:%s/%s";

		Advancement root = Advancement.Builder.advancement()
				.display(FeruchemyItems.METAL_RINGS.get(Metals.MetalType.IRON).get(),
						Component.translatable(String.format(titleFormat, tabName)),
						Component.translatable(String.format(descriptionFormat, tabName)),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,//showToast
						false,//announceChat
						false)//hidden
				.addCriterion("tick", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.ANY))
				.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));


		for (ManifestationRegistryObject<FeruchemyManifestation> manifestation : FeruchemyManifestations.FERUCHEMY_POWERS.values())
		{
			FeruchemyManifestation feruchemyManifestation = manifestation.get();

			Metals.MetalType metalType = feruchemyManifestation.getMetalType();
			String metalName = metalType.getName();


			final Item item = FeruchemyItems.METAL_RINGS.get(metalType).asItem();
			Advancement advancement1 = Advancement.Builder.advancement()
					.parent(root)
					.display(
							item,
							Component.translatable(String.format(titleFormat, tabName + "." + metalName)),
							Component.translatable(String.format(descriptionFormat, tabName + "." + metalName)),
							(ResourceLocation) null,
							FrameType.TASK,
							true, //showToast
							false, //announce
							false)//hidden
					.addCriterion(
							"has_item",
							InventoryChangeTrigger.TriggerInstance.hasItems(item))
					.rewards(new AdvancementRewards(50, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE))
					.save(advancementConsumer, String.format(achievementPathFormat, tabName, metalName));

		}


	}
}