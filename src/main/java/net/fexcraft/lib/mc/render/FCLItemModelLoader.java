package net.fexcraft.lib.mc.render;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ItemOverrideList;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

public class FCLItemModelLoader implements ICustomModelLoader {
	
	public static final FCLItemModelLoader INSTANCE = new FCLItemModelLoader();
	private static final TreeMap<ResourceLocation, FCLItemModel> MAP = new TreeMap<ResourceLocation, FCLItemModel>();
	//private static final ArrayList<String> DOMAINS = new ArrayList<String>();
	
	public FCLItemModelLoader(){}

	@Override
	public void onResourceManagerReload(IResourceManager resmag){
		//Sadly, nothing to reload here? Maybe could make the models execute an reload method? Idk.
		//Well, let's add it.
		MAP.forEach((key, value) -> { value.onResourceManagerReload(resmag); });
	}
	
	public static final ICustomModelLoader getInstance(){
		return INSTANCE;
	}
	
	@Nullable
	public static final Object addItemModel(ResourceLocation loc, FCLItemModel model){
		if(!loc.toString().contains(":models/item/")){
			ResourceLocation rs = new ResourceLocation(loc.getResourceDomain(), "models/item/" + loc.getResourcePath());
			return MAP.put(rs, model);//This specifically a fix for FVTM, but if it works with other mods well, I don't know.
		}
		return MAP.put(loc, model);
	}
	
	@Nullable
	public FCLItemModel getItemModel(ResourceLocation modelloc){
		return MAP.get(modelloc);
	}

	@Override
	public boolean accepts(ResourceLocation modelloc){
		return /*DOMAINS.contains(modelloc.getResourceDomain()) ||*/ MAP.containsKey(modelloc);
	}

	@Override
	public IModel loadModel(ResourceLocation modelloc) throws Exception {
		return new Model(modelloc);
	}
	
	private static class Model implements IModel {
		
		private final ResourceLocation modelloc;
		
		private Model(ResourceLocation rs){
			this.modelloc = rs;
		}
		
		@Override
		public Collection<ResourceLocation> getDependencies(){
			return Collections.emptyList();
		}

		@Override
		public IModelState getDefaultState(){
			return new ModelState();
		}

		@Override
		public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> func){
			return new BakedModel(modelloc, func, new ModelState());
		}

		@Override
		public Collection<ResourceLocation> getTextures(){
			return Collections.singleton(modelloc);
		}

	}
	
	private static class BakedModel implements IBakedModel {

		private final ModelState state;
		private final ResourceLocation modelloc;

		private BakedModel(ResourceLocation modelLocation, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter, ModelState state){
			this.modelloc = modelLocation;
			this.state = state;
		}

		@Override @Nonnull
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
			return Collections.emptyList();
		}

		@Override @Nonnull
		public ItemCameraTransforms getItemCameraTransforms(){
			return ItemCameraTransforms.DEFAULT;
		}
		
		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType perspective){
			FCLItemModel model = FCLItemModelLoader.INSTANCE.getItemModel(modelloc);
			if(model != null){
				model.renderItem(perspective, state.stack, state.entity);
			}
			return Pair.of(this, null);
		}

		@Override
		public boolean isBuiltInRenderer(){
			return true;
		}

		@Override @Nonnull
		public TextureAtlasSprite getParticleTexture(){
			return ModelLoader.White.INSTANCE;
		}

		@Override @Nonnull
		public ItemOverrideList getOverrides(){
			return OverrideList.INSTANCE;
		}

		@Override
		public boolean isGui3d(){
			return true;
		}

		@Override
		public boolean isAmbientOcclusion(){
			return false;
		}

		public ModelState getModelState(){
			return state;
		}
		
	}
	
	private static class ModelState implements IModelState {
		
		private ItemStack stack;
		private EntityLivingBase entity;

		public void setData(ItemStack stack, EntityLivingBase entity, World world){
			this.stack = stack; this.entity = entity;
		}

		@Override
		public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part){
			return Optional.empty();
		}
		
	}
	
	private static class OverrideList extends ItemOverrideList {
		
		private static final OverrideList INSTANCE = new OverrideList();

		private OverrideList(){ super(Collections.emptyList()); }

		@Override
		public IBakedModel handleItemState(IBakedModel org_model, ItemStack stack, World world, EntityLivingBase entity){
			((BakedModel)org_model).getModelState().setData(stack, entity, world);
			return org_model;
		}

	}
	
}