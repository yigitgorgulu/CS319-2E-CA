package game;

import network.requests.ResourceInfo;

import java.io.Serializable;
import java.util.Random;

/*
 Resource class consists of all 5 resource types in the game. It has the counts of the resources as integers.
 */

public class Resource implements Serializable {
    int resourcesTypes = 5;
    private int brick;
    private int wood;
    private int sheep;
    private int wheat;
    private int ore;

    public Resource(int brick, int wood, int sheep, int wheat, int ore) {
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

    public Resource add(Resource rsc) { // increase the resources by adding the given resource
        brick += rsc.brick;
        wood += rsc.wood;
        sheep += rsc.sheep;
        wheat += rsc.wheat;
        ore += rsc.ore;
        return this;
    }

    public Resource multiply(Resource rsc) { // increase the resources by multiplying them with the given resource
        brick *= rsc.brick;
        wood *= rsc.wood;
        sheep *= rsc.sheep;
        wheat *= rsc.wheat;
        ore *= rsc.ore;
        return this;
    }

    public Resource decrease(Resource rsc) { // decrease the resources that is given
        brick -= rsc.brick;
        wood -= rsc.wood;
        sheep -= rsc.sheep;
        wheat -= rsc.wheat;
        ore -= rsc.ore;
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

    public Resource substract(Resource rsc) {
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

    public boolean biggerEquals(Resource rsc) {
        return brick >= rsc.brick &&
                wood >= rsc.wood &&
                sheep >= rsc.sheep &&
                wheat >= rsc.wheat &&
                ore >= rsc.ore;
    }

    public int totalCount() {
        return brick + wood + sheep + wheat + ore;
    }

    public void removeRandom(int removeNo) { // removes given # of resources randomly
        for (int i = 0; i < removeNo; i++) {
            int which = new Random().nextInt(5);

            if (which == 0 && getBrick() > 0) {
                brick--;
            } else if (which == 1 && getWood() > 0) {
                wood--;
            } else if (which == 2 && getSheep() > 0) {
                sheep--;
            } else if (which == 3 && getWheat() > 0) {
                wheat--;
            } else if (which == 4 && getOre() > 0) {
                ore--;
            }
        }
    }

    boolean isZero() {
        return this.totalCount() == 0;
    }

    int monopolyDecrease(int type) {
        int returnNo = 0;
        switch (type) {
            case 0:
                if (brick > 0) {
                    returnNo = brick;
                    brick = 0;
                }
                break;
            case 1:
                if (wood > 0) {
                    returnNo = wood;
                    wood = 0;
                }
                break;
            case 2:
                if (sheep > 0) {
                    returnNo = sheep;
                    sheep = 0;
                }
                break;
            case 3:
                if (wheat > 0) {
                    returnNo = wheat;
                    wheat = 0;
                }
                break;
            case 4:
                if (ore > 0) {
                    returnNo = ore;
                    ore = 0;
                }
                break;
            default:
        }
        return returnNo;
    }

    void monopolyIncrease(int type, int count) {
        switch (type) {
            case 0:
                brick = brick + count;
                break;
            case 1:
                wood = wood + count;
                break;
            case 2:
                sheep = sheep + count;
                break;
            case 3:
                wheat = wheat + count;
                break;
            case 4:
                ore = ore + count;
                break;
            default:
        }
    }
}