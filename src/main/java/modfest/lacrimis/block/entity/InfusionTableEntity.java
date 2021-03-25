package modfest.lacrimis.block.entity;

import java.util.Random;

import modfest.lacrimis.crafting.InfusionRecipe;
import modfest.lacrimis.init.ModCrafting;
import modfest.lacrimis.init.ModEntityTypes;
import modfest.lacrimis.init.ModParticles;
import modfest.lacrimis.util.DuctUtil;
import modfest.lacrimis.util.SoulTank;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Tickable;

public class InfusionTableEntity extends SoulTankEntity implements Tickable {
    public static final int OUTPUT_STACK = 9;
    private final Random random = new Random();
    
    public ItemStack holding = ItemStack.EMPTY;
    public boolean startCrafting = false;

    public InfusionTableEntity() {
        super(ModEntityTypes.infusionTable, 1000, 10);
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

        //Manage infusion crafting
        SoulTank tank = getTank();
        if(tank.getSpace() <= 0 && !holding.isEmpty()) {
            //Completed
            ItemStack output = inventory.getStack(OUTPUT_STACK);

            //Set output item
            if (output.isEmpty())
                inventory.setStack(OUTPUT_STACK, holding.copy());
            else if (holding.getItem() == output.getItem())
                output.increment(holding.getCount());

            //Clear
            holding = ItemStack.EMPTY;
            inventory.markDirty();
            tank.setTears(0);
            tank.setLimit(0);
        }

        //Check for new recipe
        if(holding.isEmpty()) {
            InfusionRecipe recipe = this.world.getRecipeManager().getFirstMatch(ModCrafting.INFUSION_RECIPE, inventory, this.world).orElse(null);
            if(recipe == null)
                recipe = this.world.getRecipeManager().getFirstMatch(ModCrafting.CRUCIBLE_RECIPE, inventory, this.world).orElse(null);

            //Start crafting progress
            if(recipe != null && canAcceptRecipeOutput(recipe) &&
                    (startCrafting || !inventory.getStack(OUTPUT_STACK).isEmpty())) {
                takeIngredients();
                tank.setLimit(recipe.getTears());
                holding = recipe.getOutput().copy();
                startCrafting = false;
            }
        }

        if(startCrafting)
            startCrafting = false;

        //Collect tears
        if(tank.getSpace() > 0)
            tank.addTears(DuctUtil.locateTears(world, pos, 5));
    }

    public void takeIngredients() {
        if(world != null) {
            //DefaultedList<ItemStack> defaultedList = world.getRecipeManager().getRemainingStacks(ModCrafting.INFUSION_RECIPE, inventory, world);

            for(int i = 0; i < 9; ++i) {
                ItemStack itemStack = inventory.getStack(i);
                //ItemStack itemStack2 = defaultedList.get(i);
                if(!itemStack.isEmpty())
                    inventory.getStack(i).decrement(1);
            }
        }
    }

    protected boolean canAcceptRecipeOutput(InfusionRecipe recipe) {
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
        holding = ItemStack.fromTag(tag.getCompound("Holding"));
    }

    @Override
    public CompoundTag toTag(CompoundTag tag) {
        super.toTag(tag);
        CompoundTag itemTag = new CompoundTag();
        holding.toTag(itemTag);
        tag.put("Holding", itemTag);
        return tag;
    }
}
