package modfest.lacrimis.client.texture;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.resource.ResourceManager;
import com.mojang.blaze3d.systems.RenderSystem;

public class FramebufferBackedTexture extends AbstractTexture {

    private Framebuffer fb;

    @Override
    public void load(ResourceManager manager) {
    }

    public Framebuffer getFramebuffer() {
        RenderSystem.assertThread(RenderSystem::isOnRenderThreadOrInit);
        if (this.fb == null) {
            this.fb = new Framebuffer(256, 256, true, MinecraftClient.IS_SYSTEM_MAC);
            this.glId = this.fb.colorAttachment; // just in case
        }

        return this.fb;
    }

    @Override
    public int getGlId() {
        return this.getFramebuffer().colorAttachment;
    }

    @Override
    public void clearGlId() {
        if (!RenderSystem.isOnRenderThread()) {
            RenderSystem.recordRenderCall(this::clearGlId0);
        } else {
            this.clearGlId0();
        }
    }

    private void clearGlId0() {
        if (this.fb != null) {
            this.fb.delete();
            this.fb = null;
        }
        this.glId = -1;
    }

}