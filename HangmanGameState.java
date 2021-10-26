package hangman;

import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyBooleanProperty;
import javafx.beans.property.ReadOnlyBooleanWrapper;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.property.ReadOnlyIntegerWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class HangmanGameState {

    private final StringProperty word ;
    
    public final static int LIVES = 6 ;

    public final StringProperty wordProperty() {
        return this.word;
    }

    public final String getWord() {
        return this.wordProperty().get();
    }

    public final void setWord(final String word) {
        this.wordProperty().set(word);
    }
    
    private final ObservableList<Character> guesses = 
            FXCollections.observableArrayList();
    
    public ObservableList<Character> getGuesses() {
        return guesses ;
    }
    
    private final ReadOnlyIntegerWrapper wrongGuesses = 
            new ReadOnlyIntegerWrapper();

    public final ReadOnlyIntegerProperty wrongGuessesProperty() {
        return this.wrongGuesses.getReadOnlyProperty();
    }

    public final int getWrongGuesses() {
        return this.wrongGuessesProperty().get();
    }
    
    private final ReadOnlyBooleanWrapper wordGuessed = 
            new ReadOnlyBooleanWrapper() ;
    
    public final ReadOnlyBooleanProperty wordGuessedProperty() {
        return this.wordGuessed.getReadOnlyProperty();
    }

    public final boolean isWordGuessed() {
        return this.wordGuessedProperty().get();
    }    
    
    private final ReadOnlyBooleanWrapper lost = new ReadOnlyBooleanWrapper() ;
    
    public ReadOnlyBooleanProperty lostProperty() {
        return lost.getReadOnlyProperty() ;
    }
    
    public final boolean isLost() {
        return lostProperty().get();
    }

    public HangmanGameState(String initialWord) {
        
        word = new SimpleStringProperty(initialWord);
        
        wrongGuesses.bind(Bindings.createIntegerBinding(() -> {
            return (int) guesses.stream()
                    .map(c -> c.toString())
                    .filter(c -> ! word.get().contains(c))
                    .count();
        }, word, guesses));
        
        wordGuessed.bind(Bindings.createBooleanBinding(() -> {
            return word.get()
                    .chars()
                    .mapToObj((int c) -> Character.valueOf((char)c))
                    .allMatch(guesses::contains);
                    
        }, word, guesses));
        
        lost.bind(wrongGuesses.greaterThanOrEqualTo(LIVES));
        
    }
    
    public void restartGame(String newWord) {
        guesses.clear();
        word.set(newWord);
    }

    
}