package net.fexcraft.mod.fcl;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.fexcraft.mod.uni.inv.UniStack;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCL implements ModInitializer {

	private static ConcurrentHashMap<String, TagKey<Item>> tagkeys = new ConcurrentHashMap<>();
	public static final Logger LOGGER = LoggerFactory.getLogger("fcl");
	public static UniFCL CONFIG;

	@Override
	public void onInitialize(){
		FCL20.MAINDIR = FabricLoader.getInstance().getGameDirectory();
		FCL20.init(FabricLoader.getInstance().isDevelopmentEnvironment());
		CONFIG = new UniFCL(new File(FCL20.MAINDIR, "/config/"));
		//
		UniFCL.registerFCLUI(this);
		UniStack.TAG_GETTER = key -> {
			ArrayList<StackWrapper> list = new ArrayList<>();
			if(!tagkeys.containsKey(key)){
				tagkeys.put(key, TagKey.create(Registries.ITEM, new ResourceLocation(key)));
			}
			var tags = BuiltInRegistries.ITEM.getTagOrEmpty(tagkeys.get(key));
			for(Holder<Item> item : tags){
				list.add(UniStack.createStack(new ItemStack(item)));
			}
			return list;
		};
	}

}