package net.fexcraft.mod.uni.world;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.ui.UIKey;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.common.registry.EntityRegistry;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EntityWI implements EntityW {

	protected Entity entity;
	protected WorldW world;

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
		return entity instanceof EntityPlayer ? StackWrapper.wrap(((EntityPlayer)entity).getHeldItem(main ? EnumHand.MAIN_HAND : EnumHand.OFF_HAND)) : StackWrapper.EMPTY;
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
	public <E> E local(){
		return (E)entity;
	}

	@Override
	public Object direct(){
		return entity;
	}

	@Override
	public V3D getPos(){
		return new V3D(entity.posX, entity.posY, entity.posZ);
	}

	@Override
	public void send(String s){
		entity.sendMessage(new TextComponentString(Formatter.format(I18n.format(s))));
	}

	@Override
	public void send(String str, Object... args){
		entity.sendMessage(new TextComponentString(Formatter.format(I18n.format(str, args))));
	}

	@Override
	public void bar(String s){
		if(entity instanceof EntityPlayer){
			((EntityPlayer)entity.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(Formatter.format(I18n.format(s))), true);
		}
		else entity.sendMessage(new TextComponentString(Formatter.format(s)));
	}

	@Override
	public void bar(String s, Object... objs){
		if(entity instanceof EntityPlayer){
			((EntityPlayer)entity.getCommandSenderEntity()).sendStatusMessage(new TextComponentString(Formatter.format(I18n.format(s, objs))), true);
		}
		else entity.sendMessage(new TextComponentString(Formatter.format(s)));
	}

	@Override
	public void dismount(){
		entity.dismountRidingEntity();
	}

}
