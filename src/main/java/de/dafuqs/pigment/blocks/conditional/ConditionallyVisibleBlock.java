package de.dafuqs.pigment.blocks.conditional;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

public abstract class ConditionallyVisibleBlock extends Block {

    boolean couldSeeLast = true;

    protected abstract boolean isVisible(ClientPlayerEntity clientPlayerEntity, BlockState state);

    public ConditionallyVisibleBlock(Settings settings) {
        super(settings);
    }

    /**
     * If true the server calculates shadows, lighting and collisions for the block
     * @return
     */
    public abstract boolean isVisibleOnServer();
    public abstract VoxelShape getVisibleCollisionShape();
    public abstract VoxelShape getVisibleOutlineShape();

    /**
     * Hit box for walking against the block
     * @param state
     * @param world
     * @param pos
     * @param context
     * @return
     */
    @Deprecated
    public VoxelShape getCollisionShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean isVisible;
        if(world instanceof ClientWorld) {
            ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
            isVisible = isVisible(clientPlayerEntity, state);
            couldSeeLast = isVisible;
        } else {
            isVisible = isVisibleOnServer();
        }

        if(isVisible) {
            return getVisibleCollisionShape();
        } else {
            return VoxelShapes.empty();
        }
    }

    /**
     * Hit box for block breaking
     * @param state
     * @param world
     * @param pos
     * @param context
     * @return
     */
    @Deprecated
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        boolean visible;
        if(world instanceof ClientWorld) {
            ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
            visible = isVisible(clientPlayerEntity, state);
        } else {
            visible = isVisibleOnServer();
        }

        if(visible) {
            return getVisibleOutlineShape();
        } else {
            return VoxelShapes.empty();
        }
    }

    @Environment(EnvType.CLIENT)
    public boolean isSideInvisible(BlockState state, BlockState stateFrom, Direction direction) {
        ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().player;
        return !isVisible(clientPlayerEntity, state);
    }

    public boolean isTranslucent(BlockState state, BlockView world, BlockPos pos) {
        return true;
    }

}



