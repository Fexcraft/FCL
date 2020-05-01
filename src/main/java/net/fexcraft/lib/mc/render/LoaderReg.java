package net.fexcraft.lib.mc.render;

public class LoaderReg {
	
	public static void ister(){
		net.minecraftforge.client.model.ModelLoaderRegistry.registerLoader(net.fexcraft.lib.mc.render.FCLItemModelLoader.getInstance());
		net.minecraftforge.client.model.ModelLoaderRegistry.registerLoader(net.fexcraft.lib.mc.render.FCLBlockModelLoader.getInstance());
	}

}
