package gameobjects.mapobjects.districts;

import map.placing_utils.Sector;

public class NormalDistrict extends District {

    public NormalDistrict(Sector sector) {
        super(sector);
        this.candy_multiplikator = 2.5;
    }
}