package com.abneyonline.platter.tile;

import com.abneyonline.platter.Registration;

public class GoldPlatterTile extends PlatterTile {
    public GoldPlatterTile()
    {
        super (Registration.gold_platter_tile.get());
        tickForAnimals = false;
    }
}
