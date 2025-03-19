package net.fexcraft.mod.uni;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class EnvInfo {

	public static boolean CLIENT;
	public static boolean DEV;

	public static boolean is120(){
		return UniReg.LOADER_VERSION.startsWith("1.20");
	}

	public static boolean is112(){
		return UniReg.LOADER_VERSION.startsWith("1.12");
	}

	public static boolean is121(){
		return UniReg.LOADER_VERSION.startsWith("1.2") && !is120();
	}

}
