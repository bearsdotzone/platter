package zone.bears.platter;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
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
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import zone.bears.platter.client.PlatterRenderer;
import zone.bears.platter.network.PlatterClientPayloadHandler;
import zone.bears.platter.network.PlatterRenderPacket;
import zone.bears.platter.tile.PersonalPlatterTile;
import zone.bears.platter.tile.PlatterTile;

import java.util.Arrays;
import java.util.stream.Collectors;

@EventBusSubscriber(modid = "platter")
public class Registration {

    static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE,
            Platter.MODID);
    private static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(Platter.MODID);
    private static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Platter.MODID);

    private static final ItemLore description = ItemLore.EMPTY.withLineAdded(Component.translatable(
            "platter.platter_tooltip").withStyle(ChatFormatting.WHITE));

    private static final ItemLore inorganicDescription = description.withLineAdded(Component.translatable(
            "platter.non_wood_tooltip").withStyle(ChatFormatting.GRAY));

    private static final Item.Properties platterProperties = new Item.Properties().useBlockDescriptionPrefix()
            .component(DataComponents.LORE,
                    description);

    private static final Item.Properties nonWoodProperties = new Item.Properties().useBlockDescriptionPrefix()
            .component(DataComponents.LORE,
                    inorganicDescription);

    private static final String[] woodenIds = {"oak_platter_block",
            "spruce_platter_block",
            "birch_platter_block",
            "jungle_platter_block",
            "acacia_platter_block",
            "dark_oak_platter_block",
            "crimson_platter_block",
            "warped_platter_block",
            "mangrove_platter_block",
            "bamboo_platter_block",
            "cherry_platter_block",
            "pale_oak_platter_block",};
    protected static final DeferredHolder<Block, Block>[] platter_blocks = new DeferredHolder[woodenIds.length];
    private static final String[] personalIds = {"stone_platter_block", "iron_platter_block", "gold_platter_block",};
    protected static final DeferredHolder<Block, Block>[] personal_platter_blocks = new DeferredHolder[personalIds.length];

    static {
        for (int i = 0; i < woodenIds.length; i++) {
            int finalI = i;
            platter_blocks[i] = BLOCKS.register(woodenIds[i], () -> new PlatterBlock(MapColor.WOOD, woodenIds[finalI]));
            ITEMS.registerSimpleBlockItem(woodenIds[i], platter_blocks[i], () -> platterProperties);
        }

        for (int i = 0; i < personalIds.length; i++) {
            int finalI = i;
            personal_platter_blocks[i] = BLOCKS.register(personalIds[i],
                    () -> new PlatterBlock(personalIds[finalI].equals("stone_platter_block") ? MapColor.STONE : MapColor.METAL,
                            personalIds[finalI]));
            ITEMS.registerSimpleBlockItem(personalIds[i], personal_platter_blocks[i], () -> nonWoodProperties);
        }
    }

    public static void init(IEventBus modEventBus) {
        BLOCKS.register(modEventBus);
        ITEMS.register(modEventBus);
        TILES.register(modEventBus);
    }

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PlatterTile>> platter_tile = TILES.register(
            "platter_tile",
            () -> new BlockEntityType<>(PlatterTile::new,
                    Arrays.stream(platter_blocks).map(DeferredHolder::get).collect(Collectors.toSet())));

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<PersonalPlatterTile>> personal_platter_tile = TILES.register(
            "personal_platter_tile",
            () -> new BlockEntityType<>(PersonalPlatterTile::new,
                    Arrays.stream(personal_platter_blocks).map(DeferredHolder::get).collect(Collectors.toSet())));

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
                if (be instanceof PlatterTile platterTile) return platterTile.itemStackHandler;
                return null;
            });
        }
    }

    @SubscribeEvent
    public static void registerPayloads(final RegisterPayloadHandlersEvent event) {
        final PayloadRegistrar registrar = event.registrar("1");
        registrar.playToClient(PlatterRenderPacket.TYPE,
                PlatterRenderPacket.STREAM_CODEC,
                PlatterClientPayloadHandler::handleData);
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
