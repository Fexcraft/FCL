package net.fexcraft.mod.lib.fmr;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import net.fexcraft.mod.lib.tmt.ModelRendererTurbo;
import net.minecraft.client.renderer.GLAllocation;

/**
 * Although this in combination with PolygonShape may seem similar to a "ModelRendererTurbo" object, the aim in<br>
 * general usage is slightly different. MRT objects usually get stored stored in an array which then does get<br>
 * looped through and rendered, the aim of this class though is to store all data multiple MRT's would in one<br>
 * object which does require only one call.<br>
 * Theoretically anyway.
 * <hr>
 * @author Ferdinand Calo' (FEX___96)
 */
public class ModelCompound {
	
	public int textureSizeX, textureSizeY;
	public float rotAngleX = 0, rotAngleY = 0, rotAngleZ = 0, rotPointX = 0, rotPointY = 0, rotPointZ = 0;
    public PolygonShape[] polygons;
	public boolean visible = true;
	private Integer dislist;
	
	/** Generic Empty **/
	public ModelCompound(){ reset(); }
	
	/** Importer **/
	public ModelCompound(ModelRendererTurbo[] array){
		reset(); insertAll(array);
	}

	public void render(){ render(FexcraftModelRenderer.MODELSCALE); }
    
    public void render(float scale){
        if(!visible){ return; }
        if(dislist == null){
            compileDisplayList(scale);
        }
        if(rotAngleX != 0.0F || rotAngleY != 0.0F || rotAngleZ != 0.0F){
            GL11.glPushMatrix();
            GL11.glTranslatef(rotPointX * scale, rotPointY * scale, rotPointZ * scale);
            if(rotAngleY != 0.0F){
                GL11.glRotatef(rotAngleY * 57.29578F, 0.0F, 1.0F, 0.0F);
            }
            if(rotAngleZ != 0.0F){
                GL11.glRotatef(rotAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
            }
            if(rotAngleX != 0.0F){
                GL11.glRotatef(rotAngleX * 57.29578F, 1.0F, 0.0F, 0.0F);
            }
    		GL11.glCallList(dislist);
            GL11.glPopMatrix();
        }
        else if(rotPointX != 0.0F || rotPointY != 0.0F || rotPointZ != 0.0F){
            GL11.glTranslatef(rotPointX * scale, rotPointY * scale, rotPointZ * scale);
    		GL11.glCallList(dislist);
            GL11.glTranslatef(-rotPointX * scale, -rotPointY * scale, -rotPointZ * scale);
        }
        else{
    		GL11.glCallList(dislist);
        }
    }

    //TODO find error with rotations and fix
	private void compileDisplayList(float scale) {
        dislist = GLAllocation.generateDisplayLists(1);
        GL11.glNewList(dislist, 4864 /*GL_COMPILE*/);
        for(int i = 0; i < polygons.length; i++){
            if(polygons[i].rotateAngleX != 0.0F || polygons[i].rotateAngleY != 0.0F || polygons[i].rotateAngleZ != 0.0F){
                GL11.glPushMatrix();
                GL11.glTranslatef(polygons[i].rotationPointX * scale, polygons[i].rotationPointY * scale, polygons[i].rotationPointY * scale);
                if(polygons[i].rotateAngleX != 0.0F){
                    GL11.glRotatef(polygons[i].rotateAngleX * 57.29578F, 0.0F, 1.0F, 0.0F);
                }
                if(polygons[i].rotateAngleZ != 0.0F){
                    GL11.glRotatef(polygons[i].rotateAngleZ * 57.29578F, 0.0F, 0.0F, 1.0F);
                }
                if(polygons[i].rotateAngleY != 0.0F){
                    GL11.glRotatef(polygons[i].rotateAngleY * 57.29578F, 1.0F, 0.0F, 0.0F);
                }
                for(int j = 0; j < polygons[i].faces.length; j++){
                    polygons[i].faces[j].draw(Tessellator.INSTANCE, scale);
                }
                GL11.glPopMatrix();
            }
            else if(polygons[i].rotationPointX != 0.0F || polygons[i].rotationPointY != 0.0F || polygons[i].rotationPointZ != 0.0F){
                GL11.glTranslatef(polygons[i].rotationPointX * scale, polygons[i].rotationPointY * scale, polygons[i].rotationPointZ * scale);
                for(int j = 0; j < polygons[i].faces.length; j++){
                    polygons[i].faces[j].draw(Tessellator.INSTANCE, scale);
                }
                GL11.glTranslatef(-polygons[i].rotationPointX * scale, -polygons[i].rotationPointY * scale, -polygons[i].rotationPointZ * scale);
            }
            else{
                for(int j = 0; j < polygons[i].faces.length; j++){
                    polygons[i].faces[j].draw(Tessellator.INSTANCE, scale);
                }
            }
        }
        GL11.glEndList();
	}
	
    public void reset(){ polygons = new PolygonShape[]{}; }
    
    private void insert(ModelRendererTurbo turbo){
    	polygons = Arrays.copyOf(polygons, polygons.length + 1);
    	PolygonShape shape = new PolygonShape(turbo.flip, turbo.mirror);
    	shape.faces = turbo.getFaces(); shape.vertices = turbo.getVertices();
        shape.rotationPointX = turbo.rotationPointX; shape.rotationPointY = turbo.rotationPointY; shape.rotationPointZ = turbo.rotationPointZ;
        shape.rotateAngleX = turbo.rotateAngleX;  shape.rotateAngleY = turbo.rotateAngleY; shape.rotateAngleZ = turbo.rotateAngleZ;
        polygons[polygons.length - 1] = shape;
	}
	
    private void insertAll(ModelRendererTurbo[] array){
    	for(ModelRendererTurbo turbo : array){ insert(turbo); }
	}

}
