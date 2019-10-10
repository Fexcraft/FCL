package net.fexcraft.lib.mc.mixin;

import java.lang.reflect.InvocationTargetException;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fexcraft.lib.mc.signhook.SignEntityHook;
import net.fexcraft.lib.mc.signhook.SignInteractionHook;
import net.minecraft.block.AbstractSignBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

@Mixin(AbstractSignBlock.class)
public abstract class SignBlockMixin extends Block {

	public SignBlockMixin(Settings settings){ super(settings); }

	@Inject(at = @At("HEAD"), method = "activate()Z", cancellable = true)
	public void activate0(BlockState state, World world, BlockPos pos, PlayerEntity ent, Hand hand, BlockHitResult result, CallbackInfoReturnable<Boolean> info){
		if(!world.isClient){
			SignBlockEntity entity = (SignBlockEntity)world.getBlockEntity(pos);
			if(entity != null && entity instanceof SignEntityHook){
				SignEntityHook hook = (SignEntityHook)entity;
				if(hook.getSignInteractionHook() == null){
					Class<? extends SignInteractionHook> sih = SignInteractionHook.KEYWORDS.get(entity.text[0].asString());
					if(sih != null){
						try{
							hook.setSignInteractionHook(sih.getConstructor(SignBlockEntity.class).newInstance(entity));
							info.setReturnValue(true);
						}
						catch(InstantiationException | IllegalAccessException | IllegalArgumentException
								| InvocationTargetException | NoSuchMethodException | SecurityException e){
							e.printStackTrace();
						}
					}
				}
				else{
					if(hook.getSignInteractionHook().onBlockActivate(entity, state, world, pos, ent, hand, result)) info.setReturnValue(true);
				}
			}
		}
	}

}
