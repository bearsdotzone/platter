# Platter
A forge mod for Minecraft 1.15 that adds platters from which nearby creatures and players will be fed autonomously.

In practicality, a platter, when supplied with appropriate foodstuffs, will feed a player when they are nearby and hungry in addition to feeding and breeding any animals that are nearby.

Right clicking will take the item from a player's hand and stack it on the platter. Shift right clicking the platter will remove the most recently added item.

I have attempted to make the mod configurable to the extent that it can be made more or less server friendly. By default, a platter will store 9 stacks and search in a 5 block "radius" for creatures to feed every 60 seconds.

## Known Bugs

Spectral Beef

## Development
This mod has been a learning experience for me, @abneyonline, as I attempt to wrap my head around forge mod development. As such, this mod would not have been possible without [McJty's Mod Tutorials](https://wiki.mcjty.eu), and open source projects such as [AppleSkin](https://github.com/squeek502/AppleSkin), [Botania](https://github.com/Vazkii/Botania), [Iron Chests](https://github.com/progwml6/ironchest), [Railcraft](https://github.com/Railcraft/Railcraft), and [Quark](https://github.com/Vazkii/Quark/).

Particularly, under Appleskin's unlicense I used the code for network synchronization of hunger and saturation.