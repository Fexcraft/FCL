package net.fexcraft.lib.mc.mixin;

import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public class ClientMixin {
	
	@Inject(at = @At("HEAD"), method = "init()V")
	private void init(CallbackInfo info){
		Print.log("Registering Client Packets"); PacketHandler.registerPackets(false);
	}
	
}
