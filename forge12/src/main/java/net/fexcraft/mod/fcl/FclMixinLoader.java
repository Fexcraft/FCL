package net.fexcraft.mod.fcl;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixins;

import javax.annotation.Nullable;
import java.util.Map;

@IFMLLoadingPlugin.Name("FCL Mixin Loader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("net.fexcraft.mod.fcl")
@IFMLLoadingPlugin.SortingIndex(1996)
public class FclMixinLoader implements IFMLLoadingPlugin {

	public static final Logger LOGGER = LogManager.getLogger("FCL-MIXIN");

	@Override
	public String[] getASMTransformerClass(){
		return new String[0];
	}

	@Override
	public String getModContainerClass(){
		return null;
	}

	@Nullable
	@Override
	public String getSetupClass(){
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data){
		LOGGER.info("Inserting FCL mixins.json");
		Mixins.addConfiguration("fcl.mixins.json");
	}

	@Override
	public String getAccessTransformerClass(){
		return null;
	}

}
