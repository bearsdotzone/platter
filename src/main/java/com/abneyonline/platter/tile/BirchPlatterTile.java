package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BirchPlatterTile extends PlatterTile {

    public BirchPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.birch_platter_tile.get(), blockPos, blockState);
    }
}
