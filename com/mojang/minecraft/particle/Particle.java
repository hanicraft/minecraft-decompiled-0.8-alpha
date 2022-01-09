package com.mojang.minecraft.particle;

import com.mojang.minecraft.Entity;
import com.mojang.minecraft.level.Level;
import com.mojang.minecraft.renderer.Tesselator;

public class Particle extends Entity {
   private float xd;
   private float yd;
   private float zd;
   public int tex;
   private float uo;
   private float vo;
   private int age = 0;
   private int lifetime = 0;
   private float size;

   public Particle(Level level, float x, float y, float z, float xa, float ya, float za, int tex) {
      super(level);
      this.tex = tex;
      this.setSize(0.2F, 0.2F);
      this.heightOffset = this.bbHeight / 2.0F;
      this.setPos(x, y, z);
      this.xd = xa + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
      this.yd = ya + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
      this.zd = za + (float)(Math.random() * 2.0D - 1.0D) * 0.4F;
      float speed = (float)(Math.random() + Math.random() + 1.0D) * 0.15F;
      float dd = (float)Math.sqrt((double)(this.xd * this.xd + this.yd * this.yd + this.zd * this.zd));
      this.xd = this.xd / dd * speed * 0.4F;
      this.yd = this.yd / dd * speed * 0.4F + 0.1F;
      this.zd = this.zd / dd * speed * 0.4F;
      this.uo = (float)Math.random() * 3.0F;
      this.vo = (float)Math.random() * 3.0F;
      this.size = (float)(Math.random() * 0.5D + 0.5D);
      this.lifetime = (int)(4.0D / (Math.random() * 0.9D + 0.1D));
      this.age = 0;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      if (this.age++ >= this.lifetime) {
         this.remove();
      }

      this.yd = (float)((double)this.yd - 0.04D);
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.98F;
      this.yd *= 0.98F;
      this.zd *= 0.98F;
      if (this.onGround) {
         this.xd *= 0.7F;
         this.zd *= 0.7F;
      }

   }

   public void render(Tesselator t, float a, float xa, float ya, float za, float xa2, float za2) {
      float u0 = ((float)(this.tex % 16) + this.uo / 4.0F) / 16.0F;
      float u1 = u0 + 0.015609375F;
      float v0 = ((float)(this.tex / 16) + this.vo / 4.0F) / 16.0F;
      float v1 = v0 + 0.015609375F;
      float r = 0.1F * this.size;
      float x = this.xo + (this.x - this.xo) * a;
      float y = this.yo + (this.y - this.yo) * a;
      float z = this.zo + (this.z - this.zo) * a;
      t.vertexUV(x - xa * r - xa2 * r, y - ya * r, z - za * r - za2 * r, u0, v1);
      t.vertexUV(x - xa * r + xa2 * r, y + ya * r, z - za * r + za2 * r, u0, v0);
      t.vertexUV(x + xa * r + xa2 * r, y + ya * r, z + za * r + za2 * r, u1, v0);
      t.vertexUV(x + xa * r - xa2 * r, y - ya * r, z + za * r - za2 * r, u1, v1);
   }
}
