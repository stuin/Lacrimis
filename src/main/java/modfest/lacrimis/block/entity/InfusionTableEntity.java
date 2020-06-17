package modfest.lacrimis.block.entity;

import java.util.Random;

import modfest.lacrimis.infusion.InfusionInventory;
import modfest.lacrimis.infusion.ShapedInfusionRecipe;
import modfest.lacrimis.init.ModBlockEntityTypes;
import modfest.lacrimis.init.ModInfusion;
import modfest.lacrimis.init.ModParticles;
import modfest.lacrimis.util.ConduitUtil;
import modfest.lacrimis.util.SoulTank;
import net.fabricmc.fabric.api.util.NbtType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;
import net.minecraft.util.collection.DefaultedList;

public class InfusionTableEntity extends SoulTankEntity implements Inventory, Tickable {
    public static final int OUTPUT_STACK = 9;
    private final Random random = new Random();
    
    public final InfusionInventory inventory;
    public ItemStack holding = ItemStack.EMPTY;

    public InfusionTableEntity() {
        super(ModBlockEntityTypes.infusionTable, 500);
        this.inventory = new InfusionInventory(this, 10);
        getTank().setLimit(0);
    }

    @Override
    public void tick() {
        if(world == null)
            return;
        if (world.isClient && this.getTank().getTears() > 0 && random.nextInt(10) == 0) {
            double a = random.nextDouble() * 4 * Math.PI;

            double x = this.pos.getX() + 0.5 + 0.1 * Math.cos(a);
            double z = this.pos.getZ() + 0.5 + 0.1 * Math.sin(a);
            double y = this.pos.getY() + 0.75 + 0.05 * random.nextDouble();

            double dx = 0.005 * Math.cos(a + 1.5 * Math.PI / 2);
            double dz = 0.005 * Math.sin(a + 1.5 * Math.PI / 2);

            this.world.addParticle(ModParticles.PURPLE_MIST, x, y, z, dx, 0.005, dz);
        }
        if(world.isClient())
            return;

        //Manage infusion crafting
        SoulTank tank = getTank();
        if(tank.getSpace() <= 0 && !holding.isEmpty()) {
            //Completed
            ItemStack output = inventory.getStack(OUTPUT_STACK);

            //Set output item
            if (output.isEmpty())
                inventory.setStack(OUTPUT_STACK, holding.copy());
            else if (holding.getItem() == output.getItem())
                output.increment(1);

            //Clear
            holding = ItemStack.EMPTY;
            inventory.markDirty();
            tank.setTears(0);
            tank.setLimit(0);
        }

        //Check for new recipe
        if(holding.isEmpty()) {
            ShapedInfusionRecipe recipe = this.world.getRecipeManager().getFirstMatch(ModInfusion.INFUSION_RECIPE, inventory, this.world).orElse(null);

            //Start crafting progress
            if(recipe != null && canAcceptRecipeOutput(recipe)) {
                takeIngredients();
                tank.setLimit(recipe.getTears());
                holding = recipe.getOutput().copy();
            }
        }
        
        //Collect tears
        if(tank.getSpace() > 0)
            tank.addTears(ConduitUtil.locateTears(world, pos, 5));
    }

    public void takeIngredients() {
        if(world != null) {
            DefaultedList<ItemStack> defaultedList = world.getRecipeManager().getRemainingStacks(ModInfusion.INFUSION_RECIPE, inventory, world);

            for(int i = 0; i < 9; ++i) {
                ItemStack itemStack = inventory.getStack(i);
                ItemStack itemStack2 = defaultedList.get(i);
                if(!itemStack.isEmpty()) {
                    inventory.removeStack(i, 1);
                    itemStack = inventory.getStack(i);
                }

                if(!itemStack2.isEmpty()) {
                    if(itemStack.isEmpty()) {
                        inventory.setStack(i, itemStack2);
                    }
                    if(ItemStack.areItemsEqualIgnoreDamage(itemStack, itemStack2) && ItemStack.areTagsEqual(itemStack, itemStack2)) {
                        itemStack2.increment(itemStack.getCount());
                        inventory.setStack(i, itemStack2);
                    }
                }
            }
        }
    }

    protected boolean canAcceptRecipeOutput(ShapedInfusionRecipe recipe) {
        ItemStack itemStack = recipe.getOutput();
        if (itemStack.isEmpty())
            return false;

        ItemStack itemStack2 = this.inventory.getStack(OUTPUT_STACK);
        if (itemStack2.isEmpty())
            return true;
        else if (!itemStack2.isItemEqualIgnoreDamage(itemStack))
            return false;
        else if (itemStack2.getCount() < this.getMaxCountPerStack() && itemStack2.getCount() < itemStack2.getMaxCount())
            return true;
        else
            return itemStack2.getCount() < itemStack.getMaxCount();
    }

    @Override
    public void fromTag(BlockState state, CompoundTag tag) {
        super.fromTag(state, tag);
        this.inventory.clear();
        this.inventory.readTags(tag.getList("Inventory", NbtType.COMPOUND));
        holding = ItemStack.fromTag(tag.getCompound("Holding"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        tag.put("Inventory", this.inventory.getTags());

        CompoundTag itemTag = new CompoundTag();
        holding.toTag(itemTag);
        tag.put("Holding", itemTag);
        return tag;
    }

    @Override
    public int size() {
        return inventory.size();
    }

    @Override
    public boolean isEmpty() {
        return inventory.isEmpty();
    }

    @Override
    public ItemStack getStack(int slot) {
        return inventory.getStack(slot);
    }

    @Override
    public ItemStack removeStack(int slot, int amount) {
        return inventory.removeStack(slot, amount);
    }

    @Override
    public ItemStack removeStack(int slot) {
        return inventory.removeStack(slot);
    }

    @Override
    public void setStack(int slot, ItemStack stack) {
        inventory.setStack(slot, stack);
    }

    @Override
    public boolean canPlayerUse(PlayerEntity player) {
        return inventory.canPlayerUse(player);
    }

    @Override
    public void clear() {
        inventory.clear();
        getTank().setTears(0);
    }
}
