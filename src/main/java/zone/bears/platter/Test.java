package zone.bears.platter;

import net.minecraft.core.BlockPos;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.gametest.GameTestHolder;
import net.neoforged.neoforge.gametest.PrefixGameTestTemplate;
import net.neoforged.neoforge.items.IItemHandler;
import zone.bears.platter.tile.PlatterTile;

@GameTestHolder("platter")
public class Test {
    @PrefixGameTestTemplate(false)
    @GameTest
    public static void PlatterBreedingTest(GameTestHelper gameTestHelper) {
        BlockPos platterPos = new BlockPos(5, 6, 5);
        assert gameTestHelper.getBlockEntity(platterPos) instanceof PlatterTile;
        PlatterTile platterTile = gameTestHelper.getBlockEntity(platterPos);

        IItemHandler platterItemHandler = gameTestHelper.getLevel()
                .getCapability(Capabilities.ItemHandler.BLOCK, platterTile.getBlockPos(), gameTestHelper.getBlockState(platterPos), gameTestHelper.getBlockEntity(platterPos), null);
        assert platterItemHandler != null;

        gameTestHelper.assertContainerEmpty(platterPos);
        platterItemHandler.insertItem(0, new ItemStack(Items.WHEAT, 1), false);

        gameTestHelper.spawn(EntityType.COW, new BlockPos(5, 2, 5));
        platterTile.tickCount = Config.PLATTER_PERIOD.get() * 20;

        gameTestHelper.assertContainerEmpty(platterPos);
        gameTestHelper.succeedIf(() -> gameTestHelper.findOneEntity(EntityType.COW)
                .isInLove());

    }
}
