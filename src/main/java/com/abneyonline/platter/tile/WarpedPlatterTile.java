package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WarpedPlatterTile extends PlatterTile {
    public WarpedPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.warped_platter_tile.get(), blockPos, blockState);
    }
}
