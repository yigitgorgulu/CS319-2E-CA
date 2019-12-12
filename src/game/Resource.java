package game;

import display.networkDisplay.requests.ResourceInfo;

import java.io.Serializable;

public class Resource implements Serializable {
    int resourcesTypes = 5;
    private int brick;
    private int wood;
    private int sheep;
    private int wheat;
    private int ore;

    public Resource( int brick, int wood, int sheep, int wheat, int ore ) {
        this.brick = brick;

        this.wood = wood;

        this.sheep = sheep;

        this.wheat = wheat;

        this.ore = ore;
    }

    public Resource(ResourceInfo resourceInfo) {
        this.brick = resourceInfo.getBrick();
        this.wood = resourceInfo.getWood();
        this.sheep = resourceInfo.getSheep();
        this.wheat = resourceInfo.getWheat();
        this.ore = resourceInfo.getOre();
    }

    public Resource add( Resource rsc ) {
        brick += rsc.brick;
        wood += rsc.wood;
        sheep += rsc.sheep;
        wheat += rsc.wheat;
        ore += rsc.ore;
        return this;
    }

    public Resource multiply( Resource rsc ){
        brick *= rsc.brick;
        wood *= rsc.wood;
        sheep *= rsc.sheep;
        wheat *= rsc.wheat;
        ore *= rsc.ore;
        return this;
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

    public Resource substract( Resource rsc ) {
        brick -= rsc.brick;
        wood -= rsc.wood;
        sheep -= rsc.sheep;
        wheat -= rsc.wheat;
        ore -= rsc.ore;
        return this;
    }

    @Override
    public String toString() {
        return "Resource{" +
                ", brick=" + brick +
                ", wood=" + wood +
                ", sheep=" + sheep +
                ", wheat=" + wheat +
                ", ore=" + ore +
                '}';
    }

    public boolean biggerEquals(Resource rsc ) {
        return brick >= rsc.brick &&
        wood >= rsc.wood &&
        sheep >= rsc.sheep &&
        wheat >= rsc.wheat &&
        ore >= rsc.ore;
    }

    public int totalCount() {
        return brick + wood + sheep + wheat + ore;
    }

    boolean isZero() { return this.totalCount() == 0; }
}
