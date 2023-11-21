/*
 * File updated ~ 19 - 11 - 2023 ~ Leaf
 */

package leaf.cosmere.aviar.advancements;

import net.minecraft.advancements.Advancement;

import java.util.function.Consumer;

public class AviarAdvancements implements Consumer<Consumer<Advancement>>
{
	public AviarAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "aviar";

		final String titleFormat = "advancements.aviar.%s.title";
		final String descriptionFormat = "advancements.aviar.%s.description";
		final String achievementPathFormat = "aviar:%s/%s";

		//Advancement root = Advancement.Builder.advancement()
		//		.display(AviarItems.WORM.get(),
		//				Component.translatable(String.format(titleFormat, tabName)),
		//				Component.translatable(String.format(descriptionFormat, tabName)),
		//				new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
		//				FrameType.TASK,
		//				false,//showToast
		//				false,//announceChat
		//				false)//hidden
		//		.addCriterion("worm", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(AviarItems.WORM.get()).build()))
		//		.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));
	}
}