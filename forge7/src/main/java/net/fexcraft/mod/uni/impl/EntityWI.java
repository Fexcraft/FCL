package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.Vec3;

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
		world = WrapperHolder.getWorld(ent.worldObj);
	}

	@Override
	public boolean isOnClient(){
		return entity.worldObj.isRemote;
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
		return entity.getClass().getSimpleName();
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
		pos.set(entity.posX, entity.posY, entity.posZ);
		return pos;
	}

	@Override
	public void setPos(V3D pos){
		entity.setPosition(pos.x, pos.y, pos.z);
	}

	@Override
	public V3D getPrevPos(){
		prev.set(entity.prevPosX, entity.prevPosY, entity.prevPosZ);
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
	public void drop(StackWrapper stack, float height){
		entity.entityDropItem(stack.local(), height);
	}

	@Override
	public boolean isCreative(){
		return isPlayer() && ((EntityPlayer)entity).capabilities.isCreativeMode;
	}

	@Override
	public void send(String s){
		((EntityPlayer)entity).addChatComponentMessage(new ChatComponentTranslation(s));
	}

	@Override
	public void send(String str, Object... args){
		((EntityPlayer)entity).addChatComponentMessage(new ChatComponentTranslation(str, args));
	}

	@Override
	public void bar(String s){
		send(s);
	}

	@Override
	public void bar(String str, Object... args){
		send(str, args);
	}

	@Override
	public String getName(){
		return entity instanceof EntityPlayer ? ((EntityPlayer)entity).getGameProfile().getName() : entity.getCommandSenderName();
	}

	@Override
	public UUID getUUID(){
		return entity instanceof EntityPlayer ? ((EntityPlayer)entity).getGameProfile().getId() : entity.getUniqueID();
	}

	@Override
	public StackWrapper getHeldItem(boolean main){
		return entity instanceof EntityPlayer ? UniStack.getStack(((EntityPlayer)entity).getHeldItem()) : StackWrapper.EMPTY;
	}

	@Override
	public void closeUI(){
		((EntityPlayer)entity).closeScreen();
	}

	@Override
	public int getInventorySize(){
		if(!isPlayer()) return 0;
		return ((EntityPlayer)entity).inventory.mainInventory.length;
	}

	@Override
	public StackWrapper getStackAt(int idx){
		return UniStack.getStack(((EntityPlayer)entity).inventory.mainInventory[idx]);
	}

	@Override
	public void addStack(StackWrapper stack){
		((EntityPlayer)entity).inventory.addItemStackToInventory(stack.local());
	}

	@Override
	public V3D getEyeVec(){
		return new V3D();
	}

	@Override
	public V3D getLookVec(){
		Vec3 vec = Minecraft.getMinecraft().thePlayer.getLookVec();
		return new V3D(vec.xCoord, vec.yCoord, vec.zCoord);
	}

	@Override
	public boolean isShiftDown(){
		return entity.isSneaking();
	}

	@Override
	public void playSound(Object event, float volume, float pitch){
		entity.playSound(event.toString(), volume, pitch);
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
	public void setOnGround(boolean bool){
		entity.onGround = bool;
	}

	@Override
	public List<StackWrapper> copyInventory(){
		if(!isPlayer()) return Collections.emptyList();
		ArrayList<StackWrapper> stacks = new ArrayList<>();
		for(ItemStack stack : ((EntityPlayer)entity).inventory.mainInventory){
			if(stack == null || stack.stackSize < 1) continue;
			stacks.add(UniStack.createStack(stack.copy()));
		}
		return stacks;
	}

	@Override
	public void mount(EntityW veh){
		entity.mountEntity(veh.local());
	}

	@Override
	public void dismount(V3D pos){
		entity.mountEntity(null);
		entity.setPosition(pos.x, pos.y, pos.z);
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
		return UniEntity.getEntityN(entity.ridingEntity);
	}

	@Override
	public Object getVehicleDirect(){
		return entity.ridingEntity;
	}

	@Override
	public void setLeash(EntityW player, boolean attach){
		if(!isLiving()) return;
		EntityLiving ent = (EntityLiving)entity;
		if(attach){
			ent.setLeashedToEntity(player.local(), true);
		}
		else{
			ent.clearLeashed(true, !player.isCreative());
		}
	}

	@Override
	public EntityW getLeash(){
		if(!isLiving()) return null;
		EntityLiving ent = (EntityLiving)entity;
		return UniEntity.getEntityN(ent.getLeashedToEntity());
	}

	@Override
	public void move(V3D move){
		entity.moveEntity(move.x, move.y, move.z);
	}

}
