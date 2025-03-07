package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.app.json.JsonValue;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fcl.util.UIPacketReceiver;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniCon extends AbstractContainerMenu implements UIPacketReceiver {

	protected ItemStack stack;
	protected net.fexcraft.mod.uni.ui.UniUI screen;
	protected Player player;
	protected int slotam;
	//
	protected ContainerInterface con;
	protected UIKey ui_type;
	protected net.fexcraft.mod.uni.ui.UniUI uni;

	public UniCon(int id, Inventory inv, UIKey key, V3I pos, JsonMap map){
		super(FCL.UNIVERSAL, id);
		stack = inv.player.getItemInHand(InteractionHand.MAIN_HAND);
		player = inv.player;
		ui_type = key;
		UniEntity entity = UniEntity.get(inv.player);
		try{
			con = UniReg.MENU.get(ui_type).getConstructor(JsonMap.class, UniEntity.class, V3I.class).newInstance(map, entity, pos);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		con.uiid = ui_type;
		if(con.ui_map.has("slots")) initInv();
		con.root = this;
		con.init();
	}

	private void initInv(){
		ArrayList<UISlot> uislots = new ArrayList<>();
		if(con.ui_map.has("slots")){
			for(Map.Entry<String, JsonValue<?>> entry : con.ui_map.getMap("slots").entries()){
				try{
					uislots.add(new UISlot(con.ui, entry.getValue().asMap()));
				}
				catch(Exception e){
					e.printStackTrace();
				}
			}
		}
		slotam = 0;
		slots.clear();
		for(UISlot slot : uislots){
			Container inventory = slot.playerinv ? player.getInventory() : (Container)con.inventory;
			for(int y = 0; y < slot.repeat_y; y++){
				for(int x = 0; x < slot.repeat_x; x++){
					try{
						addSlot((Slot)UISlot.get(slot.type, new Object[]{ inventory, x + (y * slot.repeat_x) + slot.index, slot.x + x * 18, slot.y + y * 18 }));
					}
					catch(Exception e){
						e.printStackTrace();
					}
					if(!slot.playerinv) slotam++;
				}
			}
		}
	}

	public void addSlot(String type, Object... args){
		try{
			addSlot((Slot)UISlot.get(type, args));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

	public Slot addSlot(Slot slot){
		return super.addSlot(slot);
	}

	@Override
	public boolean stillValid(Player player){
		return (player != null);
	}

	@Override
	public ItemStack quickMoveStack(Player player, int index){
		ItemStack stack0 = ItemStack.EMPTY;
		Slot slot = slots.get(index);
		if(slot != null && slot.hasItem()){
			ItemStack stack1 = slot.getItem();
			stack0 = stack1.copy();
			if(index < slotam && !moveItemStackTo(stack1, slotam, slots.size(), true)) return ItemStack.EMPTY;
			else if(!moveItemStackTo(stack1, 0, slotam, false)) return ItemStack.EMPTY;
			if(stack1.isEmpty()) slot.setByPlayer(ItemStack.EMPTY);
			else slot.setChanged();
		}
		return stack0;
	}

	@Override
	public void broadcastChanges(){
		super.broadcastChanges();
		con.update(this);
	}

	@Override
	public void removed(Player player){
		super.removed(player);
		con.onClosed();
	}

	public void setup(UniUI ui){
		uni = ui;
	}

	public ContainerInterface container(){
		return con;
	}

	@Override
	public void onPacket(CompoundTag com, boolean client){
		if(com.getBoolean("return")){
			con.player.entity.openUI(con.ui_map.getString("return", null), con.pos);
			return;
		}
		con.packet(TagCW.wrap(com), client);
	}

}