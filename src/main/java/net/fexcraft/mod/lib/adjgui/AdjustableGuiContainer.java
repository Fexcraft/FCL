package net.fexcraft.mod.lib.adjgui;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.google.gson.JsonObject;

import net.fexcraft.mod.lib.util.render.RGB;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class AdjustableGuiContainer extends GuiContainer {
	
	public static final ResourceLocation TEXTURE = new ResourceLocation("fcl:textures/gui/adj_gui.png");
    protected static SynchronizedContainer container;
	protected RGB border_color;

	public AdjustableGuiContainer(Class<? extends SynchronizedContainer> clazz, JsonObject obj, RGB color, boolean permcheck, EntityPlayer player, int x, int y) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
		super(container = clazz.getConstructor(boolean.class, JsonObject.class, EntityPlayer.class).newInstance(permcheck, obj, player));
		this.xSize = x > 248 ? 248 : x; this.ySize = y > 248 ? 248 : y;
		this.border_color = color;
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float pt, int mouseX, int mouseY){
		this.drawDefaultBackground();
        this.mc.getTextureManager().bindTexture(TEXTURE);
        border_color.glColorApply();
        this.drawTexturedModalRect(guiLeft - 4,     guiTop - 4,       0,   0,     4,     4);
        this.drawTexturedModalRect(guiLeft,         guiTop - 4,       4,   0, xSize,     4);
        this.drawTexturedModalRect(guiLeft + xSize, guiTop - 4,     252,   0,     4,     4);
        this.drawTexturedModalRect(guiLeft + xSize, guiTop    ,     252,   4,     4, ySize);
        this.drawTexturedModalRect(guiLeft + xSize, guiTop + ySize, 252, 252,     4,     4);
        this.drawTexturedModalRect(guiLeft,         guiTop + ySize,   4, 252, xSize,     4);
        this.drawTexturedModalRect(guiLeft - 4,     guiTop + ySize,   0, 252,     4,     4);
        this.drawTexturedModalRect(guiLeft - 4,     guiTop,           0,   4,     4, ySize);
        RGB.glColorReset();
        //
        this.drawTexturedModalRect(guiLeft, guiTop, 4, 4, xSize, ySize);
        //
        container.elements.values().forEach(elm -> elm.draw(pt, mouseX, mouseY));
        this.drawGuiContainer(pt, mouseX, mouseY);
	}
	
	/* To be overriden by subclasses. */
	protected void drawGuiContainer(float pt, int mouseX, int mouseY){
		//
	}
	
	@Override
	public void initGui(){
		super.initGui(); this.buttonList.clear();
		container.elements.values().forEach(elm -> elm.onGuiInit(this.buttonList, guiLeft, guiTop));
	}
	
	@Override
	public void actionPerformed(GuiButton button) throws IOException {
		for(CustomGuiElement elm : container.elements.values()){
			if(elm.onActionPerformed(container, button)){
				return;
			}
		}
	}

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(container.isKeyTyped(typedChar, keyCode) && !(this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode))){
            super.keyTyped(typedChar, keyCode);
        }
        if(keyCode == 1){
            mc.player.closeScreen();
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException{
        super.mouseClicked(mouseX, mouseY, mouseButton);
        container.mouseClicked(mouseX, mouseY, mouseButton);
    }
	
}