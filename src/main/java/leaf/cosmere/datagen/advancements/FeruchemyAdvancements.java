/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.constants.Metals;
import leaf.cosmere.manifestation.AManifestation;
import leaf.cosmere.manifestation.feruchemy.FeruchemyBase;
import leaf.cosmere.registry.ItemsRegistry;
import leaf.cosmere.registry.ManifestationRegistry;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.TickTrigger;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
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
		final String categoryName = "feruchemy";

		Advancement root = Advancement.Builder.advancement()
				.display(ItemsRegistry.GUIDE.get(),
						new TranslatableComponent("advancements.cosmere." + categoryName + ".title"),
						new TranslatableComponent("advancements.cosmere." + categoryName + ".description"),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,
						false,
						false)
				.addCriterion("tick", new TickTrigger.TriggerInstance(EntityPredicate.Composite.ANY))
				//.withRewards(new AdvancementRewards(0, new ResourceLocation[]{new ResourceLocation("cosmere:guide")}, new ResourceLocation[0], FunctionObject.CacheableFunction.EMPTY))
				.save(advancementConsumer, categoryName + "/root");

		for (RegistryObject<AManifestation> manifestation : ManifestationRegistry.FERUCHEMY_POWERS.values())
		{
			FeruchemyBase feruchemyBase = (FeruchemyBase) manifestation.get();

			Metals.MetalType metalType = feruchemyBase.getMetalType();
			String metalName = metalType.getName();

			Advancement manifestationObtainedAdvancement = Advancement.Builder.advancement()
					.parent(root)
					.display(
							Items.WOODEN_PICKAXE,
							new TranslatableComponent("advancements.cosmere." + categoryName + ".title"),
							new TranslatableComponent("advancements.cosmere." + categoryName + ".description"),
							null,
							FrameType.TASK,
							true,
							true,
							false)
					.addCriterion(
							"get_stone",
							InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.STONE_TOOL_MATERIALS).build()))
					.save(advancementConsumer, categoryName + "/" + metalName);
		}


	}
}