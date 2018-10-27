package net.fexcraft.lib.mc.crafting;

import net.fexcraft.lib.common.lang.ArrayList;
import net.fexcraft.lib.mc.gui.GenericContainer;
import net.fexcraft.lib.mc.utils.Formatter;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;

public class BluePrintTableContainer2 extends GenericContainer {
	
	private Inventory inv = new Inventory();
	protected BluePrintRecipe recipe;

	public BluePrintTableContainer2(EntityPlayer player, World world, int x, int y, int z){
		for(int row = 0; row < 5; row++){
			for(int col = 0; col < 12; col++){
				addSlotToContainer(new Slot(inv, col + row * 12, 6 + col * 18, 34 + row * 18));
			}
		}
		addSlotToContainer(new Slot(inv, 60, 7, 10));
		//
		for(int row = 0; row < 3; row++){
			for(int col = 0; col < 9; col++){
				addSlotToContainer(new Slot(player.inventory, col + row * 9 + 9, 6 + col * 18, 129 + row * 18));
			}
		}
		for(int col = 0; col < 9; col++){
			addSlotToContainer(new Slot(player.inventory, col, 6 + col * 18, 185));
		}
		//
		recipe = RecipeRegistry.getRecipes(x, y).get(z); Print.debug(x, y, z, recipe); refresh(0);
	}
	
	public void refresh(int scroll){;
		if(recipe != null){
			int j = scroll * 60;
			inv.setInventorySlotContents(60, recipe.output);
			for(int i = 0; i < 60; i++){
				inv.setInventorySlotContents(i, i + j >= recipe.components.length ? ItemStack.EMPTY : recipe.components[i + j]);
			}
		} else{ inv.clear(); }
	}

	@Override
	protected void packet(Side side, NBTTagCompound packet, EntityPlayer player){
		if(packet.hasKey("craft") && packet.getBoolean("craft")){
			int amount = packet.hasKey("amount") ? packet.getInteger("amount") : 1;
			//
			try{
				for(int i = 0; i < amount; i++){
					if(!craftable(player)){
						Print.chat(player, I18n.format("gui.fcl.blueprinttable2.craftmissing"));
						break;
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
					Print.chat(player, I18n.format("gui.fcl.blueprinttable2.craftsuccess", recipe.output.getCount(), recipe.output.getDisplayName()));
				}
			}
			catch(Exception e){
				e.printStackTrace(); Print.chat(player, e.getMessage());
			}
			player.closeScreen();
		}
		else return;
	}
	
    @Override
    public ItemStack transferStackInSlot(EntityPlayer player, int index){
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.inventorySlots.get(index);
        if(slot != null && slot.getHasStack()){
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();
            if(index < 60){
                if(!this.mergeItemStack(itemstack1, 60, this.inventorySlots.size(), true)){
                    return ItemStack.EMPTY;
                }
            }
            else if(!this.mergeItemStack(itemstack1, 0, 60, false)){
                return ItemStack.EMPTY;
            }
            if(itemstack1.isEmpty()){
                slot.putStack(ItemStack.EMPTY);
            }
            else{
                slot.onSlotChanged();
            }
        }
        return itemstack;
    }
	
	public boolean craftable(EntityPlayer player){
		if(recipe == null){ Print.debug("Recipe is null!"); return false; }
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
		return true;
	}
    
	public static class Inventory implements IInventory {
		
		private NonNullList<ItemStack> invlist = NonNullList.<ItemStack>withSize(61, ItemStack.EMPTY);

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
			return 0;
		}

		@Override
		public void setField(int id, int value){
			//
		}

		@Override
		public int getFieldCount(){
			return 0;
		}

		@Override
		public void clear(){
			invlist.clear();
		}
		
	}
	
}