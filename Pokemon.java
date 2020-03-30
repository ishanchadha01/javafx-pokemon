/**
 * creates new Pokemon for game
 * @author Ishan Chadha
 * @version 1.0.0
 **/
public class Pokemon {
    private String name;
    private int level;
    private double maxHP;
    private double currentHP;
    private double atk;
    private Move[] moves;
    private String type;
    private boolean fainted;

    /**
    * @param n name
    * @param l level
    * @param h max HP
    * @param a atk
    * @param t type
    * @param move multiple moves
    **/
    public Pokemon(String n, int l, double h, double a, String t, Move... move) {
        name = n;
        level = l;
        maxHP = h;
        atk = a;
        type = t;
        currentHP = maxHP;
        moves = move;
    }

    /**
     * @return name
     */
    public String getName() {
        return name;
    }

    /**
     * @return level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @return atk damage
     */
    public double getAtk() {
        return atk;
    }

    /**
     * @return max HP
     */
    public double getMaxHP() {
        return maxHP;
    }

    /**
     * @return current HP
     */
    public double getCurrentHP() {
        return currentHP;
    }

    /**
     * @return array of moves
     */
    public Move[] getMoves() {
        return moves;
    }

    /**
     * @param currentHP current HP
     */
    public void setCurrentHP(double currentHP) {
        this.currentHP = currentHP;
        if (this.currentHP <= 0) {
            fainted = true;
        }
    }

    /**
     * @param move move
     * @return damage caused by move
     */
    public double compareType(Move move) {
        if (move.getType().equals(type)) {
            return 0.5;
        } else if (move.getType().equals("WATER")) {
            if (type.equals("FIRE")) {
                return 2.0;
            } else if (type.equals("GRASS")) {
                return 0.5;
            }
        } else if (move.getType().equals("GRASS")) {
            if (type.equals("WATER")) {
                return 2.0;
            } else if (type.equals("FIRE")) {
                return 0.5;
            } else if (type.equals("FLYING")) {
                return 0.5;
            }
        } else if (move.getType().equals("FIRE")) {
            if (type.equals("GRASS")) {
                return 2.0;
            } else if (type.equals("WATER")) {
                return 0.5;
            }
        } else if (move.getType().equals("FLYING")) {
            if (type.equals("GRASS")) {
                return 2.0;
            }
        }
        return 1;
    }

    /**
     * @return fainted or not
     */
    public boolean isFainted() {
        return fainted;
    }

    /**
     * @param fainted fainted
     */
    public void setFainted(boolean fainted) {
        this.fainted = fainted;
    }

    /**
     * @param level level
     */
    public void setLevel(int level) {
        this.level = level;
    }
}
