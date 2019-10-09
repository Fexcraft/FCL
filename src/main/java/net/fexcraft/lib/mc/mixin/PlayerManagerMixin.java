package net.fexcraft.lib.mc.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.network.ClientConnection;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;

@Mixin(PlayerManager.class)
public class PlayerManagerMixin {
	
	@Inject(at = @At("RETURN"), method = "onPlayerConnect()V")
	private void init(ClientConnection conn, ServerPlayerEntity player, CallbackInfo info){
		Print.debug("Checking Player: " + player.getDisplayName().asString()); SimpleUpdateHandler.checkPlayer(player);
	}

}
