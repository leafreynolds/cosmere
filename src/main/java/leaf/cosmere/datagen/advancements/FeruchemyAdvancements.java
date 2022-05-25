/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.allomancy.AllomancyBase;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementRewards;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.commands.CommandFunction;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;

public class FeruchemyAdvancements implements Consumer<Consumer<Advancement>>
{
	public FeruchemyAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "feruchemy";

		final String titleFormat = "advancements.cosmere.%s.title";
		final String descriptionFormat = "advancements.cosmere.%s.description";
		final String achievementPathFormat = "cosmere:%s/%s";

		Advancement root = Advancement.Builder.advancement()
				.display(ItemsRegistry.METAL_RINGS.get(Metals.MetalType.IRON).get(),
						new TranslatableComponent(String.format(titleFormat, tabName)),
						new TranslatableComponent(String.format(descriptionFormat, tabName)),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,//showToast
						false,//announceChat
						false)//hidden
				.addCriterion("tick", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
				.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));


		for (RegistryObject<AManifestation> manifestation : ManifestationRegistry.FERUCHEMY_POWERS.values())
		{
			FeruchemyBase feruchemyBase = (FeruchemyBase) manifestation.get();

			Metals.MetalType metalType = feruchemyBase.getMetalType();
			String metalName = metalType.getName();


			final Item item = metalType.getRingItem();
			Advancement advancement1 = Advancement.Builder.advancement()
					.parent(root)
					.display(
							item,
							new TranslatableComponent(String.format(titleFormat, tabName+"."+metalName)),
							new TranslatableComponent(String.format(descriptionFormat, tabName+"."+metalName)),
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