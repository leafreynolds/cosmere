/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.commands.permissions;

import leaf.cosmere.Cosmere;
import net.minecraftforge.server.permission.DefaultPermissionLevel;

import java.text.MessageFormat;

public enum PermissionEnum
{
    TEST("test", DefaultPermissionLevel.OP, ""),
    ;

    private final String path;
    private final DefaultPermissionLevel level;
    private final String description;

    PermissionEnum(String path, DefaultPermissionLevel level, String description)
    {
        this.path = path;
        this.level = level;
        this.description = description;
    }

    public String getNode()
    {
        return MessageFormat.format("{0}.command.{1}", Cosmere.MODID, this.path);
    }

    public DefaultPermissionLevel getLevel()
    {
        return level;
    }

    public String getDescription()
    {
        return description;
    }
}
