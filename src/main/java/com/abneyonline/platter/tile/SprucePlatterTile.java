package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class SprucePlatterTile extends PlatterTile {
    public SprucePlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.spruce_platter_tile.get(), blockPos, blockState);
    }
}
