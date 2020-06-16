package modfest.lacrimis.client.cardgen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
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
import java.util.Objects;
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
import modfest.lacrimis.util.TarotCardType;

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
        // texture size as rendered on the item has an aspect ratio of 3:5
        tr.load(Mat4.ortho(0, 3, 0, 5, -50, 50));
        tr.rotate(1.0f, 0.0f, 0.0f, -22.5f);
        tr.rotate(0.0f, 1.0f, 0.0f, 45f);

        Tessellator t = Tessellator.getInstance();
        VertexConsumerProvider.Immediate vcp = VertexConsumerProvider.immediate(t.getBuffer());

        for (Entry<TarotCardType, FramebufferBackedTexture> e : tarotCardTextures.entrySet()) {
            TarotCardType type = e.getKey();
            FramebufferBackedTexture tex = e.getValue();

            tr.push();

            Framebuffer fb = tex.getFramebuffer();
            fb.clear(MinecraftClient.IS_SYSTEM_MAC);
            fb.beginWrite(true);

            Entity ent = Objects.requireNonNull(type.create(new DummyWorld()));

            fitToBounds(tr, adjustBoundingBox(ent.getBoundingBox(), ent.getType()));

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

    // Modifies the transformation matrix so that the passed box is centered
    // and scaled to fit the frame buffer.
    private static void fitToBounds(MatStack tr, Box box) {
        Set<Vec3> positions = Stream.of(
                new Vec3((float) box.minX, (float) box.minY, (float) box.minZ),
                new Vec3((float) box.minX, (float) box.minY, (float) box.maxZ),
                new Vec3((float) box.minX, (float) box.maxY, (float) box.minZ),
                new Vec3((float) box.minX, (float) box.maxY, (float) box.maxZ),
                new Vec3((float) box.maxX, (float) box.minY, (float) box.minZ),
                new Vec3((float) box.maxX, (float) box.minY, (float) box.maxZ),
                new Vec3((float) box.maxX, (float) box.maxY, (float) box.minZ),
                new Vec3((float) box.maxX, (float) box.maxY, (float) box.maxZ)
        )
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
        float scale = 2 / max(sizeX, sizeY);
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
            return self.stretch(0.0,0.0,-0.3);
        } else if (type == EntityType.BLAZE) {
            return self.stretch(0.0,0.0,-0.3);
        } else {
            return self;
        }
    }

    private static void initTextures() {
        if (tarotCardTextures != null) return;

        HashMap<TarotCardType, FramebufferBackedTexture> map = new HashMap<>();
        HashMap<TarotCardType, Identifier> idMap = new HashMap<>();
        for (TarotCardType t : ModItems.tarotCards.keySet()) {
            if (t.cover == null) continue;

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
