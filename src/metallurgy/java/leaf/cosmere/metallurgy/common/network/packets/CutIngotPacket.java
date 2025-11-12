package leaf.cosmere.metallurgy.common.network.packets;

import leaf.cosmere.common.network.ICosmerePacket;
import leaf.cosmere.metallurgy.common.blocks.entities.MetallurgyWorkbenchBE;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkEvent;

/**
 * Packet sent from client to server when the Cut button is clicked.
 * Contains the position of the workbench and the desired cut percentage.
 */
public class CutIngotPacket implements ICosmerePacket {
    private final BlockPos pos;
    private final int percentage;

    public CutIngotPacket(BlockPos pos, int percentage) {
        this.pos = pos;
        this.percentage = percentage;
    }

    @Override
    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeInt(percentage);
    }

    public static CutIngotPacket decode(FriendlyByteBuf buf) {
        return new CutIngotPacket(buf.readBlockPos(), buf.readInt());
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerPlayer player = context.getSender();
        if (player == null) {
            return;
        }

        MinecraftServer server = player.getServer();
        if (server == null) {
            return;
        }

        server.submitAsync(() -> {
            BlockEntity blockEntity = player.level().getBlockEntity(pos);
            if (blockEntity instanceof MetallurgyWorkbenchBE workbench) {
                workbench.performCut(percentage);
            }
        });
    }
}
