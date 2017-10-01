package net.fexcraft.mod.lib.tmt;

import net.minecraft.entity.Entity;

public abstract class Model<T> extends net.minecraft.client.model.ModelBase {
	
	public abstract void render();
	
	public abstract void render(ModelRendererTurbo[] model);
	
	public abstract void render(T type, Entity element);
	
	protected abstract void translate(ModelRendererTurbo[] model, float x, float y, float z);
	
	protected abstract void rotate(ModelRendererTurbo[] model, float x, float y, float z);
	
}