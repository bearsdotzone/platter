package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IronPlatterTile extends PlatterTile {

    public IronPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.iron_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
