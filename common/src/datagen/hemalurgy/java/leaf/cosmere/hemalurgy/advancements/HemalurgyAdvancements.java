/*
 * File updated ~ 24 - 10 - 2022 ~ Leaf
 */

package leaf.cosmere.hemalurgy.advancements;

import leaf.cosmere.api.CosmereTags;
import leaf.cosmere.api.Metals;
import leaf.cosmere.hemalurgy.common.registries.HemalurgyItems;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Consumer;

public class HemalurgyAdvancements implements Consumer<Consumer<Advancement>>
{
	public HemalurgyAdvancements()
	{
	}

	public void accept(Consumer<Advancement> advancementConsumer)
	{
		String tabName = "hemalurgy";

		final String titleFormat = "advancements.hemalurgy.%s.title";
		final String descriptionFormat = "advancements.hemalurgy.%s.description";
		final String achievementPathFormat = "hemalurgy:%s/%s";

		Advancement root = Advancement.Builder.advancement()
				.display(HemalurgyItems.METAL_SPIKE.get(Metals.MetalType.IRON).get(),
						Component.translatable(String.format(titleFormat, tabName)),
						Component.translatable(String.format(descriptionFormat, tabName)),
						new ResourceLocation("textures/gui/advancements/backgrounds/stone.png"),
						FrameType.TASK,
						false,//showToast
						false,//announceChat
						false)//hidden
				.addCriterion("spike", InventoryChangeTrigger.TriggerInstance.hasItems(ItemPredicate.Builder.item().of(CosmereTags.Items.METAL_SPIKE).build()))
				.save(advancementConsumer, String.format(achievementPathFormat, tabName, "root"));


	}
}