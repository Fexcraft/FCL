package net.fexcraft.mod.uni.ui;

import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.V3I;
import net.fexcraft.lib.mc.utils.Print;
import net.fexcraft.mod.uni.UniEntity;
import net.fexcraft.mod.uni.UniReg;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UIUtils {

	public static JsonMap getMapC(String mod, UIKey key){
		ResourceLocation loc = null;
		try{
			loc = new ResourceLocation(mod, UniReg.MENU_JSON_C.get(key) + ".json");
			return JsonHandler.parse(net.minecraft.client.Minecraft.getMinecraft().getResourceManager().getResource(loc).getInputStream());
		}
		catch(Exception e){
			Print.log("ui-loc: " + loc);
			e.printStackTrace();
			return new JsonMap();
		}
	}

	public static JsonMap getMapS(String mod, UIKey key){
		String loc = null;
		try{
			loc = UniReg.MENU_JSON_S.get(key) + ".json";
			return JsonHandler.parse(UniReg.getInst(mod).getClass().getClassLoader().getResourceAsStream(loc));
		}
		catch(Exception e){
			Print.log("ui-loc: " + loc);
			e.printStackTrace();
			return new JsonMap();
		}
	}

	public static Object getClient(String mod, int id, EntityPlayer player, int x, int y, int z){
		UIKey key = UIKey.find(mod, id);
		JsonMap map = getMapC(mod, key);
		try{
			ContainerInterface con = UniReg.MENU.get(key).getConstructor(JsonMap.class, UniEntity.class, V3I.class).newInstance(map, UniEntity.get(player), new V3I(x, y, z));
			return new UniUI(UniReg.GUI.get(key).getConstructor(JsonMap.class, ContainerInterface.class).newInstance(map, con), player);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

	public static Object getServer(String mod, int id, EntityPlayer player, int x, int y, int z){
		UIKey key = UIKey.find(mod, id);
		JsonMap map = getMapS(mod, key);
		try{
			return new UniCon(UniReg.MENU.get(key).getConstructor(JsonMap.class, UniEntity.class, V3I.class).newInstance(map, UniEntity.get(player), new V3I(x, y, z)), player);
		}
		catch(Exception e){
			e.printStackTrace();
			return null;
		}
	}

}
