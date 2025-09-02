package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.UIKey;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.translation.I18n;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EntityWI implements EntityW {

	protected Entity entity;
	protected WorldW world;
	protected V3D pos = new V3D();
	protected V3D prev = new V3D();

	public EntityWI(Entity ent){
		entity = ent;
		world = WrapperHolder.getWorld(ent.world);
	}

	@Override
	public boolean isOnClient(){
		return entity.world.isRemote;
	}

	@Override
	public int getId(){
		return entity.getEntityId();
	}

	@Override
	public WorldW getWorld(){
		return world;
	}

	@Override
	public boolean isPlayer(){
		return entity instanceof EntityPlayer;
	}

	@Override
	public boolean isAnimal(){
		return entity instanceof EntityAnimal;
	}

	@Override
	public boolean isHostile(){
		return entity instanceof EntityMob;
	}

	@Override
	public boolean isLiving(){
		return entity instanceof EntityLiving;
	}

	@Override
	public boolean isRiding(){
		return entity.isRiding();
	}

	@Override
	public String getRegName(){
		EntityEntry entry = EntityRegistry.getEntry(entity.getClass());
		return entry == null ? "minecraft:null" : entry.getRegistryName().toString();
	}

	@Override
	public void decreaseXZMotion(double x){
		entity.motionX *= x;
		entity.motionZ *= x;
	}

	@Override
	public void setYawPitch(float oyaw, float opitch, float yaw, float pitch){
		entity.prevRotationYaw = oyaw;
		entity.prevRotationPitch = opitch;
		entity.rotationYaw = yaw;
		entity.rotationPitch = pitch;
	}

	@Override
	public void openUI(String id, V3I pos){
		openUI(UIKey.find(id), pos);
	}

	@Override
	public void openUI(UIKey key, V3I pos){
		if(key == null || entity instanceof EntityPlayer == false) return;
		((EntityPlayer)entity).openGui(UniReg.getInst(key), key.id, world.local(), pos.x, pos.y, pos.z);
	}

	@Override
	public String getName() {
		return entity instanceof EntityPlayer ? ((EntityPlayer)entity).getGameProfile().getName() : entity.getName();
	}

	@Override
	public void drop(StackWrapper stack, float height){
		entity.entityDropItem(stack.local(), height);
	}

	@Override
	public boolean isCreative(){
		return entity instanceof EntityPlayer && ((EntityPlayer)entity).capabilities.isCreativeMode;
	}

	@Override
	public UUID getUUID(){
		return entity instanceof EntityPlayer ? ((EntityPlayer)entity).getGameProfile().getId() : entity.getUniqueID();
	}

	@Override
	public StackWrapper getHeldItem(boolean main){
		return entity instanceof EntityPlayer ? UniStack.getStack(((EntityPlayer)entity).getHeldItem(main ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND)) : StackWrapper.EMPTY;
	}

	@Override
	public void closeUI(){
		((EntityPlayer)entity).closeScreen();
	}

	@Override
	public String dimid(){
		return entity.dimension + "";
	}

	@Override
	public int dim12(){
		return entity.dimension;
	}

	@Override
	public int getInventorySize(){
		if(!isPlayer()) return 0;
		return ((EntityPlayer)entity).inventory.mainInventory.size();
	}

	@Override
	public StackWrapper getStackAt(int idx){
		return UniStack.getStack(((EntityPlayer)entity).inventory.mainInventory.get(idx));
	}

	@Override
	public void addStack(StackWrapper stack){
		((EntityPlayer)entity).addItemStackToInventory(stack.local());
	}

	@Override
	public <E> E local(){
		return (E)entity;
	}

	@Override
	public Object direct(){
		return entity;
	}

	@Override
	public V3D getPos(){
		pos.x = entity.posX;
		pos.y = entity.posY;
		pos.z = entity.posZ;
		return pos;
	}

	@Override
	public void setPos(V3D pos){
		entity.setPosition(pos.x, pos.y, pos.z);
	}

	@Override
	public V3D getPrevPos(){
		prev.x = entity.prevPosX;
		prev.y = entity.prevPosY;
		prev.z = entity.prevPosZ;
		return prev;
	}

	@Override
	public void setPrevPos(V3D pos){
		entity.prevPosX = pos.x;
		entity.prevPosY = pos.y;
		entity.prevPosZ = pos.z;
	}

	@Override
	public V3I getV3I(){
		return new V3I((int)entity.posX, (int)entity.posY, (int)entity.posZ);
	}

	@Override
	public void send(String s){
		entity.sendMessage(new TextComponentString(Formatter.format(I18n.translateToLocal(s))));
	}

	@Override
	public void send(String str, Object... args){
		entity.sendMessage(new TextComponentString(Formatter.format(I18n.translateToLocalFormatted(str, args))));
	}

	@Override
	public void sendLink(String url){
		TextComponentString text = new TextComponentString(url);
		text.setStyle(new Style().setUnderlined(true).setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, url)));
		entity.sendMessage(text);
	}

	@Override
	public void bar(String s){
		if(entity instanceof EntityPlayer){
			((EntityPlayer)entity.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(Formatter.format(I18n.translateToLocal(s))), true);
		}
		else entity.sendMessage(new TextComponentString(Formatter.format(s)));
	}

	@Override
	public void bar(String s, Object... objs){
		if(entity instanceof EntityPlayer){
			((EntityPlayer)entity.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(Formatter.format(I18n.translateToLocalFormatted(s, objs))), true);
		}
		else entity.sendMessage(new TextComponentString(Formatter.format(s)));
	}

	@Override
	public void mount(EntityW veh){
		entity.startRiding(veh.local(), true);
	}

	@Override
	public void dismount(V3D pos){
		entity.dismountRidingEntity();
		entity.setPositionAndUpdate(pos.x, pos.y, pos.z);
	}

	@Override
	public boolean inSimRange(){
		return true;
	}

	@Override
	public int getTicks(){
		return entity.ticksExisted;
	}

	@Override
	public int pushTicks(){
		return entity.ticksExisted++;
	}

	@Override
	public EntityW getVehicle(){
		return UniEntity.getEntityN(entity.getRidingEntity());
	}

	@Override
	public Object getVehicleDirect(){
		return entity.getRidingEntity();
	}

	@Override
	public void setLeash(EntityW player, boolean attach){
		if(!isLiving()) return;
		EntityLiving ent = (EntityLiving)entity;
		if(attach){
			ent.setLeashHolder(player.local(), true);
		}
		else{
			ent.clearLeashed(true, !player.isCreative());
		}
	}

	@Override
	public EntityW getLeash(){
		if(!isLiving()) return null;
		EntityLiving ent = (EntityLiving)entity;
		return UniEntity.getEntityN(ent.getLeashHolder());
	}

	@Override
	public void move(V3D move){
		entity.move(MoverType.SELF, move.x, move.y, move.z);
	}

	@Override
	public V3D getEyeVec(){
		Vec3d vec = net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity().getPositionEyes(net.minecraft.client.Minecraft.getMinecraft().getRenderPartialTicks());
		return new V3D(vec.x, vec.y, vec.z);
	}

	@Override
	public V3D getLookVec(){
		Vec3d vec = net.minecraft.client.Minecraft.getMinecraft().getRenderViewEntity().getLook(net.minecraft.client.Minecraft.getMinecraft().getRenderPartialTicks());
		return new V3D(vec.x, vec.y, vec.z);
	}

	@Override
	public boolean isShiftDown(){
		return entity.isSneaking();
	}

	@Override
	public void playSound(Object event, float volume, float pitch){
		entity.playSound((SoundEvent)event, volume, pitch);
	}

	@Override
	public void remove(){
		entity.setDead();
	}

	@Override
	public boolean isRemoved(){
		return entity.isDead;
	}

	@Override
	public void onPacket(EntityW player, TagCW packet){
		//
	}

	@Override
	public void setOnGround(boolean bool){
		entity.onGround = bool;
	}

	@Override
	public List<StackWrapper> copyInventory(){
		if(!isPlayer()) return Collections.emptyList();
		ArrayList<StackWrapper> stacks = new ArrayList<>();
		for(ItemStack stack : ((EntityPlayer)entity).inventory.mainInventory){
			if(stack.isEmpty()) continue;
			stacks.add(UniStack.createStack(stack.copy()));
		}
		return stacks;
	}

}
