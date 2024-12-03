package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class PaleOakPlatterTile extends PlatterTile {

    public PaleOakPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.pale_oak_platter_tile.get(), blockPos, blockState);
    }
}
