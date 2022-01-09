package com.mojang.minecraft.renderer;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import javax.imageio.ImageIO;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

public class Textures {
   private HashMap idMap = new HashMap();

   public int loadTexture(String resourceName, int mode) {
      try {
         if (this.idMap.containsKey(resourceName)) {
            return ((Integer)this.idMap.get(resourceName)).intValue();
         } else {
            IntBuffer ib = BufferUtils.createIntBuffer(1);
            ib.clear();
            GL11.glGenTextures(ib);
            int id = ib.get(0);
            this.idMap.put(resourceName, id);
            System.out.println(resourceName + " -> " + id);
            GL11.glBindTexture(3553, id);
            GL11.glTexParameteri(3553, 10241, mode);
            GL11.glTexParameteri(3553, 10240, mode);
            BufferedImage img = ImageIO.read(Textures.class.getResourceAsStream(resourceName));
            int w = img.getWidth();
            int h = img.getHeight();
            ByteBuffer pixels = BufferUtils.createByteBuffer(w * h * 4);
            int[] rawPixels = new int[w * h];
            img.getRGB(0, 0, w, h, rawPixels, 0, w);

            for(int i = 0; i < rawPixels.length; ++i) {
               int a = rawPixels[i] >> 24 & 255;
               int r = rawPixels[i] >> 16 & 255;
               int g = rawPixels[i] >> 8 & 255;
               int b = rawPixels[i] & 255;
               rawPixels[i] = a << 24 | b << 16 | g << 8 | r;
            }

            pixels.asIntBuffer().put(rawPixels);
            GLU.gluBuild2DMipmaps(3553, 6408, w, h, 6408, 5121, pixels);
            return id;
         }
      } catch (IOException var15) {
         throw new RuntimeException("!!");
      }
   }
}
