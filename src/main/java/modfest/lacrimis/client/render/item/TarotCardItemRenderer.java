package modfest.lacrimis.client.render.item;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.MatrixStack.Entry;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix3f;
import net.minecraft.util.math.Matrix4f;

import modfest.lacrimis.client.cardgen.CardTextureGen;
import modfest.lacrimis.item.TarotCardItem;
import modfest.lacrimis.util.TarotCardType;

public class TarotCardItemRenderer {

    public static void renderOverlay(ItemStack stack, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        CardTextureGen.updateTexturesIfNeeded();

        TarotCardType type = ((TarotCardItem) stack.getItem()).getType();

        Identifier tex = CardTextureGen.tarotCardTextureIds.get(type);
        VertexConsumer buffer = vertexConsumers.getBuffer(RenderLayer.getEntityCutout(tex));
        Entry tos = matrices.peek();
        Matrix3f normal = tos.getNormal();
        Matrix4f tr = tos.getModel();
        buffer.vertex(tr, 4.5f / 16f, 2.5f / 16f, 8.51f / 16f).color(1f, 1f, 1f, 1f).texture(0f, 0f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
        buffer.vertex(tr, 11f / 16f, 2.5f / 16f, 8.51f / 16f).color(1f, 1f, 1f, 1f).texture(1f, 0f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
        buffer.vertex(tr, 11f / 16f, 14 / 16f, 8.51f / 16f).color(1f, 1f, 1f, 1f).texture(1f, 1f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
        buffer.vertex(tr, 4.5f / 16f, 14 / 16f, 8.51f / 16f).color(1f, 1f, 1f, 1f).texture(0f, 1f).overlay(overlay).light(light).normal(normal, 0f, 0f, 1f).next();
    }

}
