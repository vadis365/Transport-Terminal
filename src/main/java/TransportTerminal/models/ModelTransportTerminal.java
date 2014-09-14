package TransportTerminal.models;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;

public class ModelTransportTerminal extends ModelBase {

	ModelRenderer console;
	ModelRenderer support;
	ModelRenderer base;
	ModelRenderer pad;
	ModelRenderer swirl;

	public ModelTransportTerminal() {
		textureWidth = 64;
		textureHeight = 64;

		console = new ModelRenderer(this, 0, 0);
		console.addBox(-6.5F, -1F, 10.5F, 13, 5, 4);
		console.setRotationPoint(0F, 0F, 0F);
		setRotation(console, -1.047198F, 0.0174533F, 0F);
		support = new ModelRenderer(this, 0, 10);
		support.addBox(-4.5F, 12F, 5.5F, 9, 11, 2);
		support.setRotationPoint(0F, 0F, 0F);
		setRotation(support, 0F, 0F, 0F);
		base = new ModelRenderer(this, 0, 36);
		base.addBox(-8F, 23F, -8F, 16, 1, 16);
		base.setRotationPoint(0F, 0F, 0F);
		setRotation(base, 0F, 0F, 0F);
		pad = new ModelRenderer(this, 0, 24);
		pad.addBox(-7F, 22F, -7F, 14, 1, 10);
		pad.setRotationPoint(0F, 0F, 0F);
		setRotation(pad, 0F, 0F, 0F);
		swirl = new ModelRenderer(this, 27, 1);
		swirl.addBox(-6F, 21.9F, -6F, 12, 1, 8);
		swirl.setRotationPoint(0F, 0F, 0F);
		setRotation(swirl, 0F, 0F, 0F);
	}

	public void render() {
		console.render(0.0625F);
		support.render(0.0625F);
		base.render(0.0625F);
		pad.render(0.0625F);
		swirl.render(0.0625F);
	}

	private void setRotation(ModelRenderer model, float x, float y, float z) {
		model.rotateAngleX = x;
		model.rotateAngleY = y;
		model.rotateAngleZ = z;
	}
}
