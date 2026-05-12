package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class CrimsonPlatterTile extends PlatterTile {
    public CrimsonPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.crimson_platter_tile.get(), blockPos, blockState);
    }
}
