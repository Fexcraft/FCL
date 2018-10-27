package net.fexcraft.lib.mc.crafting;

import java.io.IOException;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BluePrintTableGui2 extends GenericGui<BluePrintTableContainer2> {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/bpt_2.png");
	private static int scroll;
	public static int item = -1, recipe = -1;

	public BluePrintTableGui2(EntityPlayer player, World world, int x, int y, int z){
		super(texture, new BluePrintTableContainer2(player, world, x, y, z), player);
		this.xSize = 226; this.ySize = 207; item = x;
	}

	@Override
	protected void init(){
		//
	}

	@Override
	protected void predraw(float pticks, int mouseX, int mouseY){
		if(item == -1 || recipe == -1) return;
		//
	}

	@Override
	protected void drawbackground(float pticks, int mouseX, int mouseY){
		//
	}

	@Override
	protected void buttonClicked(int mouseX, int mouseY, int mouseButton, String key, BasicButton button){
		//
	}

	@Override
	protected void scrollwheel(int am, int x, int y){
		//
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(keyCode == 1){ item = -1; recipe = -1; this.openGui("fcl", 1, new int[]{ BluePrintTableGui1.category, 0, 0 }); return; }
        super.keyTyped(typedChar, keyCode);
    }
	
}