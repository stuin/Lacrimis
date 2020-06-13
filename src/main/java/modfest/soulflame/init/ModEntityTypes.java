package modfest.soulflame.init;

import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;

import modfest.soulflame.SoulFlame;
import modfest.soulflame.entity.GhostEntity;

public class ModEntityTypes {

    public static EntityType<GhostEntity> ghost;

    public static void register() {
        ghost = Registry.register(Registry.ENTITY_TYPE, new Identifier(SoulFlame.MODID, "ghost"),
                FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, GhostEntity::new)
                        .dimensions(EntityDimensions.fixed(0.75f, 2.0f))
                        .trackable(64, 4).build());
    }

}
