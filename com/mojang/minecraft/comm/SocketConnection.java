package com.mojang.minecraft.comm;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class SocketConnection {
   public static final int BUFFER_SIZE = 131068;
   private boolean connected;
   private SocketChannel socketChannel;
   public ByteBuffer readBuffer = ByteBuffer.allocate(131068);
   public ByteBuffer writeBuffer = ByteBuffer.allocate(131068);
   protected long lastRead;
   private ConnectionListener connectionListener;
   private int bytesRead;
   private int totalBytesWritten;
   private int maxBlocksPerIteration = 3;
   private Socket socket;
   private BufferedInputStream in;
   private BufferedOutputStream out;

   public SocketConnection(String ip, int port) throws UnknownHostException, IOException {
      this.socketChannel = SocketChannel.open();
      this.socketChannel.connect(new InetSocketAddress(ip, port));
      this.socketChannel.configureBlocking(false);
      this.lastRead = System.currentTimeMillis();
      this.connected = true;
      this.readBuffer.clear();
      this.writeBuffer.clear();
   }

   public void setMaxBlocksPerIteration(int maxBlocksPerIteration) {
      this.maxBlocksPerIteration = maxBlocksPerIteration;
   }

   public String getIp() {
      return this.socket.getInetAddress().toString();
   }

   public SocketConnection(SocketChannel socketChannel) throws IOException {
      this.socketChannel = socketChannel;
      socketChannel.configureBlocking(false);
      this.lastRead = System.currentTimeMillis();
      this.socket = socketChannel.socket();
      this.connected = true;
      this.readBuffer.clear();
      this.writeBuffer.clear();
   }

   public ByteBuffer getBuffer() {
      return this.writeBuffer;
   }

   public void setConnectionListener(ConnectionListener connectionListener) {
      this.connectionListener = connectionListener;
   }

   public boolean isConnected() {
      return this.connected;
   }

   public void disconnect() {
      this.connected = false;

      try {
         if (this.in != null) {
            this.in.close();
         }

         this.in = null;
      } catch (Exception var4) {
         var4.printStackTrace();
      }

      try {
         if (this.out != null) {
            this.out.close();
         }

         this.out = null;
      } catch (Exception var3) {
         var3.printStackTrace();
      }

      try {
         if (this.socket != null) {
            this.socket.close();
         }

         this.socket = null;
      } catch (Exception var2) {
         var2.printStackTrace();
      }

   }

   public void tick() throws IOException {
      this.writeBuffer.flip();
      this.socketChannel.write(this.writeBuffer);
      this.writeBuffer.compact();
      this.readBuffer.compact();
      this.socketChannel.read(this.readBuffer);
      this.readBuffer.flip();
      if (this.readBuffer.remaining() > 0) {
         this.connectionListener.command(this.readBuffer.get(0), this.readBuffer.remaining(), this.readBuffer);
      }

   }

   public int getSentBytes() {
      return this.totalBytesWritten;
   }

   public int getReadBytes() {
      return this.bytesRead;
   }

   public void clearSentBytes() {
      this.totalBytesWritten = 0;
   }

   public void clearReadBytes() {
      this.bytesRead = 0;
   }
}
