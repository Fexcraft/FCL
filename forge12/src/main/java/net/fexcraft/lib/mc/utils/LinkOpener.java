package net.fexcraft.lib.mc.utils;

public class LinkOpener {

	public static void open(Object root, String url){
		try{
			net.minecraft.client.Minecraft.getMinecraft().displayGuiScreen(
				new net.minecraft.client.gui.GuiConfirmOpenLink((net.minecraft.client.gui.inventory.GuiContainer)root, url, 31102009, true));
		}
		catch(Throwable e){
			e.printStackTrace();
		}
	}

}
