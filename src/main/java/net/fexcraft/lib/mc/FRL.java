package net.fexcraft.lib.mc;

import net.minecraftforge.fml.common.Mod;

/**
 * @author Ferdinand Calo' (FEX___96)
 * 
 * Placeholder class to register "FRL" as Mod, just for looking nice.
 */
@Mod(modid = FRL.MODID, name = "Fex's Render Library", version = FRL.VERSION, clientSideOnly = true, acceptableRemoteVersions = "*", acceptedMinecraftVersions = "*", dependencies = "required-after:fcl")
public class FRL {
	
	@Mod.Instance("net/fexcraft/lib/frl")
	private static FRL INSTANCE;
	public static final String MODID = "net/fexcraft/lib/frl";
	public static final String VERSION = "1.2";
	
}