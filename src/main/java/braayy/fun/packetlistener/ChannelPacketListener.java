package braayy.fun.packetlistener;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import net.minecraft.server.v1_13_R2.Packet;
import net.minecraft.server.v1_13_R2.PacketLoginInStart;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class ChannelPacketListener extends ChannelDuplexHandler {

    private UUID uuid;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof PacketLoginInStart) {
            uuid = ((PacketLoginInStart) msg).b().getId();
        }

        boolean cancel;
        if (uuid != null) {
            cancel = SimplePacketListener.callPacketListeners((Packet<?>) msg, Bukkit.getPlayer(uuid));
        } else {
            cancel = SimplePacketListener.callPacketListeners((Packet<?>) msg, null);
        }

        if (!cancel) super.channelRead(ctx, msg);
    }

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        boolean cancel;
        if (uuid != null) {
            cancel = SimplePacketListener.callPacketListeners((Packet<?>) msg, Bukkit.getPlayer(uuid));
        } else {
            cancel = SimplePacketListener.callPacketListeners((Packet<?>) msg, null);
        }

        if (!cancel) super.write(ctx, msg, promise);
    }

}