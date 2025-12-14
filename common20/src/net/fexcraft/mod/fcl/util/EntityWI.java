package net.fexcraft.mod.fcl.util;

import net.fexcraft.lib.common.math.V3D;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.ui.UIKey;
import net.fexcraft.mod.uni.world.AABB;
import net.fexcraft.mod.uni.world.EntityW;
import net.fexcraft.mod.uni.world.WorldW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.ClickEvent;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
	protected V3D prev = new V3D();
	protected V3D pos = new V3D();

	public EntityWI(Entity iah){
		entity = iah;
		world = WrapperHolder.getWorld(entity.level());
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
		if(entity.level() != world.direct()){
			world = WrapperHolder.getWorld(entity.level());
		}
		return world;
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
		pos.x = entity.position().x;
		pos.y = entity.position().y;
		pos.z = entity.position().z;
		return pos;
	}

	@Override
	public void setPos(V3D pos){
		entity.setPos(pos.x, pos.y, pos.z);
	}

	@Override
	public V3D getPrevPos(){
		prev.x = entity.xOld;
		prev.y = entity.yOld;
		prev.z = entity.zOld;
		return prev;
	}

	@Override
	public void setPrevPos(V3D pos){
		entity.xOld = pos.x;
		entity.yOld = pos.y;
		entity.zOld = pos.z;
	}

	@Override
	public V3I getV3I(){
		return new V3I((int)entity.position().x, (int)entity.position().y, (int)entity.position().z);
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
		return UniStack.getStack(((Player)entity).getItemInHand(main ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND));
	}

	@Override
	public void closeUI(){
		((Player)entity).closeContainer();
	}

	@Override
	public int getInventorySize(){
		return ((Player)entity).getInventory().getContainerSize();
	}

	@Override
	public StackWrapper getStackAt(int idx){
		return UniStack.getStack(((Player)entity).getInventory().getItem(idx));
	}

	@Override
	public void addStack(StackWrapper stack){
		((Player)entity).getInventory().add(stack.local());
	}

	@Override
	public void send(String s){
		entity.sendSystemMessage(Component.translatable(s));
	}

	@Override
	public void send(String str, Object... args){
		entity.sendSystemMessage(Component.translatable(str, args));
	}

	@Override
	public void sendLink(String str){
		if(entity instanceof ServerPlayer == false) return;
		entity.sendSystemMessage(Component.literal(str)
			.withStyle(style -> style.withClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, str)).withUnderlined(true)));
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
	public void dismount(V3D pos){
		entity.unRide();
		entity.teleportTo(pos.x, pos.y, pos.z);
	}

	@Override
	public boolean inSimRange(){
		if(entity.level().isClientSide) return true;
		return ((ServerChunkCache)entity.level().getChunkSource()).chunkMap.getDistanceManager().inEntityTickingRange(entity.chunkPosition().toLong());
	}

	@Override
	public int getTicks(){
		return entity.tickCount;
	}

	@Override
	public int pushTicks(){
		return entity.tickCount++;
	}

	@Override
	public EntityW getVehicle(){
		return UniEntity.getEntityN(entity.getVehicle());
	}

	@Override
	public Object getVehicleDirect(){
		return entity.getVehicle();
	}

	@Override
	public void setLeash(EntityW player, boolean attach){
		if(entity instanceof Mob == false) return;
		Mob ent = (Mob)entity;
		if(attach){
			ent.setLeashedTo(player.local(), true);
		}
		else{
			ent.dropLeash(true, !player.isCreative());
		}
	}

	@Override
	public EntityW getLeash(){
		if(entity instanceof Mob == false) return null;
		Mob ent = (Mob)entity;
		return UniEntity.getEntityN(ent.getLeashHolder());
	}

	@Override
	public void move(V3D move){
		entity.move(MoverType.SELF, new Vec3(move.x, move.y, move.z));
	}

	@Override
	public void breakBlockAt(V3D pos){
		if(entity.level().isClientSide || !isPlayer()) return;
		//TODO
	}

	@Override
	public void setBB(AABB bb){
		entity.setBoundingBox(bb.local());
	}

	@Override
	public AABB getBB(){
		return AABB.wrap(entity.getBoundingBox());
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

	@Override
	public void remove(){
		entity.discard();
	}

	@Override
	public boolean isRemoved(){
		return entity.isRemoved();
	}

	@Override
	public void setOnGround(boolean bool){
		entity.setOnGround(bool);
	}

	@Override
	public List<StackWrapper> copyInventory(){
		if(!isPlayer()) return Collections.emptyList();
		ArrayList<StackWrapper> stacks = new ArrayList<>();
		for(ItemStack stack : ((Player)entity).getInventory().items){
			if(stack.isEmpty()) continue;
			stacks.add(UniStack.createStack(stack.copy()));
		}
		return stacks;
	}

	@Override
	public void mount(EntityW veh){
		entity.startRiding(veh.local(), true);
	}

}
