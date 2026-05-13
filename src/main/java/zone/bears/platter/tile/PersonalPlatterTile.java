package zone.bears.platter.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class PersonalPlatterTile extends PlatterTile {
    public PersonalPlatterTile(BlockPos blockPos, BlockState blockState) {
        super(zone.bears.platter.Registration.personal_platter_tile.get(), blockPos, blockState);
        tickForAnimals = false;
    }
}
