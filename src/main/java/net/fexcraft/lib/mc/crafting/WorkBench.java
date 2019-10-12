package net.fexcraft.lib.mc.crafting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockRenderLayer;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.state.StateFactory;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockMirror;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Direction;

public class WorkBench extends Block {

	public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

	public WorkBench(){
		super(FabricBlockSettings.of(Material.WOOD, DyeColor.BROWN).strength(1f, 32f).sounds(BlockSoundGroup.WOOD).build());
		this.setDefaultState(getStateFactory().getDefaultState().with(FACING, Direction.NORTH));
	}

	@Environment(EnvType.CLIENT)
	public BlockRenderLayer getRenderLayer(){
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	protected void appendProperties(StateFactory.Builder<Block, BlockState> stateFactory){
		stateFactory.add(FACING);
	}

	public BlockState getPlacementState(ItemPlacementContext ipc){
		return (BlockState) this.getDefaultState().with(FACING, ipc.getPlayerFacing().getOpposite());
	}

	public BlockState rotate(BlockState state, BlockRotation blockRotation_1){
		return state.with(FACING, blockRotation_1.rotate((Direction)state.get(FACING)));
	}

	public BlockState mirror(BlockState state, BlockMirror mirror){
		return state.rotate(mirror.getRotation((Direction)state.get(FACING)));
	}

}