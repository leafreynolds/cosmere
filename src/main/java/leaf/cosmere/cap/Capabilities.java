/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.cap;

import leaf.cosmere.cap.entity.ISpiritweb;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;

public class Capabilities
{
    @CapabilityInject(ISpiritweb.class)
    public static final Capability<ISpiritweb> SPIRITWEB_CAPABILITY = null;
}
