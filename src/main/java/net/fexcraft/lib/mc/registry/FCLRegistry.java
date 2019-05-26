package net.fexcraft.lib.mc.registry;

import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import javax.annotation.Nullable;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.mc.FCL;
import net.fexcraft.lib.mc.api.registry.fBlock;
import net.fexcraft.lib.mc.api.registry.fCommand;
import net.fexcraft.lib.mc.api.registry.fEntity;
import net.fexcraft.lib.mc.api.registry.fItem;
import net.fexcraft.lib.mc.api.registry.fModel;
import net.fexcraft.lib.mc.api.registry.fRecipeHolder;
import net.fexcraft.lib.mc.api.registry.fTESR;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.command.CommandBase;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.discovery.ASMDataTable;
import net.minecraftforge.fml.common.discovery.ASMDataTable.ASMData;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.registries.IForgeRegistry;

public class FCLRegistry {
	
	private static final String block = fBlock.class.getCanonicalName();
	private static final String item = fItem.class.getCanonicalName();
	private static final String entity = fEntity.class.getCanonicalName();
	private static final String model = fModel.class.getCanonicalName();
	private static final String tesr = fTESR.class.getCanonicalName();
	private static final String recipeholder = fRecipeHolder.class.getCanonicalName();
	private static ASMDataTable table;
	private static int eid = 0;
	//
	private static final Map<String, AutoRegisterer> regs = new TreeMap<String, AutoRegisterer>();
	//private static Map<ResourceLocation, Entity> entities = new TreeMap<ResourceLocation, Entity>();
	private static Map<ResourceLocation, Object> models = new TreeMap<ResourceLocation, Object>();

	public static void prepare(ASMDataTable asmData){
		table = asmData; new AutoRegisterer("fcl");
		scanForModels(); registerTESRs();
	}
	
	private static final void error(Throwable thr, String s){
		Print.log("Error while constructing " + s + "!");
		thr.printStackTrace(); //Uncomment for bug-hunting > Static.stop();
	}
	
	public static final void clear(FMLPostInitializationEvent event){
		//
	}
	
	public static final class AutoRegisterer {
		
		private final String modid;
		private TreeMap<ResourceLocation, Block> blocks = new TreeMap<ResourceLocation, Block>();
		private TreeMap<ResourceLocation, Item> items = new TreeMap<ResourceLocation, Item>();
		private ArrayList<ItemBlock> itemblocks = new ArrayList<ItemBlock>();
		private TreeMap<ResourceLocation, Integer> meta = new TreeMap<ResourceLocation, Integer>();
		private TreeMap<ResourceLocation, String[]> arr = new TreeMap<ResourceLocation, String[]>();
		private TreeMap<ResourceLocation, IRecipe> recipes = new TreeMap<ResourceLocation, IRecipe>();
		
		public AutoRegisterer(String mod){
			regs.put(modid = mod, this); MinecraftForge.EVENT_BUS.register(this);
			//
			TreeMap<String, Class<? extends Block>> mapb = getBlockMap(modid);
			for(Class<? extends Block> clazz : mapb.values()){
				try{
					//Block
					fBlock block = clazz.getAnnotation(fBlock.class);
					Block mBlock = clazz.newInstance();
					Print.log(block.modid() + " | " + block.name());
					mBlock.setRegistryName(block.modid(), block.name());
					mBlock.setUnlocalizedName(mBlock.getRegistryName().toString());
					blocks.put(mBlock.getRegistryName(), mBlock);
					//Item
					ItemBlock iblock = block.item().getConstructor(Block.class).newInstance(mBlock);
					iblock.setRegistryName(mBlock.getRegistryName());
					iblock.setUnlocalizedName(mBlock.getUnlocalizedName());
					if(iblock instanceof ItemBlock16){
						((ItemBlock16)iblock).setItemBurnTime(block.burn_time());
					}
					itemblocks.add(iblock);
					if(block.variants() > 1){
						meta.put(mBlock.getRegistryName(), block.variants());
					}
					if(block.custom_variants().length > 0){
						arr.put(mBlock.getRegistryName(), block.custom_variants());
					}
					//TileEntity
					if(mBlock instanceof ITileEntityProvider){
						try{ GameRegistry.registerTileEntity(block.tileentity(), mBlock.getRegistryName()); }
						catch(IllegalArgumentException e){
							if(Static.dev()){
								e.printStackTrace();
								if(e.getMessage() == null || !e.getMessage().equals("value already present: class net.minecraft.tileentity.TileEntity")){
									Static.halt();
								}
							}
						}
					}
					Print.debug("Registered Block: " + mBlock.getRegistryName().toString());
				}
				catch(Exception e){ error(e, clazz.getName()); }
			}
			//
			TreeMap<String, Class<? extends Item>> mapi = getItemMap(modid);
			for(Class<? extends Item> clazz : mapi.values()){
				try{
					fItem item = clazz.getAnnotation(fItem.class);
					Item mItem = clazz.newInstance();
					mItem.setRegistryName(item.modid(), item.name());
					mItem.setUnlocalizedName(mItem.getRegistryName().toString());
					items.put(mItem.getRegistryName(), mItem);
					if(item.variants() > 1){
						meta.put(mItem.getRegistryName(), item.variants());
					}
					if(item.custom_variants().length > 0){
						arr.put(mItem.getRegistryName(), item.custom_variants());
					}
					//GameRegistry.register(mItem);
					//registerItemModelLocation(mItem, item.variants(), item.custom_variants());
					Print.debug("Registered Item: " + mItem.getRegistryName().toString());
				}
				catch(Exception e){ error(e, clazz.getName()); }
			}
		}
		
		@SubscribeEvent
		public void registerBlocks(RegistryEvent.Register<Block> event){
			IForgeRegistry<Block> reg = event.getRegistry();
			for(Block block : blocks.values()){
				reg.register(block);
			}
		}
		
		@SubscribeEvent
		public void registerItems(RegistryEvent.Register<Item> event){
			IForgeRegistry<Item> reg = event.getRegistry();
			//
			for(Item item : items.values()){
				reg.register(item); this.registerModelLoc(item);
			}
			//
			for(ItemBlock item : itemblocks){
				reg.register(item); this.registerModelLoc(item);
			}
		}
		
		@SubscribeEvent
		public void registerRecipes(RegistryEvent.Register<IRecipe> event){
			Set<ASMData> data = table.getAll(recipeholder);
			for(ASMData entry : data){
				try{
					Class<?> clazz = Class.forName(entry.getClassName());
					fRecipeHolder rph = (fRecipeHolder)clazz.getAnnotation(fRecipeHolder.class);
					if(rph.value().equals(modid)){ clazz.newInstance(); }
				} catch(Exception e){ e.printStackTrace(); }
			}
			//
			IForgeRegistry<IRecipe> reg = event.getRegistry();
			for(Entry<ResourceLocation, IRecipe> entry : recipes.entrySet()){
				try{
					entry.getValue().setRegistryName(entry.getKey());
					reg.register(entry.getValue());
				}
				catch(Exception e){
					IRecipe recipe = entry.getValue();
					Print.debug(recipe.getRegistryName() + " | " + recipe.getRecipeOutput().toString());
					for(Ingredient ing : recipe.getIngredients()){
						for(ItemStack stack : ing.getMatchingStacks()){
							Print.log(stack.toString());
						}
					}
					e.printStackTrace();
					continue;
				}
			}
		}
		
		private final void registerModelLoc(Item item){
			if(FCL.getSide().isServer()){ return; }
			if(this.meta.get(item.getRegistryName()) != null){
				int meta = this.meta.get(item.getRegistryName());
				if(this.arr.get(item.getRegistryName()) == null){
					for(int i = 0; i < meta; i++){
						net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, i, new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName() + "_" + i, "inventory"));
					}
				}
				else{
					String[] arr = this.arr.get(item.getRegistryName());
					for(int i = 0; i < meta; i++){
						net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, i, new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName() + "_" + arr[i], "inventory"));
					}
				}
			}
			else{
				net.minecraftforge.client.model.ModelLoader.setCustomModelResourceLocation(item, 0, new net.minecraft.client.renderer.block.model.ModelResourceLocation(item.getRegistryName(), "inventory"));
			}
		}
		
		public void addBlock(String name, Block block, Class<? extends ItemBlock> item, int meta, String[] custom){
			block.setRegistryName(modid, name);
			block.setUnlocalizedName(block.getRegistryName().toString());
			blocks.put(new ResourceLocation(modid, name), block);
			if(item == null){
				ItemBlock iblock = new ItemBlock16(block);
				iblock.setRegistryName(block.getRegistryName());
				iblock.setUnlocalizedName(block.getUnlocalizedName());
				itemblocks.add(iblock);
			}
			else{
				try{ itemblocks.add(item.getConstructor(Block.class).newInstance(block)); }
				catch(Exception e){ e.printStackTrace(); }
			}
			if(meta > 1){ this.meta.put(block.getRegistryName(), meta); }
			if(custom != null){ this.arr.put(block.getRegistryName(), custom); }
		}
		
		public void addItem(String name, Item item, int meta, String[] custom){
			item.setRegistryName(modid, name);
			item.setUnlocalizedName(item.getRegistryName().toString());
			items.put(new ResourceLocation(modid, name), item);
			if(meta > 1){ this.meta.put(item.getRegistryName(), meta); }
			if(custom != null){ this.arr.put(item.getRegistryName(), custom); }
		}
		
		public void addRecipe(ResourceLocation rs, IRecipe recipe){
			recipes.put(rs, recipe);
		}
		
	}
	
	public static final TreeMap<String, Class<? extends Block>> getBlockMap(String modid){
		Set<ASMData> data = table.getAll(block);
		TreeMap<String, Class<? extends Block>> map = new TreeMap<String, Class<? extends Block>>();
		for(ASMData entry : data){
			try{ @SuppressWarnings("unchecked")
				Class<? extends Block> clazz = (Class<? extends Block>)Class.forName(entry.getClassName());
				fBlock blk = (fBlock)clazz.getAnnotation(fBlock.class);
				if(blk.modid().equals(modid)){ map.put(blk.name(), clazz); }
			} catch(Exception e){ e.printStackTrace(); }
		} return map;
	}
	
	public static final TreeMap<String, Class<? extends Item>> getItemMap(String modid){
		Set<ASMData> data = table.getAll(item);
		TreeMap<String, Class<? extends Item>> map = new TreeMap<String, Class<? extends Item>>();
		for(ASMData entry : data){
			try{ @SuppressWarnings("unchecked")
				Class<? extends Item> clazz = (Class<? extends Item>)Class.forName(entry.getClassName());
				fItem item = (fItem)clazz.getAnnotation(fItem.class);
				if(item.modid().equals(modid)){
					map.put(item.name(), clazz);
				}
			} catch(Exception e){ e.printStackTrace(); }
		} return map;
	}
	
	//@EventBusSubscriber
	
	public static void registerCommands(FMLServerStartingEvent event){
		Set<ASMData> data = table.getAll(fCommand.class.getCanonicalName());
		for(ASMData entry : data){
			try{ @SuppressWarnings("unchecked")
				Class<? extends CommandBase> cmd = (Class<? extends CommandBase>)Class.forName(entry.getClassName());
				event.registerServerCommand(cmd.newInstance());
			} catch(Exception e){ error(e, entry.getClassName()); }
		}
	}
	
	public static Item getItem(String string){
		return getItem(new ResourceLocation(string));
	}

	public static Item getItem(ResourceLocation rs){
		if(regs.get(rs.getResourceDomain()) != null){
			AutoRegisterer reg = regs.get(rs.getResourceDomain());
			Item item = reg.items.get(rs);
			if(item == null){
				for(ItemBlock iblock : reg.itemblocks){
					if(iblock.getRegistryName().equals(rs)) item = iblock; break;
				}
			} return item;
		} return null;
	}
	
	public static Block getBlock(String string){
		return getBlock(new ResourceLocation(string));
	}

	public static Block getBlock(ResourceLocation rs){
		return regs.get(rs.getResourceDomain()) == null ? null : regs.get(rs.getResourceDomain()).blocks.get(rs);
	}
	
	public static void registerEntitiesOf(String modid){
		Set<ASMData> data = table.getAll(entity);
		TreeMap<String, Class<? extends Entity>> map = new TreeMap<String, Class<? extends Entity>>();
		for(ASMData entry : data){
			try{ @SuppressWarnings("unchecked")
				Class<? extends Entity> clazz = (Class<? extends Entity>)Class.forName(entry.getClassName());
				if(clazz.getAnnotation(fEntity.class).modid().equals(modid)){ map.put(clazz.getAnnotation(fEntity.class).name(), clazz); }
			} catch(Exception e){ e.printStackTrace(); }
		}
		for(Class<? extends Entity> clazz : map.values()){
			try{
				fEntity entity = clazz.getAnnotation(fEntity.class);
				ResourceLocation rs = new ResourceLocation(entity.modid(), entity.name());
				EntityRegistry.registerModEntity(rs, clazz, rs.toString(), eid++, entity.modid(), entity.tracking_range(), entity.update_frequency(), entity.send_velocity_updates());
				Print.debug("Registered Entity: " + rs.toString());
			} catch(Exception e){ error(e, clazz.getName()); }
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void registerTESRs(){
		if(FCL.getSide().isServer()){ return; }
		Set<ASMData> data = table.getAll(tesr);
		for(ASMData entry : data){
			try{
				net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer cTESR = (net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer)Class.forName(entry.getClassName()).newInstance();
				net.minecraftforge.fml.client.registry.ClientRegistry.bindTileEntitySpecialRenderer((Class<? extends TileEntity>)((ParameterizedType)cTESR.getClass().getGenericSuperclass()).getActualTypeArguments()[0], cTESR);
			} catch(Exception e){ continue; }
		}
	}
	
	public static void registerEntityManually(String modid, String name, Class<? extends Entity> clazz, int id, int tracking_range, int update_frequency, boolean send_velocity_updates){
		ResourceLocation rs = new ResourceLocation(modid, name);
		EntityRegistry.registerModEntity(rs, clazz, rs.toString(), eid++, modid, tracking_range, update_frequency, send_velocity_updates);
		Print.debug("Registered Entity: " + rs.toString());
	}
	
	public static void scanForModels(){
		Set<ASMData> data = table.getAll(model);
		for(ASMData entry : data){
			try{
				Class<?> clazz = Class.forName(entry.getClassName()); fModel model = clazz.getAnnotation(fModel.class);
				models.put(new ResourceLocation(model.registryname()), clazz.newInstance());
				Print.debug("Registered Model: " + model.registryname());
			} catch(Throwable e){ error(e, entry.getClassName()); }
		}
	}
	
	@SuppressWarnings("unchecked") @Nullable
	public static <T> T getModel(ResourceLocation loc){
		return (T)models.get(loc);
	}
	
	public static <T> T getModel(String rs){
		return getModel(new ResourceLocation(rs));
	}
	
	public static boolean addModelManually(String resloc, Object model, boolean override){
		return addModelManually(new ResourceLocation(resloc), model, override);
	}
	
	public static boolean addModelManually(ResourceLocation loc, Object model, boolean override){
		if(models.containsKey(loc) && !override){
			Print.format("Tried to register Model with RS[%s] and Class[%s] but key already exists, try setting override to true?", loc, model); return false;
		} else models.put(loc, model); return true;
	}
	
	public static Material getMaterial(String material){
		switch(material){
			case "air": return Material.AIR;
			case "grass": return Material.GRASS;
			case "ground": return Material.GROUND;
			case "wood": return Material.WOOD;
			case "rock": return Material.ROCK;
			case "iron": return Material.IRON;
			case "anvil": return Material.ANVIL;
			case "water": return Material.WATER;
			case "lava": return Material.LAVA;
			case "leaves": return Material.LEAVES;
			case "plants": return Material.PLANTS;
			case "vine": return Material.VINE;
			case "sponge": return Material.SPONGE;
			case "cloth": return Material.CLOTH;
			case "fire": return Material.FIRE;
			case "sand": return Material.SAND;
			case "circuits": return Material.CIRCUITS;
			case "carpet": return Material.CARPET;
			case "glass": return Material.GLASS;
			case "redstone_light": return Material.REDSTONE_LIGHT;
			case "tnt": return Material.TNT;
			case "coral": return Material.CORAL;
			case "ice": return Material.ICE;
			case "packed_ice": return Material.PACKED_ICE;
			case "snow": return Material.SNOW;
			case "crafted_snow": return Material.CRAFTED_SNOW;
			case "cactus": return Material.CACTUS;
			case "clay": return Material.CLAY;
			case "gourd": return Material.GOURD;
			case "dragon_egg": return Material.DRAGON_EGG;
			case "portal": return Material.PORTAL;
			case "cake": return Material.CAKE;
			case "web": return Material.WEB;
			default: return Material.AIR;
		}
	}

	public static AutoRegisterer newAutoRegistry(String modid){
		return new AutoRegisterer(modid);
	}

	public static AutoRegisterer getAutoRegisterer(String id){
		return regs.get(id);
	}

	public static AutoRegisterer getAutoRegistry(String id){
		return regs.get(id);
	}
	
}