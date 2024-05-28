package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class IronPlatterTile extends PlatterTile {

    public IronPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.iron_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
