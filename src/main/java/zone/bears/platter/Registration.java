package zone.bears.platter;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.component.ItemLore;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.bears.platter.client.PlatterRenderer;
import zone.bears.platter.network.PlatterClientPayloadHandler;
import zone.bears.platter.network.PlatterRenderPacket;
import zone.bears.platter.tile.*;

@EventBusSubscriber(modid = "platter")
public class Registration {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Platter.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Platter.MODID);
    static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, Platter.MODID);

    private static final Item.Properties nonWoodProperties = new Item.Properties().useBlockDescriptionPrefix().component(DataComponents.LORE, ItemLore.EMPTY.withLineAdded(Component.translatable("platter.non_wood_tooltip").withStyle(ChatFormatting.GRAY)));

    public static final DeferredBlock<Block> oak_platter_block = BLOCKS.register("oak_platter_block", () -> new PlatterBlock(MapColor.WOOD, "oak_platter_block"));
    public static final DeferredItem<BlockItem> oak_platter_block_item = ITEMS.registerSimpleBlockItem("oak_platter_block", oak_platter_block);

    public static final DeferredHolder<Block, Block> spruce_platter_block = BLOCKS.register("spruce_platter_block", () -> new PlatterBlock(MapColor.WOOD, "spruce_platter_block"));
    public static final DeferredItem<BlockItem> spruce_platter_block_item = ITEMS.registerSimpleBlockItem("spruce_platter_block", spruce_platter_block);

    public static final DeferredHolder<Block, Block> birch_platter_block = BLOCKS.register("birch_platter_block", () -> new PlatterBlock(MapColor.WOOD, "birch_platter_block"));
    public static final DeferredItem<BlockItem> birch_platter_block_item = ITEMS.registerSimpleBlockItem("birch_platter_block", birch_platter_block);

    public static final DeferredHolder<Block, Block> jungle_platter_block = BLOCKS.register("jungle_platter_block", () -> new PlatterBlock(MapColor.WOOD, "jungle_platter_block"));
    public static final DeferredItem<BlockItem> jungle_platter_block_item = ITEMS.registerSimpleBlockItem("jungle_platter_block", jungle_platter_block);

    public static final DeferredHolder<Block, Block> acacia_platter_block = BLOCKS.register("acacia_platter_block", () -> new PlatterBlock(MapColor.WOOD, "acacia_platter_block"));
    public static final DeferredItem<BlockItem> acacia_platter_block_item = ITEMS.registerSimpleBlockItem("acacia_platter_block", acacia_platter_block);

    public static final DeferredHolder<Block, Block> dark_oak_platter_block = BLOCKS.register("dark_oak_platter_block", () -> new PlatterBlock(MapColor.WOOD, "dark_oak_platter_block"));
    public static final DeferredItem<BlockItem> dark_oak_platter_block_item = ITEMS.registerSimpleBlockItem("dark_oak_platter_block", dark_oak_platter_block);

    public static final DeferredHolder<Block, Block> stone_platter_block = BLOCKS.register("stone_platter_block", () -> new PlatterBlock(MapColor.STONE, "stone_platter_block"));
    public static final DeferredItem<BlockItem> stone_platter_block_item = ITEMS.registerSimpleBlockItem("stone_platter_block", stone_platter_block, () -> nonWoodProperties);

    public static final DeferredHolder<Block, Block> iron_platter_block = BLOCKS.register("iron_platter_block", () -> new PlatterBlock(MapColor.METAL, "iron_platter_block"));
    public static final DeferredItem<BlockItem> iron_platter_block_item = ITEMS.registerSimpleBlockItem("iron_platter_block", iron_platter_block, () -> nonWoodProperties);

    public static final DeferredHolder<Block, Block> gold_platter_block = BLOCKS.register("gold_platter_block", () -> new PlatterBlock(MapColor.METAL, "gold_platter_block"));
    public static final DeferredItem<BlockItem> gold_platter_block_item = ITEMS.registerSimpleBlockItem("gold_platter_block", gold_platter_block, () -> nonWoodProperties);

    public static final DeferredHolder<Block, Block> crimson_platter_block = BLOCKS.register("crimson_platter_block", () -> new PlatterBlock(MapColor.WOOD, "crimson_platter_block"));
    public static final DeferredItem<BlockItem> crimson_platter_block_item = ITEMS.registerSimpleBlockItem("crimson_platter_block", crimson_platter_block);

    public static final DeferredHolder<Block, Block> warped_platter_block = BLOCKS.register("warped_platter_block", () -> new PlatterBlock(MapColor.WOOD, "warped_platter_block"));
    public static final DeferredItem<BlockItem> warped_platter_block_item = ITEMS.registerSimpleBlockItem("warped_platter_block", warped_platter_block);

    public static final DeferredHolder<Block, Block> mangrove_platter_block = BLOCKS.register("mangrove_platter_block", () -> new PlatterBlock(MapColor.WOOD, "mangrove_platter_block"));
    public static final DeferredItem<BlockItem> mangrove_platter_block_item = ITEMS.registerSimpleBlockItem("mangrove_platter_block", mangrove_platter_block);

    public static final DeferredHolder<Block, Block> bamboo_platter_block = BLOCKS.register("bamboo_platter_block", () -> new PlatterBlock(MapColor.WOOD, "bamboo_platter_block"));
    public static final DeferredItem<BlockItem> bamboo_platter_block_item = ITEMS.registerSimpleBlockItem("bamboo_platter_block", bamboo_platter_block);

    public static final DeferredHolder<Block, Block> cherry_platter_block = BLOCKS.register("cherry_platter_block", () -> new PlatterBlock(MapColor.WOOD, "cherry_platter_block"));
    public static final DeferredItem<BlockItem> cherry_platter_block_item = ITEMS.registerSimpleBlockItem("cherry_platter_block", cherry_platter_block);

    public static final DeferredHolder<Block, Block> pale_oak_platter_block = BLOCKS.register("pale_oak_platter_block", () -> new PlatterBlock(MapColor.WOOD, "pale_oak_platter_block"));
    public static final DeferredItem<BlockItem> pale_oak_platter_block_item = ITEMS.registerSimpleBlockItem("pale_oak_platter_block", pale_oak_platter_block);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlatterTile>> platter_tile = TILES.register("platter_tile", () -> new BlockEntityType<>(PlatterTile::new, oak_platter_block.get(), spruce_platter_block.get(), birch_platter_block.get(), jungle_platter_block.get(), acacia_platter_block.get(), dark_oak_platter_block.get(), crimson_platter_block.get(), warped_platter_block.get(), mangrove_platter_block.get(), bamboo_platter_block.get(), cherry_platter_block.get(), pale_oak_platter_block.get()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PersonalPlatterTile>> personal_platter_tile = TILES.register("personal_platter_tile", () -> new BlockEntityType<>(PersonalPlatterTile::new, stone_platter_block.get(), iron_platter_block.get(), gold_platter_block.get()));

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILES.register(modEventBus);
    }

    @SubscribeEvent
    public static void buildContents(BuildCreativeModeTabContentsEvent event) {
        // Add to ingredients tab
        if (event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            for (DeferredHolder<Item, ? extends Item> i : ITEMS.getEntries()) {
                event.accept(i.get());
            }
        }
    }

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> i : TILES.getEntries()) {
            event.registerBlockEntity(Capabilities.Item.BLOCK, i.get(), (be, side) -> {
                if (be instanceof PlatterTile platterTile)
                    return platterTile.itemStackHandler;
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(PlatterRenderPacket.TYPE, PlatterRenderPacket.STREAM_CODEC, PlatterClientPayloadHandler::handleData);
    }

}

@EventBusSubscriber(modid = "platter", value = Dist.CLIENT)
class RegisterClient {
    @SubscribeEvent
    public static void registerEntityRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> i : zone.bears.platter.Registration.TILES.getEntries()) {
            event.registerBlockEntityRenderer(i.get(), (j) -> new PlatterRenderer(j));
        }
    }
}
