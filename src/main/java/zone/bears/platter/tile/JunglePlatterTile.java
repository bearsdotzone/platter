package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class JunglePlatterTile extends PlatterTile {

    public JunglePlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.jungle_platter_tile.get(), blockPos, blockState);
    }
}
