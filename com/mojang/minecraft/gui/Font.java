package com.mojang.minecraft.gui;

import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;

public class Font {
   private int[] charWidths = new int[256];
   private int fontTexture = 0;

   public Font(String name, Textures textures) {
      BufferedImage img;
      try {
         img = ImageIO.read(Textures.class.getResourceAsStream(name));
      } catch (IOException var16) {
         throw new RuntimeException(var16);
      }

      int w = img.getWidth();
      int h = img.getHeight();
      int[] rawPixels = new int[w * h];
      img.getRGB(0, 0, w, h, rawPixels, 0, w);

      for(int i = 0; i < 128; ++i) {
         int xt = i % 16;
         int yt = i / 16;
         int x = 0;

         for(boolean emptyColumn = false; x < 8 && !emptyColumn; ++x) {
            int xPixel = xt * 8 + x;
            emptyColumn = true;

            for(int y = 0; y < 8 && emptyColumn; ++y) {
               int yPixel = (yt * 8 + y) * w;
               int pixel = rawPixels[xPixel + yPixel] & 255;
               if (pixel > 128) {
                  emptyColumn = false;
               }
            }
         }

         if (i == 32) {
            x = 4;
         }

         this.charWidths[i] = x;
      }

      this.fontTexture = textures.loadTexture(name, 9728);
   }

   public void drawShadow(String str, int x, int y, int color) {
      this.draw(str, x + 1, y + 1, color, true);
      this.draw(str, x, y, color);
   }

   public void draw(String str, int x, int y, int color) {
      this.draw(str, x, y, color, false);
   }

   public void draw(String str, int x, int y, int color, boolean darken) {
      char[] chars = str.toCharArray();
      if (darken) {
         color = (color & 16579836) >> 2;
      }

      GL11.glEnable(3553);
      GL11.glBindTexture(3553, this.fontTexture);
      Tesselator t = Tesselator.instance;
      t.init();
      t.color(color);
      int xo = 0;

      for(int i = 0; i < chars.length; ++i) {
         int ix;
         int iy;
         if (chars[i] == '&') {
            ix = "0123456789abcdef".indexOf(chars[i + 1]);
            iy = (ix & 8) * 8;
            int b = (ix & 1) * 191 + iy;
            int g = ((ix & 2) >> 1) * 191 + iy;
            int r = ((ix & 4) >> 2) * 191 + iy;
            color = r << 16 | g << 8 | b;
            i += 2;
            if (darken) {
               color = (color & 16579836) >> 2;
            }

            t.color(color);
         }

         ix = chars[i] % 16 * 8;
         iy = chars[i] / 16 * 8;
         t.vertexUV((float)(x + xo), (float)(y + 8), 0.0F, (float)ix / 128.0F, (float)(iy + 8) / 128.0F);
         t.vertexUV((float)(x + xo + 8), (float)(y + 8), 0.0F, (float)(ix + 8) / 128.0F, (float)(iy + 8) / 128.0F);
         t.vertexUV((float)(x + xo + 8), (float)y, 0.0F, (float)(ix + 8) / 128.0F, (float)iy / 128.0F);
         t.vertexUV((float)(x + xo), (float)y, 0.0F, (float)ix / 128.0F, (float)iy / 128.0F);
         xo += this.charWidths[chars[i]];
      }

      t.flush();
      GL11.glDisable(3553);
   }

   public int width(String str) {
      char[] chars = str.toCharArray();
      int len = 0;

      for(int i = 0; i < chars.length; ++i) {
         if (chars[i] == '&') {
            ++i;
         } else {
            len += this.charWidths[chars[i]];
         }
      }

      return len;
   }
}
