import os

wood_variants = {'oak':'oak_planks',
            'spruce':'spruce_planks',
            'birch':'birch_planks',
            'jungle':'jungle_planks',
            'acacia':'acacia_planks',
            'dark_oak':'dark_oak_planks',
            'crimson':'crimson_planks',
            'warped':'warped_planks'}
metal_variants = {'iron':'iron_block',
            'gold':'gold_block'}
other_variants = {'stone':'stone'}
files = {'blockstates.json':'assets\\platter\\blockstates\\',
            'item.json':'assets\\platter\\item\\',
            'models_block.json':'assets\\platter\\models\\block\\',
            'models_item.json':'assets\\platter\\models\\item\\',
            'recipe.json':'data\\platter\\recipes\\',
            'loot_tables_block.json':'data\\platter\\loot_tables\\blocks\\'}

for x,y in files.items():
    for i, j in wood_variants.items():
        fileIn = open(x, 'r')
        if not os.path.exists(y):
            os.makedirs(y)
        fileOut = open(y + i +'_platter_block.json', 'w')
        readString = fileIn.readline()
        while readString:
            readString = readString.replace("{name}", i)
            readString = readString.replace("{material}", j)
            readString = readString.replace("{crafting}", j)
            fileOut.write(readString)
            readString = fileIn.readline()
        fileIn.close
        fileOut.close
    for i, j in metal_variants.items():
        fileIn = open(x, 'r')
        if not os.path.exists(y):
            os.makedirs(y)
        fileOut = open(y + i +'_platter_block.json', 'w')
        readString = fileIn.readline()
        while readString:
            readString = readString.replace("{name}", i)
            readString = readString.replace("{material}", j)
            readString = readString.replace("{crafting}", i + "_ingot")
            fileOut.write(readString)
            readString = fileIn.readline()
        fileIn.close
        fileOut.close
    for i, j in other_variants.items():
        fileIn = open(x, 'r')
        if not os.path.exists(y):
            os.makedirs(y)
        fileOut = open(y + i +'_platter_block.json', 'w')
        readString = fileIn.readline()
        while readString:
            readString = readString.replace("{name}", i)
            readString = readString.replace("{material}", j)
            readString = readString.replace("{crafting}", i)
            fileOut.write(readString)
            readString = fileIn.readline()
        fileIn.close
        fileOut.close
y= 'assets\\platter\\lang\\'
if not os.path.exists(y):
    os.makedirs(y)
fileOut = open(y + 'en_us.json', 'w')
fileOut.write('{\n')
all_variants = {**wood_variants, **metal_variants, **other_variants}
for x in all_variants:
    toWrite = '\t"block.platter.{name}_platter_block": "{name} Platter"'.replace('{name}', x, 1)
    toWrite = toWrite.replace('{name}', x.replace('_', ' ').title())
    if x != 'stone':
        toWrite += ','
    fileOut.write(toWrite + '\n')
fileOut.write('}\n')