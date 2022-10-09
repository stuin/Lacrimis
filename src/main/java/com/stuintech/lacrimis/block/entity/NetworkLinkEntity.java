package com.stuintech.lacrimis.block.entity;

import com.stuintech.lacrimis.util.NetworksState;
import com.stuintech.lacrimis.Lacrimis;
import com.stuintech.lacrimis.entity.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class NetworkLinkEntity extends BlockEntity {
    private float[] color;
    private boolean on = false;
    private NetworksState.NetworkList network;
    private NetworksState state = null;

    public NetworkLinkEntity(BlockPos pos, BlockState state) {
        super(ModEntities.networkLink, pos, state);
    }

    public void setState(boolean on, float[] color, World world) {
        if(state == null && world instanceof ServerWorld)
            state = ((ServerWorld) world).getPersistentStateManager().getOrCreate(
                    NetworksState::fromNbt, NetworksState::new, "lacrimis_network");

        if(network != null && state != null)
            state.removeLink(getColor(), pos);

        this.on = on;
        if(on && color != null) {
            this.color = color;
            if(state != null)
                network = state.addLink(getColor(), pos);
            Lacrimis.LOGGER.warn("Connected to network " + getColor());
        }
    }

    /* //TODO replace
    @Nullable
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return new BlockEntityUpdateS2CPacket(this.pos, 3, this.toInitialChunkDataNbt());
    }
    */

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        if(color != null) {
            tag.putFloat("colorA", color[0]);
            tag.putFloat("colorB", color[1]);
            tag.putFloat("colorC", color[2]);
        }
        tag.putBoolean("on", on);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        color = new float[] {0, 0, 0};
        color[0] = tag.getFloat("colorA");
        color[1] = tag.getFloat("colorB");
        color[2] = tag.getFloat("colorC");
        on = tag.getBoolean("on");
    }

    public boolean isOn() {
        return on;
    }

    public int getColor() {
        return (int)(color[0] * 100) + (int)(color[1] * 10000) + (int)(color[2] * 1000000);
    }

    public float[] getColorComponents() {
        return color;
    }

    public NetworksState.NetworkList getNetwork() {
        if(state == null && world instanceof ServerWorld)
            state = ((ServerWorld) world).getPersistentStateManager().getOrCreate(
                    NetworksState::fromNbt, NetworksState::new, "lacrimis_network");
        if(!on || state == null)
            return null;
        if(network == null || network.color != getColor())
            network = state.getNetwork(getColor());
        return network;
    }
}
