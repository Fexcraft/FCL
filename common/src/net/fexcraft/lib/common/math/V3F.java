package net.fexcraft.lib.common.math;

/**
 * @author Ferdinand Calo' (FEX___96)
*/
public class V3F {

	public static final V3F NULL = new V3F(){
		@Override
		public V3F set(float x, float y, float z){
			this.x = this.y = this.z = 0;
			return this;
		}
	};
	public float x, y, z;

    public V3F(){
    	x = y = z = 0;
    }

    public V3F(float v){
        this(v, v, v);
    }

    public V3F(float dx, float dy, float dz){
        x = dx == -0.0f ? 0.0f : dx;
        y = dy == -0.0f ? 0.0f : dy;
        z = dz == -0.0f ? 0.0f : dz;
    }

	public V3F(double dx, double dy, double dz){
		this((float)dx, (float)dy, (float)dz);
	}

    public V3F(V3I vector){
        this(vector.x, vector.y, vector.z);
    }

    public V3F(V3F vector){
        this(vector.x, vector.y, vector.z);
    }

    public V3F(V3D vector){
        this(vector.x, vector.y, vector.z);
    }

    public V3F(String[] array, int index){
		x = array.length > index ? Float.parseFloat(array[index++]) : 0;
		y = array.length > index ? Float.parseFloat(array[index++]) : 0;
		z = array.length > index ? Float.parseFloat(array[index]) : 0;
	}

	public V3F(float[] array, int index){
		x = array.length > index ? array[index++] : 0;
		y = array.length > index ? array[index++] : 0;
		z = array.length > index ? array[index] : 0;
	}

	public V3F sub(V3F vec){
        return sub(vec.x, vec.y, vec.z);
    }

    public V3F sub(float x, float y, float z){
        return add(-x, -y, -z);
    }

    public V3F add(V3F vec){
        return add(vec.x, vec.y, vec.z);
    }
    
    public V3F add(float dx, float dy, float dz){
        return new V3F(x + dx, y + dy, z + dz);
    }

    public V3F scale(float scale){
        return new V3F(x * scale, y * scale, z * scale);
    }

    public V3F multiply(float by){
        return new V3F(x * by, y * by, z * by);
    }

	public V3F divide(float div){
		return div == 0f ? this : new V3F(x / div, y / div, z / div);
	}
    
    public float dis(V3F vec){
		float x = vec.x - this.x, y = vec.y - this.y, z = vec.z - this.z;
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

    public float dis(float ox, float oy, float oz){
		float x = ox - this.x, y = oy - this.y, z = oz - this.z;
        return (float)Math.sqrt(x * x + y * y + z * z);
    }

	public float dis(V3I vec){
		float x = vec.x - this.x, y = vec.y - this.y, z = vec.z - this.z;
		return (float)Math.sqrt(x * x + y * y + z * z);
	}
    
    public float sqdis(V3F vec){
		float x = vec.x - this.x, y = vec.y - this.y, z = vec.z - this.z;
        return x * x + y * y + z * z;
    }

    public boolean equals(Object obj){
        if(this == obj) return true;
        else if(obj instanceof V3F){
            V3F vec = (V3F)obj;
            return Float.compare(vec.x, x) == 0 && Float.compare(vec.y, y) == 0 && Float.compare(vec.z, z) == 0;
        }
        else return false;
    }

    @Override
    public int hashCode(){
        long l = Float.floatToIntBits(this.x);
        int i = (int)(l ^ l >>> 32);
        l = Float.floatToIntBits(this.y);
        i = 31 * i + (int)(l ^ l >>> 32);
        l = Float.floatToIntBits(this.z);
        i = 31 * i + (int)(l ^ l >>> 32);
        return i;
    }
	
	@Override
	public String toString(){
		return String.format("V3F[ %s, %s, %s ]", x, y, z);
	}

	public V3F middle(V3F target){
		return new V3F((x + target.x) * 0.5f, (y + target.y) * 0.5f, (z + target.z) * 0.5f);
	}
	
	//based on fvtm rail sys

	public V3F distance(V3F dest, float am){
		V3F vec = new V3F((x + dest.x) * 0.5f, (y + dest.y) * 0.5f, (z + dest.z) * 0.5f);
    	vec = direction(vec.x - x, vec.y - y, vec.z - z);
		return new V3F(x + (vec.x * am), y + (vec.y * am), z + (vec.z * am));
	}
	
    public float length(){
        return (float)Math.sqrt(x * x + y * y + z * z);
    }
    
    public static float length(float... arr){
        return (float)Math.sqrt(arr[0] * arr[0] + arr[1] * arr[1] + arr[2] * arr[2]);
    }
    
    public static float length(V3F vec){
        return (float)Math.sqrt(vec.x * vec.x + vec.y * vec.y + vec.z * vec.z);
    }
    
    public static V3F direction(float... arr){
		float l = length(arr[0], arr[1], arr[2]);
    	return new V3F(arr[0] / l, arr[1] / l, arr[2] / l);
    }
    
    public static V3F direction(V3F vec){
		float l = length(vec.x, vec.y, vec.z);
    	return new V3F(vec.x / l, vec.y / l, vec.z / l);
    }

	public V3F cross(V3F vec){
		return new V3F(y * vec.z - z * vec.y, z * vec.x - x * vec.z, x * vec.y - y * vec.x);
	}

	public float dot(V3F other){
		return x * other.x + y * other.y + z * other.z;
	}

	public V3F normalize(V3F dest){
		float len = length();
		if(dest == null) return len < 0.00001 ? NULL : new V3F(x / len, y / len, z / len);
		return len < 0.00001 ? dest.set(0, 0, 0) : dest.set(x / len, y / len, z / len);
	}

	public V3F normalize(){
		return normalize(null);
	}

	public V3F norm(){
		return normalize(this);
	}

	public V3F set(float dx, float dy, float dz){
		x = dx; y = dy; z = dz;
		return this;
	}

	public float[] toFloatArray(){
		return new float[]{ x, y, z};
	}

	public double[] toDoubleArray(){
		return new double[]{ x, y, z};
	}

	public void copy(V3F vec){
		x = vec.x; y = vec.y; z = vec.z;
	}

	public boolean isNull(){
		return x == 0f && y == 0f && z == 0f;
	}

	public boolean notNull(){
		return x != 0f || y != 0f || z != 0f;
	}

	public boolean isOne(){
		return x == 1f && y == 1f && z == 1f;
	}

	public boolean notOne(){
		return x != 1f || y != 1f || z != 1f;
	}

	public V3F copy(){
		return new V3F(x, y, z);
	}

	public static V3F add(V3F vec, V3F dest){
		dest.x += vec.x;
		dest.y += vec.y;
		dest.z += vec.z;
		return dest;
	}

	public static V3F add(float x, float y, float z, V3F dest){
		dest.x += x;
		dest.y += y;
		dest.z += z;
		return dest;
	}

	public static V3F sub(V3F vec, V3F dest){
		dest.x -= vec.x;
		dest.y -= vec.y;
		dest.z -= vec.z;
		return dest;
	}

	public static V3F sub(float x, float y, float z, V3F dest){
		dest.x -= x;
		dest.y -= y;
		dest.z -= z;
		return dest;
	}

	public static V3F mul(V3F vec, V3F dest){
		dest.x *= vec.x;
		dest.y *= vec.y;
		dest.z *= vec.z;
		return dest;
	}

	public static V3F mul(float x, float y, float z, V3F dest){
		dest.x *= x;
		dest.y *= y;
		dest.z *= z;
		return dest;
	}

}