package net.fexcraft.lib.frl.gen;

import java.util.ArrayList;

import net.fexcraft.app.json.JsonArray;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.Vec3f;
import net.minecraft.util.math.Vec3d;

/**
 * Compact version of Path.class from FVTM. Only containing what is necessary for polygon generation.
 * @author Ferdinand Calo' (FEX___96)
 *
 */
public class Path {
	
	public Vec3f start, end;
	public Vec3f[] rootpath;
	public Vec3f[] vecpath;
	public float length;
	public int segmentator = 4;
	
	public Path(Vec3f[] vec316fs, Vec3f vector){
		start = vec316fs[0];
		end = vector;
		rootpath = new Vec3f[vec316fs.length + 1];
		for(int i = 0; i < rootpath.length - 1; i++) rootpath[i] = vec316fs[i].copy();
		rootpath[rootpath.length - 1] = vector.copy();
		construct();
	}

	public Path(Vec3f[] vec316fs){
		start = vec316fs[0];
		end = vec316fs[vec316fs.length - 1];
		rootpath = new Vec3f[vec316fs.length];
		for(int i = 0; i < rootpath.length; i++) rootpath[i] = vec316fs[i].copy();
		construct();
	}
	
	public Path(){}
	
	protected void construct(){
		vecpath = new Vec3f[rootpath.length];
		if(vecpath.length == 2){
			vecpath[0] = rootpath[0]; vecpath[1] = rootpath[rootpath.length - 1];
			this.length = vecpath[0].dis(vecpath[1]);
		}
		else{
			for(int i = 0; i < rootpath.length; i++){
				vecpath[i] = rootpath[i];
			}
			//
			Vec3f[] vecs = curve(vecpath);
			vecpath = new Vec3f[vecs.length + 2];
			vecpath[0] = new Vec3f(start);
			for(int i = 0; i < vecs.length; i++){
				vecpath[i + 1] = vecs[i];
			}
			vecpath[vecpath.length - 1] = new Vec3f(end);
			this.length = this.calcLength();
		}
	}

	/**
	 * Multi-Point Curve Creation
	 * @param vecpoints
	 * @return
	 */
	private Vec3f[] curve(Vec3f[] vecpoints){
		ArrayList<Vec3f> vecs = new ArrayList<Vec3f>();
		float length = getLength(vecpoints);
		float increment = 1 / length / segmentator;
		double d = 0; while(d < 1){
			Vec3f[] moved = vecpoints;
			while(moved.length > 2){
				Vec3f[] arr = new Vec3f[moved.length - 1];
				for(int i = 0; i < moved.length - 1; i++){
					arr[i] = move(moved[i], moved[i + 1], moved[i].dis(moved[i + 1]) * d);
				}
				moved = arr;
			}
			d += increment;//0.0625//0.05;
			vecs.add(move(moved[0], moved[1], moved[0].dis(moved[1]) * d));
		}
		return vecs.toArray(new Vec3f[0]);
	}

	public static Vec3f move(Vec3f vec0, Vec3f vec1, double dis){
		double[] dest = newVector(vec1), beg = newVector(vec0);
    	dest = direction(dest[0] - beg[0], dest[1] - beg[1], dest[2] - beg[2]);
    	dest = newVector(beg[0] + (dest[0] * dis), beg[1] + (dest[1] * dis), beg[2] + (dest[2] * dis));
		return new Vec3f(dest[0], dest[1], dest[2]);
	}
	
	public static double[] newVector(double x, double y, double z){
		return new double[]{ x, y, z };
	}

	public static double[] newVector(Vec3d vec){
		return new double[]{ vec.x, vec.y, vec.z };
	}
	
	/** Array of 3 values expected. **/
    public static double length(double... arr){
        return Math.sqrt(arr[0] * arr[0] + arr[1] * arr[1] + arr[2] * arr[2]);
    }
    
    public static double distance(double[] first, double[] second){
        return length(second[0] - first[0], second[1] - first[1], second[2] - first[2]);
    }
    
    public static double[] direction(double... arr){
    	double l = length(arr[0], arr[1], arr[2]); return new double[]{ arr[0] / l, arr[1] / l, arr[2] / l };
    }

	public static double[] newVector(Vec3f vec){
		return new double[]{ vec.x, vec.y, vec.z };
	}
	
	public float getLength(Vec3f[] vecs){
		vecs = vecs == null ? vecpath : vecs;
		float temp = 0;
		for(int i = 0; i < vecs.length - 1; i++){
			temp += vecs[i].dis(vecs[i + 1]);
		}
		return temp;
	}
	
	protected float calcLength(){
		return getLength(null);
	}
	
	public Path read(JsonMap map){
		JsonArray arr = map.getArray("start");
		start = new Vec3f(arr.get(0).float_value(), arr.get(1).float_value(), arr.get(2).float_value());
		arr = map.getArray("end");
		end = new Vec3f(arr.get(0).float_value(), arr.get(1).float_value(), arr.get(2).float_value());
		arr = map.getArray("vectors");
		rootpath = new Vec3f[arr.value.size()];
		int[] idx = { 0 };
		arr.value.forEach(entry -> {
			JsonArray array = entry.asArray();
			rootpath[idx[0]++] = new Vec3f(array.get(0).float_value(), array.get(1).float_value(), array.get(2).float_value());
		});
		construct();
		this.length = map.has("length") ? map.getFloat("length", 0) : calcLength();
		return this;
	}

	public JsonMap write(JsonMap map){
		JsonArray arr = new JsonArray();
		arr.add(start.x);
		arr.add(start.y);
		arr.add(start.z);
		map.add("start", arr);
		arr = new JsonArray();
		arr.add(end.x);
		arr.add(end.y);
		arr.add(end.z);
		map.add("end", arr);
		JsonArray array = new JsonArray();
		for(Vec3f vec : rootpath){
			arr = new JsonArray();
			arr.add(vec.x);
			arr.add(vec.y);
			arr.add(vec.z);
			array.add(arr);
		}
		map.add("vectors", array);
		map.add("length", length);
		return map;
	}
	
	public Vec3f getFirstVector(){
		return vecpath.length == 0 ? null : vecpath[0];
	}
	
	public Vec3f getLastVector(){
		return vecpath.length == 0 ? null : vecpath[vecpath.length - 1];
	}
	
	public <T extends Path> T createOpposite(T instance){
		instance.start = end;
		instance.end = start;
		instance.rootpath = new Vec3f[rootpath.length]; int j = rootpath.length - 1;
		for(int i = 0; i < instance.rootpath.length; i++){ instance.rootpath[i] = rootpath[j--].copy(); }
		instance.construct(); instance.length = instance.calcLength();
		return instance;
	}
	
	public float[] getPosition(float distance){
		if(distance >= this.length){
			if(distance == this.length) vecpath[vecpath.length - 1].toFloatArray();
			return new float[]{ distance - length };
		}
		float traveled = 0, temp, multi;
		for(int i = 0; i < vecpath.length - 1; i++){
			temp = traveled + (multi = vecpath[i].dis(vecpath[i + 1]));
			if(temp >= distance){
				if(temp == distance) return vecpath[i + 1].toFloatArray();
				return vecpath[i + 1].distance(vecpath[i], temp - distance).toFloatArray();
			}
			else{
				traveled += multi;
			}
		}
		return vecpath[0].toFloatArray();
	}
	
	public Vec3f getVectorPosition(float distance, boolean reverse){
		if(reverse) distance = (float)this.oppositePassed(distance);
		if(distance >= this.length){
			return new Vec3f(vecpath[vecpath.length - 1]);
		}
		float traveled = 0, temp, multi;
		for(int i = 0; i < vecpath.length - 1; i++){
			temp = traveled + (multi = vecpath[i].dis(vecpath[i + 1]));
			if(temp >= distance){
				if(temp == distance) return new Vec3f(vecpath[i + 1]);
				return vecpath[i + 1].distance(vecpath[i], temp - distance);
			}
			else{
				traveled += multi;
			}
		}
		return new Vec3f(vecpath[0]);
	}
	
	@Override
	public String toString(){
		return String.format("Path[%s-%s, %s]", start, end, vecpath.length);
	}

	public float oppositePassed(float sec){
		return sec >= length ? 0 : sec <= 0 ? length : this.length - sec;
	}

}
