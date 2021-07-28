package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StonePlatterTile extends PlatterTile {
    public StonePlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super (Registration.stone_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
