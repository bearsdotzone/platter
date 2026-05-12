package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class WarpedPlatterTile extends PlatterTile {
    public WarpedPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.warped_platter_tile.get(), blockPos, blockState);
    }
}
