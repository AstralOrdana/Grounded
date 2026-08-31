package com.ordana.grounded.blocks;

import com.mojang.serialization.MapCodec;
import com.ordana.grounded.reg.ModBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.protocol.game.ClientboundBlockUpdatePacket;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.DirectionalPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FallingBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.gamerules.GameRules;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jspecify.annotations.Nullable;

public class DetritusBlock extends FallingBlock implements SimpleWaterloggedBlock {

	public static final int MAX_LAYERS = 8;
	public static final IntegerProperty LAYERS = BlockStateProperties.LAYERS;
	public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

	private static final VoxelShape[] SHAPES = Block.boxes(8, height -> Block.column(16.0, 0.0, height * 2));
	public static final int HEIGHT_IMPASSABLE = 5;

	public static final MapCodec<DetritusBlock> CODEC = simpleCodec(DetritusBlock::new);

	public DetritusBlock(Properties properties) {
		super(properties);
		this.registerDefaultState(this.getStateDefinition().any()
			.setValue(LAYERS, 1)
			.setValue(WATERLOGGED, false)
		);
	}

	@Override
	protected MapCodec<? extends FallingBlock> codec() {
		return CODEC;
	}

	@Override
	protected boolean isPathfindable(BlockState state, PathComputationType type) {
		return type == PathComputationType.LAND && state.getValue(LAYERS) < HEIGHT_IMPASSABLE;
	}

	@Override
	protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
		int layers = state.getValue(LAYERS);
		VoxelShape prevShape = SHAPES[layers - 1];

		if (!(context instanceof EntityCollisionContext entityContext))
			return prevShape;

		if (!(entityContext.getEntity() instanceof FallingBlockEntity fallingBlock))
			return prevShape;

		return !fallingBlock.getBlockState().is(this) ? prevShape : SHAPES[layers];
	}

	@Override
	protected VoxelShape getBlockSupportShape(final BlockState state, final BlockGetter level, final BlockPos pos) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	protected VoxelShape getVisualShape(final BlockState state, final BlockGetter level, final BlockPos pos, final CollisionContext context) {
		return SHAPES[state.getValue(LAYERS)];
	}

	@Override
	protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
		if (this.canFall(level.getBlockState(pos.below())) && pos.getY() >= level.getMinY()) {
			this.falling(FallingBlockEntity.fall(level, pos, state));
		}
	}

	private boolean canFall(BlockState stateBelow) {
		return isFree(stateBelow) || stateBelow.is(this) && stateBelow.getValue(LAYERS) < MAX_LAYERS;
	}

	@Override
	public int getDustColor(BlockState blockState, BlockGetter level, BlockPos pos) {
		return blockState.getMapColor(level, pos).col;
	}

	@Override
	protected boolean useShapeForLightOcclusion(BlockState state) {
		return true;
	}

	@Override
	protected boolean canBeReplaced(BlockState state, BlockPlaceContext context) {
		int layers = state.getValue(LAYERS);

		if (!context.getItemInHand().is(this.asItem()) || layers == MAX_LAYERS)
			return layers == 1;

		return !context.replacingClickedOnBlock() || context.getClickedFace() == Direction.UP;
	}

	@Override
	@Nullable
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.increaseHeightOrGetDefault(context.getLevel(), context.getClickedPos());
	}

	public BlockState increaseHeightOrGetDefault(Level level, BlockPos pos) {
		BlockState blockState = level.getBlockState(pos);

		if (!blockState.is(this))
			return createNewState(1, level, pos);

		return blockState.setValue(LAYERS, Math.min(MAX_LAYERS, blockState.getValue(LAYERS) + 1));
	}

	@Override
	protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos, Direction directionToNeighbour, BlockPos neighbourPos, BlockState neighbourState, RandomSource random) {
		if (state.getValue(WATERLOGGED)) {
			ticks.scheduleTick(pos, Fluids.WATER.getSource(), Fluids.WATER.getTickDelay(level));
		}

		return super.updateShape(state, level, ticks, pos, directionToNeighbour, neighbourPos, neighbourState, random);
	}

	@Override
	protected FluidState getFluidState(BlockState state) {
		return state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
	}

	@Override
	protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
		BlockState belowState = level.getBlockState(pos.below());

		if (belowState.is(BlockTags.CANNOT_SUPPORT_SNOW_LAYER)) {
			return false;
		}

		if (belowState.is(BlockTags.SUPPORT_OVERRIDE_SNOW_LAYER)) {
			return true;
		}

		return isFaceFull(belowState.getCollisionShape(level, pos.below()), Direction.UP) || belowState.is(this) && belowState.getValue(LAYERS) == MAX_LAYERS;
	}

	public static void merge(ServerLevel level, BlockPos pos, BlockState stateToMergeWith, FallingBlockEntity fallingBlockEntity, int totalLayers) {
		ChunkMap chunkMap = level.getChunkSource().chunkMap;
		BlockState mergedState = createNewState(Math.min(MAX_LAYERS, totalLayers), level, pos);

		if (level.setBlockAndUpdate(pos, mergedState)) {
			finalizeMerge(level, pos, pos, stateToMergeWith, mergedState, chunkMap, fallingBlockEntity);
		}
		if (totalLayers > MAX_LAYERS) {
			placeRemainderOrDropItem(level, pos, chunkMap, fallingBlockEntity, totalLayers - MAX_LAYERS);
		}
	}

	private static void placeRemainderOrDropItem(ServerLevel level, BlockPos pos, ChunkMap chunkMap, FallingBlockEntity fallingBlockEntity, int remainder) {
		BlockPos above = pos.above();
		BlockState stateAbove = level.getBlockState(above);

		boolean replaceAbove = stateAbove.canBeReplaced(new DirectionalPlaceContext(level, above, Direction.DOWN, ItemStack.EMPTY, Direction.UP));
		BlockState remainderState = createNewState(remainder, level, above);

		if (replaceAbove && level.setBlockAndUpdate(above, remainderState)) {
			finalizeMerge(level, pos, above, stateAbove, remainderState, chunkMap, fallingBlockEntity);
		}
		else if (level.getGameRules().get(GameRules.ENTITY_DROPS)) {
			popResource(level, above, new ItemStack(ModBlocks.DETRITUS.asItem(), remainder));
		}
	}

	private static BlockState createNewState(int layers, LevelReader level, BlockPos pos) {
		return createNewState(layers, level.getFluidState(pos).isSourceOfType(Fluids.WATER));
	}

	private static BlockState createNewState(int layers, boolean waterlogged) {
		return ModBlocks.DETRITUS.get().defaultBlockState()
			.setValue(LAYERS, layers)
			.setValue(WATERLOGGED, waterlogged);
	}

	private static void finalizeMerge(Level level, BlockPos pos, BlockPos updatePos, BlockState stateToMergeWith, BlockState stateAfterMerge, ChunkMap chunkMap, FallingBlockEntity fallingBlockEntity) {
		pushEntitiesUp(stateToMergeWith, stateAfterMerge, level, pos);
		chunkMap.sendToTrackingPlayers(fallingBlockEntity, new ClientboundBlockUpdatePacket(updatePos, stateToMergeWith));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
		builder.add(LAYERS, WATERLOGGED);
	}

}
