package net.fexcraft.lib.mc.render;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import net.fexcraft.lib.common.math.Axis3DL;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
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
import net.minecraftforge.client.model.obj.OBJModel.TextureCoordinate;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad;
import net.minecraftforge.client.model.pipeline.UnpackedBakedQuad.Builder;
import net.minecraftforge.common.model.IModelPart;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 * Somewhat based on the OBJLoader in Forge/FML.
 * 
 * @author Ferdinand Calo' (FEX___96)
 * */
public enum FCLBlockModelLoader implements ICustomModelLoader {
	
	INSTANCE;

	@SuppressWarnings("unused")
	private IResourceManager manager;
	private static final TreeMap<ResourceLocation, FCLBlockModel> MAP = new TreeMap<>();
	private static final TreeMap<ResourceLocation, Model> MODELS = new TreeMap<>();
	private static final TreeMap<ResourceLocation, Model> BAKEDMODELS = new TreeMap<>();

	@Override
	public void onResourceManagerReload(IResourceManager resourcemanager){
		manager = resourcemanager;
		MODELS.clear();
		BAKEDMODELS.clear();
	}

	@Nullable
	public static final Object addBlockModel(ResourceLocation loc, FCLBlockModel model){
		if(!loc.toString().contains(":models/block/")){
			ResourceLocation rs = new ResourceLocation(loc.getNamespace(), "models/block/" + loc.getPath());
			return MAP.put(rs, model);
		}
		return MAP.put(loc, model);
	}

	@Nullable
	public FCLBlockModel getBlockModel(ResourceLocation modellocation){
		return MAP.get(modellocation);
	}

	@Override
	public boolean accepts(ResourceLocation modellocation){
		if(!MAP.containsKey(modellocation)){
			Object model = FCLRegistry.getModel(modellocation);
			if(model != null && model instanceof FCLBlockModel){
				MAP.put(modellocation, (FCLBlockModel)model);
				return true;
			}
			return false;
		}
		return MAP.containsKey(modellocation);
	}

	@Override
	public IModel loadModel(ResourceLocation modellocation) throws Exception {
		return new Model(modellocation);
	}

    @Override
    public String toString(){
        return "[FCL BLOCK MODEL LOADER]";
    }

	private static class Model implements IModel {

		private final ResourceLocation modellocation;
		private FCLBlockModel blockmodel;
		private static final IModelState defstate = new IModelState(){
			@Override
			public Optional<TRSRTransformation> apply(Optional<? extends IModelPart> part){
				return Optional.empty();
			};
		};
		private Map<String, String> customdata;

		private Model(ResourceLocation rs){
			this.modellocation = rs;
			MODELS.put(rs, this);
			blockmodel = FCLRegistry.getModel(rs);
		}

		public Model(Model model, ImmutableMap<String, String> data){
			this.modellocation = model.modellocation;
			this.blockmodel = model.blockmodel;
			customdata = data;
			if(model.customdata == null) return;
			for(Map.Entry<String, String> entry : model.customdata.entrySet()){
				if(!customdata.containsKey(entry.getKey())){
					customdata.put(entry.getKey(), entry.getValue());
				}
			}
		}

		@Override
		public Collection<ResourceLocation> getDependencies(){
			return Collections.emptyList();
		}

		@Override
		public IModelState getDefaultState(){
			return defstate;
		}

		@Override
		public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> func){
			HashMap<ResourceLocation, TextureAtlasSprite> textures = new HashMap<>();
			for(ResourceLocation resloc : getTextures()){
				textures.put(resloc, func.apply(resloc));
			}
			return new BakedModel(modellocation, this, format, blockmodel, textures);
		}

		@Override
		public Collection<ResourceLocation> getTextures(){
			if(blockmodel.getTextures() == null || blockmodel.getTextures().isEmpty()){
				ResourceLocation resloc = new ResourceLocation(modellocation.toString().replace("models/block", "blocks"));
				return Collections.singleton(resloc);
			}
			return blockmodel.getTextures();
		}
		
		@Override
		public IModel process(ImmutableMap<String, String> data){
			if(customdata == null && data != null) return new Model(this, data);
			else if(customdata == null && data.isEmpty()) return this;
			else{
				boolean same = true;
				for(Map.Entry<String, String> entry : data.entrySet()){
					if(customdata.containsKey(entry.getKey()) && customdata.get(entry.getKey()).equals(entry.getValue())) continue;
					same = false; break;
				}
				if(!same){
					return new Model(this, data);
				}
			}
			return this;
	    }

	}

	@SuppressWarnings("unused")
	private static class BakedModel implements IBakedModel {

		private final ResourceLocation modellocation;
		private HashMap<ResourceLocation, TextureAtlasSprite> textures;
		private TextureAtlasSprite deftex;
		private VertexFormat format;
		private List<BakedQuad> quads;
		private FCLBlockModel model;
		private Model root;
		//
		private Vec3f translate;
		private float scale = Static.sixteenth;
		private Axis3DL axis, axis1, axis2;

		public BakedModel(ResourceLocation modellocation, Model state, VertexFormat format, FCLBlockModel blockmodel, HashMap<ResourceLocation, TextureAtlasSprite> textures){
			this.modellocation = modellocation;
			this.format = format;
			this.root = state;
			this.model = blockmodel;
			this.textures = textures;
			deftex = textures.get(new ResourceLocation(modellocation.toString().replace("models/block", "blocks")));
		}

		@Override
		@Nonnull
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
			if(quads != null) return quads;
			quads = new ArrayList<>();
			axis = new Axis3DL();
			axis1 = new Axis3DL();
			axis2 = null;
			translate = new Vec3f();
			if(root.customdata != null && !root.customdata.isEmpty()){
				float x = root.customdata.containsKey("x") ? Float.parseFloat(root.customdata.get("x")) : 0;
				float y = root.customdata.containsKey("y") ? Float.parseFloat(root.customdata.get("y")) : 0;
				float z = root.customdata.containsKey("z") ? Float.parseFloat(root.customdata.get("z")) : 0;
				if(x != 0f || y != 0f || z != 0f){
					axis2 = new Axis3DL();
					axis2.setAngles(y, z, x);
				}
				boolean rectify = root.customdata.containsKey("rectify") ? Boolean.parseBoolean(root.customdata.get("rectify")) : true;
				if(rectify){
					axis1.setAngles(180, 180, 0);
				}
				translate.xCoord = root.customdata.containsKey("t-x") ? Float.parseFloat(root.customdata.get("t-x")) : 0;
				translate.yCoord = root.customdata.containsKey("t-y") ? Float.parseFloat(root.customdata.get("t-y")) : 0;
				translate.zCoord = root.customdata.containsKey("t-z") ? Float.parseFloat(root.customdata.get("t-z")) : 0;
				scale = root.customdata.containsKey("scale") ? Float.parseFloat(root.customdata.get("scale")) : Static.sixteenth;
			}
			else axis1.setAngles(180, 180, 0);
			Collection<ModelRendererTurbo> mrts = model.getPolygons(root.customdata);
			for(ModelRendererTurbo mrt : mrts){
				axis.setAngles(-mrt.rotationAngleY, -mrt.rotationAngleZ, -mrt.rotationAngleX);
				for(TexturedPolygon polygon : mrt.getFaces()){
					if(polygon.getVertices().length != 4) continue;
					Vec3f vec0 = new Vec3f(polygon.getVertices()[1].vector.subtract(polygon.getVertices()[0].vector));
					Vec3f vec1 = new Vec3f(polygon.getVertices()[1].vector.subtract(polygon.getVertices()[2].vector));
					Vec3f vec2 = vec1.crossProduct(vec0).normalize();
					vec2 = axis1.getRelativeVector(axis.getRelativeVector(vec2));
					if(axis2 != null) vec2 = axis2.getRelativeVector(vec2);
					UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
					builder.setContractUVs(true);
					builder.setQuadOrientation(EnumFacing.getFacingFromVector(vec2.xCoord, vec2.yCoord, vec2.zCoord));
					builder.setTexture(deftex);
					putVertexData(builder, mrt, polygon.getVertices()[0], vec2, TextureCoordinate.getDefaultUVs()[0], deftex);
					putVertexData(builder, mrt, polygon.getVertices()[1], vec2, TextureCoordinate.getDefaultUVs()[1], deftex);
					putVertexData(builder, mrt, polygon.getVertices()[2], vec2, TextureCoordinate.getDefaultUVs()[2], deftex);
					putVertexData(builder, mrt, polygon.getVertices()[3], vec2, TextureCoordinate.getDefaultUVs()[3], deftex);
					quads.add(builder.build());
				}
			}
			return quads;
		}

		private final void putVertexData(Builder builder, ModelRendererTurbo mrt, TexturedVertex vert, Vec3f normal, TextureCoordinate textureCoordinate, TextureAtlasSprite texture){
			for(int e = 0; e < format.getElementCount(); e++){
				switch(format.getElement(e).getUsage()){
					case POSITION:
						Vec3f vec = axis.getRelativeVector(vert.vector);
						vec = axis1.getRelativeVector(vec.addVector(mrt.rotationPointX, mrt.rotationPointY, mrt.rotationPointZ));
						if(axis2 != null) vec = axis2.getRelativeVector(vec);
						builder.put(e, vec.xCoord * scale + translate.xCoord, vec.yCoord * scale + translate.yCoord, vec.zCoord * scale + translate.zCoord, 1);
						break;
					case COLOR:
						if(mrt.getColor() != null){
							float[] color = mrt.getColor().toFloatArray();
							builder.put(e, color[0], color[1], color[2], color[3]);
						}
						else builder.put(e, 1, 1, 1, 1);
						break;
					case UV:
						if(!mrt.textured){
							builder.put(e, texture.getInterpolatedU(0), texture.getInterpolatedV(0), 0, 1);
						}
						else builder.put(e, texture.getInterpolatedU(vert.textureX * 16), texture.getInterpolatedV(vert.textureY * 16), 0, 1);
						break;
					case NORMAL:
						builder.put(e, normal.xCoord, normal.yCoord, normal.zCoord, 0);
						break;
					default: builder.put(e);
				}
			}
		}

		@Override
		@Nonnull
		public ItemCameraTransforms getItemCameraTransforms(){
			return ItemCameraTransforms.DEFAULT;
		}

		@Override
		public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType perspective){
			FCLBlockModel model = FCLBlockModelLoader.INSTANCE.getBlockModel(modellocation);
			if(model != null){
				// model.renderItem(perspective, state.stack, state.entity);
			}
			return Pair.of(this, null);
		}

		@Override
		public boolean isBuiltInRenderer(){
			return true;
		}

		@Override
		@Nonnull
		public TextureAtlasSprite getParticleTexture(){
			return ModelLoader.White.INSTANCE;
		}

		@Override
		@Nonnull
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

	}

	private static class OverrideList extends ItemOverrideList {

		private static final OverrideList INSTANCE = new OverrideList();

		private OverrideList(){
			super(Collections.emptyList());
		}

		@Override
		public IBakedModel handleItemState(IBakedModel bakedmodel, ItemStack stack, World world, EntityLivingBase entity){
			return bakedmodel;
		}

	}

}