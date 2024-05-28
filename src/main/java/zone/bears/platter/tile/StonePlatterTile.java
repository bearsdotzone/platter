package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class StonePlatterTile extends PlatterTile {
    public StonePlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.stone_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
