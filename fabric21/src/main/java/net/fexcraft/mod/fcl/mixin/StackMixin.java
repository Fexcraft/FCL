package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.uni.impl.SWI;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(ItemStack.class)
public class StackMixin implements SWProvider {

	@Unique
	public StackWrapper wrapper;

	@Override
	public StackWrapper fcl_wrapper(){
		if(wrapper == null) wrapper = new SWI((ItemStack)(Object)this);
		return wrapper;
	}

}