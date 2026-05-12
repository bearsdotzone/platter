package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class AcaciaPlatterTile extends PlatterTile {

    public AcaciaPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.acacia_platter_tile.get(), blockPos, blockState);
    }
}
