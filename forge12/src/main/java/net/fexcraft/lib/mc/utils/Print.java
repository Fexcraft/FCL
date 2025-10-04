package net.fexcraft.lib.mc.utils;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.zip.Deflater;

import net.fexcraft.lib.common.math.Time;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.uni.EnvInfo;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.util.text.event.HoverEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.appender.RollingFileAppender;
import org.apache.logging.log4j.core.appender.rolling.CompositeTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.DefaultRolloverStrategy;
import org.apache.logging.log4j.core.appender.rolling.SizeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.appender.rolling.TimeBasedTriggeringPolicy;
import org.apache.logging.log4j.core.config.AppenderRef;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.LoggerConfig;
import org.apache.logging.log4j.core.layout.PatternLayout;

import static net.fexcraft.mod.fcl.FCL.LOGGER;

public class Print extends net.fexcraft.lib.common.utils.Print {
	
	public static void log(Object obj){
		if(obj instanceof Iterable){
			Iterable<?> inte = (Iterable<?>)obj;
			LOGGER.info("ITERABLE: {");
			for(Object object : inte){
				LOGGER.info("    " + (object == null ? ">> IS null;" : String.valueOf(object)));
			}
			LOGGER.info("}");
			return;
		}
		if(obj instanceof Throwable){
			LOGGER.info(ExceptionUtils.getStackTrace((Throwable)obj));
		}
		LOGGER.info(obj == null ? "[OBJ IS NULL]" : String.valueOf(obj));
	}
	
	public static void log(Object prefix, Object obj){
		LOGGER.info("[" + String.valueOf(prefix) + "]> " + String.valueOf(obj));
	}
	
	public static void chat(ICommandSender sender, Object obj){
		if(sender == null) log("SENDERNULL||" + obj.toString());
		else sender.sendMessage(new TextComponentTranslation("[DEBUG]: " + obj.toString()));
	}
	
	public static void chat(ICommandSender sender, Throwable obj){
		if(sender == null) log("SENDERNULL||" + obj.toString());
		else sender.sendMessage(new TextComponentTranslation(ExceptionUtils.getStackTrace(obj)));
	}
	
	public static void chat(ICommandSender sender, String string){
		if(sender == null) log("SENDERNULL||" + string);
		else sender.sendMessage(new TextComponentTranslation(Formatter.format(string)));
	}
	
	public static void chatnn(ICommandSender sender, String string){
		if(sender == null) return;
		sender.sendMessage(new TextComponentTranslation(Formatter.format(string)));
	}
	
	public static void bar(EntityPlayer sender, String string){
		sender.sendStatusMessage(new TextComponentTranslation(Formatter.format(string)), true);
	}
	
	public static void link(ICommandSender sender, String string, String link){
		TextComponentString component = new TextComponentString(Formatter.format(string));
		component.getStyle().setClickEvent(new ClickEvent(ClickEvent.Action.OPEN_URL, link = (link == null ? string : link)));
		component.getStyle().setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new TextComponentString(link)));
		sender.sendMessage(component);
	}
	
	private static int sec = -1;
	
	public static void logOAS(String string){
		if(Time.getSecond() != sec){
			sec = Time.getSecond();
			LOGGER.info(string);
		}
	}

	public static void spam(int l, Object obj){
		for(int i = 0; i < l; i++){
			LOGGER.error(obj == null ? ">> IS null;" : String.valueOf(obj));
		}
	}

	public static void debug(Object obj){
		if(EnvInfo.DEV){
			log(obj);
		}
	}
	
	public static <T> T debugR(Object obj){
		if(EnvInfo.DEV){ log(obj); }
		return (T)obj;
	}

	@SafeVarargs
	public static <T> void debug(T... objs){
		if(EnvInfo.DEV){
			String str = "[\n";
			for(int i = 0; i < objs.length; i++){
				str += "\t" + (objs[i] == null ? ":null:" : String.valueOf(objs[i]) + (i == objs.length - 1 ? "" : ", ")) + "\n";
			}
			log(str + "]");
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static void debugChat(String string){
		if(EnvInfo.DEV){
			net.minecraft.client.Minecraft.getMinecraft().player.sendMessage(new TextComponentString(string));
		}
	}
	
	public static void stacktrace(ICommandSender sender, Exception e){
		PrintWriter writer = new PrintWriter(new StringWriter());
		e.printStackTrace(writer);
		chat(sender, writer.toString());
	}
	
	public static void stacktrace(Exception e){
		PrintWriter writer = new PrintWriter(new StringWriter());
		e.printStackTrace(writer);
		log(writer.toString());
	}
	
	public static final Logger getCustomLogger(String cat, String file, String name, String pattern){
		LoggerContext context = (LoggerContext)LogManager.getContext(false);
		final Configuration conf = context.getConfiguration();
		PatternLayout layout = PatternLayout.newBuilder().withPattern(pattern == null ? "%d{dd MMM yyyy HH:mm:ss(SSS)} [%t/%c]: %m%n" : pattern).withConfiguration(conf).build();
		RollingFileAppender appender = RollingFileAppender.newBuilder()
		   	.setConfiguration(conf)
		   	.withFileName("./logs/fcl/" + cat + "/" + file + ".log")
		   	.withFilePattern("./logs/fcl/" + cat + "/" + file + "-%d{yyyy-MM-dd}.%i.log.gz")
		   	.withName(name)
		   	.withAppend(true)
		   	.withImmediateFlush(true)
            .withBufferedIo(true)
            .withBufferSize(8192)
            .withCreateOnDemand(false)
		   	.withLocking(false)
		   	.withLayout(layout)
		   	.withPolicy(CompositeTriggeringPolicy.createPolicy(SizeBasedTriggeringPolicy.createPolicy("256 M"), TimeBasedTriggeringPolicy.createPolicy("1", null)))
		   	.withStrategy(DefaultRolloverStrategy.createStrategy(Integer.MAX_VALUE + "", "1", "max", Deflater.NO_COMPRESSION + "", null, true, conf)).build();
		appender.start();
		conf.addAppender(appender);
	    AppenderRef ref = AppenderRef.createAppenderRef(name, null, null);
	    LoggerConfig logcfg = LoggerConfig.createLogger(true, Level.INFO, name, "true", new AppenderRef[] { ref }, null, conf, null);
	    conf.addLogger(name, logcfg);
	    context.getLogger(name).addAppender(appender);
		context.updateLoggers();
		return context.getLogger(name);
	}

	public static void format(String string, Object... objs){
		Print.log(String.format(string, objs));
	}

	public static void format(ICommandSender sender, String string, Object... objs){
		Print.chat(sender, String.format(string, objs));
	}

	public static void chatbar(ICommandSender sender, String string){
		if(!(sender instanceof EntityPlayer)) chat(sender, string);
		else bar((EntityPlayer)sender, string);
	}
	
}