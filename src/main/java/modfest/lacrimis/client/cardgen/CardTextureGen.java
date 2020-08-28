package modfest.lacrimis.client.cardgen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.resource.ResourceManager;
import net.minecraft.resource.ResourceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Box;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.fabricmc.fabric.api.resource.SimpleSynchronousResourceReloadListener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.lwjgl.opengl.GL11;

import net.dblsaiko.qcommon.croco.Mat4;
import net.dblsaiko.qcommon.croco.MatStack;
import net.dblsaiko.qcommon.croco.Vec3;
import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.client.texture.FramebufferBackedTexture;
import modfest.lacrimis.init.ModItems;
import modfest.lacrimis.tarot.TarotCardType;

import static java.lang.Math.max;
import static java.lang.Math.min;

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
                return new Identifier(Lacrimis.MODID, "tarot_overlay");
            }
        });
    }

    public static void updateTexturesIfNeeded() {
        if (!needsGeneration)
            return;
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
        // texture size is 13x23, including the label at the bottom
        tr.load(Mat4.ortho(0, 13, 0, 23, -50, 50));
        tr.rotate(1.0f, 0.0f, 0.0f, -22.5f);
        tr.rotate(0.0f, 1.0f, 0.0f, 45f);

        MatStack textTr = new MatStack();
        textTr.load(Mat4.ortho(0, 13, 23, 0, 0, 100));
        textTr.translate(1.0f, 21.0f, 0.0f);
        textTr.scale(0.2f, 0.2f, 0.2f);

        Tessellator t = Tessellator.getInstance();
        VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(t.getBuffer());

        for (Entry<TarotCardType, FramebufferBackedTexture> e : tarotCardTextures.entrySet()) {
            TarotCardType type = e.getKey();
            FramebufferBackedTexture tex = e.getValue();

            Framebuffer fb = tex.getFramebuffer();
            fb.clear(MinecraftClient.IS_SYSTEM_MAC);
            fb.beginWrite(true);

            Entity ent = type.create(new DummyWorld());
            if (ent != null) {
                tr.push();

                fitToBounds(tr, adjustBoundingBox(ent.getBoundingBox(), ent.getType()), 23, 20);

                EntityRenderer<? super Entity> renderer = MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(ent);

                renderer.render(ent, 0.0f, 1.0f, tr.toMatrixStack(), vcp, 0x00F000F0);
                tr.pop();
            }

            textTr.push();

            String cardName = I18n.translate(String.format("item.lacrimis.tarot.%s", e.getKey().id));
            int width = mc.textRenderer.getWidth(cardName);
            float limit = 55f;
            if (width > limit) {
                textTr.scale(limit / width, 1.0f, 1.0f);
            } else {
                textTr.translate((limit - width) / 2, 0.0f, 0.0f);
            }
            mc.textRenderer.draw(cardName, 0, 1, -1, false, textTr.mat().toMatrix4f(), vcp, false, 0, 0x00F000F0);

            vcp.draw();
            textTr.pop();
        }

        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_PROJECTION);
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(GL11.GL_MODELVIEW);

        mc.getFramebuffer().beginWrite(true);
    }

    // Modifies the transformation matrix so that the passed box is centered
    // and scaled to fit the frame buffer.
    private static void fitToBounds(MatStack tr, Box box, int texHeight, int height) {
        Set<Vec3> positions = Stream.of(
                new Vec3((float) box.minX, (float) box.minY, (float) box.minZ),
                new Vec3((float) box.minX, (float) box.minY, (float) box.maxZ),
                new Vec3((float) box.minX, (float) box.maxY, (float) box.minZ),
                new Vec3((float) box.minX, (float) box.maxY, (float) box.maxZ),
                new Vec3((float) box.maxX, (float) box.minY, (float) box.minZ),
                new Vec3((float) box.maxX, (float) box.minY, (float) box.maxZ),
                new Vec3((float) box.maxX, (float) box.maxY, (float) box.minZ),
                new Vec3((float) box.maxX, (float) box.maxY, (float) box.maxZ))
                .map(tr.mat()::mul)
                .collect(Collectors.toSet());
        Vec3 min = positions.stream().reduce(
                new Vec3(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY),
                (a, b) -> new Vec3(min(a.x, b.x), min(a.y, b.y), min(a.z, b.z)));
        Vec3 max = positions.stream().reduce(
                new Vec3(Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY, Float.NEGATIVE_INFINITY),
                (a, b) -> new Vec3(max(a.x, b.x), max(a.y, b.y), max(a.z, b.z)));
        Mat4 mat = tr.mat();
        tr.loadIdentity();
        float sizeX = max.x - min.x;
        float sizeY = max.y - min.y;
        float scale = min(2 / sizeX, 2f * height / texHeight / sizeY);
        tr.translate(0.0f, (float) -(height - texHeight) / texHeight, 0.0f);
        tr.scale(scale, scale, scale);
        tr.translate(min.add(max).div(-2));
        tr.mul(mat);
    }

    // Some entities' bounding boxes don't exactly match their model sizes, so
    // we adjust them to fit here.
    private static Box adjustBoundingBox(Box self, EntityType<?> type) {
        if (type == EntityType.PIGLIN) {
            return self.stretch(0.0, 0.2, 0.0);
        } else if (type == EntityType.WITCH) {
            return self.stretch(0.0, 0.5, 0.0);
        } else if (type == EntityType.VEX) {
            return self.stretch(0.3, 0.0, 0.0);
        } else if (type == EntityType.STRIDER) {
            return self.stretch(0.3, 0.0, 0.0);
        } else if (type == EntityType.WITHER) {
            return self.expand(1.0, 0.0, 0.0).offset(-0.2, 0.0, 0.0);
        } else if (type == EntityType.TURTLE) {
            return self.expand(0.2);
        } else if (type == EntityType.SILVERFISH) {
            return self.stretch(0.0, 0.0, -0.3);
        } else if (type == EntityType.BLAZE) {
            return self.stretch(0.0, 0.0, -0.3);
        } else {
            return self;
        }
    }

    private static void initTextures() {
        if (tarotCardTextures != null)
            return;

        HashMap<TarotCardType, FramebufferBackedTexture> map = new HashMap<>();
        HashMap<TarotCardType, Identifier> idMap = new HashMap<>();
        for (TarotCardType t : ModItems.tarotCards.keySet()) {
            Identifier id = new Identifier(Lacrimis.MODID, String.format("textures/special/card_%s.png", t.id));
            FramebufferBackedTexture tex = new FramebufferBackedTexture();
            MinecraftClient.getInstance().getTextureManager().registerTexture(id, tex);
            map.put(t, tex);
            idMap.put(t, id);
        }
        tarotCardTextures = Collections.unmodifiableMap(map);
        tarotCardTextureIds = Collections.unmodifiableMap(idMap);
    }

}
