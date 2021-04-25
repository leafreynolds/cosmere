/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.helpers;

import com.mojang.authlib.GameProfile;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Hand;

import java.util.OptionalInt;
import java.util.UUID;
import java.util.function.Predicate;

public class PlayerHelper
{
    public static String getPlayerName(UUID id, MinecraftServer server)
    {
        GameProfile profileByUUID = server.getPlayerProfileCache().getProfileByUUID(id);
        return profileByUUID != null ? profileByUUID.getName() : "OFFLINE Player";
    }
}
