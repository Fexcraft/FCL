package net.fexcraft.lib.mc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.fabric.api.server.PlayerStream;
import net.fexcraft.lib.mc.signhook.SignEntityHook;
import net.fexcraft.lib.mc.signhook.SignInteractionHook;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.entity.SignBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

@Mixin(SignBlockEntity.class)
public abstract class SignEntityMixin extends BlockEntity implements SignEntityHook {
	
	private SignInteractionHook sign_interaction_hook;

	public SignEntityMixin(BlockEntityType<?> block){ super(block); }
	
	public SignInteractionHook getSignInteractionHook(){
		return sign_interaction_hook;
	}
	
	public void setSignInteractionHook(SignInteractionHook hook){
		sign_interaction_hook = hook;
	}

	@Inject(at = @At("RETURN"), method = "fromTag()V")
	public void fromTag0(CompoundTag tag, CallbackInfo info){
		if(tag.containsKey("FCL:SIH")){
			try{
				if(sign_interaction_hook != null){
					sign_interaction_hook.readNBT(tag);
				}
				else{
					sign_interaction_hook = SignInteractionHook.REGISTRY.get(new Identifier(tag.getString("FCL:SIH")))
						.getConstructor(CompoundTag.class).newInstance(tag);
				}
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
	}

	@Inject(at = @At("RETURN"), method = "toTag()Lnet/minecraft/nbt/CompoundTag;")
	public void toTag0(CompoundTag tag, CallbackInfoReturnable<CompoundTag> info){
		if(sign_interaction_hook != null){
			tag.putString("FCL:SIH", sign_interaction_hook.getID().toString());
			sign_interaction_hook.writeNBT(tag);
		}
	}
	
	public void sendSIHUpdate(){
		PlayerStream.around(world, pos, 256).forEach(player -> {
			((ServerPlayerEntity)player).networkHandler.sendPacket(this.toUpdatePacket());
		});
	}

}
