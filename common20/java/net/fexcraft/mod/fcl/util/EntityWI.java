package net.fexcraft.mod.fcl.util;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;

import java.util.UUID;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EntityWI implements EntityW {

	protected Entity entity;

	public EntityWI(Entity iah){
		entity = iah;
	}

	@Override
	public boolean isOnClient(){
		return entity.level().isClientSide;
	}

	@Override
	public int getId(){
		return entity.getId();
	}

	@Override
	public WorldW getWorld(){
		return WrapperHolder.getWorld(entity.level());
	}

	@Override
	public boolean isPlayer(){
		return entity instanceof Player;
	}

	@Override
	public boolean isAnimal(){
		return entity instanceof Animal;
	}

	@Override
	public boolean isHostile(){
		return entity instanceof Mob;
	}

	@Override
	public boolean isLiving(){
		return entity instanceof LivingEntity;
	}

	@Override
	public boolean isRiding(){
		return entity.isPassenger();
	}

	@Override
	public String getRegName(){
		return BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString();
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
		return new V3D(entity.position().x, entity.position().y, entity.position().z);
	}

	@Override
	public void decreaseXZMotion(double x){
		//
	}

	@Override
	public void setYawPitch(float oyaw, float opitch, float yaw, float pitch){
		//
	}

	@Override
	public void openUI(String id, V3I pos){
		EntityUtil.UI_OPENER.open((Player)entity, id, pos);
	}

	@Override
	public void openUI(UIKey key, V3I pos){
		openUI(key.key, pos);
	}

	@FunctionalInterface
	public static interface UIOpen {

		public void open(Player player, String ui, V3I pos);

	}

	@Override
	public String getName(){
		return entity.getName().getString();
	}

	@Override
	public void drop(StackWrapper stack, float height){
		entity.spawnAtLocation(stack.local(), height);
	}

	@Override
	public boolean isCreative(){
		return ((Player)entity).isCreative();
	}

	@Override
	public UUID getUUID(){
		return entity instanceof Player ? ((Player)entity).getGameProfile().getId() : entity.getUUID();
	}

	@Override
	public StackWrapper getHeldItem(boolean main){
		return StackWrapper.wrap(((Player)entity).getItemInHand(main ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));
	}

	@Override
	public void closeUI(){
		((Player)entity).closeContainer();
	}

	@Override
	public String dimid(){
		return entity.level().dimension().location().toString();
	}

	@Override
	public int dim12(){
		return 0;
	}

	@Override
	public int getInventorySize(){
		return ((Player)entity).getInventory().getContainerSize();
	}

	@Override
	public StackWrapper getStackAt(int idx){
		return StackWrapper.wrap(((Player)entity).getInventory().getItem(idx));
	}

	@Override
	public void addStack(StackWrapper stack){
		((Player)entity).getInventory().add(stack.local());
	}

	@Override
	public void send(String s){
		entity.sendSystemMessage(Component.literal(Formatter.format(Component.translatable(s).getString())));
	}

	@Override
	public void send(String str, Object... args){
		entity.sendSystemMessage(Component.translatable(str, args));
	}

	@Override
	public void bar(String s){
		((Player)entity).displayClientMessage(Component.translatable(s), true);
	}

	@Override
	public void bar(String str, Object... args){
		((Player)entity).displayClientMessage(Component.translatable(str, args), true);
	}

	@Override
	public void dismount(){
		entity.unRide();
	}

	@Override
	public V3D getEyeVec(){
		return new V3D(entity.getEyePosition().x, entity.getEyePosition().y, entity.getEyePosition().z);
	}

	@Override
	public V3D getLookVec(){;
		return new V3D(entity.getLookAngle().x, entity.getLookAngle().y, entity.getLookAngle().z);
	}

	@Override
	public boolean isShiftDown(){
		return entity.isShiftKeyDown();
	}

	@Override
	public void playSound(Object event, float volume, float pitch){
		entity.playSound((SoundEvent)event, volume, pitch);
	}

}
