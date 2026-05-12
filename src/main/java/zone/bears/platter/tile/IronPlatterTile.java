package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class IronPlatterTile extends PlatterTile {

    public IronPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.iron_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
