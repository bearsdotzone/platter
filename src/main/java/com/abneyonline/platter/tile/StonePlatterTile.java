package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;

public class StonePlatterTile extends PlatterTile {
    public StonePlatterTile()
    {
        super (Registration.stone_platter_tile.get());
        tickForAnimals = false;
    }
}
