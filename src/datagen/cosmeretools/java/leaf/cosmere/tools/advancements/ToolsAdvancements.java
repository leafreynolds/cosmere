/*
 * File updated ~ 22 - 3 - 2024 ~ Leaf
 */

package leaf.cosmere.tools.advancements;

import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class ToolsAdvancements implements Consumer<Consumer<Advancement>>
{
	public ToolsAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "tools";

		final String titleFormat = "advancements.tools.%s.title";
		final String descriptionFormat = "advancements.tools.%s.description";
		final String achievementPathFormat = "tools:%s/%s";

		//Advancement root = Advancement.Builder.advancement()
		//		.display(ToolsItems.WORM.get(),
		//				Component.translatable(String.format(titleFormat, tabName)),
		//				Component.translatable(String.format(descriptionFormat, tabName)),
		//				new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
		//				FrameType.TASK,
		//				false,//showToast
		//				false,//announceChat
		//				false)//hidden
		//		.addCriterion("worm", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(ToolsItems.WORM.get()).build()))
		//		.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));
	}
}