package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.FCL20;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(MinecraftServer.class)
public class ServerMixin {

	@Inject(at = @At("HEAD"), method = "Lnet/minecraft/server/MinecraftServer;spin(Ljava/util/function/Function;)Lnet/minecraft/server/MinecraftServer;")
	private void spin(Function<?, ?> func, CallbackInfo info) {
		FCL20.SERVER = () -> (MinecraftServer)(Object)this;
	}

}