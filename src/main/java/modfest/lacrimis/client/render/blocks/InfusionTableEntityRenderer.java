package modfest.lacrimis.client.render.blocks;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
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
public class InfusionTableEntityRenderer extends BlockEntityRenderer<InfusionTableEntity> {
    public static final ModelIdentifier INFUSION_TABLE_OVERLAY_MODEL_ID = new ModelIdentifier(Lacrimis.MODID + ":infusion_table_overlay", "");

    public InfusionTableEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
        super(dispatcher);
    }

    @Override
    public void render(InfusionTableEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        if (entity.getTank().getTears() > 0) {
            MinecraftClient mc = MinecraftClient.getInstance();
            BakedModel model = mc.getBakedModelManager().getModel(InfusionTableEntityRenderer.INFUSION_TABLE_OVERLAY_MODEL_ID);
            BlockPos pos = entity.getPos();

            matrices.push();
            matrices.translate(0, 12 / 16F + 0.0005, 0);

            BlockState visualProperties = Blocks.GLOWSTONE.getDefaultState();
            mc.getBlockRenderManager()
                    .getModelRenderer()
                    .render(entity.getWorld(), model, visualProperties, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().getRandom(), 1, 0xFFFFFF);
            matrices.pop();
        }
    }

    public static void onInit() {
        ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
            out.accept(InfusionTableEntityRenderer.INFUSION_TABLE_OVERLAY_MODEL_ID);
        });
    }
}
