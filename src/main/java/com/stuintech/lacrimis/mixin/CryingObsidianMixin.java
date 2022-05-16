package com.stuintech.lacrimis.mixin;

import com.stuintech.lacrimis.Lacrimis;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import vazkii.patchouli.common.item.ItemModBook;

@Mixin(CryingObsidianBlock.class)
public abstract class CryingObsidianMixin extends Block {
    public CryingObsidianMixin(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        ItemStack item = player.getStackInHand(hand);
        if(item.getItem() == Items.BOOK) {
            item.decrement(1);
            player.giveItemStack(ItemModBook.forBook(new Identifier(Lacrimis.MODID, "guide")));
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }
}
