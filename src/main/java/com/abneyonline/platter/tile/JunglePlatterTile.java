package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class JunglePlatterTile extends PlatterTile {

    public JunglePlatterTile(BlockPos blockPos, BlockState blockState)
    {
        super(Registration.jungle_platter_tile.get(), blockPos, blockState);
    }
}
