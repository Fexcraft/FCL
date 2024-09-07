package net.fexcraft.lib.mc.crafting;

import java.util.ArrayList;
import java.util.List;

import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.gui.GenericContainer;
import net.fexcraft.lib.mc.gui.GenericGui;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class BluePrintTableGui0 extends GenericGui<GenericContainer.DefImpl> {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/bpt_0.png");
	private ArrayList<String> tooltip = new ArrayList<>();
	private static int scroll;

	public BluePrintTableGui0(EntityPlayer player, World world, int x, int y, int z){
		super(texture, new GenericContainer.DefImpl(player), player);
		xSize = 166;
		ySize = 172;
		if(BluePrintTableGui1.category >= 0){
			openGui(1, new int[]{ BluePrintTableGui1.category, 0, 0 }, null);
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
		for(int i = 0; i < 12; i++){
			int j = i * 12, k = i;
			this.buttons.put("cat_" + i, new BasicButton("cat_" + i, guiLeft + 7, guiTop + 19 + j, 7, 19 + j, 152, 10, true){
				@Override
				public boolean onclick(int mx, int my, int mb){
					player.openGui(FCL.getInstance(), 1, player.world, k + scroll, 0, 0);
					openGui(1, new int[]{ k + scroll, 0, 0 }, null);
					return true;
				}
			});
			this.texts.put("cat_" + i, new BasicText(guiLeft + 9, guiTop + 21 + j, 148, 0x858585, "").autoscale());
		}
	}

	@Override
	protected void predraw(float pticks, int mouseX, int mouseY){
		texts.get("title").string = I18n.format("gui.fcl.blueprinttable0.title", scroll);
		for(int i = 0; i < 12; i++){
			int j = i + scroll;
			if(j < 0 || j >= RecipeRegistry.getCategories().size()){
				texts.get("cat_" + i).string = "";
				buttons.get("cat_" + i).enabled = false;
			}
			else{
				texts.get("cat_" + i).string = I18n.format(RecipeRegistry.getCategory(j));
				buttons.get("cat_" + i).enabled = true;
			}
		}
		buttons.get("arrow_up").enabled = scroll > 0;
		buttons.get("arrow_down").enabled = scroll < RecipeRegistry.getCategories().size();
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
			scroll = (scroll += 1) >= RecipeRegistry.getCategories().size() ? RecipeRegistry.getCategories().size() : scroll;
		}
	}

	@Override
	protected void drawlast(float ticks, int mx, int my){
		tooltip.clear();
		for(int i = 0; i < 12; i++){
			int j = i + scroll;
			if(j < 0 || j >= RecipeRegistry.getCategories().size()) continue;
			if(buttons.get("cat_" + i).hovered){
				int k = RecipeRegistry.getRecipes(j).size(), l = 0;
				for(List<BluePrintRecipe> obj : RecipeRegistry.getRecipes(j).values()) l += obj.size();
				tooltip.add(I18n.format(RecipeRegistry.getCategory(j)));
				tooltip.add(I18n.format("gui.fcl.blueprinttable0.catdesc", k, l));
			}
		}
		drawHoveringText(tooltip, mx, my);
	}
	
}