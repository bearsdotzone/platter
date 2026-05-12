package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class StonePlatterTile extends PlatterTile {
    public StonePlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.stone_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
