package net.fexcraft.mod.uni.ui;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.InputConstants;
import net.fexcraft.app.json.JsonMap;
import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.common.utils.Formatter;
import net.fexcraft.mod.fcl.util.ExternalTextures;
import net.fexcraft.mod.uni.IDL;
import net.fexcraft.mod.uni.UniReg;
import net.fexcraft.mod.uni.item.StackWrapper;
import net.fexcraft.mod.uni.tag.TagCW;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.TooltipFlag;

import java.util.*;

/**
 * @author Ferdinand Calo' (FEX___96)
 */
public class UniUI extends AbstractContainerScreen<UniCon> {

	public static UniUI INST;
	//protected LinkedHashMap<String, UITab> tabs = new LinkedHashMap<>();
	protected ArrayList<Component> comtip = new ArrayList<>();
	protected ArrayList<String> tooltip = new ArrayList<>();
	protected UserInterface ui;
	protected GuiGraphics matrix;
	protected UITab deftab;
	protected IDL actex;

	public UniUI(UniCon con, Inventory inventory, Component component){
		super(con, inventory, component);
		menu.setup(this);
		try{
			ui = UniReg.GUI.get(menu.ui_type).getConstructor(JsonMap.class, ContainerInterface.class).newInstance(menu.con.ui_map, menu.con);
			ui.root = this;
		}
		catch(Exception e){
			e.printStackTrace();
		}
		for(UIField field : ui.fields.values()){
			initField(field);
		}
		deftab = (UITab)ui.tabs.values().toArray()[0];
		ui.drawer = new UserInterface.Drawer() {
			private float[] colarr;
			@Override
			public void draw(float x, float y, int u, int v, int w, int h){
				matrix.blit((ResourceLocation)actex, (int)x, (int)y, u, v, w, h);
			}

			@Override
			public void drawFull(float x, float y, int w, int h){
				matrix.blit((ResourceLocation)actex, (int)x, (int)y, 0, 0, w, h, w, h);
			}

			@Override
			public void draw(int x, int y, StackWrapper stack){
				matrix.renderItem(stack.local(), x, y);
			}

			@Override
			public void bind(IDL texture){
				actex = texture;
				bindTexture(texture);
			}

			@Override
			public void apply(RGB color){
				colarr = color.toFloatArray();
				matrix.setColor(colarr[0], colarr[1], colarr[2], 1);
			}

			@Override
			public String translate(String str, Object... args){
				return Formatter.format(I18n.get(str, args));
			}

			@Override
			public IDL loadExternal(String urltex){
				return ExternalTextures.get("fcl", urltex);
			}

			@Override
			public void drawLine(double sx, double sy, double ex, double ey, float[] color){

			}
		};
		imageWidth = ui.width;
		imageHeight = ui.height;
		INST = this;
	}

	public void initField(UIField field){
		((UUIField)field).init();
		addWidget(((UUIField)field).field);
		field.visible(field.visible);
	}

	@Override
	protected void init(){
		super.init();
		ui.screen_width = width;
		ui.screen_height = height;
		ui.gLeft = leftPos;
		ui.gTop = topPos;
		for(UITab tab : ui.tabs.values()){
			for(UIField field : tab.fields.values()){
				if(((UUIField)field).field == null) continue;
				setFocused(((UUIField)field).field);
			}
		}
		ui.init();
	}

	@Override
	public boolean keyPressed(int key, int code, int mod){
		if(key == 256){
			if(ui.returnto != null){
				TagCW com = TagCW.create();
				com.set("return", true);
				menu.con.SEND_TO_SERVER.accept(com);
			}
			else minecraft.player.closeContainer();
			return true;
		}
		if(getFocused() instanceof EditBox){
			getFocused().keyPressed(key, code, mod);
			return true;
		}
		boolean inv = minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(key, code));
		return inv ? true : super.keyPressed(key, code, mod);
	}

	@Override
	public boolean keyReleased(int key, int code, int mod){
		if(getFocused() instanceof EditBox){
			getFocused().keyReleased(key, code, mod);
			return true;
		}
		return super.keyReleased(key, code, mod);
	}

	@Override
	public boolean charTyped(char c, int code){
		boolean inv = minecraft.options.keyInventory.isActiveAndMatches(InputConstants.getKey(c, code));
		if(getFocused() instanceof EditBox){
			getFocused().charTyped(c, code);
			return true;
		}
		boolean keytyped = super.charTyped(c, code);
		if(code == 1 || (inv && !keytyped)){
			if(ui.returnto != null){
				TagCW com = TagCW.create();
				com.set("return", true);
				menu.con.SEND_TO_SERVER.accept(com);
			}
			else minecraft.player.closeContainer();
			return false;
		}
		return true;
	}

	@Override
	public boolean mouseClicked(double mx, double my, int mb){
		if(ui.onClick((int)mx, (int)my, mb)) return true;
		return super.mouseClicked(mx, my, mb);
	}

	@Override
	protected void renderBg(GuiGraphics matrix, float ticks, int mx, int my){
		this.matrix = matrix;
		actex = deftab.texture;
		//if(ui.background) renderTransparentBackground(matrix);
		predraw(ticks, mx, my);
		drawbackground(ticks, mx, my);
		GlStateManager._clearColor(1.0F, 1.0F, 1.0F, 1.0F);
		GlStateManager._enableBlend();
		GlStateManager.glBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value, GlStateManager.SourceFactor.ONE.value, GlStateManager.DestFactor.ZERO.value);
		GlStateManager._blendFunc(GlStateManager.SourceFactor.SRC_ALPHA.value, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA.value);
		for(Iterator<UITab> it = ui.tabs.values().iterator(); it.hasNext();){
			UITab tab = it.next();
			if(!tab.visible()) continue;
			actex = tab.texture;
			bindTexture(tab.texture);
			tab.buttons.forEach((key, button) -> {
				button.hovered(leftPos, topPos, mx, my);
				button.draw(this, null, ticks, leftPos, topPos, mx, my);
			});
			tab.buttons.forEach((key, button) -> {
				if(button.text != null) button.text.draw(this, (UIElement)button, ticks, leftPos, topPos, mx, my);
			});
			tab.texts.forEach((key, text) -> text.draw(this, null, ticks, leftPos, topPos, mx, my));
			tab.fields.forEach((key, field) -> field.draw(this, null, ticks, leftPos, topPos, mx, my));
		}
		for(GuiEventListener w : children()){
			if(w instanceof AbstractWidget) ((AbstractWidget)w).render(matrix, mx, my, ticks);
		}
		postdraw(ticks, mx, my);
		tooltip.clear();
		for(UIButton button : ui.buttons.values()){
			if(button.visible() && button.tooltip != null && button.hovered(leftPos, topPos, mx, my)){
				tooltip.add(Formatter.format(I18n.get(button.tooltip)));
			}
		}
		ui.getTooltip(mx, my, tooltip);
		if(tooltip.size() > 0){
			comtip.clear();
			for(String str : tooltip) comtip.add(Component.literal(str));
			matrix.renderTooltip(minecraft.font, comtip, Optional.empty(), mx, my);
		}
		Slot slot = getSlotUnderMouse();
		if(slot != null && !slot.getItem().isEmpty()){
			List<Component> list = slot.getItem().getTooltipLines(menu.player, TooltipFlag.ADVANCED);
			matrix.renderTooltip(minecraft.font, list, Optional.empty(), mx, my);
		}
	}

	protected void predraw(float ticks, int mx, int my){
		ui.predraw(ticks, mx, my);
	}

	public void drawbackground(float ticks, int mx, int my){
		for(UITab tab : ui.tabs.values()){
			if(!tab.visible()) continue;
			int tx = tab.enabled() ? (tab.hovered() ? tab.htx : tab.tx) : tab.dtx;
			int ty = tab.enabled() ? (tab.hovered() ? tab.hty : tab.ty) : tab.dty;
			if(tab.absolute){
				matrix.blit((ResourceLocation)tab.texture, (tab.x < 0) ? (width + tab.x) : tab.x, (tab.y < 0) ? (height + tab.y) : tab.y, tx, ty, tab.width, tab.height);
				continue;
			}
			matrix.blit((ResourceLocation)tab.texture, leftPos + tab.x, topPos + tab.y, tx, ty, tab.width, tab.height);
		}
		ui.drawbackground(ticks, mx, my);
	}

	protected void postdraw(float ticks, int mx, int my){
		this.ui.postdraw(ticks, mx, my);
	}

	public void bindTexture(IDL texture){
		minecraft.textureManager.bindForSetup((ResourceLocation)texture);
	}

	//TODO
	public boolean mouseScrolled(double mx, double my, double delta){
		boolean exit = false;
		int x = (int)mx, y = (int)my, am = (delta > 0.0D) ? -1 : 1;
		for(UITab tab : ui.tabs.values()){
			if(!tab.visible()) continue;
			for(Map.Entry<String, UIButton> entry : tab.buttons.entrySet()){
				if(exit) break;
				if(entry.getValue().hovered(leftPos, topPos, x, y)){
					exit = (entry.getValue().onscroll(leftPos, topPos, x, y, am) || this.ui.onScroll(entry.getValue(), entry.getKey(), x, y, am));
				}
			}
			for(UIText text : tab.texts.values()){
				if(exit) break;
				if(text.hovered(leftPos, topPos, x, y)) exit = text.onscroll(leftPos, topPos, x, y, am);
			}
		}
		if(!exit) scrollwheel(am, x, y);
		return exit;
	}

	public void scrollwheel(int am, int x, int y){
		ui.scrollwheel(am, x, y);
	}

	@Override
	protected void renderLabels(GuiGraphics gg, int mx, int my){
		//
	}

}