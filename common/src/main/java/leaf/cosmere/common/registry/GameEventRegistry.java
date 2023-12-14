/*
 * File updated ~ 26 - 5 - 2023 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.GameEventDeferredRegister;
import leaf.cosmere.common.registration.impl.GameEventRegistryObject;
import net.minecraft.world.level.gameevent.GameEvent;

/**
 * Describes an in game event or action that can be detected by listeners such as the Sculk Sensor block.
 */
public class GameEventRegistry
{
	public static final GameEventDeferredRegister GAME_EVENTS = new GameEventDeferredRegister(Cosmere.MODID);

	public static final GameEventRegistryObject<GameEvent> KINETIC_INVESTITURE = GAME_EVENTS.register("kinetic_investiture", 64);

}
