package modfest.soulflame.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.model.Model;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.model.CreeperEntityModel;
import net.minecraft.client.render.entity.model.PiglinEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map.Entry;

import org.lwjgl.opengl.GL11;

import net.dblsaiko.qcommon.croco.Mat4;
import net.dblsaiko.qcommon.croco.MatStack;
import modfest.soulflame.ClientSoulFlame;
import modfest.soulflame.SoulFlame;
import modfest.soulflame.client.texture.FramebufferBackedTexture;
import modfest.soulflame.init.ModItems;
import modfest.soulflame.util.TarotCardType;

public final class CardTextureGen {

    private CardTextureGen() {
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void apply(ResourceManager manager) {
                initTextures();

                MinecraftClient mc = MinecraftClient.getInstance();

                RenderSystem.loadIdentity();
                RenderSystem.matrixMode(GL11.GL_PROJECTION);
                RenderSystem.loadIdentity();
                RenderSystem.matrixMode(GL11.GL_MODELVIEW);

                MatStack tr = new MatStack();
                tr.loadIdentity();
                // texture size as rendered on the item has an aspect ratio of 3:5
                tr.mul(Mat4.ortho(0, 3, 5, 0, 50, -50));

                Tessellator t = Tessellator.getInstance();
                VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(t.getBuffer());

                for (Entry<TarotCardType, FramebufferBackedTexture> e : ClientSoulFlame.tarotCardTextures.entrySet()) {
                    TarotCardType type = e.getKey();
                    FramebufferBackedTexture tex = e.getValue();

                    tr.push();
                    tr.translate(1.5f, entityOffsetY(type), 0.0f);
                    tr.scale(4.0f, 4.0f, 4.0f);
                    tr.rotate(1.0f, 0.0f, 0.0f, -22.5f);
                    tr.rotate(0.0f, 1.0f, 0.0f, 45f);
                    MatrixStack ojang = tr.toMatrixStack();

                    Framebuffer fb = tex.getFramebuffer();
                    fb.clear(MinecraftClient.IS_SYSTEM_MAC);
                    fb.beginWrite(true);
                    Model model = prepareModel(type);
                    model.render(ojang, getBuffer(vcp, type), 0x00F000F0, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
                    vcp.draw();
                    tr.pop();
                }
                mc.getFramebuffer().beginWrite(true);
            }

            @Override
            public Identifier getFabricId() {
                return new Identifier(SoulFlame.MODID, "tarot_overlay");
            }
        });
    }

    private static VertexConsumer getBuffer(VertexConsumerProvider vcp, TarotCardType type) {
        switch (type) {
            case TEST:
                return vcp.getBuffer(RenderLayer.getEntitySolid(new Identifier("textures/entity/creeper/creeper.png")));
            case TEST1:
                return vcp.getBuffer(RenderLayer.getEntitySolid(new Identifier("textures/entity/piglin/piglin.png")));
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    private static float entityOffsetY(TarotCardType type) {
        switch(type) {
            case TEST:
                return 2f;
            case TEST1:
                return 0f;
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    private static Model prepareModel(TarotCardType type) {
        switch (type) {
            case TEST: {
                CreeperEntityModel<?> model = new CreeperEntityModel<>(0.0f);
                model.setAngles(null, 0.0f, 0.0f, 0.0f, 45.0f, -22.0f);
                return model;
            }
            case TEST1: {
                PiglinEntityModel<?> model = new PiglinEntityModel<>(0.0f, 64, 64);
                model.head.yaw = -45.0f * (float) (Math.PI / 2.0f);
                model.head.pitch = -22.0f * (float) (Math.PI / 2.0f);
                return model;
            }
            default:
                throw new IllegalStateException("unreachable");
        }
    }

    public static void initTextures() {
        if (ClientSoulFlame.tarotCardTextures != null) return;

        HashMap<TarotCardType, FramebufferBackedTexture> map = new HashMap<>();
        HashMap<TarotCardType, Identifier> idMap = new HashMap<>();
        for (TarotCardType t : ModItems.tarotCards.keySet()) {
            Identifier id = new Identifier(SoulFlame.MODID, String.format("textures/special/card_%s.png", t.id));
            FramebufferBackedTexture tex = new FramebufferBackedTexture();
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, tex);
            map.put(t, tex);
            idMap.put(t, id);
        }
        ClientSoulFlame.tarotCardTextures = Collections.unmodifiableMap(map);
        ClientSoulFlame.tarotCardTextureIds = Collections.unmodifiableMap(idMap);
    }

}
