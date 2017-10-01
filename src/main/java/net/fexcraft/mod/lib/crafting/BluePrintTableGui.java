package net.fexcraft.mod.lib.crafting;

import java.io.IOException;

import org.lwjgl.input.Mouse;

import net.fexcraft.mod.lib.FCL;
import net.fexcraft.mod.lib.api.network.IPacketListener;
import net.fexcraft.mod.lib.crafting.BluePrintRecipe;
import net.fexcraft.mod.lib.crafting.RecipeRegistry;
import net.fexcraft.mod.lib.network.PacketHandler;
import net.fexcraft.mod.lib.network.packet.PacketNBTTagCompound;
import net.fexcraft.mod.lib.util.common.Formatter;
import net.fexcraft.mod.lib.util.common.GenericGuiButton;
import net.fexcraft.mod.lib.util.common.Print;
import net.fexcraft.mod.lib.util.lang.ArrayList;
import net.minecraft.block.material.MapColor;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.ClickType;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class BluePrintTableGui {
	
	private static final ResourceLocation texture = new ResourceLocation("fcl:textures/gui/blueprinttable.png");
	
	public static class Client extends GuiContainer {
		
		//x cat;
		//y item;
		//z alt
		
		private EntityPlayer player;
		//private World world;
		private int x, y, z;
		//
		private GenericGuiButton cat_left, cat_right;
		private GenericGuiButton item_left, item_right;
		private GenericGuiButton recipe_left, recipe_right;
		private GenericGuiButton amount_up, amount_down;
		//private GenericGuiButton items_up, items_down;
		private GenericGuiButton craft;
		//
		private Server server;

		public Client(EntityPlayer player, World world, int x, int y, int z){
			super(new Server(player, world, x, y, z));
			server = (Server)this.inventorySlots;
			this.player = player;
			//this.world = world;
			this.x = x; this.y = y; this.z = z;
			//
			this.xSize = 256;
			this.ySize = 207;
		}
		
		@Override
		public void drawScreen(int mouseX, int mouseY, float partialTicks){
	        this.drawDefaultBackground();
	        super.drawScreen(mouseX, mouseY, partialTicks);
	        this.renderHoveredToolTip(mouseX, mouseY);
	    }

		@Override
		protected void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY){
			this.mc.getTextureManager().bindTexture(texture);
			int i = this.guiLeft, j = this.guiTop;
			this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);
			//
			this.fontRenderer.drawString(RecipeRegistry.getCategory(x), i + 46, j + 7, MapColor.SNOW.colorValue);
			this.fontRenderer.drawString(I18n.format(RecipeRegistry.getCategory(x, y) + ".name", new Object[0]), i + 46, j + 21, MapColor.SNOW.colorValue);
			this.fontRenderer.drawString("Recipes: " + RecipeRegistry.getRecipes(x, y).size(), i + 171, j + 127, MapColor.SNOW.colorValue);
			this.fontRenderer.drawString("Sel: " + (z + 1), i + 188, j + 140, MapColor.SNOW.colorValue);
			this.fontRenderer.drawString(server.inv.amount + "", i + 230, j + 37, MapColor.GRAY.colorValue);
		}
		
		@Override
		protected void actionPerformed(GuiButton button){
			switch(button.id){
				case 0:{
					//craft
					NBTTagCompound compound = new NBTTagCompound();
					compound.setString("target_listener", "fcl-bpt");
					compound.setString("task", "craft");
					compound.setIntArray("args", new int[]{x, y, z, server.inv.amount});
					Print.debug(compound);
					PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(compound));
					break;
				}
				case 1:{
					if(x - 1 >= 0){
						update(x - 1, 0, 0);
					}
					break;
				}
				case 2:{
					if(x + 1 < RecipeRegistry.getCategories().size()){
						update(x + 1, 0, 0);
					}
					break;
				}
				case 3:{
					if(y - 1 < 0){
						//
					}
					else{
						update(x, y - 1, 0);
					}
					break;
				}
				case 4:{
					if(y + 1 >= RecipeRegistry.getRecipes(x).size()){
						//
					}
					else{
						update(x, y + 1, 0);
					}
					break;
				}
				case 5:{
					if(z - 1 < 0){
						//
					}
					else{
						update(x, y, z - 1);
					}
					break;
				}
				case 6:{
					if(z + 1 >= RecipeRegistry.getRecipes(x, y).size()){
						//
					}
					else{
						update(x, y, z + 1);
					}
					break;
				}
				case 7:{
					if(server.inv.amount >= 64){
						//
					}
					else{
						server.inv.setField(0, server.inv.getField(0) + 1);
						amount_up.enabled = server.inv.getField(0) < 64;
						amount_down.enabled = server.inv.getField(0) > 1;
						craft.enabled = Server.canCraft(x, y, z, player);
					}
					break;
				}
				case 8:{
					if(server.inv.amount <= 1){
						//
					}
					else{
						server.inv.setField(0, server.inv.getField(0) - 1);
						amount_up.enabled = server.inv.getField(0) < 64;
						amount_down.enabled = server.inv.getField(0) > 1;
						craft.enabled = Server.canCraft(x, y, z, player);
					}
				}
			}
		}

		@Override
		public void initGui(){
			super.initGui();
			this.buttonList.clear();
			int i = this.guiLeft;
			int j = this.guiTop;
			//
			this.buttonList.add(craft = new GenericGuiButton(0, i + 228, j + 63, 23, 18, null));
			craft.setTexture(texture);
			craft.setTexturePos(0, 112, 207);
			craft.setTexturePos(1, 112, 225);
			craft.setTexturePos(2, 89, 207);
			craft.setTexturePos(3, 89, 225);
			craft.enabled = Server.canCraft(x, y, z, player);
			//
			this.buttonList.add(cat_left = new GenericGuiButton(1, i + 27, j + 5, 15, 12, null));
			this.buttonList.add(cat_right = new GenericGuiButton(2, i + 236, j + 5, 15, 12, null));
			cat_left.setTexture(texture); cat_right.setTexture(texture);
			cat_left.setTexturePos(0, 241, 232); cat_right.setTexturePos(0, 241, 244);
			cat_left.setTexturePos(1, 196, 232); cat_right.setTexturePos(1, 196, 244);
			cat_left.setTexturePos(2, 211, 232); cat_right.setTexturePos(2, 211, 244);
			cat_left.setTexturePos(3, 226, 232); cat_right.setTexturePos(3, 226, 244);
			cat_left.enabled = x > 0;
			cat_right.enabled = x < RecipeRegistry.getCategories().size() - 1;
			//
			this.buttonList.add(item_left = new GenericGuiButton(3, i + 27, j + 19, 15, 12, null));
			this.buttonList.add(item_right = new GenericGuiButton(4, i + 236, j + 19, 15, 12, null));
			item_left.setTexture(texture); item_right.setTexture(texture);
			item_left.setTexturePos(0, 241, 232); item_right.setTexturePos(0, 241, 244);
			item_left.setTexturePos(1, 196, 232); item_right.setTexturePos(1, 196, 244);
			item_left.setTexturePos(2, 211, 232); item_right.setTexturePos(2, 211, 244);
			item_left.setTexturePos(3, 226, 232); item_right.setTexturePos(3, 226, 244);
			item_left.enabled = y > 0;
			item_right.enabled = y < RecipeRegistry.getRecipes(x).size() - 1;
			//
			this.buttonList.add(recipe_left = new GenericGuiButton(5, i + 169, j + 138, 15, 12, null));
			this.buttonList.add(recipe_right = new GenericGuiButton(6, i + 236, j + 138, 15, 12, null));
			recipe_left.setTexture(texture); recipe_right.setTexture(texture);
			recipe_left.setTexturePos(0, 241, 232); recipe_right.setTexturePos(0, 241, 244);
			recipe_left.setTexturePos(1, 196, 232); recipe_right.setTexturePos(1, 196, 244);
			recipe_left.setTexturePos(2, 211, 232); recipe_right.setTexturePos(2, 211, 244);
			recipe_left.setTexturePos(3, 226, 232); recipe_right.setTexturePos(3, 226, 244);
			recipe_left.enabled = z > 0;
			recipe_right.enabled = z < RecipeRegistry.getRecipes(x, y).size() - 1;
			//
			this.buttonList.add(this.amount_up = new GenericGuiButton(7, i + 240, j + 49, 11, 9, null));
			this.buttonList.add(this.amount_down = new GenericGuiButton(8, i + 228, j + 49, 11, 9, null));
			amount_up.setTexture(texture); amount_down.setTexture(texture);
			amount_up.setTexturePos(0, 150, 216); amount_down.setTexturePos(0, 138, 216);
			amount_up.setTexturePos(1, 150, 207); amount_down.setTexturePos(1, 138, 207);
			amount_up.setTexturePos(2, 150, 234); amount_down.setTexturePos(2, 138, 234);
			amount_up.setTexturePos(3, 150, 225); amount_down.setTexturePos(3, 138, 225);
			amount_up.enabled = server.inv.amount < 64;
			amount_down.enabled = server.inv.amount > 1;
			//
		}
		
		private void update(int x, int y, int z){
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("target_listener", "fcl-bpt");
			compound.setString("task", "open");
			compound.setIntArray("args", new int[]{x, y, z});
			Print.debug(compound);
			PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(compound));
		}
		
		@Override
		public void handleMouseInput() throws IOException{
			super.handleMouseInput();
			int e = Mouse.getEventDWheel();
			if(e == 0){
				return;
			}
			int am = e > 0 ? -1 : 1;
			int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
			int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
			int i = this.guiLeft, j = this.guiTop;
			if(x >= (i + 27) && x <= (i + 250) && y >= (j + 5) && y <= (j + 16)){
				if(this.x + am >= 0 && this.x + am < RecipeRegistry.getCategories().size()){
					update(this.x + am, 0, 0);
				}
			}
			if(x >= (i + 27) && x <= (i + 250) && y >= (j + 19) && y <= (j + 30)){
				if(this.y + am >= 0 && this.y + am < RecipeRegistry.getRecipes(this.x).size()){
					update(this.x, this.y + am, 0);
				}
			}
			if(x >= (i + 169) && x <= (i + 250) && y >= (j + 138) && y <= (j + 149)){
				if(this.z + am >= 0 && this.z + am < RecipeRegistry.getRecipes(this.x, this.y).size()){
					update(this.x, this.y, this.z + am);
				}
			}
			if(x >= (i + 228) && x <= (i + 250) && y >= (j + 35) && y <= (j + 57)){
				int f = server.inv.getField(0) + am;
				if(f > 0 && f <= 64){
					server.inv.setField(0, f);
				}
			}
		}
		
	}
	
	public static class SRBTP implements IPacketListener<PacketNBTTagCompound>{

		@Override
		public String getId(){
			return "fcl-bpt";
		}

		@Override
		public void process(PacketNBTTagCompound packet, Object[] objs){
			Print.debug(packet.nbt);
			switch(packet.nbt.getString("task")){
				case "open":{
					int[] arr = packet.nbt.getIntArray("args");
					((EntityPlayer)objs[0]).openGui(FCL.getInstance(), 1, ((EntityPlayer)objs[0]).world, arr[0], arr[1], arr[2]);
					return;
				}
				case "craft":{
					try{
						EntityPlayer player = (EntityPlayer)objs[0];
						int[] arr = packet.nbt.getIntArray("args");
						BluePrintRecipe recipe = RecipeRegistry.getRecipes(arr[0], arr[1]).get(arr[2]);
						for(int i = 0; i < arr[3]; i++){
							if(!Server.canCraft(arr[0], arr[1], arr[2], player)){
								Print.chat(player, "&7Missing components to craft the Selected Item.");
							}
							for(ItemStack stack : recipe.components){
								boolean found = false;
								for(ItemStack itemstack : player.inventory.mainInventory){
									ItemStack com = itemstack.copy();
									com.setCount(com.getCount() > stack.getCount() ? stack.getCount() : com.getCount());
									boolean equal = ItemStack.areItemStacksEqual(stack, com);
									if(equal){
										found = true;
										itemstack.shrink(stack.getCount());
									}
								}
								if(!found){
									Print.chat(player, "ERROR: Missing Items;");
									return;
								}
							}
							ItemStack stack = recipe.output.copy();
							EntityItem item = new EntityItem(player.world);
							item.setItem(stack);
							item.setPosition(player.posX, player.posY + 1, player.posZ);
							player.world.spawnEntity(item);
							Print.chat(player, "&7Crafted &3" + recipe.output.getCount() + "pcs&7 of &9" + recipe.output.getDisplayName() + "&7.");
						}
					}
					catch(Exception e){
						e.printStackTrace();
						Print.chat((ICommandSender)objs[0], e.getMessage());
					}
					return;
				}
			}
		}
		
	}
	
	public static class Server extends Container {
		
		//private EntityPlayer player;
		//private World world;
		//private int x, y, z;
		private Inventory inv;

		public Server(EntityPlayer player, World world, int x, int y, int z){
			//this.player = player;
			//this.world = world;
			//this.x = x; this.y = y; this.z = z;
			inv = new Inventory();
			//
			for(int row = 0; row < 4; row++){
				for(int col = 0; col < 12; col++){
					addSlotToContainer(new Slot(inv, col + row * 12, 6 + col * 18, 46 + row * 18));
				}
			}
			addSlotToContainer(new Slot(inv, 48, 7, 10));
			//
			for(int row = 0; row < 3; row++){
				for(int col = 0; col < 9; col++){
					addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 6 + col * 18, 125 + row * 18));
				}
			}
			for(int col = 0; col < 9; col++){
				addSlotToContainer(new Slot(player.inventory, col, 6 + col * 18, 185));
			}
			//
			BluePrintRecipe recipe = RecipeRegistry.getRecipes(x, y).get(z);
			if(recipe != null){
				inv.setInventorySlotContents(48, recipe.output);
				for(int i = 0; i < 48; i++){
					if(i >= recipe.components.length){
						break;
					}
					inv.setInventorySlotContents(i, recipe.components[i]);
				}
			}
			else{
				inv.clear();
			}
		}

		public static boolean canCraft(int x, int y, int z, EntityPlayer player){
			BluePrintRecipe recipe = null;
			try{
				recipe = RecipeRegistry.getRecipes(x, y).get(z);
			}
			catch(Exception e){
				e.printStackTrace();
			}
			if(recipe == null){
				Print.debug("Recipe is null! [" + x + "|" + y + "|" + z + "];");
				return false;
			}
			//for(int times = 0; times < inv.amount; times++){
				ArrayList<Integer> list = new ArrayList<Integer>();
				for(ItemStack stack : recipe.components){
					boolean found = false;
					for(int i = 0; i < player.inventory.mainInventory.size(); i++){
						if(list.contains(i)){
							continue;
						}
						if(ItemStack.areItemsEqual(player.inventory.mainInventory.get(i), stack)){
							found = true;
							list.add(i);
							break;
						}
					}
					if(!found){
						return false;
					}
				}
			//}
			return true;
		}

		@Override
		public boolean canInteractWith(EntityPlayer player){
			return !player.isDead;
		}
		
		@Override
		public ItemStack slotClick(int slotId, int dragType, ClickType clickTypeIn, EntityPlayer player){
			return ItemStack.EMPTY;
		}
		
	}
	
	public static class Inventory implements IInventory {
		
		private NonNullList<ItemStack> invlist = NonNullList.<ItemStack>withSize(49, ItemStack.EMPTY);
		//private Server server;

		public Inventory(){}

		@Override
		public String getName(){
			return "Blueprinttable Inventory";
		}

		@Override
		public boolean hasCustomName(){
			return true;
		}

		@Override
		public ITextComponent getDisplayName(){
			return new TextComponentString(Formatter.format("&6") + getName());
		}

		@Override
		public int getSizeInventory(){
			return invlist.size();
		}

		@Override
		public boolean isEmpty(){
			return invlist.isEmpty();
		}

		@Override
		public ItemStack getStackInSlot(int index){
			return invlist.get(index);
		}

		@Override
		public ItemStack decrStackSize(int index, int count){
			return ItemStack.EMPTY;
		}

		@Override
		public ItemStack removeStackFromSlot(int index){
			return invlist.set(index, ItemStack.EMPTY);
		}

		@Override
		public void setInventorySlotContents(int index, ItemStack stack){
			invlist.set(index, stack);
		}

		@Override
		public int getInventoryStackLimit(){
			return 64;
		}

		@Override
		public void markDirty(){
			//
		}

		@Override
		public boolean isUsableByPlayer(EntityPlayer player){
			return false;
		}

		@Override
		public void openInventory(EntityPlayer player){
			//
		}

		@Override
		public void closeInventory(EntityPlayer player){
			//
		}

		@Override
		public boolean isItemValidForSlot(int index, ItemStack stack){
			return false;
		}

		@Override
		public int getField(int id){
			return id == 0 ? amount : 0;
		}

		@Override
		public void setField(int id, int value){
			switch(id){
				case 0:{
					amount = value;
					break;
				}
			}
		}

		@Override
		public int getFieldCount(){
			return 1;
		}

		@Override
		public void clear(){
			invlist.clear();
		}
		
		public int amount = 1;
		
	}
	
}