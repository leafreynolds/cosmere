/*
 * File updated ~ 6 - 9 - 2022 ~ Leaf
 */

package leaf.cosmere.common.registry;

import leaf.cosmere.common.Cosmere;
import leaf.cosmere.common.registration.impl.GameEventDeferredRegister;

/**
 * Describes an in game event or action that can be detected by listeners such as the Sculk Sensor block.
 */
public class GameEventRegistry
{
	public static final GameEventDeferredRegister GAME_EVENTS = new GameEventDeferredRegister(Cosmere.MODID);


}
