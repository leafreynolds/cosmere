/*
 * File updated ~ 29 - 7 - 2021 ~ Leaf
 * Thank you botania! Pretty much directly copied from botania as this is so much easier to work with!
 */
package leaf.cosmere.common.loot;

import leaf.cosmere.common.Cosmere;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Cosmere.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public final class LootHandler
{

	@SubscribeEvent
	public static void lootLoad(LootTableLoadEvent evt)
	{
		String prefix = "minecraft:chests/";
		String name = evt.getName().toString();

		if (name.startsWith(prefix))
		{
			String file = name.substring(name.indexOf(prefix) + prefix.length());
			switch (file)
			{
				case "abandoned_mineshaft":
				case "bastion_treasure":
				case "bastion_other":
				case "bastion_bridge":
				case "desert_pyramid":
				case "end_city_treasure":
				case "jungle_temple":
				case "simple_dungeon":
				case "spawn_bonus_chest":
				case "stronghold_corridor":
				case "stronghold_crossing":
				case "stronghold_library":
				case "village_blacksmith":
				case "woodland_mansion":
					evt.getTable().addPool(getInjectPool(file));
					break;
				default:
					break;
			}
		}
	}

	public static LootPool getInjectPool(String entryName)
	{
		return LootPool.lootPool()
				.add(getInjectEntry(entryName, 1))
				.setBonusRolls(UniformGenerator.between(0, 1))
				.name("cosmere_inject")
				.build();
	}

	private static LootPoolEntryContainer.Builder<?> getInjectEntry(String name, int weight)
	{
		ResourceLocation table = Cosmere.rl("inject/" + name);
		return LootTableReference.lootTableReference(table)
				.setWeight(weight);
	}

}
