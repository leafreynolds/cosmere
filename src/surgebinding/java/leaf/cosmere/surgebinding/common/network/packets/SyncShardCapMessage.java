/*
 * File updated ~ 2026-05-02 ~ Leaf (ported 1.20.1 Forge -> 1.21.1 NeoForge)
 *
 * Old shape: SyncShardCapMessage(CompoundTag, int slot) plus FriendlyByteBuf encode/decode and
 * NetworkEvent.Context. The handler called `stack.getCapability(RADIANT_SHARD_DATA).ifPresent`
 * → `cap.deserializeNBT(nbt)`. In 1.21.1 stack capabilities are gone — RadiantShardData lives
 * directly on the stack via `DataComponents.CUSTOM_DATA`. The new handler merges the received
 * CompoundTag into the stack's CUSTOM_DATA.
 *
 * Migrated to record + StreamCodec + IPayloadContext per StoreTapPowerMessage template.
 */

package leaf.cosmere.surgebinding.common.network.packets;

import io.netty.buffer.ByteBuf;
import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.surgebinding.common.Surgebinding;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.neoforged.neoforge.network.handling.IPayloadContext;

public record SyncShardCapMessage(CompoundTag nbt, int slot) implements ICosmerePacket
{
	public static final CustomPacketPayload.Type<SyncShardCapMessage> TYPE =
			new CustomPacketPayload.Type<>(Surgebinding.rl("sync_shard_cap"));

	public static final StreamCodec<ByteBuf, SyncShardCapMessage> STREAM_CODEC =
			StreamCodec.of(
					(buf, msg) ->
					{
						ByteBufCodecs.COMPOUND_TAG.encode(buf, msg.nbt);
						ByteBufCodecs.VAR_INT.encode(buf, msg.slot);
					},
					buf -> new SyncShardCapMessage(
							ByteBufCodecs.COMPOUND_TAG.decode(buf),
							ByteBufCodecs.VAR_INT.decode(buf)
					)
			);

	@Override
	public CustomPacketPayload.Type<? extends CustomPacketPayload> type()
	{
		return TYPE;
	}

	@Override
	public void handle(IPayloadContext context)
	{
		context.enqueueWork(() ->
		{
			Minecraft mc = Minecraft.getInstance();
			if (mc.player == null)
			{
				return;
			}

			ItemStack stack = mc.player.getInventory().getItem(slot);
			if (stack.isEmpty())
			{
				return;
			}
			// Merge incoming tag into the stack's CUSTOM_DATA (replaces the old cap.deserializeNBT).
			CustomData.update(DataComponents.CUSTOM_DATA, stack, existing ->
			{
				for (String key : nbt.getAllKeys())
				{
					existing.put(key, nbt.get(key));
				}
			});
		});
	}
}
