package leaf.cosmere.common.registration.impl;

import leaf.cosmere.common.registration.WrappedDeferredRegister;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.gameevent.GameEvent;

import java.util.function.Supplier;

public class GameEventDeferredRegister extends WrappedDeferredRegister<GameEvent>
{

	public GameEventDeferredRegister(String modid)
	{
		super(modid, Registries.GAME_EVENT);
	}

	public GameEventRegistryObject<GameEvent> register(String name)
	{
		return register(name, 16);
	}

	public GameEventRegistryObject<GameEvent> register(String name, int notificationRadius)
	{
		return register(name, () -> new GameEvent(modid + ":" + name, notificationRadius));
	}

	public <GAME_EVENT extends GameEvent> GameEventRegistryObject<GAME_EVENT> register(String name, Supplier<? extends GAME_EVENT> sup)
	{
		return register(name, sup, GameEventRegistryObject::new);
	}
}