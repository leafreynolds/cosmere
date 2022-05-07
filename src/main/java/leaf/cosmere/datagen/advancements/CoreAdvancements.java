/*
 * File created ~ 13 - 7 - 2021 ~ Leaf
 */

package leaf.cosmere.datagen.advancements;

import leaf.cosmere.registry.ItemsRegistry;
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

import java.util.function.Consumer;

public class CoreAdvancements implements Consumer<Consumer<Advancement>>
{
	public CoreAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		final String categoryName = "root";

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
				.save(advancementConsumer, "core/" + categoryName);

		Advancement advancement1 = Advancement.Builder.advancement()
				.parent(root)
				.display(
						Items.WOODEN_PICKAXE,
						new TranslatableComponent("advancements.cosmere.mine_stone.title"),
						new TranslatableComponent("advancements.cosmere.mine_stone.description"),
						null,
						FrameType.TASK,
						true,
						true,
						false)
				.addCriterion(
						"get_stone",
						InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ItemTags.STONE_TOOL_MATERIALS).build()))
				.save(advancementConsumer, "core/mine_stone");

	}
}