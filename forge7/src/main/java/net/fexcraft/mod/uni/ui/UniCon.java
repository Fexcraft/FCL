package net.fexcraft.mod.uni.ui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fcl.packet.PacketHandler;
import net.fexcraft.mod.fcl.packet.PacketTagHandler.I12_PacketTag;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniCon extends Container {

	protected EntityPlayer player;
	protected ContainerInterface con;
	protected UIKey ui_type;
	@SideOnly(Side.CLIENT)
	protected UniUI uni;
	protected int slots;

	public UniCon(ContainerInterface con, UIKey key, EntityPlayer player){
		this.con = con;
		this.player = player;
		ui_type = key;
		if(ContainerInterface.SEND_TO_CLIENT == null){
			ContainerInterface.SEND_TO_CLIENT = (com, pass) -> PacketHandler.getInstance().sendTo(new I12_PacketTag("fcl:ui", com), pass.entity.local());
		}
		if(ContainerInterface.SEND_TO_SERVER == null){
			ContainerInterface.SEND_TO_SERVER = com -> PacketHandler.getInstance().sendToServer(new I12_PacketTag("fcl:ui", com));
		}
		con.uiid = ui_type;
		if(con.ui_map.has("slots")) initInv();
		con.root = this;
		con.init();
	}

	private void initInv(){
		ArrayList<UISlot> uislots = new ArrayList<>();
		for(Map.Entry<String, JsonValue<?>> entry : con.ui_map.getMap("slots").entries()){
			try{
				uislots.add(new UISlot(con.ui, entry.getValue().asMap()));
			}
			catch(Exception e){
				FCL.LOGGER.info("error during inventory slot parsing");
				e.printStackTrace();
			}
		}
		slots = 0;
		inventoryItemStacks.clear();
		inventorySlots.clear();
		for(UISlot slot : uislots){
			IInventory inventory = slot.playerinv ? player.inventory : con.inventory.cast();
			for(int y = 0; y < slot.repeat_y; y++){
				for(int x = 0; x < slot.repeat_x; x++){
					try{
						addSlot((Slot)UISlot.get(slot.type, new Object[]{ inventory, x + (y * slot.repeat_x) + slot.index, slot.x + x * 18, slot.y + y * 18 }));
					}
					catch(Exception e){
						FCL.LOGGER.info("error during inventory slot creation");
						e.printStackTrace();
					}
					if(!slot.playerinv) slots++;
				}
			}
		}
	}

	public void addSlot(String type, Object... args){
		try{
			Slot slot = (Slot)UISlot.get(type, args);
			addSlot(slot);
			slots++;
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer player){
		return player != null;
	}

	public void setup(UniUI ui){
		uni = ui;
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index){
		ItemStack itemstack = null;
		Slot slot = (Slot)inventorySlots.get(index);
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index < slots){
				if(!mergeItemStack(itemstack1, slots, inventorySlots.size(), true)) return null;
			}
			else if(!mergeItemStack(itemstack1, 0, slots, false)) return null;
			if(itemstack1 == null) slot.putStack(null);
			else slot.onSlotChanged();
		}
		return itemstack;
	}

	public ContainerInterface container(){
		return con;
	}

	@Override
	public void onContainerClosed(EntityPlayer player){
		super.onContainerClosed(player);
		con.onClosed();
	}

	@Override
	public void detectAndSendChanges(){
		super.detectAndSendChanges();
		con.update(this);
	}

	public void addSlot(Slot slot){
		addSlotToContainer(slot);
	}

	public StackWrapper getPickedStack(){
		return UniStack.getStack(player.inventory.getItemStack());
	}

}
