package net.fexcraft.lib.common.math;

/**
 * @author Ferdinand Calo' (FEX___96)
*/
public class V3I {

	public static final V3I NULL = new V3I();
	public int x, y, z;

    public V3I(){
    	x = y = z = 0;
    }

    public V3I(int v){
        this(v, v, v);
    }

    public V3I(int ix, int iy, int iz){
        x = ix == -0 ? 0 : ix;
        y = iy == -0 ? 0 : iy;
        z = iz == -0 ? 0 : iz;
    }

    public V3I(V3I vector){
        this(vector.x, vector.y, vector.z);
    }
    
    public V3I(V3D vector){
        this((int)vector.x, (int)vector.y, (int)vector.z);
    }

    public V3I(Vec3f vector){
        this((int)vector.x, (int)vector.y, (int)vector.z);
    }

    public V3I(String[] array, int index){
		x = array.length >= index ? Integer.parseInt(array[index++]) : 0;
		y = array.length >= index ? Integer.parseInt(array[index++]) : 0;
		z = array.length >= index ? Integer.parseInt(array[index]) : 0;
	}

	public V3I(int[] array, int index){
		x = array.length >= index ? array[index++] : 0;
		y = array.length >= index ? array[index++] : 0;
		z = array.length >= index ? array[index] : 0;
	}

	public V3I sub(V3I vec){
        return sub(vec.x, vec.y, vec.z);
    }

    public V3I sub(int x, int y, int z){
        return add(-x, -y, -z);
    }

    public V3I add(V3I vec){
        return add(vec.x, vec.y, vec.z);
    }
    
    public V3I add(int dx, int dy, int dz){
        return new V3I(x + dx, y + dy, z + dz);
    }

    public V3I scale(int scale){
        return new V3I(x * scale, y * scale, z * scale);
    }

    public V3I multiply(int by){
        return new V3I(x * by, y * by, z * by);
    }

	public V3I divide(int div){
		return div == 0f ? this : new V3I(x / div, y / div, z / div);
	}

    public boolean equals(Object obj){
        if(this == obj) return true;
        else if(obj instanceof V3I){
            V3I vec = (V3I)obj;
            return vec.x == x && vec.y == y && vec.z == z;
        }
        else return false;
    }

    @Override
    public int hashCode(){
        long l = Integer.toUnsignedLong(this.x);
        int i = (int)(l ^ l >>> 32);
        l = Integer.toUnsignedLong(this.y);
        i = 31 * i + (int)(l ^ l >>> 32);
        l = Integer.toUnsignedLong(this.z);
        i = 31 * i + (int)(l ^ l >>> 32);
        return i;
    }
	
	@Override
	public String toString(){
		return String.format("V3I[ %s, %s, %s ]", x, y, z);
	}

	public V3I middle(V3I target){
		return new V3I((x + target.x) * 2, (y + target.y) * 2, (z + target.z) * 2);
	}
	
    public int length(){
        return (int)Math.sqrt(x * x + y * y + z * z);
    }

	public V3I cross(V3I other){
		return new V3I(y * other.z - z * other.y, other.x * z - other.z * x, x * other.y - y * other.x);
	}

	public double dot(V3I other){
		return x * other.x + y * other.y + z * other.z;
	}

	public V3I normalize(V3I dest){
		int len = length();
		return dest == null ? new V3I(x / len, y / len, z / len) : dest.set(x / len, y / len, z / len);
	}

	public V3I normalize(){
		return normalize(null);
	}

	public V3I set(int nx, int ny, int nz){
		x = nx;
		y = ny;
		z = nz;
		return this;
	}

	public int[] toIntegerArray(){
		return new int[]{ x, y, z};
	}

	public void copy(V3I vec){
		x = vec.x; y = vec.y; z = vec.z;
	}

	public boolean isNull(){
		return x == 0f && y == 0f && z == 0f;
	}

	public V3I copy(){
		return new V3I(x, y, z);
	}

	public V3I rotate(int times){
		if(times > 3) times = times % 4;
		switch(times){
			case 1: return new V3I(-z, y, x);
			case 2: return new V3I(-x, y, -z);
			case 3: return new V3I(z, y, -x);
		}
		return this;
	}

	public String asString(){
		return x + ";" + y + ";" + z;
	}

	public static V3I fromString(String str){
		return new V3I(str.split(";"), 0);
	}

}