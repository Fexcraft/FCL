package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniCon extends Container {

	protected EntityPlayer player;
	protected ContainerInterface con;
	@SideOnly(Side.CLIENT)
	protected UniUI uni;
	protected int slots;

	public UniCon(ContainerInterface con, EntityPlayer player){
		this.con = con;
		this.player = player;
		if(ContainerInterface.SEND_TO_CLIENT == null){
			ContainerInterface.SEND_TO_CLIENT = (com, pass) -> {
				com.set("target_listener", "fcl:ui");
				PacketHandler.getInstance().sendTo(new PacketNBTTagCompound(com.local()), pass.entity.local());
				//Packets.sendTo(Packet_TagListener.class, (Passenger)pass, "ui", com);
			};
		}
		if(ContainerInterface.SEND_TO_SERVER == null){
			ContainerInterface.SEND_TO_SERVER = com -> {
				com.set("target_listener", "fcl:ui");
				PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(com.local()));
				//Packets.send(Packet_TagListener.class, "ui", com);
			};
		}
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
				Print.log("error during inventory slot parsing");
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
						Print.log("error during inventory slot creation");
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
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);
		if(slot != null && slot.getHasStack()){
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();
			if(index < slots){
				if(!mergeItemStack(itemstack1, slots, inventorySlots.size(), true)) return ItemStack.EMPTY;
			}
			else if(!mergeItemStack(itemstack1, 0, slots, false)) return ItemStack.EMPTY;
			if(itemstack1.isEmpty()) slot.putStack(ItemStack.EMPTY);
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

}
