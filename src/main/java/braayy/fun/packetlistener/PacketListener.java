package braayy.fun.packetlistener;

import net.minecraft.server.v1_13_R2.Packet;
import org.bukkit.entity.Player;

@FunctionalInterface
public interface PacketListener {

    Response listen(Packet<?> packet, Player player);

}