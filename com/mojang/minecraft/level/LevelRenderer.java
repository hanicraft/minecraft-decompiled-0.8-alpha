package com.mojang.minecraft.level;

import com.mojang.minecraft.HitResult;
import com.mojang.minecraft.Player;
import com.mojang.minecraft.level.tile.Tile;
import com.mojang.minecraft.phys.AABB;
import com.mojang.minecraft.renderer.Frustum;
import com.mojang.minecraft.renderer.Tesselator;
import com.mojang.minecraft.renderer.Textures;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.lwjgl.opengl.GL11;

public class LevelRenderer implements LevelListener {
   public static final int MAX_REBUILDS_PER_FRAME = 8;
   public static final int CHUNK_SIZE = 16;
   private Level level;
   private Chunk[] chunks;
   private int xChunks;
   private int yChunks;
   private int zChunks;
   private Textures textures;

   public LevelRenderer(Level level, Textures textures) {
      this.level = level;
      this.textures = textures;
      level.addListener(this);
      this.xChunks = level.width / 16;
      this.yChunks = level.depth / 16;
      this.zChunks = level.height / 16;
      this.chunks = new Chunk[this.xChunks * this.yChunks * this.zChunks];

      for(int x = 0; x < this.xChunks; ++x) {
         for(int y = 0; y < this.yChunks; ++y) {
            for(int z = 0; z < this.zChunks; ++z) {
               int x0 = x * 16;
               int y0 = y * 16;
               int z0 = z * 16;
               int x1 = (x + 1) * 16;
               int y1 = (y + 1) * 16;
               int z1 = (z + 1) * 16;
               if (x1 > level.width) {
                  x1 = level.width;
               }

               if (y1 > level.depth) {
                  y1 = level.depth;
               }

               if (z1 > level.height) {
                  z1 = level.height;
               }

               this.chunks[(x + y * this.xChunks) * this.zChunks + z] = new Chunk(level, x0, y0, z0, x1, y1, z1);
            }
         }
      }

   }

   public List getAllDirtyChunks() {
      ArrayList dirty = null;

      for(int i = 0; i < this.chunks.length; ++i) {
         Chunk chunk = this.chunks[i];
         if (chunk.isDirty()) {
            if (dirty == null) {
               dirty = new ArrayList();
            }

            dirty.add(chunk);
         }
      }

      return dirty;
   }

   public void render(Player player, int layer) {
      GL11.glEnable(3553);
      int id = this.textures.loadTexture("/terrain.png", 9728);
      GL11.glBindTexture(3553, id);
      Frustum frustum = Frustum.getFrustum();

      for(int i = 0; i < this.chunks.length; ++i) {
         if (frustum.isVisible(this.chunks[i].aabb)) {
            this.chunks[i].render(layer);
         }
      }

      GL11.glDisable(3553);
   }

   public void updateDirtyChunks(Player player) {
      List dirty = this.getAllDirtyChunks();
      if (dirty != null) {
         Collections.sort(dirty, new DirtyChunkSorter(player, Frustum.getFrustum()));

         for(int i = 0; i < 8 && i < dirty.size(); ++i) {
            ((Chunk)dirty.get(i)).rebuild();
         }

      }
   }

   public void pick(Player player, Frustum frustum) {
      Tesselator t = Tesselator.instance;
      float r = 3.0F;
      AABB box = player.bb.grow(r, r, r);
      int x0 = (int)box.x0;
      int x1 = (int)(box.x1 + 1.0F);
      int y0 = (int)box.y0;
      int y1 = (int)(box.y1 + 1.0F);
      int z0 = (int)box.z0;
      int z1 = (int)(box.z1 + 1.0F);
      GL11.glInitNames();
      GL11.glPushName(0);
      GL11.glPushName(0);

      for(int x = x0; x < x1; ++x) {
         GL11.glLoadName(x);
         GL11.glPushName(0);

         for(int y = y0; y < y1; ++y) {
            GL11.glLoadName(y);
            GL11.glPushName(0);

            for(int z = z0; z < z1; ++z) {
               Tile tile = Tile.tiles[this.level.getTile(x, y, z)];
               if (tile != null && frustum.isVisible(tile.getTileAABB(x, y, z))) {
                  GL11.glLoadName(z);
                  GL11.glPushName(0);

                  for(int i = 0; i < 6; ++i) {
                     GL11.glLoadName(i);
                     t.init();
                     tile.renderFaceNoTexture(t, x, y, z, i);
                     t.flush();
                  }

                  GL11.glPopName();
               }
            }

            GL11.glPopName();
         }

         GL11.glPopName();
      }

      GL11.glPopName();
      GL11.glPopName();
   }

   public void renderHit(HitResult h, int mode, int tileType) {
      Tesselator t = Tesselator.instance;
      GL11.glEnable(3042);
      GL11.glBlendFunc(770, 1);
      GL11.glColor4f(1.0F, 1.0F, 1.0F, ((float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.4F) * 0.5F);
      if (mode == 0) {
         t.init();

         for(int i = 0; i < 6; ++i) {
            Tile.rock.renderFaceNoTexture(t, h.x, h.y, h.z, i);
         }

         t.flush();
      } else {
         GL11.glBlendFunc(770, 771);
         float br = (float)Math.sin((double)System.currentTimeMillis() / 100.0D) * 0.2F + 0.8F;
         GL11.glColor4f(br, br, br, (float)Math.sin((double)System.currentTimeMillis() / 200.0D) * 0.2F + 0.5F);
         GL11.glEnable(3553);
         int id = this.textures.loadTexture("/terrain.png", 9728);
         GL11.glBindTexture(3553, id);
         int x = h.x;
         int y = h.y;
         int z = h.z;
         if (h.f == 0) {
            --y;
         }

         if (h.f == 1) {
            ++y;
         }

         if (h.f == 2) {
            --z;
         }

         if (h.f == 3) {
            ++z;
         }

         if (h.f == 4) {
            --x;
         }

         if (h.f == 5) {
            ++x;
         }

         t.init();
         t.noColor();
         Tile.tiles[tileType].render(t, this.level, 0, x, y, z);
         Tile.tiles[tileType].render(t, this.level, 1, x, y, z);
         t.flush();
         GL11.glDisable(3553);
      }

      GL11.glDisable(3042);
   }

   public void setDirty(int x0, int y0, int z0, int x1, int y1, int z1) {
      x0 /= 16;
      x1 /= 16;
      y0 /= 16;
      y1 /= 16;
      z0 /= 16;
      z1 /= 16;
      if (x0 < 0) {
         x0 = 0;
      }

      if (y0 < 0) {
         y0 = 0;
      }

      if (z0 < 0) {
         z0 = 0;
      }

      if (x1 >= this.xChunks) {
         x1 = this.xChunks - 1;
      }

      if (y1 >= this.yChunks) {
         y1 = this.yChunks - 1;
      }

      if (z1 >= this.zChunks) {
         z1 = this.zChunks - 1;
      }

      for(int x = x0; x <= x1; ++x) {
         for(int y = y0; y <= y1; ++y) {
            for(int z = z0; z <= z1; ++z) {
               this.chunks[(x + y * this.xChunks) * this.zChunks + z].setDirty();
            }
         }
      }

   }

   public void tileChanged(int x, int y, int z) {
      this.setDirty(x - 1, y - 1, z - 1, x + 1, y + 1, z + 1);
   }

   public void lightColumnChanged(int x, int z, int y0, int y1) {
      this.setDirty(x - 1, y0 - 1, z - 1, x + 1, y1 + 1, z + 1);
   }

   public void allChanged() {
      this.setDirty(0, 0, 0, this.level.width, this.level.depth, this.level.height);
   }
}
