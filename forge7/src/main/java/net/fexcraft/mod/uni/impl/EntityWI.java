package net.fexcraft.mod.uni.impl;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;

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
		return null;
	}

	@Override
	public void closeUI(){

	}

	@Override
	public int getInventorySize(){
		return 0;
	}

	@Override
	public StackWrapper getStackAt(int idx){
		return null;
	}

	@Override
	public void addStack(StackWrapper stack){

	}

	@Override
	public V3D getEyeVec(){
		return null;
	}

	@Override
	public V3D getLookVec(){
		return null;
	}

	@Override
	public boolean isShiftDown(){
		return false;
	}

	@Override
	public void playSound(Object event, float volume, float pitch){

	}

	@Override
	public void remove(){

	}

	@Override
	public boolean isRemoved(){
		return false;
	}

	@Override
	public void onPacket(EntityW player, TagCW packet){

	}

	@Override
	public void setOnGround(boolean bool){

	}

	@Override
	public List<StackWrapper> copyInventory(){
		return Collections.emptyList();
	}

	@Override
	public void mount(EntityW veh){

	}

	@Override
	public void dismount(V3D pos){

	}

	@Override
	public boolean inSimRange(){
		return false;
	}

	@Override
	public int getTicks(){
		return 0;
	}

	@Override
	public int pushTicks(){
		return 0;
	}

	@Override
	public EntityW getVehicle(){
		return null;
	}

	@Override
	public Object getVehicleDirect(){
		return null;
	}

	@Override
	public void setLeash(EntityW player, boolean attach){

	}

	@Override
	public EntityW getLeash(){
		return null;
	}

	@Override
	public void move(V3D move){

	}
}
