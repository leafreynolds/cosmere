/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.utils.helpers;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public class PlayerHelper
{
    public static String getPlayerName(UUID id, MinecraftServer server)
    {
        GameProfile profileByUUID = server.getPlayerProfileCache().getProfileByUUID(id);
        return profileByUUID != null ? profileByUUID.getName() : "OFFLINE Player";
    }
}
