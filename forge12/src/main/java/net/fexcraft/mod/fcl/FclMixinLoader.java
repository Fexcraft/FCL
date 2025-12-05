package net.fexcraft.mod.fcl;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.IEarlyMixinLoader;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@IFMLLoadingPlugin.Name("FCL Mixin Loader")
@IFMLLoadingPlugin.MCVersion("1.12.2")
@IFMLLoadingPlugin.TransformerExclusions("net.fexcraft.mod.fcl")
@IFMLLoadingPlugin.SortingIndex(1996)
@IFMLLoadingPlugin.DependsOn("zone.rong.mixinbooter.MixinBooterPlugin")
public class FclMixinLoader implements IFMLLoadingPlugin, IEarlyMixinLoader {

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

	}

	@Override
	public String getAccessTransformerClass(){
		return null;
	}

	@Override
	public List<String> getMixinConfigs(){
		return Arrays.asList(new String[]{"fcl.mixins.json"});
	}

}
