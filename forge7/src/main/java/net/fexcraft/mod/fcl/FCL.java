package net.fexcraft.mod.fcl;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import net.fexcraft.mod.uni.EnvInfo;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.impl.TagCWI;
import net.fexcraft.mod.uni.impl.TagLWI;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.tag.TagLW;
import net.minecraft.launchwrapper.Launch;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@Mod(modid = FCL.MODID, version = FCL.VERSION)
public class FCL {

    public static final String MODID = "fcl";
    public static final String VERSION = "7.x";

    @EventHandler
    public void init(FMLInitializationEvent event){
		EnvInfo.CLIENT = event.getSide().isClient();
		EnvInfo.DEV = (Boolean)Launch.blackboard.get("fml.deobfuscatedEnvironment");
		UniReg.LOADER_VERSION = "1.7";
		TagCW.SUPPLIER[0] = () -> new TagCWI();
		TagCW.WRAPPER[0] = obj -> new TagCWI(obj);
		TagLW.SUPPLIER[0] = () -> new TagLWI();
		TagLW.WRAPPER[0] = obj -> new TagLWI(obj);
    }

}
