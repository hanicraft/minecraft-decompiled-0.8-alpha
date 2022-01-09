package com.mojang.minecraft;

import com.mojang.minecraft.level.Level;
import org.lwjgl.input.Keyboard;

public class Player extends Entity {
   public Player(Level level) {
      super(level);
      this.heightOffset = 1.62F;
   }

   public void tick() {
      this.xo = this.x;
      this.yo = this.y;
      this.zo = this.z;
      float xa = 0.0F;
      float ya = 0.0F;
      if (Keyboard.isKeyDown(19)) {
         this.resetPos();
      }

      if (Keyboard.isKeyDown(200) || Keyboard.isKeyDown(17)) {
         --ya;
      }

      if (Keyboard.isKeyDown(208) || Keyboard.isKeyDown(31)) {
         ++ya;
      }

      if (Keyboard.isKeyDown(203) || Keyboard.isKeyDown(30)) {
         --xa;
      }

      if (Keyboard.isKeyDown(205) || Keyboard.isKeyDown(32)) {
         ++xa;
      }

      if ((Keyboard.isKeyDown(57) || Keyboard.isKeyDown(219)) && this.onGround) {
         this.yd = 0.5F;
      }

      this.moveRelative(xa, ya, this.onGround ? 0.1F : 0.02F);
      this.yd = (float)((double)this.yd - 0.08D);
      this.move(this.xd, this.yd, this.zd);
      this.xd *= 0.91F;
      this.yd *= 0.98F;
      this.zd *= 0.91F;
      if (this.onGround) {
         this.xd *= 0.7F;
         this.zd *= 0.7F;
      }

   }
}
