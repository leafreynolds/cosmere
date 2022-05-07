/*
 * File created ~ 24 - 4 - 2021 ~ Leaf
 */

package leaf.cosmere.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

public class DataSerializersRegistry
{
	public static final EntityDataSerializer<ResourceLocation> RESOURCE_LOCATION = new EntityDataSerializer<ResourceLocation>()
	{

		@Override
		public void write(FriendlyByteBuf buf, ResourceLocation value)
		{
			buf.writeResourceLocation(value);
		}

		public ResourceLocation read(FriendlyByteBuf buf)
		{
			return buf.readResourceLocation();
		}

		@Override
		public ResourceLocation copy(ResourceLocation value)
		{
			return value;
		}
	};

	public static void register()
	{
		EntityDataSerializers.registerSerializer(RESOURCE_LOCATION);
	}

}
