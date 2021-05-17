package modfest.lacrimis.block.entity;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModEntityTypes;
import modfest.lacrimis.util.NetworksState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Pair;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class NetworkLinkEntity extends BlockEntity {
    private int color;
    private boolean on = false;
    private NetworksState.NetworkList network;
    private static NetworksState state = null;

    public NetworkLinkEntity() {
        super(ModEntityTypes.networkLink);
    }

    public void setState(boolean on, float[] color, World serverWorld) {
        if(state == null && serverWorld instanceof ServerWorld)
            state = ((ServerWorld) serverWorld).getPersistentStateManager().getOrCreate(NetworksState::new, "lacrimis_network");

        if(network != null && state != null)
            state.removeLink(this.color, makePair());

        this.on = on;
        if(on && color != null && state != null) {
            this.color = NetworksState.getColor(color);
            network = state.addLink(this.color, makePair());
            Lacrimis.LOGGER.warn("Connected to network " + this.color);
        }
    }

    public Pair<RegistryKey<World>, BlockPos> makePair() {
        return new Pair<>(world.getRegistryKey(), pos);
    }

    public boolean isOn() {
        return on;
    }

    public int getColor() {
        return color;
    }

    public NetworksState.NetworkList getNetwork() {
        return network;
    }
}
