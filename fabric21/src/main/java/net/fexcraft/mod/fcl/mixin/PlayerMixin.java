package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.fcl.util.EntityUtil;
import net.fexcraft.mod.fcl.util.EntityWI;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.impl.SWI;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.world.EntityW;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(Player.class)
public class PlayerMixin implements EWProvider {

	@Unique
	public UniEntity wrapper;

	@Override
	public UniEntity fcl_wrapper(){
		if(wrapper == null) wrapper = new UniEntity().set((Entity)(Object)this);
		return wrapper;
	}

}