package net.fexcraft.mod.lib.util.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.fexcraft.mod.lib.util.math.Time;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Print{
	
	private static final Logger logger = LogManager.getLogger("FCL");
	
	public static void log(Object obj){
		if(obj instanceof Iterable){
			Iterable inte = (Iterable)obj;
			logger.info("ITERABLE: {");
			for(Object object : inte){
				logger.info("    " + (object == null ? ">> IS null;" : String.valueOf(object)));
			}
			logger.info("}");
			return;
		}
		logger.info(obj == null ? ">> IS null;" : String.valueOf(obj));
	}
	
	public static void log(Object prefix, Object obj){
		logger.info("[" + String.valueOf(prefix) + "]> " + String.valueOf(obj));
	}
	
	public static void chat(ICommandSender sender, Object obj){
		sender.sendMessage(new TextComponentString("[DEBUG]: " + obj.toString()));
	}
	
	public static void chat(ICommandSender sender, String string){
		sender.sendMessage(new TextComponentString(Formatter.format(string)));
	}
	
	public static void link(ICommandSender sender, String string, String link){
		TextComponentString textcomponent = new TextComponentString(Formatter.format(string));
		textcomponent.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link == null ? string : link));
		sender.sendMessage(textcomponent);
	}
	
	private static int sec = -1;
	
	public static void logOAS(String string){
		if(Time.getSecond() != sec){
			sec = Time.getSecond();
			logger.info(string);
		}
	}

	public static void spam(int l, String string) {
		for(int i = 0; i < l; i++){
			logger.error(string);
		}
	}

	public static void debug(Object obj) {
		if(Static.dev()){
			log(obj);
		}
	}

	public static void debug(Object... objs){
		if(Static.dev()){
			String str = "[";
			for(int i = 0; i < objs.length; i++){
				str += objs[i] == null ? ">> IS null;" : String.valueOf(objs[i]) + (i == objs.length - 1 ? "" : ", ");
			}
			log(str + "]");
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void debugChat(String string){
		if(Static.dev()){
			Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
		}
	}
	
}