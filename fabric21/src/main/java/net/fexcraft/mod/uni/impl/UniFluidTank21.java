package net.fexcraft.mod.uni.impl;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidStorage;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.storage.base.SingleVariantStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniFluidTank;
import net.fexcraft.mod.uni.inv.UniStack;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.WrapperHolder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.RegistryOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniFluidTank21 extends UniFluidTank {

	private SingleVariantStorage<FluidVariant> storage;
	private int cap;

	public UniFluidTank21(int capacity){
		super();
		cap = capacity;
		storage = new SingleVariantStorage<FluidVariant>() {
			@Override
			protected FluidVariant getBlankVariant(){
				return FluidVariant.blank();
			}
			@Override
			protected long getCapacity(FluidVariant variant){
				return capacity;
			}
		};
	}

	@Override
	public int capacity(){
		return cap;
	}

	@Override
	public TagCW save(){
		TagCW tag = TagCW.create();
		tag.set("var", TagCW.wrap(FluidVariant.CODEC.encodeStart(RegistryOps.create(NbtOps.INSTANCE, FCL.SERVER.get().registryAccess()), storage.variant).getOrThrow()));
		tag.set("am", storage.amount);
		return tag;
	}

	@Override
	public void load(TagCW com){
		if(com.has("var")){
			try{
				var ra = FCL.SERVER.isPresent() ? FCL.SERVER.get().registryAccess() : ((Level)WrapperHolder.getClientWorld().local()).registryAccess();
				storage.variant = FluidVariant.CODEC.decode(RegistryOps.create(NbtOps.INSTANCE, ra), com.getCompound("var").local()).getOrThrow().getFirst();
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		storage.amount = com.getLong("am");
	}

	@Override
	public <T> T local(){
		return (T)storage;
	}

	@Override
	public int amount(){
		return Math.toIntExact(storage.amount);
	}

	@Override
	public void amount(String type, int am){
		clear();
		FluidVariant var = FluidVariant.of(BuiltInRegistries.FLUID.getValue(ResourceLocation.parse(type)));
		if(var == null) return;
		storage.variant = var;
		storage.amount = am;
	}

	@Override
	public void drain(int am, boolean con){
		if(!con) return;
		storage.amount -= am;
		storage.amount = storage.amount < 0 ? 0 : storage.amount > cap ? cap : storage.amount;
	}

	@Override
	public void fill(int am, boolean con){
		if(!con) return;
		storage.amount += am;
		storage.amount = storage.amount < 0 ? 0 : storage.amount > cap ? cap : storage.amount;
	}

	@Override
	public String getFluid(){
		return storage.variant.getRegistryEntry().unwrapKey().get().location().getPath();
	}

	@Override
	public String getFluidFromStack(StackWrapper sw){
		ContainerItemContext context = ContainerItemContext.withConstant(sw.local());
		Storage<FluidVariant> stor = context.find(FluidStorage.ITEM);
		if(stor == null) return null;
		for(StorageView<FluidVariant> var : stor){
			return var.getResource().getRegistryEntry().unwrapKey().get().location().getPath();
		}
		return null;
	}

	@Override
	public Pair<StackWrapper, Boolean> drainFrom(StackWrapper stack, int amount){
		if(stack.empty()) return Pair.of(stack, false);
		ContainerItemContext context = ContainerItemContext.withConstant(stack.local());
		Storage<FluidVariant> stor = context.find(FluidStorage.ITEM);
		if(stor == null) return Pair.of(stack, false);
		FluidVariant ver = null;
		for(StorageView<FluidVariant> var : stor){
			if(storage.variant.isBlank() || var.getResource().equals(storage.variant)){
				ver = var.getResource();
				break;
			}
		}
		if(ver == null) return Pair.of(stack, false);
		try(Transaction tran = Transaction.openOuter()){
			context.extract(context.getItemVariant(), FluidConstants.BUCKET / 81, tran);
			tran.commit();
		}
		if(storage.variant.isBlank()) storage.variant = ver;
		storage.amount += 1000;
		ItemStack steck = context.getItemVariant().getItem() instanceof BucketItem ? new ItemStack(Items.BUCKET) : context.getItemVariant().toStack();
		return Pair.of(UniStack.getStack(steck), true);
	}

	@Override
	public void clear(){
		storage.variant = FluidVariant.blank();
		storage.amount = 0;
	}

}
