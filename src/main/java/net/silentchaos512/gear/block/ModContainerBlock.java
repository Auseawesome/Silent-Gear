package net.silentchaos512.gear.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

import javax.annotation.Nullable;

public abstract class ModContainerBlock<T extends BlockEntity> extends BaseEntityBlock {
    private final BlockEntityType.BlockEntitySupplier<T> tileFactory;

    public ModContainerBlock(BlockEntityType.BlockEntitySupplier<T> tileFactory, Properties properties) {
        super(properties);
        this.tileFactory = tileFactory;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return tileFactory.create(pos, state);
    }

    @Override
    public void onRemove(BlockState state, Level worldIn, BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof Container) {
                Containers.dropContents(worldIn, pos, (Container) tile);
                worldIn.updateNeighbourForOutputSignal(pos, this);
            }
            super.onRemove(state, worldIn, pos, newState, isMoving);
        }
    }

    @Override
    public InteractionResult useWithoutItem(BlockState state, Level worldIn, BlockPos pos, Player player, BlockHitResult hit) {
        if (!worldIn.isClientSide) {
            BlockEntity tile = worldIn.getBlockEntity(pos);
            if (tile instanceof INamedContainerExtraData te && player instanceof ServerPlayer) {
                player.openMenu(te, te::encodeExtraData);
            }
        }
        return InteractionResult.SUCCESS;
    }

    @SuppressWarnings("deprecation")
    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }
}
