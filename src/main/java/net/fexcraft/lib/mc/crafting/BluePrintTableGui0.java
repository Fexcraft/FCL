package net.fexcraft.lib.mc.crafting;

import java.util.List;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.mc.gui.GenericContainer;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BluePrintTableGui0 extends GenericGui<GenericContainer.DefImpl> {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/bpt_0.png");
	private static int scroll;

	public BluePrintTableGui0(EntityPlayer player, World world, int x, int y, int z){
		super(texture, new GenericContainer.DefImpl(player), player);
		this.xSize = 256; this.ySize = 204;
		if(BluePrintTableGui1.category >= 0){
			this.openGui("fcl", 1, new int[]{ BluePrintTableGui1.category, 0, 0 });
		}
	}

	@Override
	protected void init(){
		this.buttons.put("arrow_up", new BasicButton("arrow_up", guiLeft + 241, guiTop + 5, 241, 5, 10, 14, true));
		this.buttons.put("arrow_down", new BasicButton("arrow_down", guiLeft + 241, guiTop + 185, 241, 185, 10, 14, true));
		//
		for(int i = 0; i < 7; i++){
			int j = i * 28;
			this.buttons.put("category_" + i, new BasicButton("category_" + i, guiLeft + 4, guiTop + 4 + j, 4, 4 + j, 219, 28, true));
			this.buttons.put("cat_" + i, new BasicButton("cat_" + i, guiLeft + 224, guiTop + 6 + j, 224, 6 + j, 15, 24, true));
			buttons.get("cat_" + i).rgb_hover = new RGB(180, 120, 20);
			//
			this.texts.put("cat0_" + i, new BasicText(guiLeft + 29, guiTop + 7 + j, 191, MapColor.GRAY.colorValue, ""));
			this.texts.put("cat1_" + i, new BasicText(guiLeft + 29, guiTop + 21 + j, 191, MapColor.GRAY.colorValue, ""));
		}
	}

	@Override
	protected void predraw(float pticks, int mouseX, int mouseY){
		for(int i = 0; i < 7; i++){
			int j = i + scroll;
			if(j < 0 || j >= RecipeRegistry.getCategories().size()){
				texts.get("cat0_" + i).string = ""; texts.get("cat1_" + i).string = "";
				buttons.get("category_" + i).enabled = false; buttons.get("cat_" + i).enabled = false;
			}
			else{
				texts.get("cat0_" + i).string = I18n.format(RecipeRegistry.getCategory(j));
				int k = RecipeRegistry.getRecipes(j).size(), l = 0;
				for(List<BluePrintRecipe> obj : RecipeRegistry.getRecipes(j).values()) l += obj.size();
				texts.get("cat1_" + i).string = I18n.format("gui.fcl.blueprinttable0.catdesc", k, l);
				buttons.get("category_" + i).enabled = true; buttons.get("cat_" + i).enabled = true;
			}
		}
		buttons.get("arrow_up").enabled = scroll > 0;
		buttons.get("arrow_down").enabled = scroll < RecipeRegistry.getCategories().size();
	}

	@Override
	protected void drawbackground(float pticks, int mouseX, int mouseY){
		//
	}

	@Override
	protected boolean buttonClicked(int mouseX, int mouseY, int mouseButton, String key, BasicButton button){
		switch(key){
			case "arrow_up":{ scroll = (scroll -= 1) < 0 ? 0 : scroll; break; }
			case "arrow_down":{
				scroll = (scroll += 1) >= RecipeRegistry.getCategories().size() ? RecipeRegistry.getCategories().size() : scroll;
				break;
			}
		}
		if(key.startsWith("category_") || key.startsWith("cat_")){
			int i = Integer.parseInt(key.replace("category_", "").replace("cat_", ""));
			this.openGui("fcl", 1, new int[]{ i + scroll, 0, 0 });
		}
		return false;
	}

	@Override
	protected void scrollwheel(int am, int x, int y){
		if(am < 0){
			scroll = (scroll -= 1) < 0 ? 0 : scroll;
		}
		else{
			scroll = (scroll += 1) >= RecipeRegistry.getCategories().size() ? RecipeRegistry.getCategories().size() : scroll;
		}
	}
	
}