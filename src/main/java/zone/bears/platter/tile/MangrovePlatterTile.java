package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class MangrovePlatterTile extends PlatterTile {

    public MangrovePlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.mangrove_platter_tile.get(), blockPos, blockState);
    }
}
