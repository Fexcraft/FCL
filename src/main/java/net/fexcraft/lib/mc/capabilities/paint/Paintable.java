package net.fexcraft.lib.mc.capabilities.paint;

import net.fexcraft.lib.common.math.RGB;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

/** @author Ferdinand Calo' (FEX___96) */
public interface Paintable {
	
	//public void onPaintItemUse(RGB color, EnumDyeColor dye, ItemStack stack, EntityPlayer player, BlockPos pos, World world);
	
	/** Get's the cached color. */
	public RGB getColor();
	
	/** Sets new color, returns old. */
	public RGB setColor(RGB color);
	
	/** Vanilla Alternative */
	public void setColor(EnumDyeColor dye);
	
	/** Call to tell client about changes. */
	public void updateClient();

	public NBTBase writeToNBT(Capability<Paintable> capability, EnumFacing side);

	public void readNBT(Capability<Paintable> capability, EnumFacing side, NBTBase nbt);
	
	public Object getObject();
	
	public <H> H getHolder();
	
}
