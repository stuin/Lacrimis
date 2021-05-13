package modfest.lacrimis.block.entity;

import modfest.lacrimis.init.ModEntityTypes;
import modfest.lacrimis.util.NetworksState;
import net.minecraft.block.entity.BlockEntity;

public class NetworkLinkEntity extends BlockEntity {
    private float[] color;
    private boolean on = false;
    private NetworksState.NetworkList network;

    public NetworkLinkEntity() {
        super(ModEntityTypes.networkLink);
    }

    public void setState(boolean on, float[] color) {
        this.on = on;
        if(on && color != null) {
            this.color = color;

        }
    }
}
