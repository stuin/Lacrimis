package modfest.lacrimis.block.entity;

import modfest.lacrimis.crafting.InfusionInventory;
import modfest.lacrimis.crafting.InfusionRecipe;
import modfest.lacrimis.crafting.InfusionScreenHandler;
import modfest.lacrimis.crafting.ModCrafting;
import modfest.lacrimis.entity.ModEntities;
import modfest.lacrimis.util.DuctUtil;
import modfest.lacrimis.util.SoulTank;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.CraftingRecipe;
import net.minecraft.recipe.RecipeType;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.Random;
import java.util.stream.IntStream;

public class InfusionTableEntity extends SoulTankEntity implements NamedScreenHandlerFactory {
    public static final int CAPACITY = 1000;
    public static final int SIZE = 10;
    public static final int OUTPUT_STACK = 9;
    public static final int[] INPUT_STACKS = IntStream.rangeClosed(0, 8).toArray();
    private final Random random = new Random();
    
    public ItemStack holding = ItemStack.EMPTY;

    public InfusionTableEntity(BlockPos pos, BlockState state) {
        super(ModEntities.infusionTable, pos, state, CAPACITY, SIZE);
        getTank().setLimit(0);
    }

    public static void tick(World world, BlockPos pos, BlockState state, InfusionTableEntity blockEntity) {
        SoulTank tank = blockEntity.getTank();
        ItemStack holding = blockEntity.holding;
        InfusionInventory inventory = blockEntity.inventory;
        ItemStack output = inventory.getStack(OUTPUT_STACK);

        //Finish infusion crafting
        if(tank.getSpace() <= 0 && !holding.isEmpty()) {
            //Set output item
            if(output.isEmpty())
                inventory.setStack(OUTPUT_STACK, holding.copy());
            else if(holding.getItem() == output.getItem())
                output.increment(holding.getCount());

            //Clear
            holding = ItemStack.EMPTY;
            inventory.markDirty();
            tank.setTears(0);
            tank.setLimit(0);
        }

        //Check for new recipe
        if(holding.isEmpty() && (inventory.properties.hasSignal() || !inventory.getStack(OUTPUT_STACK).isEmpty())) {
            InfusionRecipe recipe = world.getRecipeManager().getFirstMatch(ModCrafting.INFUSION_RECIPE, inventory, world).orElse(null);
            if(recipe == null)
                recipe = world.getRecipeManager().getFirstMatch(ModCrafting.CRUCIBLE_RECIPE, inventory, world).orElse(null);

            CraftingRecipe vanillaRecipe = null;
            if(recipe == null)
                vanillaRecipe = world.getRecipeManager().getFirstMatch(RecipeType.CRAFTING, inventory.setupCrafting(), world).orElse(null);

            //Start crafting progress
            if(recipe != null && blockEntity.canAcceptOutput(recipe.getOutput())) {
                blockEntity.takeIngredients(false);
                tank.setLimit(recipe.getTears());
                blockEntity.holding = recipe.getOutput().copy();
                inventory.properties.setSignal(false);
            } else if(vanillaRecipe != null && blockEntity.canAcceptOutput(vanillaRecipe.getOutput())) {
                blockEntity.takeIngredients(true);
                tank.setLimit(5);
                blockEntity.holding = vanillaRecipe.getOutput().copy();
                inventory.properties.setSignal(false);
            }
        }

        if(inventory.properties.hasSignal())
            inventory.properties.setSignal(false);

        //Collect tears
        if(tank.getSpace() > 0)
            tank.addTears(DuctUtil.locateTears(world, pos, 5));
    }

    public void takeIngredients(boolean vanilla) {
        if(world != null) {
            DefaultedList<ItemStack> defaultedList = world.getRecipeManager().getRemainingStacks(ModCrafting.INFUSION_RECIPE, inventory, world);

            for(int i = 0; i < 9; ++i) {
                ItemStack itemStack = inventory.getStack(i);
                //ItemStack itemStack2 = defaultedList.get(i);
                if(!itemStack.isEmpty())
                    inventory.getStack(i).decrement(1);
            }
        }
    }

    @Override
    public Text getDisplayName() {
        return new TranslatableText("lacrimis.gui.infusion");
    }

    @Nullable
    @Override
    public ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
        return new InfusionScreenHandler(syncId, inv, inventory);
    }

    protected boolean canAcceptOutput(ItemStack itemStack) {
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

    public int[] getAvailableSlots(Direction side) {
        if (side == Direction.DOWN)
            return new int[]{OUTPUT_STACK};
        return INPUT_STACKS;
    }

    public boolean canInsert(int slot, ItemStack stack, @Nullable Direction dir) {
        return this.isValid(slot, stack);
    }

    public boolean canExtract(int slot, ItemStack stack, Direction dir) {
        return true;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);
        holding = ItemStack.fromNbt(tag.getCompound("Holding"));
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);
        NbtCompound itemTag = new NbtCompound();
        holding.writeNbt(itemTag);
        tag.put("Holding", itemTag);
    }
}
