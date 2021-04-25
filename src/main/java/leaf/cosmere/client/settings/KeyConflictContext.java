/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.client.settings;

import net.minecraftforge.client.settings.IKeyConflictContext;

public class KeyConflictContext implements IKeyConflictContext
{
    public static final KeyConflictContext DEFAULT = new KeyConflictContext();

    @Override
    public boolean isActive()
    {
        return true;
    }

    @Override
    public boolean conflicts(IKeyConflictContext other)
    {
        return false;
    }
}
