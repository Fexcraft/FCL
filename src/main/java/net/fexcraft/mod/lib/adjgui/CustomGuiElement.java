package net.fexcraft.mod.lib.adjgui;

import java.util.List;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface CustomGuiElement {
	
	public String getId();
	
	@SideOnly(Side.CLIENT)
	public void draw(float partial_ticks, int mouseX, int mouseY);
	
	public void onGuiInit(List<GuiButton> buttons, int guiLeft, int guiTop);
	
	public boolean onActionPerformed(SynchronizedContainer container, GuiButton button);

	public void onDataPacket(EntityPlayer player, NBTTagCompound data, Side from);

	public boolean isKeyTyped(char typedChar, int keyCode);

	public void mouseClicked(int mouseX, int mouseY, int mouseButton);

	public void toggleState();
	
}