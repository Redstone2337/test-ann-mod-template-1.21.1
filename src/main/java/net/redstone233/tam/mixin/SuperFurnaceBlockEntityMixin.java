package net.redstone233.tam.mixin;

import net.minecraft.block.*;
import net.minecraft.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.block.pattern.BlockPattern;
import net.minecraft.block.pattern.BlockPatternBuilder;
import net.minecraft.block.pattern.CachedBlockPosition;
import net.minecraft.predicate.block.BlockStatePredicate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Set;

@Mixin(AbstractFurnaceBlockEntity.class)
public abstract class SuperFurnaceBlockEntityMixin {
    @Unique
    private boolean isFast = false;

    @Unique
    private final static BlockPattern SUPER_FURNACE = BlockPatternBuilder.start()
            .aisle("AAA","AAA","AAA")
            .aisle("AAA","AAA","ABA")
            .aisle("AAA","AAA","AAA")
            .where('A', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.STONE)))
            .where('B', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.FURNACE)))
            .build();

    @Unique
    private final static BlockPattern SUPER_BLAST_FURNACE = BlockPatternBuilder.start()
            .aisle("CCC","CCC","CCC")
            .aisle("CCC","CCC","CBC")
            .aisle("AAA","AAA","AAA")
            .where('A', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SMOOTH_STONE)))
            .where('B', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.BLAST_FURNACE)))
            .where('C', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.IRON_BLOCK)))
            .build();

    @Unique
    private final static BlockPattern SUPER_SMOKER = BlockPatternBuilder.start()
            .aisle("CAC","ACA","CAC")
            .aisle("CAC","ACA","CBC")
            .aisle("CAC","ACA","CAC")
            .where('A', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.OAK_LOG)
                    .or(BlockStatePredicate.forBlock(Blocks.SPRUCE_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.BIRCH_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.JUNGLE_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.ACACIA_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.DARK_OAK_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.MANGROVE_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.CHERRY_LOG))
                    .or(BlockStatePredicate.forBlock(Blocks.CRIMSON_STEM))
                    .or(BlockStatePredicate.forBlock(Blocks.WARPED_STEM))
            ))
            .where('B', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.SMOKER)))
            .where('C', CachedBlockPosition.matchesBlockState(BlockStatePredicate.forBlock(Blocks.COAL_BLOCK)))
            .build();

    @Unique
    private final static Set<BlockPattern> ALL_PATTERNS = Set.of(SUPER_FURNACE, SUPER_BLAST_FURNACE, SUPER_SMOKER);


    @Inject(method = "tick", at = @At("TAIL"))
    private static void onServerTick(World world, BlockPos pos, BlockState state, AbstractFurnaceBlockEntity blockEntity, CallbackInfo ci) {
        if (!(state.getBlock() instanceof FurnaceBlock ||
                state.getBlock() instanceof BlastFurnaceBlock ||
                state.getBlock() instanceof SmokerBlock)) return;
        SuperFurnaceBlockEntityMixin mixin = (SuperFurnaceBlockEntityMixin) (Object) blockEntity;
        boolean found = ALL_PATTERNS.stream().anyMatch(pattern -> pattern.searchAround(world, pos) != null);
        if (found && mixin != null) {
            mixin.isFast = true;
        } else {
            if (mixin != null) {
                mixin.isFast = false;
            }
        }
    }

    @Inject(method = "getCookTime", at = @At("RETURN"), cancellable = true)
    private static void onGetCookTime(World world, AbstractFurnaceBlockEntity furnace, CallbackInfoReturnable<Integer> cir) {
        SuperFurnaceBlockEntityMixin mixin = (SuperFurnaceBlockEntityMixin) (Object) furnace;
        if (mixin != null && mixin.isFast) {
            cir.setReturnValue(cir.getReturnValueI()/4);
        }
    }
}