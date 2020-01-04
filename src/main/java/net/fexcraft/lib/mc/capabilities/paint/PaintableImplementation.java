package net.fexcraft.lib.mc.capabilities.paint;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.entity.Entity;
import net.minecraft.item.EnumDyeColor;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;

public class PaintableImplementation implements Paintable {
	
	private Object container;
	private RGB color = RGB.WHITE.copy();
	
	public PaintableImplementation(){}

	@Override
	public NBTBase writeToNBT(Capability<Paintable> capability, EnumFacing side){
		NBTTagCompound compound = new NBTTagCompound(); compound.setInteger("color", color.packed);
		if(color.alpha != 1f) compound.setFloat("alpha", color.alpha); return compound;
	}

	@Override
	public void readNBT(Capability<Paintable> capability, EnumFacing side, NBTBase nbt){
		if(nbt == null || nbt instanceof NBTTagCompound == false) return;
		NBTTagCompound compound = (NBTTagCompound)nbt; color.packed = compound.getInteger("color");
		if(compound.hasKey("alpha")) color.alpha = compound.getFloat("alpha");
	}

	@Override
	public RGB getColor(){
		return color;
	}

	@Override
	public RGB setColor(RGB newcolor){
		RGB old = color; color = newcolor; return old;
	}

	@Override
	public void setColor(EnumDyeColor dye){
		color = RGB.fromDyeColor(dye);
	}

	@Override
	public void updateClient(){
		if(container == null) return;
		NBTTagCompound compound = (NBTTagCompound)this.writeToNBT(null, null);
		compound.setString("target_listener", "fcl_gui");
		compound.setString("task", "paintable");
		TargetPoint point = null;
		if(container instanceof Entity){
			Entity entity = (Entity)container;
			compound.setString("type", "entity");
			compound.setInteger("id", entity.getEntityId());
			point = new TargetPoint(entity.dimension, entity.posX, entity.posY, entity.posZ, 512);
		}
		else if(container instanceof TileEntity){
			TileEntity tileentity = (TileEntity)container;
			compound.setString("type", "tileentity");
			compound.setLong("pos", tileentity.getPos().toLong());
			point = new TargetPoint(tileentity.getWorld().provider.getDimension(),
				tileentity.getPos().getX(), tileentity.getPos().getY(), tileentity.getPos().getZ(), 512);
		}
		else{
			Print.log("UNKNOWN PAINTABLE CAPABILITY CONTAINER TYPE, THIS MAY CAUSE ERRORS;");
		}
		PacketHandler.getInstance().sendToAllAround(new PacketNBTTagCompound(compound), point);
	}

	protected void setObject(Object object){
		this.container = object;
	}

	@Override
	public Object getObject(){
		return container;
	}

	@SuppressWarnings("unchecked") @Override
	public <H> H getHolder(){
		return (H)container;
	}

}
