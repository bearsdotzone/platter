package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class DarkOakPlatterTile extends PlatterTile {

    public DarkOakPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.dark_oak_platter_tile.get(), blockPos, blockState);
    }
}
