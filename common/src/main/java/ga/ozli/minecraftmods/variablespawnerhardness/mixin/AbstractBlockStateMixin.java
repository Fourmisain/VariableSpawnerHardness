package ga.ozli.minecraftmods.variablespawnerhardness.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.storage.LevelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class AbstractBlockStateMixin {
	@Shadow
	public abstract Block getBlock();

	/**
	 * @reason Make block hardness variable for spawner blocks depending on world difficulty
	 * @author Paint_Ninja
	 */
	@Inject(method = "getDestroySpeed(Lnet/minecraft/world/level/BlockGetter;Lnet/minecraft/core/BlockPos;)F", at = @At("TAIL"), cancellable = true)
	public void getDestroySpeed(BlockGetter blockGetter, BlockPos blockPos, CallbackInfoReturnable<Float> cir) {
		if (this.getBlock() == Blocks.SPAWNER && blockGetter instanceof Level) {
			cir.setReturnValue(onGetDestroySpeed(blockGetter));
		}
	}

	private static final float[] hardnessByDifficulty = {5F, 9F, 22.5F, 30F};
	private static float onGetDestroySpeed(BlockGetter blockGetter) {
		LevelData levelData = ((Level) blockGetter).getLevelData();
		return levelData.isHardcore() ? 50F : hardnessByDifficulty[levelData.getDifficulty().ordinal()];
	}
}