package net.fexcraft.mod.lib.util.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

public class GenericGuiButton extends GuiButton {
	
	private int[] xpos = new int[4], ypos = new int[4], textpos = new int[2];
	private ResourceLocation texture;

	public GenericGuiButton(int id, int x, int y, int width, int height, String text){
		super(id, x, y, width, height, text == null ? "" : text);
	}
	
	public final void setTexture(ResourceLocation rs){
		this.texture = rs;
	}
	
	/** 
	 * 0 -  enabled           <br>
	 * 1 -  enabled - hovered <br>
	 * 2 - disabled           <br>
	 * 3 - disabled - hovered <br>
	 */
	public final void setTexturePos(int i, int x, int y){
		xpos[i] = x; ypos[i] = y;
	}
	
	public final void setTextPos(int x, int y){
		textpos[0] = x; textpos[1] = y;
	}
	
	@Override
	public void drawButton(Minecraft mc, int mouseX, int mouseY, float f){
		if(!this.visible){
			return;
		}
		if(texture != null){
			mc.getTextureManager().bindTexture(texture);
		}
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.hovered = mouseX >= this.x && mouseY >= this.y && mouseX < this.x + this.width && mouseY < this.y + this.height;
		GlStateManager.enableBlend();
		GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
		
		if(this.enabled){
			if(this.hovered){
				this.drawTexturedModalRect(this.x, this.y, xpos[1], ypos[1], this.width, this.height);
			}
			else{
				this.drawTexturedModalRect(this.x, this.y, xpos[0], ypos[0], this.width, this.height);
			}
		}
		else{
			if(this.hovered){
				this.drawTexturedModalRect(this.x, this.y, xpos[3], ypos[3], this.width, this.height);
			}
			else{
				this.drawTexturedModalRect(this.x, this.y, xpos[2], ypos[2], this.width, this.height);
			}
		}
		if(this.displayString.length() > 0){
			int j = 14737632;
			if(packedFGColour != 0){ j = packedFGColour; }
            else if(!this.enabled){ j = 10526880; }
            else if(this.hovered){ j = 16777120; }
			mc.fontRenderer.drawString(this.displayString, textpos[0], textpos[1], j);
			//this.drawString(mc.fontRenderer, this.displayString, textpos[0], textpos[1], j);
		}
	}
	
}