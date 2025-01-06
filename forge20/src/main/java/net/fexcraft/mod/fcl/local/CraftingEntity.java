package net.fexcraft.mod.fcl.local;

import net.fexcraft.mod.fcl.FCL;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class CraftingEntity extends BlockEntity {

	public CraftingEntity(BlockPos pos, BlockState state){
		super(FCL.CRAFTING_ENTITY.get(), pos, state);
	}

}
