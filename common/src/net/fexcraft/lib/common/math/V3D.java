package net.fexcraft.lib.common.math;

/**
 * @author Ferdinand Calo' (FEX___96)
*/
public class V3D {

	public static final V3D NULL = new V3D();
	public double x, y, z;

    public V3D(){
    	x = y = z = 0;
    }

    public V3D(double v){
        this(v, v, v);
    }

    public V3D(double dx, double dy, double dz){
        x = dx == -0.0f ? 0.0f : dx;
        y = dy == -0.0f ? 0.0f : dy;
        z = dz == -0.0f ? 0.0f : dz;
    }

    public V3D(V3I vector){
        this(vector.x, vector.y, vector.z);
    }

    public V3D(V3D vector){
        this(vector.x, vector.y, vector.z);
    }

    public V3D(Vec3f vector){
        this(vector.x, vector.y, vector.z);
    }

    public V3D(String[] array, int index){
		x = array.length > index ? Double.parseDouble(array[index++]) : 0;
		y = array.length > index ? Double.parseDouble(array[index++]) : 0;
		z = array.length > index ? Double.parseDouble(array[index]) : 0;
	}

	public V3D(float[] array, int index){
		x = array.length > index ? array[index++] : 0;
		y = array.length > index ? array[index++] : 0;
		z = array.length > index ? array[index] : 0;
	}

	public V3D sub(V3D vec){
        return sub(vec.x, vec.y, vec.z);
    }

    public V3D sub(double x, double y, double z){
        return add(-x, -y, -z);
    }

    public V3D add(V3D vec){
        return add(vec.x, vec.y, vec.z);
    }
    
    public V3D add(double dx, double dy, double dz){
        return new V3D(x + dx, y + dy, z + dz);
    }

    public V3D scale(double scale){
        return new V3D(x * scale, y * scale, z * scale);
    }

    public V3D multiply(double by){
        return new V3D(x * by, y * by, z * by);
    }

	public V3D divide(double div){
		return div == 0f ? this : new V3D(x / div, y / div, z / div);
	}
    
    public double dis(V3D vec){
        double x = vec.x - this.x, y = vec.y - this.y, z = vec.z - this.z;
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double dis(double ox, double oy, double oz){
        double x = ox - this.x, y = oy - this.y, z = oz - this.z;
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public double sqdis(V3D vec){
        double x = vec.x - this.x, y = vec.y - this.y, z = vec.z - this.z;
        return x * x + y * y + z * z;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        else if(obj instanceof V3D){
            V3D vec = (V3D)obj;
            return Double.compare(vec.x, x) == 0 && Double.compare(vec.y, y) == 0 && Double.compare(vec.z, z) == 0;
        }
        else return false;
    }

    @Override
    public int hashCode(){
        long l = Double.doubleToLongBits(this.x);
        int i = (int)(l ^ l >>> 32);
        l = Double.doubleToLongBits(this.y);
        i = 31 * i + (int)(l ^ l >>> 32);
        l = Double.doubleToLongBits(this.z);
        i = 31 * i + (int)(l ^ l >>> 32);
        return i;
    }
	
	@Override
	public String toString(){
		return String.format("V3D[ %s, %s, %s ]", x, y, z);
	}

	public V3D middle(V3D target){
		return new V3D((x + target.x) * 0.5, (y + target.y) * 0.5, (z + target.z) * 0.5);
	}
	
	//based on fvtm rail sys

	public V3D distance(V3D dest, double am){
		V3D vec = new V3D((x + dest.x) * 0.5, (y + dest.y) * 0.5, (z + dest.z) * 0.5);
    	vec = direction(vec.x - x, vec.y - y, vec.z - z);
		return new V3D(x + (vec.x * am), y + (vec.y * am), z + (vec.z * am));
	}
	
    public double length(){
        return Math.sqrt(x * x + y * y + z * z);
    }
    
    public static double length(double... arr){
        return Math.sqrt(arr[0] * arr[0] + arr[1] * arr[1] + arr[2] * arr[2]);
    }
    
    public static double length(V3D vec){
        return Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }
    
    public static V3D direction(double... arr){
    	double l = length(arr[0], arr[1], arr[2]);
    	return new V3D(arr[0] / l, arr[1] / l, arr[2] / l);
    }
    
    public static V3D direction(V3D vec){
    	double l = length(vec.x, vec.y, vec.z);
    	return new V3D(vec.x / l, vec.y / l, vec.z / l);
    }

	public V3D cross(V3D vec){
		return new V3D(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}

	public double dot(V3D other){
		return x * other.x + y * other.y + z * other.z;
	}

	public V3D normalize(V3D dest){
		double len = length();
		if(dest == null) return len < 0.00001 ? NULL : new V3D(x / len, y / len, z / len);
		return len < 0.00001 ? dest.set(0, 0, 0) : dest.set(x / len, y / len, z / len);
	}

	public V3D normalize(){
		return normalize(null);
	}

	public V3D set(double dx, double dy, double dz){
		x = dx; y = dy; z = dz;
		return this;
	}

	public float[] toFloatArray(){
		return new float[]{ (float)x, (float)y, (float)z};
	}

	public double[] toDoubleArray(){
		return new double[]{ x, y, z };
	}

	public void copy(V3D vec){
		x = vec.x; y = vec.y; z = vec.z;
	}

	public boolean isNull(){
		return x == 0f && y == 0f && z == 0f;
	}

	public V3D copy(){
		return new V3D(x, y, z);
	}

	public static V3D add(V3D vec, V3D dest){
		dest.x += vec.x;
		dest.y += vec.y;
		dest.z += vec.z;
		return dest;
	}

	public static V3D sub(V3D vec, V3D dest){
		dest.x -= vec.x;
		dest.y -= vec.y;
		dest.z -= vec.z;
		return dest;
	}

}