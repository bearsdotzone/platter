package com.abneyonline.platter;

import com.abneyonline.platter.tile.*;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import static com.abneyonline.platter.PlatterMod.MODID;

public class Registration {

    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);
    private static final DeferredRegister<TileEntityType<?>> TILES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MODID);
//    private static final DeferredRegister<ContainerType<?>> CONTAINERS = new DeferredRegister<>(ForgeRegistries.CONTAINERS, MODID);
//    private static final DeferredRegister<EntityType<?>> ENTITIES = new DeferredRegister<>(ForgeRegistries.ENTITIES, MODID);
//    private static final DeferredRegister<ModDimension> DIMENSIONS = new DeferredRegister<>(ForgeRegistries.MOD_DIMENSIONS, MODID);

    public static final RegistryObject<PlatterBlock> oak_platter_block = BLOCKS.register("oak_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> oak_platter_block_item = ITEMS.register("oak_platter_block", () -> new BlockItem(oak_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<OakPlatterTile>> oak_platter_tile = TILES.register("oak_platter_block", () -> TileEntityType.Builder.create(OakPlatterTile::new, oak_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> spruce_platter_block = BLOCKS.register("spruce_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> spruce_platter_block_item = ITEMS.register("spruce_platter_block", () -> new BlockItem(spruce_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<SprucePlatterTile>> spruce_platter_tile = TILES.register("spruce_platter_block", () -> TileEntityType.Builder.create(SprucePlatterTile::new, spruce_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> birch_platter_block = BLOCKS.register("birch_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> birch_platter_block_item = ITEMS.register("birch_platter_block", () -> new BlockItem(birch_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<BirchPlatterTile>> birch_platter_tile = TILES.register("birch_platter_block", () -> TileEntityType.Builder.create(BirchPlatterTile::new, birch_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> jungle_platter_block = BLOCKS.register("jungle_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> jungle_platter_block_item = ITEMS.register("jungle_platter_block", () -> new BlockItem(jungle_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<JunglePlatterTile>> jungle_platter_tile = TILES.register("jungle_platter_block", () -> TileEntityType.Builder.create(JunglePlatterTile::new, jungle_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> acacia_platter_block = BLOCKS.register("acacia_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> acacia_platter_block_item = ITEMS.register("acacia_platter_block", () -> new BlockItem(acacia_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<AcaciaPlatterTile>> acacia_platter_tile = TILES.register("acacia_platter_block", () -> TileEntityType.Builder.create(AcaciaPlatterTile::new, acacia_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> dark_oak_platter_block = BLOCKS.register("dark_oak_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> dark_oak_platter_block_item = ITEMS.register("dark_oak_platter_block", () -> new BlockItem(dark_oak_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<DarkOakPlatterTile>> dark_oak_platter_tile = TILES.register("dark_oak_platter_block", () -> TileEntityType.Builder.create(DarkOakPlatterTile::new, dark_oak_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> stone_platter_block = BLOCKS.register("stone_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> stone_platter_block_item = ITEMS.register("stone_platter_block", () -> new BlockItem(stone_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<StonePlatterTile>> stone_platter_tile = TILES.register("stone_platter_block", () -> TileEntityType.Builder.create(StonePlatterTile::new, stone_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> iron_platter_block = BLOCKS.register("iron_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> iron_platter_block_item = ITEMS.register("iron_platter_block", () -> new BlockItem(iron_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<IronPlatterTile>> iron_platter_tile = TILES.register("iron_platter_block", () -> TileEntityType.Builder.create(IronPlatterTile::new, iron_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> gold_platter_block = BLOCKS.register("gold_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> gold_platter_block_item = ITEMS.register("gold_platter_block", () -> new BlockItem(gold_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<GoldPlatterTile>> gold_platter_tile = TILES.register("gold_platter_block", () -> TileEntityType.Builder.create(GoldPlatterTile::new, gold_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> crimson_platter_block = BLOCKS.register("crimson_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> crimson_platter_block_item = ITEMS.register("crimson_platter_block", () -> new BlockItem(crimson_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<CrimsonPlatterTile>> crimson_platter_tile = TILES.register("crimson_platter_block", () -> TileEntityType.Builder.create(CrimsonPlatterTile::new, crimson_platter_block.get()).build(null));

    public static final RegistryObject<PlatterBlock> warped_platter_block = BLOCKS.register("warped_platter_block", PlatterBlock::new);
    public static final RegistryObject<Item> warped_platter_block_item = ITEMS.register("warped_platter_block", () -> new BlockItem(warped_platter_block.get(), new Item.Properties().group(ItemGroup.FOOD)));
    public static final RegistryObject<TileEntityType<WarpedPlatterTile>> warped_platter_tile = TILES.register("warped_platter_block", () -> TileEntityType.Builder.create(WarpedPlatterTile::new, warped_platter_block.get()).build(null));

    public static void init() {
        BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        TILES.register(FMLJavaModLoadingContext.get().getModEventBus());
//        CONTAINERS.register(FMLJavaModLoadingContext.get().getModEventBus());
//        ENTITIES.register(FMLJavaModLoadingContext.get().getModEventBus());
//        DIMENSIONS.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
