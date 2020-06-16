package modfest.soulflame.client.cardgen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;

import org.lwjgl.opengl.GL11;

import net.dblsaiko.qcommon.croco.Mat4;
import net.dblsaiko.qcommon.croco.MatStack;
import modfest.soulflame.ClientSoulFlame;
import modfest.soulflame.SoulFlame;
import modfest.soulflame.client.texture.FramebufferBackedTexture;
import modfest.soulflame.init.ModItems;
import modfest.soulflame.util.TarotCardType;

public final class CardTextureGen {

    private static boolean needsGeneration = true;

    public static Map<TarotCardType, FramebufferBackedTexture> tarotCardTextures;
    public static Map<TarotCardType, Identifier> tarotCardTextureIds;

    private CardTextureGen() {
    }

    public static void register() {
        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new SimpleSynchronousResourceReloadListener() {
            @Override
            public void apply(ResourceManager manager) {
                needsGeneration = true;
            }

            @Override
            public Identifier getFabricId() {
                return new Identifier(SoulFlame.MODID, "tarot_overlay");
            }
        });
    }

    public static void updateTexturesIfNeeded() {
        if (!needsGeneration) return;
        needsGeneration = false;

        initTextures();

        MinecraftClient mc = MinecraftClient.getInstance();

        // Reset GL matrix stack
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);

        MatStack tr = new MatStack();
        tr.loadIdentity();
        // texture size as rendered on the item has an aspect ratio of 3:5
        tr.mul(Mat4.ortho(0, 3, 0, 5, -50, 50));

        Tessellator t = Tessellator.getInstance();
        VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(t.getBuffer());

        for (Entry<TarotCardType, FramebufferBackedTexture> e : tarotCardTextures.entrySet()) {
            TarotCardType type = e.getKey();
            FramebufferBackedTexture tex = e.getValue();

            tr.push();
            tr.translate(1.5f, 0.0f, 0.0f);
            tr.scale(3.5f, 3.5f, 3.5f);
            tr.rotate(1.0f, 0.0f, 0.0f, -22.5f);
            tr.rotate(0.0f, 1.0f, 0.0f, 45f);

            Framebuffer fb = tex.getFramebuffer();
            fb.clear(MinecraftClient.IS_SYSTEM_MAC);
            fb.beginWrite(true);

            Entity ent = Objects.requireNonNull(type.cover.create(new DummyWorld()));

            tr.translate(0.0f, (float) -ent.getEyeY() + 0.7f, 0.0f);
            EntityRenderer<? super Entity> renderer = MinecraftClient.getInstance().getEntityRenderManager().getRenderer(ent);

            renderer.render(ent, 0.0f, 1.0f, tr.toMatrixStack(), vcp, 0x00F000F0);

            vcp.draw();
            tr.pop();
        }

        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);

        mc.getFramebuffer().beginWrite(true);
    }

    private static void initTextures() {
        if (tarotCardTextures != null) return;

        HashMap<TarotCardType, FramebufferBackedTexture> map = new HashMap<>();
        HashMap<TarotCardType, Identifier> idMap = new HashMap<>();
        for (TarotCardType t : ModItems.tarotCards.keySet()) {
            if (t.cover == null) continue;

            Identifier id = new Identifier(SoulFlame.MODID, String.format("textures/special/card_%s.png", t.id));
            FramebufferBackedTexture tex = new FramebufferBackedTexture();
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, tex);
            map.put(t, tex);
            idMap.put(t, id);
        }
        tarotCardTextures = Collections.unmodifiableMap(map);
        tarotCardTextureIds = Collections.unmodifiableMap(idMap);
    }

}
