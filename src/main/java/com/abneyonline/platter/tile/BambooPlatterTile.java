package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BambooPlatterTile extends PlatterTile {

    public BambooPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.bamboo_platter_tile.get(), blockPos, blockState);
    }
}
