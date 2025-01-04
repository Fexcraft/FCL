//FMT-Marker FVTM-1.6
package net.fexcraft.mod.fcl.util;

import net.fexcraft.lib.tmt.ModelRendererTurbo;

import java.util.ArrayList;

public class BPTModel {

	private static final ArrayList<ArrayList<ModelRendererTurbo>> groups = new ArrayList<>();

	public BPTModel(){
		super();
		int textureX = 128;
		int textureY = 128;
		//
		ArrayList<ModelRendererTurbo> blueprints = new ArrayList<ModelRendererTurbo>();
		blueprints.add(new ModelRendererTurbo(blueprints, 97, 31, textureX, textureY).addBox(-2, 0, 0, 6, 0.2f, 8)
			.setRotationPoint(3, -16.2f, -0.7f).setRotationAngle(0, -4, 0)
		);
		blueprints.add(new ModelRendererTurbo(blueprints, 35, 18, textureX, textureY).addBox(0, 0, 0, 6, 0.2f, 8)
			.setRotationPoint(-3, -16.4f, -0.4f).setRotationAngle(0, 4, 0)
		);
		blueprints.add(new ModelRendererTurbo(blueprints, 49, 0, textureX, textureY).addBox(-3, 0, 0, 6, 0.2f, 8)
			.setRotationPoint(-4, -16.2f, -0.6f).setRotationAngle(0, 5, 0)
		);
		groups.add(blueprints);
		//
		ArrayList<ModelRendererTurbo> glass = new ArrayList<ModelRendererTurbo>();
		glass.add(new ModelRendererTurbo(glass, 49, 10, textureX, textureY).newCylinderBuilder()
			.setPosition(0, 0, 0).setRadius(1.4f, 0.001f).setLength(0.5f).setSegments(16, 0).setScale(1, 1).setDirection(4)
			.setTopOffset(new net.fexcraft.lib.common.math.Vec3f(0.0, -0.4, 0.0)).setTopRotation(new net.fexcraft.lib.common.math.Vec3f(0.0, 0.0, 0.0)).build()
			.setRotationPoint(-5.2f, -17.2f, -5).setRotationAngle(0, 13, 0)
		);
		glass.add(new ModelRendererTurbo(glass, 56, 18, textureX, textureY).newCylinderBuilder()
			.setPosition(0, 0, 0).setRadius(2, 1.7f).setLength(1).setSegments(16, 0).setScale(0.8f, 0.8f).setDirection(4)
			.setTopOffset(new net.fexcraft.lib.common.math.Vec3f(0.0, -0.6, 0.0)).setTopRotation(new net.fexcraft.lib.common.math.Vec3f(0.0, 0.0, 0.0)).build()
			.setRotationPoint(-5.2f, -17.4f, -5).setRotationAngle(0, 13, 0)
		);
		glass.add(new ModelRendererTurbo(glass, 119, 8, textureX, textureY).addBox(-0.2f, 0, 1.5f, 0.4f, 0.2f, 3)
			.setRotationPoint(-5.2f, -17.2f, -5).setRotationAngle(0, -27, 0)
		);
		groups.add(glass);
		//
		ArrayList<ModelRendererTurbo> book5 = new ArrayList<ModelRendererTurbo>();
		book5.add(new ModelRendererTurbo(book5, 69, 39, textureX, textureY).addBox(0.1f, 0, 0, 3.7f, 0.6f, 6)
			.setRotationPoint(-7.5f, -16.8f, -7).setRotationAngle(0, 0, 0)
		);
		book5.add(new ModelRendererTurbo(book5, 48, 39, textureX, textureY).addBox(0, 0, 0, 3.8f, 0.2f, 6)
			.setRotationPoint(-7.5f, -16.2f, -7).setRotationAngle(0, 0, 0)
		);
		book5.add(new ModelRendererTurbo(book5, 0, 0, textureX, textureY)
			.addShapeBox(3.8f, 0, 0, 0.2f, 1, 6, 0, 0, 0, 0, 0, -0.1f, 0, 0, -0.1f, 0, 0, 0, 0, 0, 0, 0, 0, -0.1f, 0, 0, -0.1f, 0, 0, 0, 0)
			.setRotationPoint(-7.5f, -17, -7).setRotationAngle(0, 0, 0)
		);
		book5.add(new ModelRendererTurbo(book5, 27, 39, textureX, textureY).addBox(0, 0, 0, 3.8f, 0.2f, 6)
			.setRotationPoint(-7.5f, -17, -7).setRotationAngle(0, 0, 0)
		);
		groups.add(book5);
		//
		ArrayList<ModelRendererTurbo> pencils = new ArrayList<ModelRendererTurbo>();
		pencils.add(new ModelRendererTurbo(pencils, 123, 31, textureX, textureY).addBox(0, -6, 0, 0.3f, 6, 0.3f)
			.setRotationPoint(5, -16, -4.7f).setRotationAngle(3, 8, 14)
		);
		pencils.add(new ModelRendererTurbo(pencils, 118, 31, textureX, textureY).addBox(0, -6, 0, 0.3f, 6, 0.3f)
			.setRotationPoint(5, -16, -4.9f).setRotationAngle(13, 0, 9)
		);
		pencils.add(new ModelRendererTurbo(pencils, 35, 18, textureX, textureY).addBox(0, -6, 0, 0.3f, 6, 0.3f)
			.setRotationPoint(5, -16, -5.2f).setRotationAngle(15, 5, 0)
		);
		pencils.add(new ModelRendererTurbo(pencils, 9, 8, textureX, textureY).addBox(0, -6, 0, 0.3f, 6, 0.3f)
			.setRotationPoint(4.8f, -16, -4.6f).setRotationAngle(-1, -9, -14)
		);
		pencils.add(new ModelRendererTurbo(pencils, 122, 0, textureX, textureY).addBox(0, -6, 0, 0.3f, 6, 0.3f)
			.setRotationPoint(4.7f, -16, -4.9f).setRotationAngle(13, 7, -9)
		);
		groups.add(pencils);
		//
		ArrayList<ModelRendererTurbo> body = new ArrayList<ModelRendererTurbo>();
		body.add(new ModelRendererTurbo(body, 65, 0, textureX, textureY).addBox(0, 0, 0, 16, 15, 14.8f)
			.setRotationPoint(-8, -15, -7.8f).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 70, 31, textureX, textureY).addBox(0.1f, 0.1f, 0, 15.8f, 2.8f, 0.7f)
			.setRotationPoint(-8, -3, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 35, 31, textureX, textureY).addBox(0.1f, 0.1f, 0, 15.8f, 2.8f, 0.7f)
			.setRotationPoint(-8, -6, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 28, textureX, textureY).addBox(0.1f, 0.1f, 0, 15.8f, 2.8f, 0.7f)
			.setRotationPoint(-8, -9, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 23, textureX, textureY).addBox(0.1f, 0.1f, 0, 15.8f, 2.8f, 0.7f)
			.setRotationPoint(-8, -12, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 18, textureX, textureY).addBox(0.1f, 0.1f, 0, 15.8f, 2.8f, 0.7f)
			.setRotationPoint(-8, -15, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 0, textureX, textureY).addBox(0, 0, 0, 16, 1, 16)
			.setRotationPoint(-8, -16, -8).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 39, textureX, textureY).addBox(0.5f, 0.2f, 0, 15, 0.5f, 1)
			.setRotationPoint(-8, -15, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 66, 36, textureX, textureY).addBox(0.5f, 0.2f, 0, 15, 0.5f, 1)
			.setRotationPoint(-8, -12, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 33, 36, textureX, textureY).addBox(0.5f, 0.2f, 0, 15, 0.5f, 1)
			.setRotationPoint(-8, -9, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 36, textureX, textureY).addBox(0.5f, 0.2f, 0, 15, 0.5f, 1)
			.setRotationPoint(-8, -6, 7).setRotationAngle(0, 0, 0)
		);
		body.add(new ModelRendererTurbo(body, 0, 33, textureX, textureY).addBox(0.5f, 0.2f, 0, 15, 0.5f, 1)
			.setRotationPoint(-8, -3, 7).setRotationAngle(0, 0, 0)
		);
		groups.add(body);
		//
		ArrayList<ModelRendererTurbo> wrench = new ArrayList<ModelRendererTurbo>();
		wrench.add(new ModelRendererTurbo(wrench, 0, 8, textureX, textureY).addBox(-0.3f, 0, 0, 0.6f, 0.2f, 3)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		wrench.add(new ModelRendererTurbo(wrench, 49, 0, textureX, textureY)
			.addShapeBox(-0.8f, 0, 3, 1.6f, 0.2f, 0.6f, 0, -0.5f, 0, 0, -0.5f, 0, 0, 0, 0, 0, 0, 0, 0, -0.5f, 0, 0, -0.5f, 0, 0, 0, 0, 0, 0, 0, 0)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		wrench.add(new ModelRendererTurbo(wrench, 49, 3, textureX, textureY)
			.addShapeBox(-0.8f, 0, 3.6f, 0.5f, 0.2f, 0.8f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.2f, 0, 0)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		wrench.add(new ModelRendererTurbo(wrench, 9, 3, textureX, textureY)
			.addShapeBox(0.3f, 0, 3.6f, 0.5f, 0.2f, 0.8f, 0, 0, 0, 0, 0, 0, 0, -0.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.2f, 0, 0, 0, 0, 0)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		wrench.add(new ModelRendererTurbo(wrench, 9, 0, textureX, textureY)
			.addShapeBox(-0.8f, 0, -0.6f, 1.6f, 0.2f, 0.6f, 0, 0, 0, 0, 0, 0, 0, -0.5f, 0, 0, -0.5f, 0, 0, 0, 0, 0, 0, 0, 0, -0.5f, 0, 0, -0.5f, 0, 0)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		wrench.add(new ModelRendererTurbo(wrench, 0, 3, textureX, textureY)
			.addShapeBox(0.3f, 0, -1.4f, 0.5f, 0.2f, 0.8f, 0, 0, 0, 0, -0.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.2f, 0, 0, 0, 0, 0, 0, 0, 0)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		wrench.add(new ModelRendererTurbo(wrench, 0, 0, textureX, textureY)
			.addShapeBox(-0.8f, 0, -1.4f, 0.5f, 0.2f, 0.8f, 0, -0.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -0.2f, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0)
			.setRotationPoint(-1, -16.2f, -5).setRotationAngle(0, 30, 0)
		);
		groups.add(wrench);
		//
		ArrayList<ModelRendererTurbo> cup = new ArrayList<ModelRendererTurbo>();
		cup.add(new ModelRendererTurbo(cup, 113, 0, textureX, textureY).newCylinderBuilder()
			.setPosition(0, 0, 0).setRadius(2, 1.6f).setLength(3.5f).setSegments(12, 0).setScale(0.8f, 0.8f).setDirection(4)
			.setTopOffset(null).setTopRotation(new net.fexcraft.lib.common.math.Vec3f(0.0, 0.0, 0.0)).build()
			.setRotationPoint(5, -20, -5).setRotationAngle(0, 0, 0)
		);
		cup.add(new ModelRendererTurbo(cup, 70, 0, textureX, textureY).newCylinderBuilder()
			.setPosition(0, 0, 0).setRadius(2, 0.001f).setLength(0.5f).setSegments(12, 0).setScale(0.8f, 0.7f).setDirection(4)
			.setTopOffset(null).setTopRotation(new net.fexcraft.lib.common.math.Vec3f(0.0, 0.0, 0.0)).build()
			.setRotationPoint(5, -16.5f, -5).setRotationAngle(0, 0, 0)
		);
		groups.add(cup);
		//
	}

}
