package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class WarpedPlatterTile extends PlatterTile {
    public WarpedPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.warped_platter_tile.get(), blockPos, blockState);
    }
}
