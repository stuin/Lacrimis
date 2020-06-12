package modfest.soulflame.block.entity.renderer;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.CrucibleEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;
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
public class CrucibleEntityRenderer extends BlockEntityRenderer<CrucibleEntity> {
	public static final ModelIdentifier CRUCIBLE_TEARS_MODEL_ID = new ModelIdentifier(SoulFlame.MODID + ":crucible_tears", "");

	public CrucibleEntityRenderer(BlockEntityRenderDispatcher dispatcher) {
		super(dispatcher);
	}

	@Override
	public void render(CrucibleEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		MinecraftClient mc = MinecraftClient.getInstance();
		BakedModel model = mc.getBakedModelManager().getModel(CrucibleEntityRenderer.CRUCIBLE_TEARS_MODEL_ID);
		BlockPos pos = entity.getPos();

		float level = 4 / 16F + entity.getRelativeLevel() * 11 / 16F;

		matrices.push();
		matrices.translate(0, level, 0);
		mc.getBlockRenderManager()
				.getModelRenderer()
				.render(entity.getWorld(), model, entity.getCachedState(), pos, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().getRandom(), 1, 0xFFFFFF);
		matrices.pop();
	}

	public static void onInit() {
		ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
			out.accept(CrucibleEntityRenderer.CRUCIBLE_TEARS_MODEL_ID);
		});
	}
}
