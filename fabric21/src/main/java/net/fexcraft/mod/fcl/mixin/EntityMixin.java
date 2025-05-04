package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.mixint.EWProvider;
import net.fexcraft.mod.uni.UniEntity;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(Entity.class)
public class EntityMixin implements EWProvider {

	@Unique
	public UniEntity wrapper;

	@Override
	public UniEntity fcl_wrapper(){
		if(wrapper == null) wrapper = new UniEntity().set(this);
		return wrapper;
	}

}