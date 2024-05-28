package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class OakPlatterTile extends PlatterTile {
    public OakPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.oak_platter_tile.get(), blockPos, blockState);
    }
}
