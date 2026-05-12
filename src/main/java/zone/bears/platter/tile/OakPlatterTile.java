package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class OakPlatterTile extends PlatterTile {
    public OakPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.oak_platter_tile.get(), blockPos, blockState);
    }
}
