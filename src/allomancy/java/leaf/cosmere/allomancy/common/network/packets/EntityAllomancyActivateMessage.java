package leaf.cosmere.allomancy.common.network.packets;

import leaf.cosmere.allomancy.common.manifestation.AllomancyBrass;
import leaf.cosmere.allomancy.common.manifestation.AllomancyZinc;
import leaf.cosmere.api.Metals;
import leaf.cosmere.common.cap.entity.SpiritwebCapability;
import leaf.cosmere.common.network.ICosmerePacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkEvent;

public class EntityAllomancyActivateMessage implements ICosmerePacket
{
	public CompoundTag data;

	public EntityAllomancyActivateMessage(Metals.MetalType metalType, boolean isSingleTarget, int singleTargetEntityID)
	{
		data = new CompoundTag();
		data.putInt("metalID", metalType.getID());
		data.putBoolean("isSingleTarget", isSingleTarget);
		data.putInt("singleTargetEntityID", singleTargetEntityID);
	}

	public static EntityAllomancyActivateMessage decode(FriendlyByteBuf buf)
	{
		CompoundTag data = buf.readNbt();
		Metals.MetalType metalType = Metals.MetalType.valueOf(data.getInt("metalID")).get();
		boolean isSingleTarget = data.getBoolean("isSingleTarget");
		int singleTargetEntityID = data.getInt("singleTargetEntityID");

		return new EntityAllomancyActivateMessage(metalType, isSingleTarget, singleTargetEntityID);
	}

	@Override
	public void handle(NetworkEvent.Context context)
	{
		ServerPlayer player = context.getSender();
		MinecraftServer server = player.getServer();
		server.submitAsync(() -> SpiritwebCapability.get(player).ifPresent((cap) -> {
			if (!data.isEmpty())
			{
				Metals.MetalType metalType = Metals.MetalType.valueOf(data.getInt("metalID")).get();
				boolean isSingleTarget = data.getBoolean("isSingleTarget");
				int singleTargetEntityID = data.getInt("singleTargetEntityID");

				switch (metalType)
				{
					case BRASS:
						AllomancyBrass.BrassThread brassThread = AllomancyBrass.playerThreadMap.get(player.getStringUUID());
						brassThread.isSingleTarget = isSingleTarget;
						if (isSingleTarget)
						{
							brassThread.singleTargetEntityID = singleTargetEntityID;
						}
						break;
					case ZINC:
						AllomancyZinc.ZincThread zincThread = AllomancyZinc.playerThreadMap.get(player.getStringUUID());
						zincThread.isSingleTarget = isSingleTarget;
						if (isSingleTarget)
						{
							zincThread.singleTargetEntityID = singleTargetEntityID;
						}
						break;
					default:
						break;
				}
			}
		}));
	}

	@Override
	public void encode(FriendlyByteBuf buf)
	{
		buf.writeNbt(data);
	}
}