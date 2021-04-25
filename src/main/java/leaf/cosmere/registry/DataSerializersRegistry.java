/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import net.minecraft.network.PacketBuffer;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.IDataSerializer;
import net.minecraft.util.ResourceLocation;

public class DataSerializersRegistry
{
    public static final IDataSerializer<ResourceLocation> RESOURCE_LOCATION = new IDataSerializer<ResourceLocation>()
    {

        @Override
        public void write(PacketBuffer buf, ResourceLocation value)
        {
            buf.writeResourceLocation(value);
        }

        public ResourceLocation read(PacketBuffer buf)
        {
            return buf.readResourceLocation();
        }

        @Override
        public ResourceLocation copyValue(ResourceLocation value)
        {
            return value;
        }
    };

    public static void register()
    {
        DataSerializers.registerSerializer(RESOURCE_LOCATION);
    }

}
