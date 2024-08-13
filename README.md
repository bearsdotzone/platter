# Platter
Platter adds a variety of platter blocks that automatically feed nearby creatures and players.

Right-clicking puts items onto the platter. Sneak right-clicking the platter
removes the item on top of the platter. Platters will stop dispensing food when a redstone signal is applied. Platters support comparator output. Wooden platters will feed animals *and* players, metal and stone platters will only feed players.

I have tried to give Platter sensible, configurable defaults. By default, a platter will store 9 stacks and search in a 5 block cubic "radius" for creatures to feed every 60 seconds.

### Compatibility
From 1.15 to 1.20.1 Platter was developed for the Forge mod loader. As of 1.20.6 Platter will be developed exclusively for the NeoForge mod loader.

## Recipe

![recipe](https://i.imgur.com/NTVeDxD.png "recipe")

Replace the wood planks with other wood planks, iron or gold ingots, or stone to achieve other platter variants.

## Modpack Permission

Yes.

## To-Do

N/A

## Known Bugs

N/A

## Development

This mod has been a learning experience for me, @bearsdotzone, as I attempt to wrap my head around forge mod
development. As such, this mod would not have been possible without [McJty's Mod Tutorials](https://wiki.mcjty.eu), and
open source projects such
as [AppleSkin](https://github.com/squeek502/AppleSkin), [Botania](https://github.com/Vazkii/Botania), [Iron Chests](https://github.com/progwml6/ironchest), [Railcraft](https://github.com/Railcraft/Railcraft),
and [Quark](https://github.com/Vazkii/Quark/).

Particularly, under Appleskin's unlicense I used the code for network synchronization of hunger and saturation.