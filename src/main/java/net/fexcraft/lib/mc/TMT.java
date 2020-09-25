package net.fexcraft.lib.mc;

import net.fexcraft.lib.mc.network.SimpleUpdateHandler;
import net.fexcraft.lib.mc.utils.Formatter;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * Placeholder class to register "TMT" as Mod, just for looking nice.
 */
@Mod(modid = TMT.MODID, name = "(Fex's) Turbo Model Thingy", version = TMT.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", dependencies = "required-after:fcl")
public class TMT {
	
	@Mod.Instance("tmt")
	private static TMT INSTANCE;
	public static final String MODID = "tmt";
	public static final String VERSION = "1.11";
	
	@Mod.EventHandler
	public void preinit(FMLPreInitializationEvent event){
		SimpleUpdateHandler.register(MODID, 1, VERSION);
		SimpleUpdateHandler.setUpdateMessage(MODID, Formatter.format("&0[&9TMT&0]&7") + " Update available! (" + SimpleUpdateHandler.getLatestVersionOf("tmt") + ")");
	}
	
}