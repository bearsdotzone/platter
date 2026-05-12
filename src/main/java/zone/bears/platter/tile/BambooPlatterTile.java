package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class BambooPlatterTile extends PlatterTile {

    public BambooPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.bamboo_platter_tile.get(), blockPos, blockState);
    }
}
