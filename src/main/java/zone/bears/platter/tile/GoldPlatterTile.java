package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class GoldPlatterTile extends PlatterTile {
    public GoldPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.gold_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
