import os

variants = ['oak', 'spruce', 'birch', 'jungle', 'acacia', 'dark_oak']
files = {'blockstates.json':'assets\\platter\\blockstates\\',
            'item.json':'assets\\platter\\item\\',
            'models_block.json':'assets\\platter\\models\\block\\',
            'models_item.json':'assets\\platter\\models\\item\\',
            'recipe.json':'data\\platter\\recipes\\',
            'loot_tables_block.json':'data\\platter\\loot_tables\\blocks\\'}

for x,y in files.items():
    for z in variants:
        fileIn = open(x, 'r')
        if not os.path.exists(y):
            os.makedirs(y)
        fileOut = open(y + z +'_platter_block.json', 'w')
        readString = fileIn.readline()
        while readString:
            readString = readString.replace("{name}", z)
            fileOut.write(readString)
            readString = fileIn.readline()
        fileIn.close
        fileOut.close
y= 'assets\\platter\\lang\\'
if not os.path.exists(y):
    os.makedirs(y)
fileOut = open(y + 'en_us.json', 'w')
fileOut.write('{\n')
for x in variants:
    toWrite = '\t"block.platter.{name}_platter_block": "{name} Platter"'.replace('{name}', x, 1)
    toWrite = toWrite.replace('{name}', x.replace('_', ' ').title())
    if x != 'dark_oak':
        toWrite += ','
    fileOut.write(toWrite + '\n')
fileOut.write('}\n')