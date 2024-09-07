package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.tag.TagCW;
import net.fexcraft.mod.uni.world.EntityW;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class ContainerInterface {

	public static BiConsumer<TagCW, UniEntity> SEND_TO_CLIENT;
	public static Consumer<TagCW> SEND_TO_SERVER;
	public static Function<String, String> TRANSLATOR;
	public static BiFunction<String, Object[], String> TRANSFORMAT;
	public UserInterface ui;//client side only
	public Object root;
	public UIKey uiid;
	public UniEntity player;
	public JsonMap ui_map;
	public V3I pos;

	public ContainerInterface(JsonMap map, UniEntity ply, V3I pos){
		ui_map = map;
		player = ply;
		this.pos = pos;
	}

	public void init(){}

	public Object get(String key, Object... objs){
		return null;
	}

	public void packet(TagCW com, boolean client){}

	public ContainerInterface set(UserInterface ui){
		this.ui = ui;
		return this;
	}

	public void onClosed(){}

	public void update(Object localcon){}

	public static String transformat(String str, Object... objs){
		return TRANSFORMAT.apply(str, objs);
	}

	public static String translate(String str){
		return TRANSLATOR.apply(str);
	}

}
