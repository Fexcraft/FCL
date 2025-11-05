package net.fexcraft.mod.uni.ui;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fcl.util.ExternalTextureHelper;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.impl.ResLoc;
import net.fexcraft.mod.uni.inv.StackWrapper;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.util.ArrayList;
import java.util.Map;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniUI extends GuiContainer  {

	protected ArrayList<String> tooltip = new ArrayList<>();
	protected ArrayList<UITab> tabs = new ArrayList<>();
	//
	protected UniCon container;
	protected UserInterface ui;

	public UniUI(UserInterface ui, UIKey key, UniCon con, EntityPlayer player){
		super(con == null ? con = new UniCon(ui.container, key, player) : con);
		this.ui = ui;
		ui.root = this;
		(container = con).setup(this);
		xSize = ui.width;
		ySize = ui.height;
		ui.drawer = new UserInterface.Drawer() {
			@Override
			public void draw(float x, float y, int u, int v, int w, int h){
				drawTexturedModalRect((int)x, (int)y, u, v, w, h);
			}

			@Override
			public void drawFull(float x, float y, int w, int h){
				//drawModalRectWithCustomSizedTexture((int)x, (int)y, 0, 0, w, h, w, h);
				drawTexturedModalRect((int)x, (int)y, 0, 0, w, h);
			}

			@Override
			public void draw(int x, int y, StackWrapper stack, boolean text){
				RenderHelper.enableGUIStandardItemLighting();
				itemRender.renderItemAndEffectIntoGUI(fontRendererObj, mc.getTextureManager(), stack.local(), x, y);
				if(text){
					itemRender.renderItemOverlayIntoGUI(fontRendererObj, mc.getTextureManager(), stack.local(), x, y, null);
				}
			}

			@Override
			public void bind(IDL texture){
				//TexUtil.bindTexture(texture);
				if(texture.local() == null) return;
				mc.getTextureManager().bindTexture(texture.local());
			}

			@Override
			public void apply(RGB color){
				color.glColorApply();
			}

			@Override
			public String translate(String str, Object... args){
				return Formatter.format(I18n.format(str, args));
			}

			@Override
			public IDL loadExternal(String urltex){
				return new ResLoc(ExternalTextureHelper.get(urltex).toString());
			}

			@Override
			public void drawLine(double sx, double sy, double ex, double ey, float[] color){
				Tessellator.instance.startDrawing(3);
				Tessellator.instance.addVertex(sx, sy, zLevel + 1);
				Tessellator.instance.setColorRGBA_F(color[0], color[1], color[2], 1F);
				Tessellator.instance.addVertex(ex, ey, zLevel + 1);
				Tessellator.instance.setColorRGBA_F(color[0], color[1], color[2], 1F);
				Tessellator.instance.draw();
			}

		};

	}
	public UniUI(UserInterface ui, UIKey key, EntityPlayer player){
		this(ui, key, null, player);
	}

	@Override
	public void drawScreen(int mouseX, int mouseY, float partialTicks){
		super.drawScreen(mouseX, mouseY, partialTicks);
	}

	@Override
	public void initGui(){
		super.initGui();
		ui.screen_width = width;
		ui.screen_height = height;
		ui.gLeft = guiLeft;
		ui.gTop = guiTop;
		ui.init();
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float ticks, int mx, int my){
		if(ui.background) super.drawDefaultBackground();
		predraw(ticks, mx, my);
		drawbackground(ticks, mx, my);
		GL11.glClearColor(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		for(UITab tab : ui.tabs.values()){
			if(!tab.visible()) continue;
			bindTexture(tab.texture);
			tab.buttons.forEach((key, button) -> {
				button.hovered(guiLeft, guiTop, mx, my);
				button.draw(this, null, ticks, guiLeft, guiTop, mx, my);
			});
			tab.buttons.forEach((key, button) -> {
				if(button.text != null) button.text.draw(this, button, ticks, guiLeft, guiTop, mx, my);
			});
			tab.texts.forEach((key, text) -> {
				text.draw(this, null, ticks, guiLeft, guiTop, mx, my);
			});
			tab.fields.forEach((key, field) -> {
				field.draw(this, null, ticks, guiLeft, guiTop, mx, my);
			});
		}
		postdraw(ticks, mx, my);
	}

	protected void predraw(float ticks, int mx, int my){
		ui.predraw(ticks, mx, my);
	}

	public void drawbackground(float ticks, int mx, int my){
		for(UITab tab : ui.tabs.values()){
			if(!tab.visible()) continue;
			int tx = tab.enabled() ? tab.hovered() ? tab.htx : tab.tx : tab.dtx;
			int ty = tab.enabled() ? tab.hovered() ? tab.hty : tab.ty : tab.dty;
			bindTexture(tab.texture);
			if(tab.absolute){
				drawTexturedModalRect(tab.x < 0 ? width + tab.x : tab.x, tab.y < 0 ? height + tab.y : tab.y, tx, ty, tab.width, tab.height);
			}
			else{
				drawTexturedModalRect(guiLeft + tab.x, guiTop + tab.y, tx, ty, tab.width, tab.height);
			}
		}
		ui.drawbackground(ticks, mx, my);
	}

	protected void postdraw(float ticks, int mx, int my){
		ui.postdraw(ticks, mx, my);
		tooltip.clear();
		ui.getTooltip(mx, my, tooltip);
		for(UITab tab : ui.tabs.values()){
			if(!tab.visible()) continue;
			for(UIButton button : tab.buttons.values()){
				if(!button.visible()) continue;
				if(button.tooltip != null && button.hovered()) tooltip.add(button.tooltip);
			}
		}
		if(tooltip.size() > 0) drawHoveringText(tooltip, mx, my, fontRendererObj);
	}


	@Override
	protected void mouseClicked(int mx, int my, int mb){
		if(ui.onClick(mx, my, mb)) return;
		super.mouseClicked(mx, my, mb);
	}

	@Override
	protected void keyTyped(char c, int code){
		boolean invbutton = mc.gameSettings.keyBindInventory.getKeyCode() == code && mc.gameSettings.keyBindInventory.isPressed();
		boolean keytyped = false;
		for(UITab tab : ui.tabs.values()){
			if(!tab.visible() || tab.fields.isEmpty()) continue;
			boolean bool = false;
			for(UIField field : tab.fields.values()){
				if(bool) break;
				if(field.visible() && field.keytyped(c, code)){
					bool = true;
					//break;
				}
			}
			if(!bool && !invbutton){
				super.keyTyped(c, code);
			}
			else keytyped = true;
		}
		if(!keytyped) keytyped = ui.keytyped(c, code);
		if(!keytyped && code == 1) mc.thePlayer.closeScreen();
	}

	@Override
	public void handleMouseInput(){
		super.handleMouseInput();
		int e = Mouse.getEventDWheel();
		if(e == 0) return;
		int am = e > 0 ? -1 : 1;
		int x = Mouse.getEventX() * width / mc.displayWidth;
		int y = this.height - Mouse.getEventY() * height / mc.displayHeight - 1;
		boolean exit = false;
		tabs.clear();
		tabs.addAll(ui.tabs.values());
		for(UITab tab : tabs){
			if(!tab.visible()) continue;
			for(Map.Entry<String, UIButton> entry : tab.buttons.entrySet()){
				if(exit) break;
				if(entry.getValue().hovered(guiLeft, guiTop, x, y)){
					exit = entry.getValue().onscroll(guiLeft, guiTop, x, y, am) || ui.onScroll(entry.getValue(), entry.getKey(), x, y, am);
				}
			}
			for(UIText text : tab.texts.values()){
				if(exit) break;
				if(text.hovered(guiLeft, guiTop, x, y)) exit = text.onscroll(guiLeft, guiTop, x, y, am);
			}
		}
		if(!exit) scrollwheel(am, x, y);
	}

	public void scrollwheel(int am, int x, int y){
		ui.scrollwheel(am, x, y);
	}

	public void bindTexture(IDL texture){
		mc.renderEngine.bindTexture((ResourceLocation)texture);
	}

	public void initField(UIField field){
		GuiTextField gfield = ((UUIField)field).field;
		gfield.width = field.width;
		gfield.height = field.height;
		gfield.xPosition = field.x;
		gfield.yPosition = field.y;
		gfield.setEnableBackgroundDrawing(field.background);
	}

	public String getClipboard(){
		try{
			Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable data = cp.getContents(null);
			if(!data.isDataFlavorSupported(DataFlavor.stringFlavor)) return "";
			return data.getTransferData(DataFlavor.stringFlavor).toString();
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return "";
	}

	public void setClipboard(String str){
		try{
			Clipboard cp = Toolkit.getDefaultToolkit().getSystemClipboard();
			StringSelection sel = new StringSelection(str);
			cp.setContents(sel, new StringSelection("fcl"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}

}
