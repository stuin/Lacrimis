package modfest.lacrimis.client.render.block;

import modfest.lacrimis.block.entity.NetworkLinkEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;
import net.minecraft.util.math.Vec3f;

@Environment(EnvType.CLIENT)
public class NetworkLinkEntityRenderer implements BlockEntityRenderer<NetworkLinkEntity> {
    public static final Identifier BEAM_TEXTURE = new Identifier("textures/entity/beacon_beam.png");

    public NetworkLinkEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    public void render(NetworkLinkEntity beaconBlockEntity, float f, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, int j) {
        long l = beaconBlockEntity.getWorld().getTime();

        if(beaconBlockEntity.getColorComponents() != null && beaconBlockEntity.isOn())
            renderLightBeam(matrixStack, vertexConsumerProvider, BEAM_TEXTURE, f, 1.0F, l, 1, 1024, beaconBlockEntity.getColorComponents(), 0.2F, 0.25F);
    }

    public static void renderLightBeam(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, Identifier identifier, float f, float g, long l, int i, int j, float[] fs, float h, float k) {
        int m = i + j;
        matrixStack.push();
        matrixStack.translate(0.5D, 0.0D, 0.5D);
        float n = (float)Math.floorMod(l, 40L) + f;
        float o = j < 0 ? n : -n;
        float p = MathHelper.fractionalPart(o * 0.2F - (float)MathHelper.floor(o * 0.1F));
        float q = fs[0];
        float r = fs[1];
        float s = fs[2];
        matrixStack.push();
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(n * 2.25F - 45.0F));
        float af = 0.0F;
        float ai = 0.0F;
        float aj = -h;
        float y = 0.0F;
        float z = 0.0F;
        float aa = -h;
        float an = 0.0F;
        float ao = 1.0F;
        float ap = -1.0F + p;
        float aq = (float)j * g * (0.5F / h) + ap;
        method_22741(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(identifier, false)), q, r, s, 1.0F, i, m, 0.0F, h, h, 0.0F, aj, 0.0F, 0.0F, aa, 0.0F, 1.0F, aq, ap);
        matrixStack.pop();
        af = -k;
        float ag = -k;
        ai = -k;
        aj = -k;
        an = 0.0F;
        ao = 1.0F;
        ap = -1.0F + p;
        aq = (float)j * g + ap;
        method_22741(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getBeaconBeam(identifier, true)), q, r, s, 0.125F, i, m, af, ag, k, ai, aj, k, k, k, 0.0F, 1.0F, aq, ap);
        matrixStack.pop();
    }

    private static void method_22741(MatrixStack matrixStack, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s, float t, float u, float v, float w) {
        MatrixStack.Entry entry = matrixStack.peek();
        Matrix4f matrix4f = entry.getModel();
        Matrix3f matrix3f = entry.getNormal();
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, l, m, n, o, t, u, v, w);
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, r, s, p, q, t, u, v, w);
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, n, o, r, s, t, u, v, w);
        method_22740(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, k, p, q, l, m, t, u, v, w);
    }

    private static void method_22740(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, int k, float l, float m, float n, float o, float p, float q, float r, float s) {
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, k, l, m, q, r);
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, l, m, q, s);
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, j, n, o, p, s);
        method_23076(matrix4f, matrix3f, vertexConsumer, f, g, h, i, k, n, o, p, r);
    }

    private static void method_23076(Matrix4f matrix4f, Matrix3f matrix3f, VertexConsumer vertexConsumer, float f, float g, float h, float i, int j, float k, float l, float m, float n) {
        vertexConsumer.vertex(matrix4f, k, (float)j, l).color(f, g, h, i).texture(m, n).overlay(OverlayTexture.DEFAULT_UV).light(15728880).normal(matrix3f, 0.0F, 1.0F, 0.0F).next();
    }

    public boolean rendersOutsideBoundingBox(NetworkLinkEntity beaconBlockEntity) {
        return true;
    }
}
