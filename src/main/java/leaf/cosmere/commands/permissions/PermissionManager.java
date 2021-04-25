/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.permissions;

import net.minecraftforge.server.permission.PermissionAPI;

import java.util.Arrays;

public class PermissionManager
{

    public static void init()
    {
        Arrays.stream(PermissionEnum.values()).forEach(perm -> PermissionAPI.registerNode(perm.getNode(), perm.getLevel(), perm.getDescription()));
    }
}
