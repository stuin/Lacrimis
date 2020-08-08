package modfest.lacrimis.block.entity;

import modfest.lacrimis.block.DrainedCryingObsidianBlock;
import modfest.lacrimis.init.ModBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.Tickable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

import java.util.Optional;

import modfest.lacrimis.crafting.CrucibleRecipe;
import modfest.lacrimis.init.ModBlockEntityTypes;
import modfest.lacrimis.init.ModCrafting;
import modfest.lacrimis.init.ModNetworking;

public class CrucibleEntity extends SoulTankEntity implements Tickable {
    private static final Box ITEM_BOX = new Box(0, 0.4, 0, 1, 1, 1);
    private static final int CRAFT_COOLDOWN = 15;

    private int craftTime = 0;

    public CrucibleEntity() {
        super(ModBlockEntityTypes.crucible, 1000, 1);
    }

    @Override
    public void tick() {
        if (getRelativeLevel() < 1 && this.world.getTime() % 10 == 0) {
            for (int dy = 1; dy < 5; dy++) {
                BlockPos obsidianPos = this.pos.up(dy);
                BlockState obsidianState = this.world.getBlockState(obsidianPos);
                if (obsidianState.getBlock() == ModBlocks.tearLantern)
                    break;
                if (obsidianState.getBlock() == ModBlocks.drainedCryingObsidian || obsidianState.getBlock() == Blocks.CRYING_OBSIDIAN) {
                    this.getTank().addTears(1);
                    world.setBlockState(obsidianPos, DrainedCryingObsidianBlock.removeTearState(obsidianState, 1));
                    break;
                }
            }
        }


        if (craftTime < CRAFT_COOLDOWN)
            craftTime++;

        if (craftTime >= CRAFT_COOLDOWN && !world.isClient) {
            for (ItemEntity entity : world.getEntitiesByClass(ItemEntity.class, ITEM_BOX.offset(pos), null)) {
                inventory.setStack(0, entity.getStack());

                Optional<CrucibleRecipe> optional = world.getServer()
                        .getRecipeManager()
                        .getFirstMatch(ModCrafting.CRUCIBLE_RECIPE, inventory, world);
                if (optional.isPresent()) {
                    CrucibleRecipe recipe = optional.get();
                    if (recipe.matches(inventory, world)) {
                        // Craft item
                        ItemStack remainder = inventory.getStack(0);
                        remainder.decrement(1);
                        entity.setStack(remainder);
                        ItemScatterer.spawn(world, pos.up(), new SimpleInventory(recipe.getOutput().copy()));
                        ModNetworking.sendCrucibleParticlesPacket(this, entity.getX(), entity.getY(), entity.getZ());
                        getTank().removeTears(recipe.getTears());
                        craftTime = 0;
                        break;
                    }
                }
            }
        }
    }
}
