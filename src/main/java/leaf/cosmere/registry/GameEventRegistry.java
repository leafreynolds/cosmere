/*
 * File created ~ 6 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.registry;

import net.minecraft.core.Registry;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Describes an in game event or action that can be detected by listeners such as the Sculk Sensor block.
 */
public class GameEventRegistry
{
	private static GameEvent register(String pName) {
		return register(pName, 16);
	}
	private static GameEvent register(String pName, int pNotificationRadius)
	{
		return Registry.register(Registry.GAME_EVENT, pName, new GameEvent(pName, pNotificationRadius));
	}
}
