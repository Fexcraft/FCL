package net.fexcraft.mod.lib.util.common;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.minecraft.command.ICommandSender;

public class Log {
	
	private final Logger logger;
	private final String prefix;
	
	public Log(String id){
		this(id, null);
	}
	
	public Log(String id, String pre){
		logger = LogManager.getLogger(id);
		prefix = pre == null ? "&0[&e" + id.toUpperCase() + "&0]&7" : pre;
	}
	
	public void log(Object obj){
		if(obj instanceof Iterable){
			Iterable<?> inte = (Iterable<?>)obj;
			logger.info("ITERABLE: {");
			for(Object object : inte){
				logger.info("    " + (object == null ? ";>> null >>;" : String.valueOf(object)));
			}
			logger.info("}");
			return;
		}
		logger.info(obj == null ? ";>> null >>;" : String.valueOf(obj));
	}

	public void log(Object... objs){
		String str = "[";
		for(int i = 0; i < objs.length; i++){
			str += objs[i] == null ? ";>> null >>;" : String.valueOf(objs[i]) + (i == objs.length - 1 ? "" : ", ");
		}
		log(str + "]");
	}
	
	public void log(Object prefix, Object obj){
		logger.info("[" + String.valueOf(prefix) + "]> " + String.valueOf(obj));
	}

	public void debug(Object obj){
		if(Static.dev()){
			log(obj);
		}
	}
	
	public void debug(Object... objs){
		if(Static.dev()){
			log(objs);
		}
	}
	
	public void chat(ICommandSender sender, Object obj){
		Print.chat(sender, prefix + obj);
	}
	
	public void chat(ICommandSender sender, Object... objs){
		for(Object obj : objs){
			chat(sender, obj);
		}
	}
	
	public void stacktrace(ICommandSender sender, Exception e){
		PrintWriter writer = new PrintWriter(new StringWriter());
		e.printStackTrace(writer);
		chat(sender, writer.toString());
	}
	
	public void stacktrace(Exception e){
		PrintWriter writer = new PrintWriter(new StringWriter());
		e.printStackTrace(writer);
		log(writer.toString());
	}
	
}