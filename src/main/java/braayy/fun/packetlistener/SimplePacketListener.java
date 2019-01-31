package braayy.fun.packetlistener;

import io.netty.channel.Channel;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.server.v1_13_R2.*;
import org.bukkit.craftbukkit.v1_13_R2.CraftServer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public class SimplePacketListener extends JavaPlugin {

    private static final Map<Class<?>, List<PacketListener>> packetListenerMap = new HashMap<>();

    @Override
    public void onEnable() {
        MinecraftServer server = ((CraftServer) getServer()).getServer();
        ServerConnection serverConnection = server.getServerConnection();
        List f = (List) Util.getFieldValue(ServerConnection.class, serverConnection, "f").orElse(new ArrayList<>());

        if (f.size() > 0) {
            ChannelFuture future = (ChannelFuture) f.get(0);
            if (future.channel().pipeline().names().contains("BraayyPacketInjector")) {
                future.channel().pipeline().remove("BraayyPacketInjector");
            }

            future.channel().pipeline().addFirst("BraayyPacketInjector", new ChannelDuplexHandler() {
                @Override
                public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                    Channel clientChannel = (Channel) msg;

                    clientChannel.pipeline().addLast("BraayyPacketInjector", new ChannelDuplexHandler() {
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            ctx.channel().pipeline().addBefore("packet_handler", "BraayyPacketHandler", new ChannelPacketListener());
                            ctx.channel().pipeline().remove("BraayyPacketInjector");

                            super.channelRead(ctx, msg);
                        }
                    });

                    super.channelRead(ctx, msg);
                }
            });
        }
    }

    public static synchronized void registerPacketListener(PacketListener packetListener, Class<?> packetClass) {
        synchronized (packetListenerMap) {
            List<PacketListener> packetListenerList = packetListenerMap.getOrDefault(packetClass, new ArrayList<>());

            packetListenerList.add(packetListener);

            packetListenerMap.put(packetClass, packetListenerList);
        }
    }

    static synchronized boolean callPacketListeners(Packet<?> packet, Player player) {
        synchronized (packetListenerMap) {
            Optional<Response> responses = packetListenerMap.getOrDefault(packet.getClass(), new ArrayList<>()).stream()
                    .map(packetListener -> Util.defaultIfNull(packetListener.listen(packet, player), Response.DEFAULT))
                    .min(Comparator.comparingInt(Response::getPriority));

            return responses.orElse(Response.DEFAULT).isCancel();
        }
    }

}