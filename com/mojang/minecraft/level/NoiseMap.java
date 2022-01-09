package com.mojang.minecraft.level;

import java.util.Random;

public class NoiseMap {
   Random random = new Random();
   int seed;
   int levels;
   int fuzz;

   public NoiseMap(int levels) {
      this.seed = this.random.nextInt();
      this.levels = 0;
      this.fuzz = 16;
      this.levels = levels;
   }

   public int[] read(int width, int height) {
      Random random = new Random();
      int[] tmp = new int[width * height];
      int level = this.levels;
      int step = width >> level;

      int val;
      int ss;
      for(val = 0; val < height; val += step) {
         for(ss = 0; ss < width; ss += step) {
            tmp[ss + val * width] = (random.nextInt(256) - 128) * this.fuzz;
         }
      }

      for(step = width >> level; step > 1; step /= 2) {
         val = 256 * (step << level);
         ss = step / 2;

         int y;
         int x;
         int c;
         int r;
         int d;
         int mu;
         int ml;
         for(y = 0; y < height; y += step) {
            for(x = 0; x < width; x += step) {
               c = tmp[(x + 0) % width + (y + 0) % height * width];
               r = tmp[(x + step) % width + (y + 0) % height * width];
               d = tmp[(x + 0) % width + (y + step) % height * width];
               mu = tmp[(x + step) % width + (y + step) % height * width];
               ml = (c + d + r + mu) / 4 + random.nextInt(val * 2) - val;
               tmp[x + ss + (y + ss) * width] = ml;
            }
         }

         for(y = 0; y < height; y += step) {
            for(x = 0; x < width; x += step) {
               c = tmp[x + y * width];
               r = tmp[(x + step) % width + y * width];
               d = tmp[x + (y + step) % width * width];
               mu = tmp[(x + ss & width - 1) + (y + ss - step & height - 1) * width];
               ml = tmp[(x + ss - step & width - 1) + (y + ss & height - 1) * width];
               int m = tmp[(x + ss) % width + (y + ss) % height * width];
               int u = (c + r + m + mu) / 4 + random.nextInt(val * 2) - val;
               int l = (c + d + m + ml) / 4 + random.nextInt(val * 2) - val;
               tmp[x + ss + y * width] = u;
               tmp[x + (y + ss) * width] = l;
            }
         }
      }

      int[] result = new int[width * height];

      for(val = 0; val < height; ++val) {
         for(ss = 0; ss < width; ++ss) {
            result[ss + val * width] = tmp[ss % width + val % height * width] / 512 + 128;
         }
      }

      return result;
   }
}
