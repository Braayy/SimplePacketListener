# SimplePacketListener

Hello folks!

This is a simple project of a packet listener.

This project uses Java 8 lambda syntax(if you want) to get more simplicity to the developer :D

How to use
----------

1. Put `SimplePacketListener.jar` on plugins folder and classpath of your IDE.

2. On `onEnable` method of your plugin register a packet listener using this example:
```Java
SimplePacketListener.registerPacketListener((packet, player) -> {
    PacketStatusOutServerInfo packetStatus = (PacketStatusOutServerInfo) packet;

    return Response.DEFAULT;
}, PacketStatusOutServerInfo.class);
```

3. Put on plugin.yml:
```YML
depend: [SimplePacketListener]
```

4. Compile and run your plugin

Explanation of that code
------------------------

1. `SimplePacketListener.registerPacketListener` will register your packet listener to can be called when choosed packet get caught.

2. `(packet, player) -> {}` is your packet listener. `packet` is the instance of the caught packet. `player` is the player **IF** when the packet was caught has a player assigned with it.

3. `return Response.DEFAULT` is the response, a class that will tell to `SimplePacketListener` if you want to cancel the packet. The `DEFAULT` response will not cancel and have a 0 priority.

4. `PacketStatusOutServerInfo.class` is the class of the packet that you want to listen.

Util class
----------

I've created a simple class called `Util` on the project, and you and use it.

1. `void Util.setFieldValue(class, obj, name, value)`

2. `Optional<Object> Util.getFieldValue(class, obj, name)`