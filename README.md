# Platter
A forge mod for Minecraft that adds platters from which nearby creatures and players will be fed autonomously.

In practicality, a platter, when supplied with appropriate foodstuffs, will feed a player when they are nearby and hungry. Wooden platters will also feed and breed any animals that are nearby.

Right clicking will take the item from a player's hand and stack it on the platter. Shift right clicking the platter will remove the most recently added item. Platters will disable when applied a redstone signal and output a comparator signal as appropriate. Platters created without wood will only feed players.

I have attempted to make the mod configurable to the extent that it can be made more or less server friendly. By default, a platter will store 9 stacks and search in a 5 block "radius" for creatures to feed every 60 seconds.

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
This mod has been a learning experience for me, @bearsdotzone, as I attempt to wrap my head around forge mod development. As such, this mod would not have been possible without [McJty's Mod Tutorials](https://wiki.mcjty.eu), and open source projects such as [AppleSkin](https://github.com/squeek502/AppleSkin), [Botania](https://github.com/Vazkii/Botania), [Iron Chests](https://github.com/progwml6/ironchest), [Railcraft](https://github.com/Railcraft/Railcraft), and [Quark](https://github.com/Vazkii/Quark/).

Particularly, under Appleskin's unlicense I used the code for network synchronization of hunger and saturation.