package modfest.lacrimis.block.entity;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.init.ModEntityTypes;
import modfest.lacrimis.util.NetworksState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;

public class NetworkLinkEntity extends BlockEntity {
    private int color;
    private boolean on = false;
    private NetworksState.NetworkList network;
    private static NetworksState state = null;

    public NetworkLinkEntity() {
        super(ModEntityTypes.networkLink);
        if(state == null && world instanceof ServerWorld)
            state = ((ServerWorld) world).getPersistentStateManager().getOrCreate(NetworksState::new, "lacrimis:networks");
    }

    public void setState(boolean on, float[] color) {
        this.on = on;
        if(on && color != null) {
            this.color = NetworksState.getColor(color);
            network = state.getNetwork(this.color);
            Lacrimis.LOGGER.warn("Connected to network " + this.color);
        }
    }
}
