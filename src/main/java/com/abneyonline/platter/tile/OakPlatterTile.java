package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OakPlatterTile extends PlatterTile {
    public OakPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.oak_platter_tile.get(), blockPos, blockState);
    }
}
