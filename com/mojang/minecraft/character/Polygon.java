package com.mojang.minecraft.character;

import org.lwjgl.opengl.GL11;

public class Polygon {
   public Vertex[] vertices;
   public int vertexCount;

   public Polygon(Vertex[] vertices) {
      this.vertexCount = 0;
      this.vertices = vertices;
      this.vertexCount = vertices.length;
   }

   public Polygon(Vertex[] vertices, int u0, int v0, int u1, int v1) {
      this(vertices);
      vertices[0] = vertices[0].remap((float)u1, (float)v0);
      vertices[1] = vertices[1].remap((float)u0, (float)v0);
      vertices[2] = vertices[2].remap((float)u0, (float)v1);
      vertices[3] = vertices[3].remap((float)u1, (float)v1);
   }

   public void render() {
      GL11.glColor3f(1.0F, 1.0F, 1.0F);

      for(int i = 3; i >= 0; --i) {
         Vertex v = this.vertices[i];
         GL11.glTexCoord2f(v.u / 63.999F, v.v / 31.999F);
         GL11.glVertex3f(v.pos.x, v.pos.y, v.pos.z);
      }

   }
}
