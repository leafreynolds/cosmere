package leaf.cosmere.common.compat.dynamickeybinds;

import leaf.cosmere.api.spiritweb.ISpiritweb;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.fml.ModList;

import java.util.*;
import java.util.function.Predicate;

/**
 * Power-gated keybinds. Without DynamicKeybinds these are plain keybinds registered at startup.
 * With it, the server grants each key to a player once they hold the matching power.
 */
public final class PowerKeybinds
{
	public static final boolean ENABLED = ModList.get().isLoaded("dynamickeybinds");

	public record Def(String name, int defaultKey, String category, Predicate<ISpiritweb> unlocked)
	{
		/** DynamicKeybinds names its mappings "key." + id, so this keeps our existing lang keys. */
		public String id()
		{
			return name.substring("key.".length());
		}
	}

	public static final List<Def> DEFS = new ArrayList<>();

	// keyed by player object, which is recreated on login/respawn, so each key is re-checked once per session
	private static final Map<ServerPlayer, Set<String>> GRANTED = new WeakHashMap<>();

	private PowerKeybinds()
	{
	}

	public static Def add(String name, int defaultKey, String category, Predicate<ISpiritweb> unlocked)
	{
		Def def = new Def(name, defaultKey, category, unlocked);
		DEFS.add(def);
		return def;
	}

	/** Gives this player the key now. No-op if DynamicKeybinds is absent or the key was already granted. */
	public static void grant(ServerPlayer player, Def def)
	{
		if (!ENABLED || !GRANTED.computeIfAbsent(player, p -> new HashSet<>()).add(def.id()))
		{
			return;
		}
		DynamicKeybindsCompat.grant(player, def);
	}

	/** Grants every key whose power the player currently holds. Keys are never revoked, so rebinds survive. */
	public static void tick(ServerPlayer player, ISpiritweb spiritweb)
	{
		for (Def def : DEFS)
		{
			if (def.unlocked().test(spiritweb))
			{
				grant(player, def);
			}
		}
	}
}
