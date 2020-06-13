package modfest.soulflame.client.entity.render;

import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.client.entity.render.model.GhostEntityModel;
import modfest.soulflame.entity.GhostEntity;

public class GhostEntityRenderer extends EntityRenderer<GhostEntity> {

    public static final Identifier TEXTURE = new Identifier(SoulFlame.MODID, "textures/entity/ghost.png");

    private final GhostEntityModel model;

    public GhostEntityRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.model = new GhostEntityModel();
        this.shadowRadius = 0.5f;
    }

    @Override
    public void render(GhostEntity entity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light) {
        super.render(entity, yaw, tickDelta, matrices, vertexConsumers, light);

        GhostEntityModel model = new GhostEntityModel();
        model.setAngles(entity, 0.0f, yaw, tickDelta, entity.getYaw(tickDelta), entity.getPitch(tickDelta));
        model.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(this.getTexture(entity))), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
    }

    @Override
    public Identifier getTexture(GhostEntity entity) {
        return TEXTURE;
    }

}
