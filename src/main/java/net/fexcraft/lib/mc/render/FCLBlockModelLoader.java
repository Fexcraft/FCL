package net.fexcraft.lib.mc.render;

import java.util.*;
import java.util.function.Function;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableMap;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonHandler;
import net.fexcraft.app.json.JsonObject;
import net.fexcraft.lib.common.math.Axis3DL;
import net.fexcraft.lib.common.math.TexturedPolygon;
import net.fexcraft.lib.common.math.TexturedVertex;
import net.fexcraft.lib.common.math.Vec3f;
import net.fexcraft.lib.mc.registry.FCLRegistry;
import net.fexcraft.lib.mc.utils.Static;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.minecraft.block.properties.IProperty;
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
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Somewhat based on the OBJLoader in Forge/FML.
 * 
 * @author Ferdinand Calo' (FEX___96)
 * */
public class FCLBlockModelLoader implements ICustomModelLoader {

	private static final FCLBlockModelLoader INSTANCE = new FCLBlockModelLoader();
	private static final TreeMap<ResourceLocation, FCLBlockModel> MAP = new TreeMap<>();
	private static final TreeMap<ResourceLocation, Model> MODELS = new TreeMap<>();
	private static final TreeMap<ResourceLocation, Model> BAKEDMODELS = new TreeMap<>();

	@Override
	public void onResourceManagerReload(IResourceManager resourcemanager){
		MODELS.clear();
		BAKEDMODELS.clear();
		BakedModel.tempres.clear();
		BakedModel.quads.clear();
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
			if(model != null){
				try{
					MAP.put(modellocation, ((Class<? extends FCLBlockModel>)model).newInstance());
					return true;
				}
				catch(InstantiationException | IllegalAccessException e){
					e.printStackTrace();
					return false;
				}
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
		private HashMap<ResourceLocation, TextureAtlasSprite> textures = new HashMap<>();
		private Collection<ResourceLocation> textur;
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
			blockmodel = MAP.get(rs);
			getTexturesFromModel();
		}

		public Model(Model model, ImmutableMap<String, String> data){
			this.modellocation = model.modellocation;
			this.blockmodel = model.blockmodel;
			customdata = data;
			if(model.customdata != null){
				for(Map.Entry<String, String> entry : model.customdata.entrySet()){
					if(!customdata.containsKey(entry.getKey())){
						customdata.put(entry.getKey(), entry.getValue());
					}
				}
			}
			getTexturesFromModel();
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
			for(ResourceLocation loc : textur) textures.put(loc, func.apply(loc));
			return new BakedModel(modellocation, this, format, blockmodel);
		}

		private void getTexturesFromModel(){
			Collection<ResourceLocation> coll = blockmodel.getTextures(customdata);
			if(coll == null) coll = new ArrayList<>();
			if(customdata != null && customdata.containsKey("textures")){
				JsonArray array = JsonHandler.parse(customdata.get("textures"), false).asArray();
				for(JsonObject<?> elm : array.elements()) coll.add(new ResourceLocation(elm.string_value()));
			}
			if(coll.size() == 0){
				String str = modellocation.toString().replace("models/block", "blocks");
				if(str.contains(".")) str = str.substring(0, str.indexOf("."));
				coll.add(new ResourceLocation(str));
			}
			textur = coll;
		}

		@Override
		public Collection<ResourceLocation> getTextures(){
			return textur;
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

	public static class BakedModel implements IBakedModel {

		private final ResourceLocation modellocation;
		public static TreeMap<String, ResourceLocation> tempres = new TreeMap<>();
		private static HashMap<String, List<BakedQuad>> quads = new HashMap<>();
		private TextureAtlasSprite deftex;
		private VertexFormat format;
		private FCLBlockModel model;
		private Model root;
		//
		private Vec3f translate;
		private float scale = Static.sixteenth;
		private Axis3DL axis, axis1, axis2;

		public BakedModel(ResourceLocation modellocation, Model state, VertexFormat format, FCLBlockModel blockmodel){
			this.modellocation = modellocation;
			this.format = format;
			this.root = state;
			this.model = blockmodel;
			deftex = root.textures.values().toArray(new TextureAtlasSprite[0])[0];
		}

		@Override
		@Nonnull
		public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand){
			String statekey = model.useDefaultCacheKey() ? getStateKey(state) : model.getCacheKey(state, side, root.customdata, rand);
			if(quads.containsKey(statekey)) return quads.get(statekey);
			List<BakedQuad> newquads = new ArrayList<>();
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
				translate.x = root.customdata.containsKey("t-x") ? Float.parseFloat(root.customdata.get("t-x")) : 0;
				translate.y = root.customdata.containsKey("t-y") ? Float.parseFloat(root.customdata.get("t-y")) : 0;
				translate.z = root.customdata.containsKey("t-z") ? Float.parseFloat(root.customdata.get("t-z")) : 0;
				scale = root.customdata.containsKey("scale") ? Float.parseFloat(root.customdata.get("scale")) : Static.sixteenth;
			}
			else axis1.setAngles(180, 180, 0);
			Collection<ModelRendererTurbo> mrts = model.getPolygons(state, side, root.customdata, rand);
			//
			try{
				for(ModelRendererTurbo mrt : mrts){
					TextureAtlasSprite sprite = mrt.texName == null ? deftex : getTex(root, mrt.texName);
					axis.setAngles(-mrt.rotationAngleY, -mrt.rotationAngleZ, -mrt.rotationAngleX);
					for(TexturedPolygon polygon : mrt.getFaces()){
						if(polygon.getVertices().length != 4) continue;
						Vec3f vec0 = new Vec3f(polygon.getVertices()[1].vector.sub(polygon.getVertices()[0].vector));
						Vec3f vec1 = new Vec3f(polygon.getVertices()[1].vector.sub(polygon.getVertices()[2].vector));
						Vec3f vec2 = vec1.cross(vec0).normalize();
						vec2 = axis1.getRelativeVector(axis.getRelativeVector(vec2));
						if(axis2 != null) vec2 = axis2.getRelativeVector(vec2);
						UnpackedBakedQuad.Builder builder = new UnpackedBakedQuad.Builder(format);
						builder.setContractUVs(true);
						builder.setQuadOrientation(EnumFacing.getFacingFromVector(vec2.x, vec2.y, vec2.z));
						builder.setTexture(sprite);
						putVertexData(builder, mrt, polygon.getVertices()[0], vec2, TextureCoordinate.getDefaultUVs()[0], sprite);
						putVertexData(builder, mrt, polygon.getVertices()[1], vec2, TextureCoordinate.getDefaultUVs()[1], sprite);
						putVertexData(builder, mrt, polygon.getVertices()[2], vec2, TextureCoordinate.getDefaultUVs()[2], sprite);
						putVertexData(builder, mrt, polygon.getVertices()[3], vec2, TextureCoordinate.getDefaultUVs()[3], sprite);
						newquads.add(builder.build());
					}
				}
			}
			catch(Throwable thr){
				thr.printStackTrace();
			}
			quads.put(statekey, newquads);
			model.reset(state, side, root.customdata, rand);
			return newquads;
		}

		public static final String getStateKey(IBlockState state){
			String key = state.getBlock().getRegistryName().toString();
			if(state.getPropertyKeys().size() > 0) key += ",";
			Iterator<IProperty<?>> it = state.getPropertyKeys().iterator();
			while(it.hasNext()){
				IProperty<?> prop = it.next();
				key += prop.getName() + "=" + state.getValue(prop);
				if(it.hasNext()) key += ",";
			}
			if(state instanceof IExtendedBlockState){
				IExtendedBlockState ext = (IExtendedBlockState)state;
				if(key.length() > 0 && ext.getUnlistedNames().size() > 0) key += ",";
				Iterator<IUnlistedProperty<?>> ite = ext.getUnlistedNames().iterator();
				while(ite.hasNext()){
					IUnlistedProperty<?> prop = ite.next();
					key += prop.getName() + "=" + ext.getValue(prop);
					if(ite.hasNext()) key += ",";
				}
			}
			return key;
		}

		private TextureAtlasSprite getTex(Model root, String texName){
			if(!tempres.containsKey(texName)){
				tempres.put(texName, new ResourceLocation(texName));
			}
			return root.textures.get(tempres.get(texName));
		}

		private final void putVertexData(Builder builder, ModelRendererTurbo mrt, TexturedVertex vert, Vec3f normal, TextureCoordinate textureinate, TextureAtlasSprite texture){
			for(int e = 0; e < format.getElementCount(); e++){
				switch(format.getElement(e).getUsage()){
					case POSITION:
						Vec3f vec = axis.getRelativeVector(vert.vector);
						vec = axis1.getRelativeVector(vec.add(mrt.rotationPointX, mrt.rotationPointY, mrt.rotationPointZ));
						if(axis2 != null) vec = axis2.getRelativeVector(vec);
						builder.put(e, vec.x * scale + translate.x, vec.y * scale + translate.y, vec.z * scale + translate.z, 1);
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
						builder.put(e, normal.x, normal.y, normal.z, 0);
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

	public static FCLBlockModelLoader getInstance(){
		return INSTANCE;
	}

}