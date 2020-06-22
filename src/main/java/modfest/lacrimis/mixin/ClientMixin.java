package modfest.lacrimis.mixin;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.client.CustomOptions;
import modfest.lacrimis.tarot.CardHolder;
import modfest.lacrimis.tarot.TarotCardType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class ClientMixin {
    @Shadow
    public ClientPlayerEntity player;

    @Shadow
    public ClientWorld world;

    @Shadow @Final
    public GameOptions options;

    @Inject(at = @At("HEAD"), method = "handleInputEvents()V")
    private void handleInputEvents(CallbackInfo ci) {
        //Custom key input
        if(player instanceof CardHolder && options instanceof CustomOptions) {
            while(((CustomOptions) options).getKeyTarot().wasPressed()) {
                Lacrimis.LOGGER.info("Tarot Button pressed");
                for(int i = ((CardHolder) player).getCards().length; i > 0; i--) {
                    TarotCardType card = ((CardHolder) player).getCards()[i - 1];
                    if(card == TarotCardType.THE_WORLD) {
                        //world.spawnEntity(new EnderPearlEntity(world, this));
                        player.sendChatMessage("Lanching pearl");
                        break;
                    }
                    if(card == TarotCardType.DEATH) {
                        //world.spawnEntity(new SnowballEntity(world, this));
                        player.sendChatMessage("Lanching wither skull");
                        break;
                    }
                /*if(card == TarotCardType.THE_MAGICIAN) {
                    BlockPos blockPos = getBlockPos().add(-2 + random.nextInt(5), 1, -2 + random.nextInt(5));
                    VexEntity vexEntity = EntityType.VEX.create(world);
                    vexEntity.refreshPositionAndAngles(blockPos, 0.0F, 0.0F);
                    vexEntity.initialize(world, world.getLocalDifficulty(blockPos), SpawnReason.MOB_SUMMONED, null, null);
                    vexEntity.setOwner(this);
                    vexEntity.setBounds(blockPos);
                    vexEntity.setLifeTicks(20 * (30 + random.nextInt(90)));
                    this.world.spawnEntity(vexEntity);
                }*/
                }
            }
        }
    }
}
