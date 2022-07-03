/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.*;
import net.minecraft.commands.CommandFunction;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class AllomancyAdvancements implements Consumer<Consumer<Advancement>>
{
	public AllomancyAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "allomancy";

		final String titleFormat = "advancements.cosmere.%s.title";
		final String descriptionFormat = "advancements.cosmere.%s.description";
		final String achievementPathFormat = "cosmere:%s/%s";

		Advancement root = Advancement.Builder.advancement()
				.display(ItemsRegistry.METAL_VIAL.get(),
						Component.translatable(String.format(titleFormat, tabName)),
						Component.translatable(String.format(descriptionFormat, tabName)),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,//showToast
						false,//announceChat
						false)//hidden
				.addCriterion("tick", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.ANY))
				.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));


		for (RegistryObject<AllomancyBase> manifestation : ManifestationRegistry.ALLOMANCY_POWERS.values())
		{
			AllomancyBase allomancyBase = (AllomancyBase) manifestation.get();

			Metals.MetalType metalType = allomancyBase.getMetalType();
			String metalName = metalType.getName();

			final Item item = allomancyBase.getMetalType().getNuggetItem();
			Advancement advancement1 = Advancement.Builder.advancement()
					.parent(root)
					.display(
							item,
							Component.translatable(String.format(titleFormat, (tabName+"."+metalName))),
							Component.translatable(String.format(descriptionFormat, tabName+"."+metalName)),
							(ResourceLocation) null,
							FrameType.TASK,
							true, //showToast
							true, //announce
							false)//hidden
					.addCriterion(
							"has_item",
							InventoryChangeTrigger.TriggerInstance.hasItems(item))
					.rewards(new AdvancementRewards(50, new ResourceLocation[0], new ResourceLocation[0], CommandFunction.CacheableFunction.NONE))
					.save(advancementConsumer, String.format(achievementPathFormat, tabName, metalName));

		}


	}
}