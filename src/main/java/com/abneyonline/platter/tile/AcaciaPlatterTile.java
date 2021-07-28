package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AcaciaPlatterTile extends PlatterTile {

    public AcaciaPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.acacia_platter_tile.get(), blockPos, blockState);
    }
}
