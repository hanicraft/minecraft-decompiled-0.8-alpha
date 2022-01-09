package com.mojang.minecraft;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;

public class MinecraftApplet extends Applet {
   private Canvas canvas;
   private Minecraft minecraft;
   private Thread thread = null;

   public void init() {
      this.canvas = new Canvas() {
         public void addNotify() {
            super.addNotify();
            MinecraftApplet.this.startGameThread();
         }

         public void removeNotify() {
            MinecraftApplet.this.stopGameThread();
            super.removeNotify();
         }
      };
      this.minecraft = new Minecraft(this.canvas, 640, 480, false);
      this.minecraft.appletMode = true;
      this.setLayout(new BorderLayout());
      this.add(this.canvas, "Center");
      this.canvas.setFocusable(true);
      this.validate();
   }

   public void startGameThread() {
      if (this.thread == null) {
         this.thread = new Thread(this.minecraft);
         this.thread.start();
      }
   }

   public void start() {
      this.minecraft.pause = false;
   }

   public void stop() {
      this.minecraft.pause = true;
   }

   public void destroy() {
      this.stopGameThread();
   }

   public void stopGameThread() {
      if (this.thread != null) {
         this.minecraft.stop();

         try {
            this.thread.join(5000L);
         } catch (InterruptedException var4) {
            try {
               this.minecraft.destroy();
            } catch (Exception var3) {
               var3.printStackTrace();
            }
         }

         this.thread = null;
      }
   }
}
