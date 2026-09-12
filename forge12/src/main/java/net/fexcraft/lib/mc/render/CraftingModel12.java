package net.fexcraft.lib.mc.render;

import net.fexcraft.lib.frl.CompactModel;
import net.fexcraft.lib.frl.Polyhedron;
import net.fexcraft.lib.mc.api.registry.fModel;
import net.fexcraft.mod.fcl.UniFCL;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
@fModel(registryname = "fcl:models/block/crafting")
public class CraftingModel12 implements FCLBlockModel {

	public CraftingModel12(){ super(); }

	@Override
	public Collection<Polyhedron> getPolygons(IBlockState state, EnumFacing side, Map<String, String> arguments, long rand){
		if(UniFCL.CRAFTING_MODEL == null) return Collections.emptyList();
		ArrayList<Polyhedron> list = new ArrayList<>();
		for(CompactModel.CompactGroup group : UniFCL.CRAFTING_MODEL.groups.values()) list.addAll(group.polyhedrons);
		return list;
	}

}
