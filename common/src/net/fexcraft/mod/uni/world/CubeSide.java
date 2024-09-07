package net.fexcraft.mod.uni.world;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public enum CubeSide {

    UP, DOWN, NORTH, EAST, SOUTH, WEST;

	public static CubeSide fromIndex(int idx, CubeSide def){
		switch(idx){
			case 0: return DOWN;
			case 1: return UP;
			case 2: return NORTH;
			case 3: return SOUTH;
			case 4: return WEST;
			case 5: return EAST;
		}
		return def == null ? NORTH : def;
	}

	public static CubeSide fromIndex(int idx){
		return fromIndex(idx, null);
	}

	public boolean negative(){
		return ordinal() % 2 == 0;
	}

	public boolean positive(){
		return ordinal() % 2 == 1;
	}

	public Axe axe(){
		return ordinal() < 2 ? Axe.Y : ordinal() < 4 ? Axe.Z : Axe.X;
	}

	public CubeSide rotate(){
		if(ordinal() < 2) return this;
		int ord = ordinal() + 1;
		if(ord >= 6) ord = 2;
		return values()[ord];
	}

	public CubeSide rotateCC(){
		if(ordinal() < 2) return this;
		int ord = ordinal() - 1;
		if(ord <= 1) ord = 5;
		return values()[ord];
	}

	public <L> L local(){
		return WrapperHolder.getLocalSide(this);
	}

	public static enum Axe {
		X, Y, Z
	}

}
