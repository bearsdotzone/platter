package zone.bears.platter;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.material.MapColor;
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

@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD, modid = "platter")
public class Registration {

    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(PlatterMod.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(PlatterMod.MODID);
    private static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, PlatterMod.MODID);
//    private static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES = DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, PlatterMod.MODID);

    public static final DeferredBlock<Block> oak_platter_block = BLOCKS.register("oak_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredItem<BlockItem> oak_platter_block_item = ITEMS.registerSimpleBlockItem("oak_platter_block", oak_platter_block);
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<OakPlatterTile>> oak_platter_tile = TILES.register("oak_platter_block", () -> BlockEntityType.Builder.of(OakPlatterTile::new, oak_platter_block.get())
                                                                                                                                                                                .build(null));

    public static final DeferredHolder<Block, Block> spruce_platter_block = BLOCKS.register("spruce_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> spruce_platter_block_item = ITEMS.register("spruce_platter_block", () -> new BlockItem(spruce_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SprucePlatterTile>> spruce_platter_tile = TILES.register("spruce_platter_block", () -> BlockEntityType.Builder.of(SprucePlatterTile::new, spruce_platter_block.get())
                                                                                                                                                                                         .build(null));

    public static final DeferredHolder<Block, Block> birch_platter_block = BLOCKS.register("birch_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> birch_platter_block_item = ITEMS.register("birch_platter_block", () -> new BlockItem(birch_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BirchPlatterTile>> birch_platter_tile = TILES.register("birch_platter_block", () -> BlockEntityType.Builder.of(BirchPlatterTile::new, birch_platter_block.get())
                                                                                                                                                                                      .build(null));

    public static final DeferredHolder<Block, Block> jungle_platter_block = BLOCKS.register("jungle_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> jungle_platter_block_item = ITEMS.register("jungle_platter_block", () -> new BlockItem(jungle_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<JunglePlatterTile>> jungle_platter_tile = TILES.register("jungle_platter_block", () -> BlockEntityType.Builder.of(JunglePlatterTile::new, jungle_platter_block.get())
                                                                                                                                                                                         .build(null));

    public static final DeferredHolder<Block, Block> acacia_platter_block = BLOCKS.register("acacia_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> acacia_platter_block_item = ITEMS.register("acacia_platter_block", () -> new BlockItem(acacia_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<AcaciaPlatterTile>> acacia_platter_tile = TILES.register("acacia_platter_block", () -> BlockEntityType.Builder.of(AcaciaPlatterTile::new, acacia_platter_block.get())
                                                                                                                                                                                         .build(null));

    public static final DeferredHolder<Block, Block> dark_oak_platter_block = BLOCKS.register("dark_oak_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> dark_oak_platter_block_item = ITEMS.register("dark_oak_platter_block", () -> new BlockItem(dark_oak_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<DarkOakPlatterTile>> dark_oak_platter_tile = TILES.register("dark_oak_platter_block", () -> BlockEntityType.Builder.of(DarkOakPlatterTile::new, dark_oak_platter_block.get())
                                                                                                                                                                                              .build(null));

    public static final DeferredHolder<Block, Block> stone_platter_block = BLOCKS.register("stone_platter_block", () -> new PlatterBlock(MapColor.STONE));
    public static final DeferredHolder<Item, Item> stone_platter_block_item = ITEMS.register("stone_platter_block", () -> new BlockItem(stone_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<StonePlatterTile>> stone_platter_tile = TILES.register("stone_platter_block", () -> BlockEntityType.Builder.of(StonePlatterTile::new, stone_platter_block.get())
                                                                                                                                                                                      .build(null));

    public static final DeferredHolder<Block, Block> iron_platter_block = BLOCKS.register("iron_platter_block", () -> new PlatterBlock(MapColor.METAL));
    public static final DeferredHolder<Item, Item> iron_platter_block_item = ITEMS.register("iron_platter_block", () -> new BlockItem(iron_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<IronPlatterTile>> iron_platter_tile = TILES.register("iron_platter_block", () -> BlockEntityType.Builder.of(IronPlatterTile::new, iron_platter_block.get())
                                                                                                                                                                                   .build(null));

    public static final DeferredHolder<Block, Block> gold_platter_block = BLOCKS.register("gold_platter_block", () -> new PlatterBlock(MapColor.METAL));
    public static final DeferredHolder<Item, Item> gold_platter_block_item = ITEMS.register("gold_platter_block", () -> new BlockItem(gold_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<GoldPlatterTile>> gold_platter_tile = TILES.register("gold_platter_block", () -> BlockEntityType.Builder.of(GoldPlatterTile::new, gold_platter_block.get())
                                                                                                                                                                                   .build(null));

    public static final DeferredHolder<Block, Block> crimson_platter_block = BLOCKS.register("crimson_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> crimson_platter_block_item = ITEMS.register("crimson_platter_block", () -> new BlockItem(crimson_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CrimsonPlatterTile>> crimson_platter_tile = TILES.register("crimson_platter_block", () -> BlockEntityType.Builder.of(CrimsonPlatterTile::new, crimson_platter_block.get())
                                                                                                                                                                                            .build(null));

    public static final DeferredHolder<Block, Block> warped_platter_block = BLOCKS.register("warped_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> warped_platter_block_item = ITEMS.register("warped_platter_block", () -> new BlockItem(warped_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<WarpedPlatterTile>> warped_platter_tile = TILES.register("warped_platter_block", () -> BlockEntityType.Builder.of(WarpedPlatterTile::new, warped_platter_block.get())
                                                                                                                                                                                         .build(null));

    public static final DeferredHolder<Block, Block> mangrove_platter_block = BLOCKS.register("mangrove_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> mangrove_platter_block_item = ITEMS.register("mangrove_platter_block", () -> new BlockItem(mangrove_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<MangrovePlatterTile>> mangrove_platter_tile = TILES.register("mangrove_platter_block", () -> BlockEntityType.Builder.of(MangrovePlatterTile::new, mangrove_platter_block.get())
                                                                                                                                                                                               .build(null));

    public static final DeferredHolder<Block, Block> bamboo_platter_block = BLOCKS.register("bamboo_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> bamboo_platter_block_item = ITEMS.register("bamboo_platter_block", () -> new BlockItem(bamboo_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<BambooPlatterTile>> bamboo_platter_tile = TILES.register("bamboo_platter_block", () -> BlockEntityType.Builder.of(BambooPlatterTile::new, bamboo_platter_block.get())
                                                                                                                                                                                         .build(null));

    public static final DeferredHolder<Block, Block> cherry_platter_block = BLOCKS.register("cherry_platter_block", () -> new PlatterBlock(MapColor.WOOD));
    public static final DeferredHolder<Item, Item> cherry_platter_block_item = ITEMS.register("cherry_platter_block", () -> new BlockItem(cherry_platter_block.get(), new Item.Properties()));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<CherryPlatterTile>> cherry_platter_tile = TILES.register("cherry_platter_block", () -> BlockEntityType.Builder.of(CherryPlatterTile::new, cherry_platter_block.get())
                                                                                                                                                                                         .build(null));

//    public static final Supplier<AttachmentType<PlatterTile.PlatterItemStackHandler>> PLATTER_ITEM_STACK_HANDLER = ATTACHMENT_TYPES.register("platter_item_stack_handler", () -> AttachmentType.serializable((x) -> new PlatterTile.PlatterItemStackHandler(Config.PLATTER_SLOTS.getAsInt())).build());

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILES.register(modEventBus);
//        ATTACHMENT_TYPES.register(modEventBus);
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
            event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, i.get(), (be, side) -> {
                if (be instanceof PlatterTile platterTile)
                    return platterTile.itemStackHandler;
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        for (DeferredHolder<BlockEntityType<?>, ? extends BlockEntityType<?>> i : TILES.getEntries()) {
            event.registerBlockEntityRenderer(i.get(), PlatterRenderer::new);
        }
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(PlatterRenderPacket.TYPE, PlatterRenderPacket.STREAM_CODEC, PlatterClientPayloadHandler::handleData);
    }

}
