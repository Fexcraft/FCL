package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.BSWProvider;
import net.fexcraft.mod.uni.world.StateWrapper;
import net.minecraft.block.state.BlockStateContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(BlockStateContainer.StateImplementation.class)
public class BlockStateMixin implements BSWProvider {

	@Unique
	public StateWrapper sw;

	@Override
	public StateWrapper fcl_wrapper(){
		if(sw == null) sw = StateWrapper.create(this);
		return sw;
	}

}