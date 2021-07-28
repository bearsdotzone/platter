package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class DarkOakPlatterTile extends PlatterTile {

    public DarkOakPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.dark_oak_platter_tile.get(), blockPos, blockState);
    }
}
