package Game;

public class Resource {
    int brick;
    int wood;
    int sheep;
    int wheat;
    int ore;

    public Resource() {
        this( 0, 0, 0, 0, 0);
    }

    public Resource( int brick, int wood, int sheep, int wheat, int ore ) {
        this.brick = brick;
        this.wood = wood;
        this.sheep = sheep;
        this.wheat = wheat;
        this.ore = ore;
    }

    public Resource add( Resource rsc ) {
        brick += rsc.brick;
        wood += rsc.wood;
        sheep += rsc.sheep;
        wheat += rsc.wheat;
        ore += rsc.ore;
        return this;
    }

    public Resource substract( Resource rsc ) {
        brick -= rsc.brick;
        wood -= rsc.wood;
        sheep -= rsc.sheep;
        wheat -= rsc.wheat;
        ore -= rsc.ore;
        return this;
    }

    public boolean biggerEquals( Resource rsc ) {
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