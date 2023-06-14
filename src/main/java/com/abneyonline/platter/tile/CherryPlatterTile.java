package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CherryPlatterTile extends PlatterTile {

    public CherryPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.cherry_platter_tile.get(), blockPos, blockState);
    }
}
