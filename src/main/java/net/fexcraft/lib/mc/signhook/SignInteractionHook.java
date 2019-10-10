package net.fexcraft.lib.mc.signhook;

import java.util.TreeMap;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class SignInteractionHook {

	public static TreeMap<String, Class<? extends SignInteractionHook>> KEYWORDS = new TreeMap<>();
	public static TreeMap<Identifier, Class<? extends SignInteractionHook>> REGISTRY = new TreeMap<>();
	
	public SignInteractionHook(SignBlockEntity ent){}
	
	public SignInteractionHook(CompoundTag compound){}

	/** Return true to cancel further default sign code. */
	public abstract boolean onBlockActivate(SignBlockEntity entity, BlockState state, World world, BlockPos pos, PlayerEntity ent, Hand hand, BlockHitResult result);

	public abstract Identifier getID();

	public abstract String getKeyword();

	public abstract void readNBT(CompoundTag tag);

	public abstract void writeNBT(CompoundTag tag);

}
