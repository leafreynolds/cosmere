package leaf.cosmere.common.compat.dynamickeybinds;

import dev.munebase.dynamickeybinds.model.DisplaySpec;
import dev.munebase.dynamickeybinds.server.DynamicKeyServerApi;
import net.minecraft.server.level.ServerPlayer;

import java.util.Optional;

/**
 * The only class that touches DynamicKeybinds. Only call it behind {@link PowerKeybinds#ENABLED}.
 */
final class DynamicKeybindsCompat
{
	private static final String CATEGORY_PREFIX = "category.";

	private DynamicKeybindsCompat()
	{
	}

	static void grant(ServerPlayer player, PowerKeybinds.Def def)
	{
		// DynamicKeybinds adds "category." itself; strip ours so dynamic and static keys share one category
		String category = def.category().startsWith(CATEGORY_PREFIX) ? def.category().substring(CATEGORY_PREFIX.length()) : def.category();
		DynamicKeyServerApi.registerForPlayer(player, def.id(), def.defaultKey(), category, Optional.empty(), DisplaySpec.empty());
	}
}
