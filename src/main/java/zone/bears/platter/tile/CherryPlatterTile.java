package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class CherryPlatterTile extends PlatterTile {

    public CherryPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.cherry_platter_tile.get(), blockPos, blockState);
    }
}
