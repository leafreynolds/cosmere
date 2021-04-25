/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.compat.hwyla;

import mcp.mobius.waila.api.IRegistrar;
import mcp.mobius.waila.api.IWailaPlugin;
import mcp.mobius.waila.api.TooltipPosition;
import mcp.mobius.waila.api.WailaPlugin;
import net.minecraft.entity.LivingEntity;

@WailaPlugin
public class HwylaCompat implements IWailaPlugin
{
    @Override
    public void register(IRegistrar iRegistrar)
    {
        iRegistrar.registerComponentProvider(SpiritWebTooltip.INSTANCE, TooltipPosition.BODY, LivingEntity.class);
    }
}
