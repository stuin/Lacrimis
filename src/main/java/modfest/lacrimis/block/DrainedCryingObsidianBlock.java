package modfest.lacrimis.block;

import modfest.lacrimis.init.ModBlocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager.Builder;
import net.minecraft.state.property.IntProperty;
import net.minecraft.text.LiteralText;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class DrainedCryingObsidianBlock extends CryingObsidianBlock {

    public static final IntProperty TEARS_LEAST = IntProperty.of("tears_least_sig", 0, 49);
    public static final IntProperty TEARS_MOST = IntProperty.of("tears_most_sig", 0, 9);

    public DrainedCryingObsidianBlock(Settings settings) {
        super(settings);
        this.setDefaultState(setTearsValue(this.getDefaultState(), 499));
    }

    public void onWrenched(World world, PlayerEntity player, BlockHitResult blockHitResult) {
        if(player != null && !player.isSneaking() && !world.isClient) {
            int level = getTearsLevel(world.getBlockState(blockHitResult.getBlockPos()));
            Text text = new LiteralText(level + " Tears");
            player.sendMessage(text, false);
        }
    }

    @Override
    public PistonBehavior getPistonBehavior(BlockState state) {
        return PistonBehavior.BLOCK;
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if(itemStack.hasNbt())
            world.setBlockState(pos, setTearsValue(state, itemStack.getOrCreateNbt().getInt("TearLevel")));
    }

    @Override
    protected void appendProperties(Builder<Block, BlockState> builder) {
        builder.add(TEARS_LEAST);
        builder.add(TEARS_MOST);
    }


    public static BlockState setTearsValue(BlockState self, int tears) {
        tears -= 1;
        int least = tears % 50;
        int most = tears / 50;
        return self.with(TEARS_LEAST, least).with(TEARS_MOST, most);
    }

    public static BlockState removeTearState(BlockState state, int count) {
        int tears = getTearsLevel(state) - count;
        if (tears <= 1)
            return Blocks.OBSIDIAN.getDefaultState();
        else if(tears <= 500) {
            if(state.getBlock() == ModBlocks.drainedCryingObsidian)
                return setTearsValue(state, tears);
            return setTearsValue(ModBlocks.drainedCryingObsidian.getDefaultState(), tears);
        }
        return state;
    }

    public static int getTearsLevel(BlockState self) {
        if (self.getBlock() == Blocks.CRYING_OBSIDIAN) return 501;
        else if(self.getBlock() == ModBlocks.drainedCryingObsidian)
            return self.get(TEARS_MOST) * 50 + self.get(TEARS_LEAST) + 1;
        return 0;
    }
}
