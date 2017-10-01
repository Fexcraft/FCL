package net.fexcraft.mod.lib.network;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import net.fexcraft.mod.lib.FCL;
import net.fexcraft.mod.lib.util.common.Print;
import net.minecraft.command.ICommandSender;

public class Browser {

	public static void browse(ICommandSender sender, String url) {
	    Desktop d = Desktop.getDesktop();
	    
	    if(Network.isConnected()){
	    	try {
				d.browse(new URI(url));
			} catch (IOException | URISyntaxException e){
				Print.chat(sender, FCL.prefix + "Error, couldn't open link.");
				e.printStackTrace();
			}
	    }
	    else{
	    	Print.chat(sender, FCL.prefix + "Error, could not check for connection.");
	    }
	}
}