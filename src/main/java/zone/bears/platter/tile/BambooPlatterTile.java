package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class BambooPlatterTile extends PlatterTile {

    public BambooPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.bamboo_platter_tile.get(), blockPos, blockState);
    }
}
