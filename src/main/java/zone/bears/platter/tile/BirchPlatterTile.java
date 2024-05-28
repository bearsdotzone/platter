package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class BirchPlatterTile extends PlatterTile {

    public BirchPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.birch_platter_tile.get(), blockPos, blockState);
    }
}
