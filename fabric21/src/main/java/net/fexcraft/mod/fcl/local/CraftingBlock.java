package net.fexcraft.mod.fcl.local;

import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.fcl.FCL;
import net.fexcraft.mod.fcl.UniFCL;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.BlockHitResult;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingBlock extends Block implements EntityBlock {

	public static final EnumProperty<Direction> FACING = EnumProperty.create("facing", Direction.class, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST);

	public CraftingBlock(BlockBehaviour.Properties props){
		super(props);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state){
		return FCL.CRAFTING_ENTITY.create(pos, state);
	}

	@Override
	public RenderShape getRenderShape(BlockState pState){
		return RenderShape.INVISIBLE;
	}

	@Override
    public InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult res){
		if(!level.isClientSide && hand != InteractionHand.OFF_HAND){
			UniEntity.get(player).entity.openUI(UniFCL.SELECT_RECIPE_CATEGORY, new V3I(pos.getX(), pos.getY(), pos.getZ()));
        }
		return InteractionResult.SUCCESS;
    }

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> sd){
		sd.add(FACING);
	}

	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return defaultBlockState().setValue(FACING, context.getPlayer().getDirection().getOpposite());
	}

}
