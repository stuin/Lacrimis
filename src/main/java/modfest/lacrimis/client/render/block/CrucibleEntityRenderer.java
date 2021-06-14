package modfest.lacrimis.client.render.block;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderDispatcher;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.model.BakedModel;
import net.minecraft.client.util.ModelIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.model.ModelLoadingRegistry;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.CrucibleEntity;

@Environment(EnvType.CLIENT)
public class CrucibleEntityRenderer implements BlockEntityRenderer<CrucibleEntity> {
	public static final ModelIdentifier CRUCIBLE_TEARS_MODEL_ID = new ModelIdentifier(new Identifier(Lacrimis.MODID, "crucible_tears"), "");

	public CrucibleEntityRenderer(BlockEntityRendererFactory.Context context) {

	}

	@Override
	public void render(CrucibleEntity entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		float level = entity.getRelativeLevel();
		if (level > 0) {
			float displayLevel = 4F / 16F + level * 11 / 16F;

			MinecraftClient mc = MinecraftClient.getInstance();
			BakedModel model = mc.getBakedModelManager().getModel(CrucibleEntityRenderer.CRUCIBLE_TEARS_MODEL_ID);
			BlockPos pos = entity.getPos();

			matrices.push();
			matrices.translate(0, displayLevel, 0);

			BlockState visualProperties = Blocks.GLOWSTONE.getDefaultState();
            mc.getBlockRenderManager()
					.getModelRenderer()
					.render(entity.getWorld(), model, visualProperties, pos, matrices, vertexConsumers.getBuffer(RenderLayer.getCutout()), false, entity.getWorld().getRandom(), 1, 0xFFFFFF);
			matrices.pop();
		}
	}

	public static void onInit() {
		ModelLoadingRegistry.INSTANCE.registerAppender((manager, out) -> {
			out.accept(CrucibleEntityRenderer.CRUCIBLE_TEARS_MODEL_ID);
		});
	}
}
