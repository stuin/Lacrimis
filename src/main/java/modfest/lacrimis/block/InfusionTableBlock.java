package modfest.lacrimis.block;

import modfest.lacrimis.Lacrimis;
import modfest.lacrimis.block.entity.InfusionTableEntity;
import modfest.lacrimis.init.ModEntities;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

import java.util.List;

public class InfusionTableBlock extends SoulTankBlock {
	protected static final VoxelShape SHAPE = Block.createCuboidShape(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);
	public static final BooleanProperty POWERED;

	public InfusionTableBlock(AbstractBlock.Settings settings) {
		super(settings, false);
		setDefaultState(getDefaultState().with(POWERED, false));
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		return new InfusionTableEntity(pos, state);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState state, BlockEntityType<T> type) {
		return ModEntities.checkType(type, ModEntities.infusionTable, InfusionTableEntity::tick);
	}

	@Override
	public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return SHAPE;
	}

	@Override
	public BlockRenderType getRenderType(BlockState state) {
		return BlockRenderType.MODEL;
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (world.isClient) {
			return ActionResult.SUCCESS;
		} else {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof InfusionTableEntity) {
				player.openHandledScreen((InfusionTableEntity)blockEntity);
			}

			return ActionResult.CONSUME;
		}
	}

	@Override
	public void neighborUpdate(BlockState state, World world, BlockPos pos, Block block, BlockPos fromPos, boolean notify) {
		if(state.get(POWERED) != world.isReceivingRedstonePower(pos)) {
			BlockEntity entity = world.getBlockEntity(pos);
			if(!state.get(POWERED) && entity instanceof InfusionTableEntity)
				((InfusionTableEntity) entity).inventory.properties.setSignal(true);
			world.setBlockState(pos, state.with(POWERED, world.isReceivingRedstonePower(pos)));
		}
	}

	@Override
    public void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		if (!state.isOf(newState.getBlock())) {
			BlockEntity blockEntity = world.getBlockEntity(pos);
			if (blockEntity instanceof Inventory) {
				ItemScatterer.spawn(world, pos, (Inventory)blockEntity);
				world.updateComparators(pos, this);
				((Inventory) blockEntity).clear();
			}
		}
	}

	@Override
	public void appendTooltip(ItemStack stack, BlockView world, List<Text> tooltip, TooltipContext options) {
		super.appendTooltip(stack, world, tooltip, options);
		if(stack.hasNbt())
			tooltip.add(new TranslatableText(Lacrimis.MODID + ".tooltip.crucible",
				stack.getOrCreateNbt().getInt("TearLevel")).formatted(Formatting.GRAY));
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(POWERED);
	}

	static {
		POWERED = Properties.POWERED;
	}
}
