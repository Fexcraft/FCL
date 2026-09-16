package net.fexcraft.lib.frl;

import net.fexcraft.lib.common.math.V3F;
import net.fexcraft.lib.frl.CompactModel.CompactGroup;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

/**
 * Originally BEOModelLoader in FVTM.
 * All rights reserved. Only to be distributed within authorized mods.
 *
 * @author Ferdinand Calo' (FEX___96)
 */
public class CompactParserBEO {

	private static final int END = 0;
	private static final int NAME = 1;
	private static final int AUTHOR = 2;
	private static final int TEXSIZE = 3;
	private static final int GROUP = 4;
	private static final int OBJECT = 5;
	private static final int POSITION = 2;
	private static final int ROTATION = 3;
	private static final int VECTOR = 4;
	private static final int UV = 5;
	private static final int NORMAL = 6;
	private static final int FACE = 7;
	private static final int SETTING = 8;

	/**
	 * Returns a CompactModel.
	 * Runs the fixStream method, use other method if you use unaffected streams.
	 * */
	public static CompactModel parse(InputStream stream, float scale) throws Exception {
		return parse(stream, scale, true);
	}

	/**
	 * Returns a CompactModel.
	 * @param fixstream If the inputstream fix should be run.
	 *                     Useful for models streams from zip files.
	 *                     Set to false if the fix was already applied to the stream beforehand.
	 * */
	public static CompactModel parse(InputStream stream, float scale, boolean fixstream) throws Exception {
		CompactModel model = new CompactModel();
		if(fixstream) stream = fixStream(stream);
		int f0 = stream.read(), f1 = stream.read(), f2 = stream.read(), format = stream.read();
		if(f0 != 6 || f1 != 2 || f2 != 15 || format < 0) return model;
		int r;
		ParseData data = new ParseData();
		while((r = stream.read()) > -1){
			switch(r){
				case NAME:{
					model.name = readString(stream);
					break;
				}
				case SETTING:{
					switch(stream.read()){
						case NORMAL:{
							data.normal_mode = stream.read();
							break;
						}
					}
					break;
				}
				case AUTHOR:{
					model.addCreator(readString(stream));
					break;
				}
				case TEXSIZE:{
					int[] in = readIntegers(stream, 2);
					model.tex_width = in[0];
					model.tex_height = in[1];
					break;
				}
				case GROUP:{
					CompactGroup group = new CompactGroup(readString(stream));
					readPolygons(stream, group, model.tex_width, model.tex_height, scale, data);
					model.groups.put(group.name, group);
					break;
				}
				default:
					break;
			}
		}
		//
		stream.close();
		return model;
	}

	public static InputStream fixStream(InputStream stream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int read;
		while((read = stream.read(buffer)) != -1) out.write(buffer, 0, read);
		return new ByteArrayInputStream(out.toByteArray());
	}

	private static byte[] read(InputStream stream) throws IOException{
		ArrayList<Byte> list = new ArrayList<>();
		while(true){
			int i = stream.read();
			if(i == END || i == -1) break;
			list.add((byte)i);
		}
		byte[] arr = new byte[list.size()];
		for(int i = 0; i < list.size(); i++) arr[i] = list.get(i);
		return arr;
	}

	private static String readString(InputStream stream) throws IOException {
		return new String(read(stream), StandardCharsets.UTF_8);
	}

	private static void readPolygons(InputStream stream, CompactGroup group, int tx, int ty, float scale, ParseData pd) throws IOException {
		Polyhedron hedron = null;
		int r;
		while(true){
			if((r = stream.read()) == -1) break;
			if(r != OBJECT) break;
			hedron = new Polyhedron();
			while(true){
				if((r = stream.read()) == -1) break;
				if(r == END){
					group.polyhedrons.add(hedron.rescale(scale));
					break;
				}
				switch(r){
					case NAME:{
						hedron.name = readString(stream);
						continue;
					}
					case POSITION:{
						float[] fl = readFloats(stream, 3);
						hedron.pos(fl[0], fl[1], fl[2]);
						continue;
					}
					case ROTATION:{
						float[] fl = readFloats(stream, 3);
						hedron.rot(fl[0], fl[1], fl[2]);
						continue;
					}
					case VECTOR:{
						float[] fl = readFloats(stream, 3);
						pd.vecs.add(new V3F(fl[0], fl[1], fl[2]));
						continue;
					}
					case UV:{
						pd.uvs.add(readFloats(stream, 2));
						continue;
					}
					case NORMAL:{
						float[] fl = readFloats(stream, 3);
						pd.nor.add(new V3F(fl[0], fl[1], fl[2]));
						continue;
					}
					case FACE:{
						int len = readIntegers(stream, 1)[0];
						int[] ids = readIntegers(stream, len + len + (pd.normal_length(len)));
						Vertex[] verts = new Vertex[len];
						for(int i = 0; i < len; i++){
							V3F vec = pd.vecs.get(ids[i]);
							float[] uv = pd.uvs.get(ids[i + len]);
							verts[i] = new Vertex(vec, uv[0], uv[1]);
						}
						Polygon poly = new Polygon(verts);
						pd.applyNormals(ids, poly);
						hedron.polygons.add(poly);
						continue;
					}
					default: break;
				}
			}
		}
	}

	private static float[] readFloats(InputStream stream, int t) throws IOException {
		float[] arr = new float[t];
		for(int i = 0; i < t; i++){
			byte[] bit = new byte[4];
			int r = stream.read(bit);
			if(r < 0) return arr;//error
			arr[i] = ByteBuffer.wrap(bit).getFloat();
		}
		return arr;
	}

	private static int[] readIntegers(InputStream stream, int t) throws IOException {
		int[] arr = new int[t];
		for(int i = 0; i < t; i++){
			byte[] bit = new byte[4];
			int r = stream.read(bit);
			if(r < 0) return arr;//error
			arr[i] = ByteBuffer.wrap(bit).getInt();
		}
		return arr;
	}

	public static class ParseData {

		public ArrayList<V3F> vecs = new ArrayList<>();
		public ArrayList<float[]> uvs = new ArrayList<>();
		public ArrayList<V3F> nor = new ArrayList<>();
		public int normal_mode = 0;

		public int normal_length(int len){
			return normal_mode == 0 ? 0 : normal_mode == 1 ? len : 1;
		}

		public void applyNormals(int[] ids, Polygon poly){
			if(normal_mode == 0) poly.genNorm();
			else if(normal_mode == 1){
				for(int i = 0; i < poly.vertices.length; i++){
					poly.vertices[i].norm(nor.get(ids[i + poly.vertices.length * 2]));
				}
			}
			else if(normal_mode == 2){
				V3F norm = nor.get(ids[ids.length - 1]);
				for(int i = 0; i < poly.vertices.length; i++){
					poly.vertices[i].norm(norm);
				}
			}
		}

	}

}
