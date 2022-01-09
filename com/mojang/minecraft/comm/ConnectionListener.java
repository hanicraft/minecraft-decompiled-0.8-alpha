package com.mojang.minecraft.comm;

import java.nio.ByteBuffer;

public interface ConnectionListener {
   void handleException(Exception var1);

   void command(byte var1, int var2, ByteBuffer var3);
}
