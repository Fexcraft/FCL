package net.fexcraft.lib.mc.crafting;

import java.io.IOException;

import net.fexcraft.lib.mc.gui.GenericGui;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class BluePrintTableGui2 extends GenericGui<BluePrintTableContainer2> {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/bpt_2.png");
	private static int scroll, amount = 1;
	public static int item = -1, recipe = -1, recipes = -2;

	public BluePrintTableGui2(EntityPlayer player, World world, int x, int y, int z){
		super(texture, new BluePrintTableContainer2(player, world, x, y, z), player);
		this.xSize = 226; this.ySize = 207; item = y; recipe = z;
		recipes = RecipeRegistry.getRecipes(BluePrintTableGui1.category, item).size();
	}

	@Override
	protected void init(){
		this.texts.put("title", new BasicText(guiLeft + 46, guiTop + 7, 156, MapColor.SNOW.colorValue, container.recipe.output.getDisplayName()));
		this.texts.put("status", new BasicText(guiLeft + 46, guiTop + 21, 156, MapColor.SNOW.colorValue, I18n.format("gui.fcl.blueprinttable2.recstatus", recipe + 1, recipes), true, null){
			@Override
			public boolean scrollwheel(int am, int x, int y){
				if(am > 0 && buttons.get("next").enabled){
					openGui(2, new int[]{ BluePrintTableGui1.category, item, recipe + 1 }, null);
					return true;
				}
				else if(am < 0 && buttons.get("prev").enabled){
					openGui(2, new int[]{ BluePrintTableGui1.category, item, recipe - 1 }, null);
					return true;
				}
				else return false;
			}
		});
		this.buttons.put("ingr_up", new BasicButton("ingr_up", guiLeft + 172, guiTop + 124, 172, 124, 23, 9, true));
		this.buttons.put("ingr_down", new BasicButton("ingr_down", guiLeft + 198, guiTop + 124, 198, 124, 23, 9, true));
		this.texts.put("amount", new BasicText(guiLeft + 174, guiTop + 172, 45, MapColor.SNOW.colorValue, ""));
		this.texts.put("amount_dest", new BasicText(guiLeft + 174, guiTop + 159, 45, MapColor.SNOW.colorValue, "Amount:"));
		this.buttons.put("craft", new BasicButton("craft", guiLeft + 198, guiTop + 184, 198, 184, 23, 18, container.craftable(player)));
		this.buttons.put("less", new BasicButton("less", guiLeft + 172, guiTop + 184, 172, 184, 11, 18, true));
		this.buttons.put("more", new BasicButton("more", guiLeft + 185, guiTop + 184, 185, 184, 11, 18, true));
		this.buttons.put("prev", new BasicButton("prev", guiLeft + 27, guiTop + 19, 27, 19, 15, 12, false));
		this.buttons.put("next", new BasicButton("next", guiLeft + 206, guiTop + 19, 206, 19, 15, 12, false));
	}

	@Override
	protected void predraw(float pticks, int mouseX, int mouseY){
		if(item == -1 || recipe == -1) return;
		buttons.get("ingr_up").enabled = scroll > 0; buttons.get("ingr_down").enabled = scroll < container.recipe.components.length / 60;
		texts.get("amount").string = amount + ""; buttons.get("more").enabled = amount < 64; buttons.get("less").enabled = amount > 1;
		buttons.get("next").enabled = RecipeRegistry.getRecipes(BluePrintTableGui1.category, item).size() - 1 > recipe;
		buttons.get("prev").enabled = recipe > 0;
	}

	@Override
	protected void drawbackground(float pticks, int mouseX, int mouseY){
		//
	}

	@Override
	protected boolean buttonClicked(int mouseX, int mouseY, int mouseButton, String key, BasicButton button){
		switch(key){
			case "ingr_up":{ scroll = (scroll -= 1) < 0 ? 0 : scroll; container.refresh(scroll); break; }
			case "ingr_down":{
				int l = container.recipe.components.length / 60; scroll = (scroll += 1) >= l ? l : scroll;
				container.refresh(scroll); break;
			}
			case "less":{ amount--; break; }
			case "more":{ amount++; break; }
			case "craft":{ craft(); break; }
			case "prev":{ openGui(2, new int[]{ BluePrintTableGui1.category, item, recipe - 1 }, null); break; }
			case "next":{ openGui(2, new int[]{ BluePrintTableGui1.category, item, recipe + 1 }, null); break; }
		}
		return false;
	}

	private void craft(){
		NBTTagCompound compound = new NBTTagCompound();
		compound.setBoolean("craft", true);
		compound.setInteger("amount", amount);
		this.container.send(Side.SERVER, compound);
	}

	@Override
	protected void scrollwheel(int am, int x, int y){
		if(am < 0){ scroll = (scroll -= 1) < 0 ? 0 : scroll; }
		else{ int l = container.recipe.components.length / 60; scroll = (scroll += 1) >= l ? l : scroll; }
		container.refresh(scroll); 
	}
	
	@Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
		if(this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)){
			mc.player.closeScreen();
			return;
		}
        if(keyCode == 1){
        	item = -1;
        	recipe = -1;
        	openGui(1, new int[]{ BluePrintTableGui1.category, 0, 0 }, null);
        	return;
        }
        super.keyTyped(typedChar, keyCode);
    }
	
}