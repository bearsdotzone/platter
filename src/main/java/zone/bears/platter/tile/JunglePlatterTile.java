package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import zone.bears.platter.Registration;

public class JunglePlatterTile extends PlatterTile {

    public JunglePlatterTile(BlockPos blockPos, BlockState blockState) {
        super(Registration.jungle_platter_tile.get(), blockPos, blockState);
    }
}
