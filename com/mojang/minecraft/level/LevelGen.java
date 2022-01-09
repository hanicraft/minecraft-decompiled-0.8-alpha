package com.mojang.minecraft.level;

import com.mojang.minecraft.level.tile.Tile;
import java.util.Random;

public class LevelGen {
   private int width;
   private int height;
   private int depth;
   private Random random = new Random();

   public LevelGen(int width, int height, int depth) {
      this.width = width;
      this.height = height;
      this.depth = depth;
   }

   public byte[] generateMap() {
      int w = this.width;
      int h = this.height;
      int d = this.depth;
      int[] heightmap1 = (new NoiseMap(0)).read(w, h);
      int[] heightmap2 = (new NoiseMap(0)).read(w, h);
      int[] cf = (new NoiseMap(1)).read(w, h);
      int[] rockMap = (new NoiseMap(1)).read(w, h);
      byte[] blocks = new byte[this.width * this.height * this.depth];

      int x;
      int y;
      int length;
      for(x = 0; x < w; ++x) {
         for(y = 0; y < d; ++y) {
            for(int z = 0; z < h; ++z) {
               int dh1 = heightmap1[x + z * this.width];
               int dh2 = heightmap2[x + z * this.width];
               length = cf[x + z * this.width];
               if (length < 128) {
                  dh2 = dh1;
               }

               int dh = dh1;
               if (dh2 > dh1) {
                  dh = dh2;
               }

               dh = dh / 8 + d / 3;
               int rh = rockMap[x + z * this.width] / 8 + d / 3;
               if (rh > dh - 2) {
                  rh = dh - 2;
               }

               int i = (y * this.height + z) * this.width + x;
               int id = 0;
               if (y == dh) {
                  id = Tile.grass.id;
               }

               if (y < dh) {
                  id = Tile.dirt.id;
               }

               if (y <= rh) {
                  id = Tile.rock.id;
               }

               blocks[i] = (byte)id;
            }
         }
      }

      x = w * h * d / 256 / 64;

      for(y = 0; y < x; ++y) {
         float x = this.random.nextFloat() * (float)w;
         float y = this.random.nextFloat() * (float)d;
         float z = this.random.nextFloat() * (float)h;
         length = (int)(this.random.nextFloat() + this.random.nextFloat() * 150.0F);
         float dir1 = (float)((double)this.random.nextFloat() * 3.141592653589793D * 2.0D);
         float dira1 = 0.0F;
         float dir2 = (float)((double)this.random.nextFloat() * 3.141592653589793D * 2.0D);
         float dira2 = 0.0F;

         for(int l = 0; l < length; ++l) {
            x = (float)((double)x + Math.sin((double)dir1) * Math.cos((double)dir2));
            z = (float)((double)z + Math.cos((double)dir1) * Math.cos((double)dir2));
            y = (float)((double)y + Math.sin((double)dir2));
            dir1 += dira1 * 0.2F;
            dira1 *= 0.9F;
            dira1 += this.random.nextFloat() - this.random.nextFloat();
            dir2 += dira2 * 0.5F;
            dir2 *= 0.5F;
            dira2 *= 0.9F;
            dira2 += this.random.nextFloat() - this.random.nextFloat();
            float size = (float)(Math.sin((double)l * 3.141592653589793D / (double)length) * 2.5D + 1.0D);

            for(int xx = (int)(x - size); xx <= (int)(x + size); ++xx) {
               for(int yy = (int)(y - size); yy <= (int)(y + size); ++yy) {
                  for(int zz = (int)(z - size); zz <= (int)(z + size); ++zz) {
                     float xd = (float)xx - x;
                     float yd = (float)yy - y;
                     float zd = (float)zz - z;
                     float dd = xd * xd + yd * yd * 2.0F + zd * zd;
                     if (dd < size * size && xx >= 1 && yy >= 1 && zz >= 1 && xx < this.width - 1 && yy < this.depth - 1 && zz < this.height - 1) {
                        int ii = (yy * this.height + zz) * this.width + xx;
                        if (blocks[ii] == Tile.rock.id) {
                           blocks[ii] = 0;
                        }
                     }
                  }
               }
            }
         }
      }

      return blocks;
   }
}
