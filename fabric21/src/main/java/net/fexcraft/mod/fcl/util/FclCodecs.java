package net.fexcraft.mod.fcl.util;

import com.mojang.serialization.Codec;
import net.fexcraft.lib.common.math.V3D;
import net.minecraft.Util;
import net.minecraft.world.level.storage.ValueOutput;

import java.util.List;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class FclCodecs {

	public static Codec<V3D> V3D = Codec.DOUBLE.listOf().comapFlatMap(
		ds -> Util.fixedSize(ds, 3).map(list ->  new V3D(list.get(0), list.get(1), list.get(2))),
		vec -> List.of(vec.x, vec.y, vec.z)
	);

}
