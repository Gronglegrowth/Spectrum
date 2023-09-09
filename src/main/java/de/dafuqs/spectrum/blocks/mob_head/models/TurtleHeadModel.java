package de.dafuqs.spectrum.blocks.mob_head.models;

import de.dafuqs.spectrum.blocks.mob_head.*;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;

public class TurtleHeadModel extends SpectrumHeadModel {

    public TurtleHeadModel(ModelPart root) {
        super(root);
    }

    public static TexturedModelData getTexturedModelData() {
        ModelData modelData = new ModelData();
        ModelPartData modelPartData = modelData.getRoot();
    
        modelPartData.addChild(EntityModelPartNames.HEAD, ModelPartBuilder.create()
                .uv(3, 0).cuboid(-11.0F, -5.0F, 5.0F, 6.0F, 5.0F, 6.0F, new Dilation(0.0F)), ModelTransform.pivot(8.0F, 24.0F, -8.0F));
    
        return TexturedModelData.of(modelData, 128, 64);
    }

}