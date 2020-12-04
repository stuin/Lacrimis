package azoth.client.render;

import azoth.Azoth;
import azoth.blocks.entity.InfusionTableBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class InfusionTableEntityRenderer extends BlockEntityRenderer<InfusionTableBlockEntity> {
    public static final ModelIdentifier INFUSION_TABLE_AZOTH_MODEL_ID = new ModelIdentifier(Azoth.createID("infusion_table/azoth"), "");

    public InfusionTableEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(InfusionTableBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.shouldDisplayTears()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            BakedModel model = mc.getBakedModelManager().getModel(INFUSION_TABLE_AZOTH_MODEL_ID);
            BlockPos pos = entity.getPos();

            matrices.push();
            matrices.translate(0, 13 / 16F + 0.0005, 0);

            BlockState visualProperties = Blocks.GLOWSTONE.getDefaultState();
            mc.getBlockRenderManager()
                    .getModelRenderer()
                    .render(entity.getWorld(), model, visualProperties, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().getRandom(), 1, 0xFFFFFF);
            matrices.pop();
        }
    }
}
