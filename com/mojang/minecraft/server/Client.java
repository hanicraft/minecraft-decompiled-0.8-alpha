package com.mojang.minecraft.server;

import com.mojang.minecraft.comm.ConnectionListener;
import com.mojang.minecraft.comm.SocketConnection;
import java.nio.ByteBuffer;

public class Client implements ConnectionListener {
   public final SocketConnection serverConnection;
   private final MinecraftServer server;

   public Client(MinecraftServer server, SocketConnection serverConnection) {
      this.server = server;
      this.serverConnection = serverConnection;
      serverConnection.setConnectionListener(this);
   }

   public void command(byte cmd, int remaining, ByteBuffer in) {
   }

   public void handleException(Exception e) {
      this.disconnect();
   }

   public void disconnect() {
      this.server.disconnect(this);
   }
}
