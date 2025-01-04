package net.fexcraft.lib.mc.render;

import net.fexcraft.lib.mc.api.registry.fModel;
import net.fexcraft.lib.tmt.ModelRendererTurbo;
import net.fexcraft.mod.fcl.util.CraftingModel;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fModel(registryname = "fcl:models/block/crafting")
public class CraftingModel12 extends CraftingModel implements FCLBlockModel {

	public CraftingModel12(){ super(); }

	@Override
	public Collection<ModelRendererTurbo> getPolygons(IBlockState state, EnumFacing side, Map<String, String> arguments, long rand){
		ArrayList<ModelRendererTurbo> list = new ArrayList<>();
		for(ArrayList<ModelRendererTurbo> tlist : groups) list.addAll(tlist);
		return list;
	}

}
