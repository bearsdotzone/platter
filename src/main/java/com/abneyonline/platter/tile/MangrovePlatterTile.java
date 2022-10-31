package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MangrovePlatterTile extends PlatterTile {

    public MangrovePlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.mangrove_platter_tile.get(), blockPos, blockState);
    }
}
