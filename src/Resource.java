public class Resource {
    int brick;
    int wood;
    int sheep;
    int wheat;
    int ore;

    Resource() {
        this( 0, 0, 0, 0, 0);
    }

    Resource( int brick, int wood, int sheep, int wheat, int ore ) {
        this.brick = brick;
        this.wood = wood;
        this.sheep = sheep;
        this.wheat = wheat;
        this.ore = ore;
    }

    Resource add( Resource rsc ) {
        brick += rsc.brick;
        wood += rsc.wood;
        sheep += rsc.sheep;
        wheat += rsc.wheat;
        ore += rsc.ore;
        return this;
    }

    Resource substract( Resource rsc ) {
        brick -= rsc.brick;
        wood -= rsc.wood;
        sheep -= rsc.sheep;
        wheat -= rsc.wheat;
        ore -= rsc.ore;
        return this;
    }

    boolean biggerEquals( Resource rsc ) {
        return brick >= rsc.brick &&
        wood >= rsc.wood &&
        sheep >= rsc.sheep &&
        wheat >= rsc.wheat &&
        ore >= rsc.ore;
    }

    int totalCount() {
        return brick + wood + sheep + wheat + ore;
    }
}
