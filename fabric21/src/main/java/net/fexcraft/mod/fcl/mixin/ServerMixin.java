package net.fexcraft.mod.fcl.mixin;

import net.fexcraft.mod.fcl.FCL;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mixin(MinecraftServer.class)
public class ServerMixin {

	@Inject(at = @At("RETURN"), method = "Lnet/minecraft/server/MinecraftServer;spin(Ljava/util/function/Function;)Lnet/minecraft/server/MinecraftServer;")
	private static void spin(Function<?, ?> func, CallbackInfoReturnable<MinecraftServer> info){
		FCL.SERVER = () -> (MinecraftServer)info.getReturnValue();
	}

}