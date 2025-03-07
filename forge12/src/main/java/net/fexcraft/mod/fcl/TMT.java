package net.fexcraft.mod.fcl;

import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * Placeholder class to register "TMT" as Mod, just for looking nice.
 */
@Mod(modid = TMT.MODID, name = "(Fex's) Turbo Model Thingy", version = TMT.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", dependencies = "required-after:fcl")
public class TMT {
	
	@Mod.Instance("net/fexcraft/lib/tmt")
	private static TMT INSTANCE;
	public static final String MODID = "net/fexcraft/lib/tmt";
	public static final String VERSION = "1.15";
	
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event){
		SimpleUpdateHandler.register(MODID, 1, VERSION);
		SimpleUpdateHandler.setUpdateMessage(MODID, Formatter.format("&0[&9TMT&0]&7") + " Update available! (" + SimpleUpdateHandler.getLatestVersionOf("net/fexcraft/lib/tmt") + ")");
	}
	
}