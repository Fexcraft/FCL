package net.fexcraft.lib.mc.gui;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.annotation.Nullable;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.fexcraft.lib.common.math.RGB;
import net.fexcraft.lib.mc.network.PacketHandler;
import net.fexcraft.lib.mc.network.packet.PacketNBTTagCompound;
import net.fexcraft.lib.mc.utils.Print;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class GenericGui<CONTAINER extends GenericContainer> extends GuiContainer {

	protected ResourceLocation texloc = null;
    protected TreeMap<String, BasicButton> buttons = new TreeMap<>();
    protected TreeMap<String, BasicText> texts = new TreeMap<>();
    protected TreeMap<String, TextField> fields = new TreeMap<>();
    protected CONTAINER container;
    protected boolean deftexrect = true;
    protected boolean defbackground = true;
    protected EntityPlayer player;
    
    public GenericGui(ResourceLocation texture, GenericContainer container, EntityPlayer player){
    	super(container == null ? new GenericContainer.DefImpl(player) : container);
    	this.texloc = texture == null ? new ResourceLocation("minecraft:textures/blocks/stone.png") : texture;
    	this.container = (CONTAINER)this.inventorySlots;
    	this.container.setPlayer(this.player = player);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.renderHoveredToolTip(mouseX, mouseY);
    }
    
    @Override
    public void initGui(){
        super.initGui();
        buttons.clear();
        texts.clear();
        fields.clear();
        init();
    }

	@Override
    protected void drawGuiContainerBackgroundLayer(float pticks, int mouseX, int mouseY){
		if(defbackground) super.drawDefaultBackground();
    	predraw(pticks, mouseX, mouseY);
    	this.mc.getTextureManager().bindTexture(texloc);
        if(deftexrect) this.drawTexturedModalRect(guiLeft, guiTop, 0, 0, this.xSize, this.ySize);
    	drawbackground(pticks, mouseX, mouseY);
    	//
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
    	buttons.forEach((key, button) -> {
    		button.hovered(mouseX, mouseY);
    		button.draw(this, pticks, mouseX, mouseY);
    	});
    	texts.forEach((key, text) -> {
    		text.draw(this, pticks, mouseX, mouseY);
    	});
    	fields.forEach((key, elm) -> elm.drawTextBox());
    	drawlast(pticks, mouseX, mouseY);
    }
    
	/** Client Side Method. */
    public static void openGui(int gui, int[] xyz, String listener){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("target_listener", listener == null ? "fcl_gui" : listener);
        compound.setString("task", "open_gui");
        compound.setInteger("gui", gui);
        if(xyz != null) compound.setIntArray("args", xyz);
        PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(compound));
    }

	/** Client Side Method. */
    public static void openGui(int gui, int[] xyz, String listener, NBTTagCompound data){
        NBTTagCompound compound = new NBTTagCompound();
        compound.setString("target_listener", listener == null ? "fcl_gui" : listener);
        compound.setString("task", "open_gui");
        compound.setInteger("gui", gui);
        compound.setTag("data", data);
        if(xyz != null) compound.setIntArray("args", xyz);
        PacketHandler.getInstance().sendToServer(new PacketNBTTagCompound(compound));
    }
    
    protected abstract void init();

	protected abstract void predraw(float pticks, int mouseX, int mouseY);
    
    protected abstract void drawbackground(float pticks, int mouseX, int mouseY);
    
    protected void drawlast(float pticks, int mouseX, int mouseY){}

	protected abstract boolean buttonClicked(int mouseX, int mouseY, int mouseButton, String key, BasicButton button);

	protected abstract void scrollwheel(int am, int x, int y);
    
    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
    	for(java.util.Map.Entry<String, BasicButton> entry : buttons.entrySet()){
    		if(entry.getValue().mousePressed(this.mc, mouseX, mouseY)){
    			//can't add the forge event as it needs a _GuiButton_, this isn't one.
    			Print.debug("[GG] Button Pressed: " + entry.getKey() + " / " + mouseButton);
    			buttonClicked(mouseX, mouseY, mouseButton, entry.getKey(), entry.getValue());
    			return;
    		}
    	}
        if(!fields.isEmpty()){
        	for(TextField field : fields.values()){
        		if(field.mouseClicked(mouseX, mouseY, mouseButton)) return;
        	}
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

	public static class BasicButton {
    	
		public int x, y, tx, ty, sizex, sizey;
		public boolean enabled, visible = true, hovered;
    	public String name; private RGB rgb;
    	public RGB rgb_disabled = new RGB(119, 119, 119, 0.5f);
    	public RGB rgb_none = new RGB(255, 255, 255, 0.5f);
    	public RGB rgb_hover = new RGB(244, 215,  66, 0.5f);
    	
    	public BasicButton(String name, int x, int y, int tx, int ty, int sizex, int sizey, boolean enabled){
    		this.name = name; this.x = x; this.y = y; this.sizex = sizex; this.sizey = sizey;
    		this.enabled = enabled; this.tx = tx; this.ty = ty;
    	}

		public boolean mousePressed(Minecraft mc, int mouseX, int mouseY){
			return enabled && visible && mouseX >= x && mouseY >= y && mouseX < x + sizex && mouseY < y + sizey;
		}

		public boolean hovered(int mouseX, int mouseY){
			return hovered = mouseX >= x && mouseY >= y && mouseX < x + sizex && mouseY < y + sizey;
		}

		public void draw(GenericGui<?> gui, float pticks, int mouseX, int mouseY){
			if(!visible) return;
			rgb = hovered ? enabled ? rgb_hover : rgb_disabled : rgb_none;
			RGB.glColorReset();
            rgb.glColorApply();
            gui.drawTexturedModalRect(x, y, tx, ty, sizex, sizey);
            RGB.glColorReset();
		}

		public boolean scrollwheel(int am, int x, int y){ return false; }
    	
    }
	
	public static class BasicText {
		
		private static final RGB defcolor = new RGB(128, 128, 128);
		public int x, y, width, color, hovercolor = new RGB(244, 215,  66, 0.5f).packed;
		public String string;
		public boolean visible = true, hovered, hoverable;
		public float scale;
		
		public BasicText(int x, int y, int width, @Nullable Integer color, String string){
			this.x = x; this.y = y; this.width = width;
			this.string = string; this.color = color == null ? defcolor.packed : color;
		}

		public BasicText(int x, int y, int width, @Nullable Integer color, String string, boolean hover, @Nullable Integer hovercolor){
			this(x, y, width, color, string);
			this.hoverable = hover;
			if(hovercolor != null) this.hovercolor = hovercolor;
		}
		
		public boolean hovered(int mouseX, int mouseY){
			return hoverable ? hovered = mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + 8 : false;
		}

		public boolean scrollwheel(int am, int x, int y){ return false; }
		
		public void translate(){
			this.string = I18n.format(string);
		}

		public void translate(Object... objects){
			this.string = I18n.format(string, objects);
		}

		public BasicText scale(float scale){
			this.scale = scale;
			return this;
		}

		public BasicText autoscale(){
			this.scale = -1;
			return this;
		}
		
		public void draw(GenericGui<?> gui, float pticks, int mouseX, int mouseY){
			if(!visible) return;
			hovered(mouseX, mouseY);
			if(scale == 0 || (scale < 0 && gui.mc.fontRenderer.getStringWidth(string) < width)){
            	gui.mc.fontRenderer.drawString(string, x, y, hovered ? hovercolor : color);
				return;
			}
			else{
				float scale = this.scale < 0 ? (float)width / gui.mc.fontRenderer.getStringWidth(string) : this.scale;
        		GL11.glPushMatrix();
        		GL11.glTranslatef(x, y, 0);
        		GL11.glScalef(scale, scale, scale);
            	gui.mc.fontRenderer.drawString(string, 0, 0, hovered ? hovercolor : color);
            	GL11.glPopMatrix();
			}
		}
		
	}
	
	public static class TextField extends GuiTextField {

		public TextField(int id, FontRenderer renderer, int x, int y, int width, int height){
			this(id, renderer, x, y, width, height, true);
		}
		
		public TextField(int id, FontRenderer renderer, int x, int y, int width, int height, boolean draw){
			super(id, renderer, x, y, width, height);
			this.setEnableBackgroundDrawing(draw);
		}
		
		public Integer getIntegerValue(){
			try{ return Integer.parseInt(this.getText()); }
			catch(Exception e){ e.printStackTrace(); return null; }
		}
		
		public Float getValue(){
			try{ return Float.parseFloat(this.getText()); }
			catch(Exception e){ e.printStackTrace(); return null; }
		}
		
	    public TextField setMaxLength(int length){
	        super.setMaxStringLength(length); return this;
	    }
		
	}
	
	public static class NumberField extends TextField {
		
		private String regex = "[^\\d\\-\\.\\,]";

		public NumberField(int id, FontRenderer renderer, int x, int y, int width, int height){
			super(id, renderer, x, y, width, height, true);
		}
		
		public NumberField(int id, FontRenderer renderer, int x, int y, int width, int height, boolean draw){
			super(id, renderer, x, y, width, height, draw);
		}
		
		public NumberField setRegex(String regex){
			this.regex = regex; return this;
		}
		
		@Override
	    public void writeText(String text){
	        super.writeText(text.replaceAll(regex, ""));
	    }
		
	}
	
	//---///----////----///---//
	
    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException{
        if(!fields.isEmpty()){
        	boolean bool = false;
        	for(Entry<String, TextField> entry : fields.entrySet()){
        		if(bool) break;
        		if(entry.getValue().textboxKeyTyped(typedChar, keyCode)) bool = true;
        	}
            if(bool && !this.mc.gameSettings.keyBindInventory.isActiveAndMatches(keyCode)) super.keyTyped(typedChar, keyCode);
        }
        if(keyCode == 1) player.closeScreen();
    }
    
    @Override
    public void handleMouseInput() throws IOException {
        super.handleMouseInput();
		int e = Mouse.getEventDWheel(); if(e == 0){ return; }
		int am = e > 0 ? -1 : 1;
		int x = Mouse.getEventX() * this.width / this.mc.displayWidth;
		int y = this.height - Mouse.getEventY() * this.height / this.mc.displayHeight - 1;
		boolean exit = false;
		for(BasicButton button : buttons.values()){
			if(exit) break;
			if(button.hovered(x, y)) exit = button.scrollwheel(am, x, y);
		}
		for(BasicText button : texts.values()){
			if(exit) break;
			if(button.hovered(x, y)) exit = button.scrollwheel(am, x, y);
		}
		if(!exit) this.scrollwheel(am, x, y);
    }

	public ResourceLocation getTexLoc(){
		return texloc;
	}

}
