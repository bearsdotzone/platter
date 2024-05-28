package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class CrimsonPlatterTile extends PlatterTile {
    public CrimsonPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.crimson_platter_tile.get(), blockPos, blockState);
    }
}
