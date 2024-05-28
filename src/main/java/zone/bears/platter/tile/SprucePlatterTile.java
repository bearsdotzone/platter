package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class SprucePlatterTile extends PlatterTile {
    public SprucePlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.spruce_platter_tile.get(), blockPos, blockState);
    }
}
