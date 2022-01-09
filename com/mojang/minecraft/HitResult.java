package com.mojang.minecraft;

public class HitResult {
   public int type;
   public int x;
   public int y;
   public int z;
   public int f;

   public HitResult(int type, int x, int y, int z, int f) {
      this.type = type;
      this.x = x;
      this.y = y;
      this.z = z;
      this.f = f;
   }
}
