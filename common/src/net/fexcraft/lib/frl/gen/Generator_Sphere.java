package net.fexcraft.lib.frl.gen;

import net.fexcraft.lib.common.Static;
import net.fexcraft.lib.common.math.M4DW;
import net.fexcraft.lib.common.math.V3F;
import net.fexcraft.lib.frl.Polygon;
import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.frl.Vertex;

import java.util.ArrayList;

import static net.fexcraft.lib.common.Static.*;
import static net.fexcraft.lib.frl.gen.Generator.Values.*;
import static net.fexcraft.lib.frl.gen.Generator.intToBoolArray;

/**
 * 
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Generator_Sphere {

	public static void make(Polyhedron poly, ValueMap map){
		float radius = map.getValue(RADIUS1, 1f);
		int segments = map.getValue(SEGMENTS, 4);
		int seglimit = map.getValue(SEG_LIMIT, 0);
		int circles = map.getValue(CIRCLES, segments);
		int circlelimit = map.getValue(CIR_LIMIT, 0);
		float seg_off = Static.toRadians(map.getValue(SEG_OFFSET, 0f));
		float cir_off = Static.toRadians(map.getValue(CIR_OFFSET, 0f));
		float x = map.getValue(OFF_X, 0f), y = map.getValue(OFF_Y, 0f), z = map.getValue(OFF_Z, 0f);
		boolean[] rems = intToBoolArray(map.getArray(REMOVE_POLYGONS), 3);
		if(segments < 3) segments = 3;
		if(seglimit <= 0) seglimit = segments;
		if(circles < 3) circles = 3;
		if(circlelimit <= 0) circlelimit = circles;
		ArrayList<Polygon> polis = new ArrayList<>();
		//
		M4DW[] mat = new M4DW[]{ M4DW.create(), M4DW.create(), M4DW.create(), M4DW.create() };
		V3F dv = new V3F(0, 1, 0);
		V3F[] vs = new V3F[]{ new V3F(), new V3F(), new V3F(), new V3F() };
		float rr = rad180 / circles, r;
		float sr = (rad180 / segments) * 2f;
		if(!rems[0]){
			for(int i = 0; i < segments; i++){
				if(i >= seglimit) break;
				mat[0].setRadians((i) * sr + seg_off, 0, 0);
				mat[1].setRadians((i) * sr + seg_off, cir_off + rr, 0);
				mat[2].setRadians((i + 1) * sr + seg_off, cir_off + rr, 0);
				vs[0] = mat[0].rotate(dv, new V3F()).add(x, y, z);
				vs[1] = mat[1].rotate(dv, new V3F()).add(x, y, z);
				vs[2] = mat[2].rotate(dv, new V3F()).add(x, y, z);
				polis.add(new Polygon(new Vertex[]{
					new Vertex(vs[0]),
					new Vertex(vs[1]),
					new Vertex(vs[2]),
				}));
			}
		}
		if(!rems[1]){
			for(int i = 0; i < segments; i++){
				if(i >= seglimit) break;
				mat[0].setRadians((i) * sr + seg_off, rad180, 0);
				mat[1].setRadians((i) * sr + seg_off, cir_off + (rad180 - rr), 0);
				mat[2].setRadians((i + 1) * sr + seg_off, cir_off + (rad180 - rr), 0);
				vs[0] = mat[0].rotate(dv, new V3F()).add(x, y, z);
				vs[1] = mat[1].rotate(dv, new V3F()).add(x, y, z);
				vs[2] = mat[2].rotate(dv, new V3F()).add(x, y, z);
				polis.add(new Polygon(new Vertex[]{
					new Vertex(vs[0]),
					new Vertex(vs[1]),
					new Vertex(vs[2]),
				}));
			}
		}
		if(!rems[2]){
			for(int ri = 1; ri < circles - 1; ri++){
				if(ri >= circlelimit) break;
				r = rr * ri;
				for(int i = 0; i < segments; i++){
					if(i >= seglimit) break;
					mat[0].setRadians((i) * sr + seg_off, cir_off + r, 0);
					mat[1].setRadians((i + 1) * sr + seg_off, cir_off + r, 0);
					mat[2].setRadians((i) * sr + seg_off, cir_off + r + rr, 0);
					mat[3].setRadians((i + 1) * sr + seg_off, cir_off + r + rr, 0);
					vs[0] = mat[0].rotate(dv, new V3F()).add(x, y, z);
					vs[1] = mat[1].rotate(dv, new V3F()).add(x, y, z);
					vs[2] = mat[2].rotate(dv, new V3F()).add(x, y, z);
					vs[3] = mat[3].rotate(dv, new V3F()).add(x, y, z);
					polis.add(new Polygon(new Vertex[]{
						new Vertex(vs[0]),
						new Vertex(vs[1]),
						new Vertex(vs[3]),
						new Vertex(vs[2]),
					}));
				}
			}
		}
		//
		float scale = radius;//map.getValue(SCALE, 1f);
		if(scale != 1f) for(Polygon gon : polis) for(Vertex vert : gon.vertices) vert.vector = vert.vector.scale(scale);
        for(Polygon gon : polis) poly.polygons.add(gon);
	}
	
}
