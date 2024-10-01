package net.fexcraft.mod.fcl;

import net.fabricmc.api.ModInitializer;

import net.fabricmc.loader.api.FabricLoader;
import net.fexcraft.mod.fcl.mixint.SWProvider;
import net.fexcraft.mod.uni.item.ItemWrapper;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.ui.UniCon;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;

import static net.minecraft.world.flag.FeatureFlags.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FCL implements ModInitializer {

	public static final String MOD_ID = "fcl";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static Optional<MenuType<UniCon>> UNIVERSAL;

	@Override
	public void onInitialize(){
		FCL20.MAINDIR = FabricLoader.getInstance().getGameDirectory();
		FCL20.init(FabricLoader.getInstance().isDevelopmentEnvironment(), false);//TODO
		StackWrapper.SUPPLIER = obj -> {
			if(obj instanceof ItemWrapper){
				return StackWrapper.SUPPLIER.apply(new ItemStack((Item)((ItemWrapper)obj).local()));
			}
			if(obj instanceof ItemStack){
				return ((SWProvider)obj).fcl_wrapper();
			};
			return StackWrapper.EMPTY;
		};
		//
		MenuType<?> mt = Registry.register(BuiltInRegistries.MENU, "fcl:universal", (MenuType<?>)new MenuType((i, inv) -> new UniCon(i, inv, null), DEFAULT_FLAGS));
		UNIVERSAL = Optional.of((MenuType<UniCon>)mt);
	}

}