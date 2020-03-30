import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import javafx.scene.control.ProgressBar;
import javafx.animation.PauseTransition;
import javafx.util.Duration;

/**
 * creates new game
 * @author Ishan Chadha
 * @version 1.0.0
 **/
public class PokeBattle extends Application {
    /**
     * @param args command args
     */
    public static void main(String[] args) {
        launch(args);
    }

    /**
     * @param stage window for game
     */
    public void start(Stage stage) throws FileNotFoundException {
        // initialize Pokemon and bag
        ArrayList<Pokemon> myList = PokeBattle.initMyPokemon();
        ArrayList<Pokemon> oppList = PokeBattle.initOppPokemon();
        Pokemon current = myList.get(0);
        Pokemon opponent = oppList.get(0);
        ArrayList<Item> bag = new ArrayList<>();
        Pane leftPane = new Pane();
        StackPane ivStack1 = new StackPane();
        ivStack1.setLayoutY(200);
        HBox oppBox = new HBox();
        oppBox.getChildren().add(new Label(opponent.getName()));
        VBox oppStats = new VBox();
        oppStats.getChildren().add(new Label(" Lv" + opponent.getLevel()));
        HBox oppHP = new HBox();
        oppHP.getChildren().add(new Label("HP: "));
        ProgressBar oppProgress = new ProgressBar();
        oppProgress.setProgress(1);
        oppHP.getChildren().add(oppProgress);
        oppStats.getChildren().add(oppHP);
        oppBox.getChildren().add(oppStats);
        ImageView[] ivArray1 = setupMyImages();
        for (int i = 0; i < 3; i++) {
            ivStack1.getChildren().add(ivArray1[i]);
        }
        Label prompt = new Label("What should " + current.getName() + " do?");
        prompt.setTranslateY(400);
        leftPane.getChildren().addAll(oppBox, ivStack1, prompt);
        Pane rightPane = new Pane();
        StackPane ivStack2 = new StackPane();
        ImageView[] ivArray2 = setupOppImages();
        for (int i = 0; i < 3; i++) {
            ivStack2.getChildren().add(ivArray2[i]);
        }
        GridPane grid = createGrid();
        VBox rightBox = new VBox();
        HBox myBox = new HBox();
        myBox.getChildren().add(new Label(current.getName()));
        VBox myStats = new VBox();
        myStats.getChildren().add(new Label(" Lv" + current.getLevel()));
        HBox myHP = new HBox();
        myHP.getChildren().add(new Label("HP: "));
        ProgressBar myProgress = new ProgressBar();
        myProgress.setProgress(1);
        myHP.getChildren().add(myProgress);
        myStats.getChildren().add(myHP);
        Label hpIndicator = new Label(current.getCurrentHP() + "/" + current.getMaxHP());
        myStats.getChildren().add(hpIndicator);
        myBox.getChildren().add(myStats);
        StackPane gridStack = new StackPane();
        rightBox.getChildren().addAll(ivStack2, myBox, gridStack);
        rightPane.getChildren().add(rightBox);
        GridPane grid1 = PokeBattle.setButtons(current.getMoves()[0].getName(), current.getMoves()[1].getName(),
            current.getMoves()[2].getName(), current.getMoves()[3].getName());
        VBox options = new VBox();
        ArrayList<String> pokeButtons = initPokeButtons(myList);
        GridPane grid2 = PokeBattle.setButtons(pokeButtons.get(0), pokeButtons.get(1),
            pokeButtons.get(2), pokeButtons.get(3));
        gridStack.getChildren().addAll(grid2, grid1, grid);
        ((Button) grid.getChildren().get(0)).setOnAction((event) -> {
                grid1.toFront();
                for (int i = 0; i < 4; i++) {
                    final int x = i;
                    final int oppMove = (int) Math.random() * 4;
                    ((Button) grid1.getChildren().get(i)).setOnAction(e -> {
                            double myFight = fight(myList.get(0), oppList.get(0), myList.get(0).getMoves()[x]);
                            updateHP(oppList.get(0), oppList, myFight);
                            prompt.setText(myList.get(0).getName() + " used " + myList.get(0).getMoves()[x].getName());
                            PauseTransition pause = new PauseTransition(Duration.seconds(1));
                            pause.setOnFinished(e2 -> {
                                    prompt.setText(oppList.get(0).getName() + " used "
                                        + oppList.get(0).getMoves()[oppMove].getName());
                                });
                            pause.play();
                            double fightBack = fight(oppList.get(0), myList.get(0), oppList.get(0).getMoves()[oppMove]);
                            updateHP(myList.get(0), myList, fightBack);
                            if (myList.get(0).getCurrentHP() == 0) {
                                myList.get(0).setFainted(true);
                            } else if (oppList.get(0).getCurrentHP() == 0) {
                                oppList.get(0).setFainted(true);
                            }
                            if (myList.get(0).isFainted()) {
                                if (myList.size() <= 1) {
                                    System.exit(0);
                                } else {
                                    switchChar(ivStack1, myList);
                                    prompt.setText("What should " + myList.get(0).getName() + " do?");
                                }
                            } else if (oppList.get(0).isFainted()) {
                                if (oppList.size() <= 1) {
                                    System.exit(0);
                                } else {
                                    switchChar(ivStack2, oppList);
                                }
                            }
                            oppProgress.setProgress(oppList.get(0).getCurrentHP() / oppList.get(0).getMaxHP());
                            myProgress.setProgress(myList.get(0).getCurrentHP() / myList.get(0).getMaxHP());
                            hpIndicator.setText(String.format("%.2f",
                                myList.get(0).getCurrentHP()) + "/" + myList.get(0).getMaxHP());
                            grid.toFront();
                        });
                }
            });
        ((Button) grid.getChildren().get(1)).setOnAction((event) -> {
                grid2.toFront();
                ((Button) grid2.getChildren().get(3)).setOnAction(e -> grid.toFront());
            });
        ((Button) grid.getChildren().get(2)).setOnAction((event) -> {
                try {
                    VBox items = new VBox();
                    for (int i = 0; i < myList.size(); i++) {
                        items.getChildren().add(new Button(bag.get(i).getName()));
                    }
                    for (int i = 0; i < 4; i++) {
                        grid.getChildren().remove(0);
                    }
                    rightBox.getChildren().add(items);
                } catch (IndexOutOfBoundsException ibe) {
                    rightBox.getChildren().add(new Label("Your bag is empty"));
                }
            });
        ((Button) grid.getChildren().get(3)).setOnAction((event) -> {
                System.exit(0);
            });
        HBox root = new HBox();
        root.getChildren().addAll(leftPane, rightPane);
        Scene scene = new Scene(root, 400, 500);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param name pokemon name
     * @return array of moves
     */
    public static Move[] accessMoves(String name) throws FileNotFoundException {
        File info = new File("info.txt");
        Scanner reader = new Scanner(info);
        boolean found = false;
        Move[] moveset = new Move[4];
        while (!found && reader.hasNextLine()) {
            if (reader.nextLine().equals(name)) {
                found = true;
                for (int i = 0; i < 4; i++) {
                    String[] stats = reader.nextLine().split(" ");
                    moveset[i] = new Move(stats[0], Integer.valueOf(stats[1]), stats[2]);
                }
            }
        }
        return moveset;
    }

    /**
     * reflect item action
     */
    public static void reflect() {
        return;
    }

    /**
     * @return list of user pokemon
     */
    public static ArrayList<Pokemon> initMyPokemon() throws FileNotFoundException {
        ArrayList<Pokemon> myList = new ArrayList<Pokemon>();
        Move[] snorlaxMoveset = PokeBattle.accessMoves("snorlax");
        Pokemon snorlax = new Pokemon("snorlax", 4, 25, 1.4, "GRASS", snorlaxMoveset);
        Move[] charizardMoveset = PokeBattle.accessMoves("charizard");
        Pokemon charizard = new Pokemon("charizard", 3, 25, 0.7, "FIRE", charizardMoveset);
        Move[] pikachuMoveset = PokeBattle.accessMoves("pikachu");
        Pokemon pikachu = new Pokemon("pikachu", 7, 25, 1.1, "WATER", pikachuMoveset);
        myList.add(snorlax);
        myList.add(charizard);
        myList.add(pikachu);
        return myList;
    }

    /**
     * @return list of opponent pokemon
     */
    public static ArrayList<Pokemon> initOppPokemon() throws FileNotFoundException {
        ArrayList<Pokemon> oppList = new ArrayList<Pokemon>();
        Move[] pidgeyMoveset = PokeBattle.accessMoves("pidgey");
        Pokemon pidgey = new Pokemon("pidgey", 3, 25, 0.6, "FLYING", pidgeyMoveset);
        Move[] bulbasaurMoveset = PokeBattle.accessMoves("bulbasaur");
        Pokemon bulbasaur = new Pokemon("bulbasaur", 1, 25, 0.9, "GRASS", bulbasaurMoveset);
        Move[] squirtleMoveset = PokeBattle.accessMoves("squirtle");
        Pokemon squirtle = new Pokemon("squirtle", 6, 25, 1.1, "normal", squirtleMoveset);
        oppList.add(pidgey);
        oppList.add(bulbasaur);
        oppList.add(squirtle);
        return oppList;
    }

    /**
     * @param topLeft top left button
     * @param topRight top right button
     * @param botLeft bottom left button
     * @param botRight bottom right button
     * @return grid with specified buttons
     */
    public static GridPane setButtons(String topLeft, String topRight, String botLeft, String botRight) {
        GridPane grid = new GridPane();
        Button one = new Button(topLeft);
        Button two = new Button(topRight);
        Button three = new Button(botLeft);
        Button four = new Button(botRight);
        one.setMinWidth(100);
        two.setMinWidth(100);
        three.setMinWidth(100);
        four.setMinWidth(100);
        grid.add(one, 0, 0);
        grid.add(two, 1, 0);
        grid.add(three, 0, 1);
        grid.add(four, 1, 1);
        return grid;
    }

    /**
     * @param current pokemon
     * @param opponent pokemon
     * @param move move from current
     * @return attack damage
     */
    public static double fight(Pokemon current, Pokemon opponent, Move move) {
        double value1 = opponent.compareType(move);
        double value2 = current.getAtk();
        double value3 = move.getPower();
        int value4 = current.getLevel();
        double action = value1 * value2 * value3 * (value4 / 5.0);
        return action;
    }

    /**
     * @return original grid of buttons
     */
    public static GridPane createGrid() {
        GridPane grid = PokeBattle.setButtons("FIGHT", "POKEMON", "BAG", "RUN");
        return grid;
    }

    /**
     * @param current pokemon
     * @param myList pokemon list
     * @param fight fight
     */
    public static void updateHP(Pokemon current, ArrayList<Pokemon> myList, double fight) {
        if (current.getCurrentHP() > fight) {
            current.setCurrentHP(current.getCurrentHP() - fight);
        } else {
            current.setCurrentHP(0);
        }
    }

    /**
     * @return array of image views
     */
    public static ImageView[] setupMyImages() {
        ImageView iv1 = new ImageView();
        iv1.setImage(new Image("Images/snorlax1.png"));
        iv1.setFitWidth(190);
        iv1.setPreserveRatio(true);
        iv1.setSmooth(true);
        ImageView iv2 = new ImageView();
        iv2.setImage(new Image("Images/charizard1.png"));
        iv2.setFitWidth(190);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setOpacity(0);
        ImageView iv3 = new ImageView();
        iv3.setImage(new Image("Images/pikachu1.png"));
        iv3.setFitWidth(190);
        iv3.setPreserveRatio(true);
        iv3.setSmooth(true);
        iv3.setOpacity(0);
        ImageView[] array = new ImageView[3];
        array[0] = iv3;
        array[1] = iv2;
        array[2] = iv1;
        return array;
    }

    /**
     * @return array of image views
     */
    public static ImageView[] setupOppImages() {
        ImageView iv1 = new ImageView();
        iv1.setImage(new Image("Images/pidgey1.png"));
        iv1.setFitWidth(190);
        iv1.setPreserveRatio(true);
        iv1.setSmooth(true);
        ImageView iv2 = new ImageView();
        iv2.setImage(new Image("Images/bulbasaur1.png"));
        iv2.setFitWidth(190);
        iv2.setPreserveRatio(true);
        iv2.setSmooth(true);
        iv2.setOpacity(0);
        ImageView iv3 = new ImageView();
        iv3.setImage(new Image("Images/squirtle1.png"));
        iv3.setFitWidth(190);
        iv3.setPreserveRatio(true);
        iv3.setSmooth(true);
        iv3.setOpacity(0);
        ImageView[] array = new ImageView[3];
        array[0] = iv3;
        array[1] = iv2;
        array[2] = iv1;
        return array;
    }

    /**
     * @param ivStack1 image view stack
     * @param myList pokemon list
     */
    public static void switchChar(StackPane ivStack1, ArrayList<Pokemon> myList) {
        ((ImageView) ivStack1.getChildren().get(2)).setOpacity(0);
        ((ImageView) ivStack1.getChildren().get(2)).toBack();
        ((ImageView) ivStack1.getChildren().get(2)).setOpacity(1);
        myList.remove(0);
        myList.get(0).setCurrentHP(myList.get(0).getMaxHP());
    }

    /**
     * @param myList pokemon list
     * @return list of pokemon buttons
     */
    public static ArrayList<String> initPokeButtons(ArrayList<Pokemon> myList) {
        ArrayList<String> pokeButtons = new ArrayList<>();
        for (int i = 0; i < myList.size(); i++) {
            pokeButtons.add(myList.get(i).getName());
        }
        pokeButtons.add("BACK");
        return pokeButtons;
    }
}