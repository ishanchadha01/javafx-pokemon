/**
 * creates new game
 * @author Ishan Chadha
 * @version 1.0.0
 **/
public class Move {
    private String name;
    private int power;
    private String type;

    /**
     * @param n name
     * @param p power
     * @param t type
     */
    public Move(String n, int p, String t) {
        name = n;
        power = p;
        type = t;

    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return power
     */
    public int getPower() {
        return power;
    }

    /**
     * @return type
     */
    public String getType() {
        return type;
    }
}
