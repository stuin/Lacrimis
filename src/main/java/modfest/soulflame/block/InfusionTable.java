package modfest.soulflame.block;

import javax.annotation.Nullable;

import grondag.fluidity.api.storage.Store;
import modfest.soulflame.SoulFlame;
import modfest.soulflame.block.entity.InfusionTableEntity;
import modfest.soulflame.infusion.InfusionScreenHandler;
import modfest.soulflame.infusion.ModInfusion;
import net.fabricmc.fabric.api.container.ContainerProviderRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.screen.NamedScreenHandlerFactory;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.SimpleNamedScreenHandlerFactory;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class InfusionTable extends LiquidTank {
	private static final Text TITLE = new TranslatableText("container.infusion");

	public InfusionTable(Settings settings) {
		super(settings);
	}

	@Nullable
	@Override
	public BlockEntity createBlockEntity(BlockView world) {
		return new InfusionTableEntity();
	}

	public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		ActionResult parentResult = super.onUse(state, world, pos, player, hand, hit);
		if(parentResult == ActionResult.PASS) {
			if(world.isClient) {
				return ActionResult.SUCCESS;
			} else {
				ContainerProviderRegistry.INSTANCE.openContainer(ModInfusion.INFUSION_SCREEN_ID, player, buf -> buf.writeBlockPos(pos));

				return ActionResult.CONSUME;
			}
		}
		return parentResult;
	}

	public NamedScreenHandlerFactory createScreenHandlerFactory(BlockState state, World world, BlockPos pos) {
		Store tank = null;
		if(world.getBlockEntity(pos) != null)
			tank = ((InfusionTableEntity)world.getBlockEntity(pos)).getEffectiveStorage();
		Store finalTank = tank;
		return new SimpleNamedScreenHandlerFactory((i, playerInventory, playerEntity) ->
				new InfusionScreenHandler(i, playerInventory, finalTank, ScreenHandlerContext.create(world, pos)), TITLE);
	}
}
