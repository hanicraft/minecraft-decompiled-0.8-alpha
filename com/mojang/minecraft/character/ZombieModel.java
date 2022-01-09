package com.mojang.minecraft.character;

public class ZombieModel {
   public Cube head = new Cube(0, 0);
   public Cube body;
   public Cube arm0;
   public Cube arm1;
   public Cube leg0;
   public Cube leg1;

   public ZombieModel() {
      this.head.addBox(-4.0F, -8.0F, -4.0F, 8, 8, 8);
      this.body = new Cube(16, 16);
      this.body.addBox(-4.0F, 0.0F, -2.0F, 8, 12, 4);
      this.arm0 = new Cube(40, 16);
      this.arm0.addBox(-3.0F, -2.0F, -2.0F, 4, 12, 4);
      this.arm0.setPos(-5.0F, 2.0F, 0.0F);
      this.arm1 = new Cube(40, 16);
      this.arm1.addBox(-1.0F, -2.0F, -2.0F, 4, 12, 4);
      this.arm1.setPos(5.0F, 2.0F, 0.0F);
      this.leg0 = new Cube(0, 16);
      this.leg0.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.leg0.setPos(-2.0F, 12.0F, 0.0F);
      this.leg1 = new Cube(0, 16);
      this.leg1.addBox(-2.0F, 0.0F, -2.0F, 4, 12, 4);
      this.leg1.setPos(2.0F, 12.0F, 0.0F);
   }

   public void render(float time) {
      this.head.yRot = (float)Math.sin((double)time * 0.83D) * 1.0F;
      this.head.xRot = (float)Math.sin((double)time) * 0.8F;
      this.arm0.xRot = (float)Math.sin((double)time * 0.6662D + 3.141592653589793D) * 2.0F;
      this.arm0.zRot = (float)(Math.sin((double)time * 0.2312D) + 1.0D) * 1.0F;
      this.arm1.xRot = (float)Math.sin((double)time * 0.6662D) * 2.0F;
      this.arm1.zRot = (float)(Math.sin((double)time * 0.2812D) - 1.0D) * 1.0F;
      this.leg0.xRot = (float)Math.sin((double)time * 0.6662D) * 1.4F;
      this.leg1.xRot = (float)Math.sin((double)time * 0.6662D + 3.141592653589793D) * 1.4F;
      this.head.render();
      this.body.render();
      this.arm0.render();
      this.arm1.render();
      this.leg0.render();
      this.leg1.render();
   }
}
