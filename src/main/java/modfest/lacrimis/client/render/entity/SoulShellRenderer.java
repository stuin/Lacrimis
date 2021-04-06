package modfest.lacrimis.client.render.entity;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.entity.SoulShellEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.*;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

@Environment(EnvType.CLIENT)
public class SoulShellRenderer extends LivingEntityRenderer<SoulShellEntity, SoulShellModel<SoulShellEntity>> {
    public static final Identifier TEXTURE = new Identifier(Lacrimis.MODID, "textures/entity/ghost.png");

    public SoulShellRenderer(EntityRenderDispatcher dispatcher) {
        super(dispatcher, new SoulShellModel(0.0F), 0.5F);
        this.addFeature(new ArmorFeatureRenderer(this, new BipedEntityModel(0.5F), new BipedEntityModel(1.0F)));
        this.addFeature(new HeldItemFeatureRenderer(this));
        this.addFeature(new StuckArrowsFeatureRenderer(this));
        this.addFeature(new HeadFeatureRenderer(this));
        this.addFeature(new ElytraFeatureRenderer(this));
        this.addFeature(new StuckStingersFeatureRenderer(this));
    }

    public void render(SoulShellEntity abstractClientPlayerEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        this.setModelPose(abstractClientPlayerEntity);
        super.render(abstractClientPlayerEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    public Vec3d getPositionOffset(SoulShellEntity abstractClientPlayerEntity, float f) {
        return abstractClientPlayerEntity.isInSneakingPose() ? new Vec3d(0.0D, -0.125D, 0.0D) : super.getPositionOffset(abstractClientPlayerEntity, f);
    }

    private void setModelPose(SoulShellEntity abstractClientPlayerEntity) {
        SoulShellModel<SoulShellEntity> playerEntityModel = this.getModel();
        if (abstractClientPlayerEntity.isSpectator()) {
            playerEntityModel.setVisible(false);
            playerEntityModel.head.visible = true;
            playerEntityModel.helmet.visible = true;
        } else {
            playerEntityModel.setVisible(true);
            BipedEntityModel.ArmPose armPose = getArmPose(abstractClientPlayerEntity, Hand.MAIN_HAND);
            BipedEntityModel.ArmPose armPose2 = getArmPose(abstractClientPlayerEntity, Hand.OFF_HAND);
            if (armPose.method_30156()) {
                armPose2 = abstractClientPlayerEntity.getOffHandStack().isEmpty() ? BipedEntityModel.ArmPose.EMPTY : BipedEntityModel.ArmPose.ITEM;
            }

            if (abstractClientPlayerEntity.getMainArm() == Arm.RIGHT) {
                playerEntityModel.rightArmPose = armPose;
                playerEntityModel.leftArmPose = armPose2;
            } else {
                playerEntityModel.rightArmPose = armPose2;
                playerEntityModel.leftArmPose = armPose;
            }
        }

    }

    private static BipedEntityModel.ArmPose getArmPose(SoulShellEntity abstractClientPlayerEntity, Hand hand) {
        ItemStack itemStack = abstractClientPlayerEntity.getStackInHand(hand);
        if (itemStack.isEmpty()) {
            return BipedEntityModel.ArmPose.EMPTY;
        } else {
            return BipedEntityModel.ArmPose.ITEM;
        }
    }

    public Identifier getTexture(SoulShellEntity abstractClientPlayerEntity) {
        return TEXTURE;
    }

    protected void scale(SoulShellEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f) {
        matrixStack.scale(0.9375F, 0.9375F, 0.9375F);
    }

    public void renderRightArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SoulShellEntity player) {
        this.renderArm(matrices, vertexConsumers, light, player, model.rightArm, model.rightSleeve);
    }

    public void renderLeftArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SoulShellEntity player) {
        this.renderArm(matrices, vertexConsumers, light, player, model.leftArm, model.leftSleeve);
    }

    private void renderArm(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, SoulShellEntity player, ModelPart arm, ModelPart sleeve) {
        this.setModelPose(player);
        model.handSwingProgress = 0.0F;
        model.sneaking = false;
        model.leaningPitch = 0.0F;
        model.setAngles(player, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
        arm.pitch = 0.0F;
        arm.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
        sleeve.pitch = 0.0F;
        sleeve.render(matrices, vertexConsumers.getBuffer(RenderLayer.getEntityTranslucent(TEXTURE)), light, OverlayTexture.DEFAULT_UV);
    }

    protected void setupTransforms(SoulShellEntity abstractClientPlayerEntity, MatrixStack matrixStack, float f, float g, float h) {
        float i = abstractClientPlayerEntity.getLeaningPitch(h);
        float n;
        float k;
        if (abstractClientPlayerEntity.isFallFlying()) {
            super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
            n = (float)abstractClientPlayerEntity.getRoll() + h;
            k = MathHelper.clamp(n * n / 100.0F, 0.0F, 1.0F);
            if (!abstractClientPlayerEntity.isUsingRiptide()) {
                matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(k * (-90.0F - abstractClientPlayerEntity.pitch)));
            }

            Vec3d vec3d = abstractClientPlayerEntity.getRotationVec(h);
            Vec3d vec3d2 = abstractClientPlayerEntity.getVelocity();
            double d = Entity.squaredHorizontalLength(vec3d2);
            double e = Entity.squaredHorizontalLength(vec3d);
            if (d > 0.0D && e > 0.0D) {
                double l = (vec3d2.x * vec3d.x + vec3d2.z * vec3d.z) / Math.sqrt(d * e);
                double m = vec3d2.x * vec3d.z - vec3d2.z * vec3d.x;
                matrixStack.multiply(Vector3f.POSITIVE_Y.getRadialQuaternion((float)(Math.signum(m) * Math.acos(l))));
            }
        } else if (i > 0.0F) {
            super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
            n = abstractClientPlayerEntity.isTouchingWater() ? -90.0F - abstractClientPlayerEntity.pitch : -90.0F;
            k = MathHelper.lerp(i, 0.0F, n);
            matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(k));
            if (abstractClientPlayerEntity.isInSwimmingPose()) {
                matrixStack.translate(0.0D, -1.0D, 0.30000001192092896D);
            }
        } else {
            super.setupTransforms(abstractClientPlayerEntity, matrixStack, f, g, h);
        }

    }
}

