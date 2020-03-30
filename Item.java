/**
 * creates new item after attack
 * @author Ishan Chadha
 * @version 1.0.0
 **/
public class Item {
    private String name;
    private String ability;

    /**
     * @param name name
     * @param ability ability
     */
    public Item(String name, String ability) {
        this.name = name;
        this.ability = ability;
    }

    /**
     * @return name
     */
    public String getName() {
        return this.name;
    }

    /**
     * @return ability
     */
    public String getAbility() {
        return this.ability;
    }

    /**
     * @param name name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param ability ability
     */
    public void setAbility(String ability) {
        this.ability = ability;
    }

    /**
     * @param p pokemon
     */
    public void useAbility(Pokemon p) {
        if (this.ability.equals("hp") && p.getCurrentHP() <= p.getMaxHP() - 10) {
            p.setCurrentHP(p.getCurrentHP() + 10);
        } else if (this.ability.equals("hp")) {
            p.setCurrentHP(p.getMaxHP());
        } else if (this.ability.equals("level")) {
            p.setLevel(p.getLevel() + 3);
        } else if (this.ability.equals("reflect")) {
            PokeBattle.reflect();
        }
    }
}