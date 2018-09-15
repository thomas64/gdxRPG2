package nl.t64.game.rpg.constants;

public enum MapTitle {

    MAP1("map1.tmx"),
    MAP2("map2.tmx"),
    MAP3("map3.tmx");

    private final String mapPath;

    MapTitle(String mapPath) {
        this.mapPath = "maps/" + mapPath;
    }

    public String getMapPath() {
        return mapPath;
    }

}
