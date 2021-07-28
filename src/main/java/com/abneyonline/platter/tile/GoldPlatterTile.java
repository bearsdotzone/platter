package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class GoldPlatterTile extends PlatterTile {
    public GoldPlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super (Registration.gold_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
