package net.fexcraft.lib.mc.crafting;

import org.jetbrains.annotations.Nullable;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class BluePrintTable extends Block {
	
    public BluePrintTable(){
    	super(FabricBlockSettings.of(Material.METAL).strength(4.0f).nonOpaque());
    	setDefaultState(getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
	}
    
    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> stateManager){
        stateManager.add(Properties.HORIZONTAL_FACING);
    }
    
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx){
		return getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite());
	}
	
}