package azoth.client.render;

import azoth.Azoth;
import azoth.common.blocks.entity.AzothCauldronBlockEntity;
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
public class CrucibleEntityRenderer extends BlockEntityRenderer<AzothCauldronBlockEntity> {
    public static final ModelIdentifier CRUCIBLE_AZOTH_MODEL_ID = new ModelIdentifier(Azoth.createID("special/crucible_azoth"), "");

    public CrucibleEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(AzothCauldronBlockEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        float level = entity.getLevelForDisplay();
        if (level > 0) {
            MinecraftClient mc = MinecraftClient.getInstance();
            BakedModel model = mc.getBakedModelManager().getModel(CRUCIBLE_AZOTH_MODEL_ID);
            BlockPos pos = entity.getPos();

            matrices.push();

            float dy = 4F / 16F + level * 11 / 16F;
            matrices.translate(0, dy, 0);

            BlockState visualProperties = Blocks.GLOWSTONE.getDefaultState();
            mc.getBlockRenderManager()
                    .getModelRenderer()
                    .render(entity.getWorld(), model, visualProperties, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().getRandom(), 1, 0xFFFFFF);
            matrices.pop();
        }
    }
}
