package net.fexcraft.lib.mc.crafting;

import java.io.IOException;
import java.util.List;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.mc.gui.GenericContainer;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.minecraft.block.material.MapColor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BluePrintTableGui1 extends GenericGui<GenericContainer.DefImpl> {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/bpt_1.png");
	private static int scroll;
	public static int category = -1;

	public BluePrintTableGui1(EntityPlayer player, World world, int x, int y, int z){
		super(texture, new GenericContainer.DefImpl(), player);
		this.xSize = 256; this.ySize = 204; category = x;
		if(BluePrintTableGui2.item >= 0 && BluePrintTableGui2.recipe >= 0){
			this.openGui("fcl", 2, new int[]{ category, BluePrintTableGui2.item, BluePrintTableGui2.recipe });
		}
	}

	@Override
	protected void init(){
		this.buttons.put("arrow_up", new BasicButton("arrow_up", guiLeft + 241, guiTop + 5, 241, 5, 10, 14, true));
		this.buttons.put("arrow_down", new BasicButton("arrow_down", guiLeft + 241, guiTop + 185, 241, 185, 10, 14, true));
		//
		for(int i = 0; i < 14; i++){
			int j = i * 14;
			this.buttons.put("item_" + i, new BasicButton("item_" + i, guiLeft + 4, guiTop + 4 + j, 4, 4 + j, 219, 14, true));
			this.buttons.put("i_" + i, new BasicButton("i_" + i, guiLeft + 224, guiTop + 5 + j, 224, 5 + j, 15, 12, true));
			buttons.get("i_" + i).rgb_hover = new RGB(180, 120, 20);
			//
			this.texts.put("rec_" + i, new BasicText(guiLeft + 21, guiTop + 7 + j, 199, MapColor.GRAY.colorValue, ""));
		}
	}

	@Override
	protected void predraw(float pticks, int mouseX, int mouseY){
		if(category == -1) return;
		for(int i = 0; i < 14; i++){
			int j = i + scroll;
			if(j < 0 || j >= RecipeRegistry.getRecipes(category).size()){
				texts.get("rec_" + i).string = "";
				buttons.get("item_" + i).enabled = false; buttons.get("i_" + i).enabled = false;
			}
			else{
				List<BluePrintRecipe> str = RecipeRegistry.getRecipes(category, j);
				texts.get("rec_" + i).string = str.get(0).output.getDisplayName() + String.format(" [%s]", str.size());
				buttons.get("item_" + i).enabled = true; buttons.get("i_" + i).enabled = true;
			}
		}
		buttons.get("arrow_up").enabled = scroll > 0;
		buttons.get("arrow_down").enabled = scroll < RecipeRegistry.getRecipes(category).size();
	}

	@Override
	protected void drawbackground(float pticks, int mouseX, int mouseY){
		//
	}

	@Override
	protected void buttonClicked(int mouseX, int mouseY, int mouseButton, String key, BasicButton button){
		switch(key){
			case "arrow_up":{ scroll = (scroll -= 1) < 0 ? 0 : scroll; break; }
			case "arrow_down":{
				scroll = (scroll += 1) >= RecipeRegistry.getRecipes(category).size() ? RecipeRegistry.getRecipes(category).size() : scroll;
				break;
			}
		}
		if(key.startsWith("item_") || key.startsWith("i_")){
			int i = Integer.parseInt(key.replace("item_", "").replace("i_", ""));
			this.openGui("fcl", 2, new int[]{ category, i + scroll, 0 });
		}
	}

	@Override
	protected void scrollwheel(int am, int x, int y){
		if(am < 0){
			scroll = (scroll -= 1) < 0 ? 0 : scroll;
		}
		else{
			scroll = (scroll += 1) >= RecipeRegistry.getRecipes(category).size() ? RecipeRegistry.getRecipes(category).size() : scroll;
		}
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(keyCode == 1){ category = -1; this.openGui("fcl", 0, new int[]{ 0, 0, 0 }); return; }
        super.keyTyped(typedChar, keyCode);
    }
	
}