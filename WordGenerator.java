package hangman;

import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.function.Supplier;

public class WordGenerator implements Supplier<String> {
    
    private final String allWords = "Accident agree arrive astronomy "
            + "atlas attention award aware Balance banner bare base "
            + "beach besides blast board bounce brain branch brave "
            + "bright Cage calf calm career center cheer chew claw "
            + "clear cliff club collect connect core corner couple crowd "
            + "curious Damp dangerous dash dawn deep demolish design "
            + "discard dive dome doubt dozen Earth enemy evening exactly "
            + "excess Factory fair famous feast field finally flap float "
            + "flood fold fresh frighten fuel Gap gaze gift gravity greedy "
            + "Harm herd Idea insect instrument invent island Leader leap "
            + "lizard local lonely luxury March mention motor Nervous net "
            + "nibble notice Ocean Pack pale parade past peak planet "
            + "present proof Reflect rumor Safe scholar seal search settle "
            + "share shelter shiver shy skill slight smooth soil stack "
            + "steady strand stream support Team telescope tiny tower "
            + "travel tremble Universe Village Warn weak wealthy whisper "
            + "wise wonder worry Yard Zigzag" ;
    
    private final List<String> words ;
    
    private Iterator<String> iterator ;
    
    public WordGenerator() {
        words = Arrays.asList(allWords.split(" ")) ;
        Collections.shuffle(words);
        iterator = words.iterator() ;
    }
        
    public String get() {
        if (! iterator.hasNext()) {
           Collections.shuffle(words);
           iterator = words.iterator() ;
        }
        return iterator.next().toUpperCase() ;
    }
}