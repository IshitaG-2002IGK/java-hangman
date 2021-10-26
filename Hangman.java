package hangman;

import java.util.function.Supplier;
import java.util.stream.Collectors;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;

public class Hangman extends Application {
    
    private StringBinding wordBinding;
    private StringBinding statusBinding;

	@Override
	public void start(Stage primaryStage) {
	    
	    Supplier<String> wordGenerator = new WordGenerator();
	    
	    HangmanGameState game = new HangmanGameState(wordGenerator.get());
	    
		BorderPane root = new BorderPane();
		
		Label word = new Label();
		
		wordBinding = Bindings.createStringBinding(() -> {
		    if (game.isWordGuessed() || game.isLost()) {
		        return game.getWord();
		    }
		    return game.getWord()
		        .chars()
		        .map(c -> game.getGuesses().contains((char)c) ? (char)c : '?')
		        .mapToObj(c -> Character.toString((char)c))
		        .collect(Collectors.joining());
		}, game.wordProperty(), game.getGuesses(), 
		   game.wordGuessedProperty(), game.lostProperty());
		
		
        word.textProperty().bind(wordBinding);
		
        HBox livesRemaining = new HBox();
        for (int i = 0 ; i < HangmanGameState.LIVES ; i++) {
            Label lifeLost = new Label("X");
            StackPane lifeLostHolder = new StackPane(lifeLost);
            lifeLostHolder.getStyleClass().add("life-lost");
            livesRemaining.getChildren().add(lifeLostHolder);
            lifeLost.visibleProperty().bind(
                    game.wrongGuessesProperty().greaterThan(i));
        }
		
		Label statusLabel = new Label();
		statusBinding = Bindings
		        .when(game.lostProperty())
		        .then("You lost!")
		        .otherwise("You won!");
        statusLabel.textProperty().bind(statusBinding);
        statusLabel.getStyleClass().add("status");
		
		Button playAgain = new Button("Play again");
		playAgain.setOnAction(e -> game.restartGame(wordGenerator.get()));
		
		HBox controls = new HBox(5, statusLabel, playAgain);
		controls.getStyleClass().add("controls");
		controls.visibleProperty().bind(
		        game.wordGuessedProperty().or(game.lostProperty()));
		
		TilePane buttons = new TilePane();
		buttons.getStyleClass().add("buttons");
		
		for (char c = 'A'; c <= 'Z'; c++) {
		    Character ch = new Character(c);
		    Button button = new Button(ch.toString());
		    button.disableProperty().bind(Bindings.createBooleanBinding(
		            () -> game.getGuesses().contains(ch), 
		            game.getGuesses()));
		    button.visibleProperty().bind(button.disableProperty().not());
		    button.setOnAction(e -> game.getGuesses().add(ch));
		    button.getStyleClass().add("letter-button");
		    buttons.getChildren().add(button);
		}
		
		StackPane wordHolder = new StackPane(word);
		wordHolder.getStyleClass().add("word");
		
		root.setCenter(wordHolder);
		root.setRight(buttons);
		root.setTop(livesRemaining);
		root.setBottom(controls);
		
		Scene scene = new Scene(root);
		scene.getStylesheets().add(
		        getClass().getResource("hangman.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}