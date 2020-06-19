package modfest.lacrimis.client.render.blocks;

import modfest.lacrimis.block.entity.TarotAdderEntity;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.item.TarotCardItem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformation;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;

public class TarotAdderEntityRenderer extends BlockEntityRenderer<TarotAdderEntity>{
    private final ItemRenderer itemRenderer;
    
    public TarotAdderEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
        this.itemRenderer = MinecraftClient.getInstance().getItemRenderer();
    }

    @Override
    public void render(TarotAdderEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if(entity.card != null) {
            matrices.push();
            matrices.translate(0, 10, 0);
            matrices.scale(0.5F, 0.5F, 0.5F);
            //matrices.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion((float)2 * 360.0F / 8.0F));
            TarotCardItem card = ModItems.tarotCards.get(entity.card);
            this.itemRenderer.renderItem(new ItemStack(card), ModelTransformation.Mode.FIXED, light, OverlayTexture.DEFAULT_UV, matrices, vertexConsumers);
            matrices.pop();
        }
            
    }

    public static void onInit() {

    }

}
