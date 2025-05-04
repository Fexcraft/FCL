package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(ItemStack.class)
public class StackMixin implements SWProvider {

	@Unique
	public UniStack uni;

	@Override
	public UniStack fcl_wrapper(){
		if(uni == null) uni = new UniStack().set(this);
		return uni;
	}

}