package display.networkDisplay.requests;

import game.Resource;

import java.io.Serializable;

public class ResourceInfo implements Serializable {
    private int brick;
    private int wood;
    private int sheep;
    private int wheat;
    private int ore;

    public ResourceInfo(Resource resource) {
        this.brick = resource.getBrick();
        this.wood = resource.getWood();
        this.sheep = resource.getSheep();
        this.wheat = resource.getWheat();
        this.ore = resource.getOre();
    }

    public int getBrick() {
        return brick;
    }

    public int getWood() {
        return wood;
    }

    public int getSheep() {
        return sheep;
    }

    public int getWheat() {
        return wheat;
    }

    public int getOre() {
        return ore;
    }
}
