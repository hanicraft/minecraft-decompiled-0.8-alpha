package com.mojang.minecraft.server;

import com.mojang.minecraft.comm.ServerListener;
import com.mojang.minecraft.comm.SocketConnection;
import com.mojang.minecraft.comm.SocketServer;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MinecraftServer implements Runnable, ServerListener {
   private SocketServer socketServer;
   private Map clientMap = new HashMap();
   private List clients = new ArrayList();

   public MinecraftServer(byte[] ips, int port) throws IOException {
      this.socketServer = new SocketServer(ips, port, this);
   }

   public void clientConnected(SocketConnection serverConnection) {
      Client client = new Client(this, serverConnection);
      this.clientMap.put(serverConnection, client);
      this.clients.add(client);
   }

   public void disconnect(Client client) {
      this.clientMap.remove(client.serverConnection);
      this.clients.remove(client);
   }

   public void clientException(SocketConnection serverConnection, Exception e) {
      Client client = (Client)this.clientMap.get(serverConnection);
      client.handleException(e);
   }

   public void run() {
      while(true) {
         this.tick();

         try {
            Thread.sleep(5L);
         } catch (InterruptedException var2) {
            ;
         }
      }
   }

   private void tick() {
      try {
         this.socketServer.tick();
      } catch (IOException var2) {
         var2.printStackTrace();
      }

   }

   public static void main(String[] args) throws IOException {
      MinecraftServer server = new MinecraftServer(new byte[]{127, 0, 0, 1}, 20801);
      Thread thread = new Thread(server);
      thread.start();
   }
}
