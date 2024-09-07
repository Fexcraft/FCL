package net.fexcraft.lib.mc.crafting;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import net.fexcraft.lib.mc.gui.GenericContainer;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BluePrintTableGui1 extends GenericGui<GenericContainer.DefImpl> {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/bpt_1.png");
	private ArrayList<String> tooltip = new ArrayList<>();
	private static int scroll;
	public static int category = -1;

	public BluePrintTableGui1(EntityPlayer player, World world, int x, int y, int z){
		super(texture, new GenericContainer.DefImpl(player), player);
		xSize = 166;
		ySize = 172;
		category = x;
		if(BluePrintTableGui2.item >= 0 && BluePrintTableGui2.recipe >= 0){
			openGui(2, new int[]{ category, BluePrintTableGui2.item, BluePrintTableGui2.recipe }, null);
		}
	}

	@Override
	protected void init(){
		this.texts.put("title", new BasicText(guiLeft + 9, guiTop + 9, 148, 0x858585, "").autoscale());
		this.buttons.put("arrow_up", new BasicButton("arrow_up", guiLeft + 163, guiTop + 14, 163, 14, 10, 10, true){
			@Override
			public boolean onclick(int mx, int my, int mb){
				scroll = (scroll -= 1) < 0 ? 0 : scroll;
				return true;
			}
		});
		this.buttons.put("arrow_down", new BasicButton("arrow_down", guiLeft + 163, guiTop + 26, 163, 26, 10, 10, true){
			@Override
			public boolean onclick(int mx, int my, int mb){
				scroll = (scroll += 1) >= RecipeRegistry.getCategories().size() ? RecipeRegistry.getCategories().size() : scroll;
				return true;
			}
		});
		//
		for(int i = 0; i < 12; i++){
			int j = i * 12, k = i;
			this.buttons.put("item_" + i, new BasicButton("item_" + i, guiLeft + 7, guiTop + 19 + j, 7, 19 + j, 152, 10, true){
				@Override
				public boolean onclick(int mx, int my, int mb){
					openGui(2, new int[]{ category, k + scroll, 0 }, null);
					return true;
				}
			});
			this.texts.put("rec_" + i, new BasicText(guiLeft + 9, guiTop + 21 + j, 148, 0x858585, "").autoscale());
		}
	}

	@Override
	protected void predraw(float pticks, int mouseX, int mouseY){
		if(category == -1) return;
		texts.get("title").string = I18n.format(RecipeRegistry.getCategory(category));
		for(int i = 0; i < 12; i++){
			int j = i + scroll;
			if(j < 0 || j >= RecipeRegistry.getRecipes(category).size()){
				texts.get("rec_" + i).string = "";
				buttons.get("item_" + i).enabled = false;
			}
			else{
				List<BluePrintRecipe> str = RecipeRegistry.getRecipes(category, j);
				texts.get("rec_" + i).string = str.get(0).output.getDisplayName();
				buttons.get("item_" + i).enabled = true;
			}
		}
		buttons.get("arrow_up").enabled = scroll > 0;
		buttons.get("arrow_down").enabled = scroll < RecipeRegistry.getRecipes(category).size();
	}

	@Override
	protected void drawbackground(float pticks, int mouseX, int mouseY){
		drawTexturedModalRect(guiLeft + 166, guiTop + 7, 166, 7, 15, 36);
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
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)){
			mc.player.closeScreen();
			return;
		}
        if(keyCode == 1){
        	category = -1;
        	openGui(0, new int[]{ 0, 0, 0 }, null);
        	return;
        }
        super.keyTyped(typedChar, keyCode);
    }

	@Override
	protected void drawlast(float ticks, int mx, int my){
		tooltip.clear();
		if(category < 0) return;
		for(int i = 0; i < 12; i++){
			int j = i + scroll;
			if(j < 0 || j >= RecipeRegistry.getRecipes(category).size()) continue;
			if(buttons.get("item_" + i).hovered){
				List<BluePrintRecipe> str = RecipeRegistry.getRecipes(category, j);
				tooltip.add(str.get(0).output.getDisplayName());
				tooltip.add(I18n.format("gui.fcl.blueprinttable1.recipes", str.size()));
			}
		}
		drawHoveringText(tooltip, mx, my);
	}
	
}