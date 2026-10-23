public class EmitterDetails {
    public Character val;
    public Pair position;
    public boolean on;
    public Character clockwise;
    public Character counterclockwise;

    public EmitterDetails(Character val, Pair position, boolean on, Character clockwise, Character counterclockwise) {
        this.val = val;
        this.position = position;
        this.on = on;
        this.clockwise = clockwise;
        this.counterclockwise = counterclockwise;
    }
}
