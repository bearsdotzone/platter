package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CherryPlatterTile extends PlatterTile {

    public CherryPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.cherry_platter_tile.get(), blockPos, blockState);
    }
}
