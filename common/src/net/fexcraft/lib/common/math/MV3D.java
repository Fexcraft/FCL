package net.fexcraft.lib.common.math;

/**
 * @author Ferdinand Calo' (FEX___96)
*/
public class MV3D extends V3D {

    public MV3D(){
    	super();
    }

    public MV3D(double v){
        super(v, v, v);
    }

    public MV3D(double dx, double dy, double dz){
        super(dx, dy, dz);
    }

    public MV3D(V3I vector){
        super(vector.x, vector.y, vector.z);
    }

    public MV3D(V3D vector){
		super(vector.x, vector.y, vector.z);
    }

    public MV3D(Vec3f vector){
		super(vector.x, vector.y, vector.z);
    }

    public MV3D(String[] array, int index){
		super(array, index);
	}

	public MV3D(float[] array, int index){
		super(array, index);
	}

	@Override
	public V3D sub(V3D vec){
        return sub(vec.x, vec.y, vec.z);
    }

	@Override
    public V3D sub(double x, double y, double z){
        return add(-x, -y, -z);
    }

	@Override
    public V3D add(V3D vec){
        return add(vec.x, vec.y, vec.z);
    }

	@Override
    public V3D add(double dx, double dy, double dz){
        return set(x + dx, y + dy, z + dz);
    }

	@Override
    public V3D scale(double scale){
        return set(x * scale, y * scale, z * scale);
    }

	@Override
    public V3D multiply(double by){
        return set(x * by, y * by, z * by);
    }

	@Override
	public V3D divide(double div){
		return div == 0f ? this : set(x / div, y / div, z / div);
	}
	
	@Override
	public String toString(){
		return String.format("MV3D[ %s, %s, %s ]", x, y, z);
	}

	@Override
	public V3D middle(V3D target){
		return set((x + target.x) * 0.5, (y + target.y) * 0.5, (z + target.z) * 0.5);
	}

	@Override
	public V3D cross(V3D vec){
		return set(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}

	public V3D norm(){
		double len = length();
		return len < 0.00001 ? set(0, 0, 0) : set(x / len, y / len, z / len);
	}

	public MV3D copy(){
		return new MV3D(x, y, z);
	}

}