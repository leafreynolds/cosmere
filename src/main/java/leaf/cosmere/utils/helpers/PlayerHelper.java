/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;

import java.util.Optional;
import java.util.UUID;

public class PlayerHelper
{
	public static String getPlayerName(UUID id, MinecraftServer server)
	{
		final Optional<GameProfile> gameProfile = server.getProfileCache().get(id);
		return gameProfile.isPresent() ? gameProfile.get().getName() : "OFFLINE Player";
	}
}
