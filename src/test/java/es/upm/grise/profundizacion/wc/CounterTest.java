package es.upm.grise.profundizacion.wc;


import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

import java.io.IOException;

public class CounterTest {

    @Test
    public void testCountCharactersWordsAndLines() throws IOException {
    	//Add \t before the first word.
        String content = "\tEsta frase\nes un ejemplo para\nel test de recuento.\n";
        BufferedReader reader = new BufferedReader(new StringReader(content));
        
        Counter counter = new Counter(reader);
        //Change the result for new sentence
        assertEquals(52, counter.getNumberCharacters());
        assertEquals(3, counter.getNumberLines());
        assertEquals(11, counter.getNumberWords());
    }
   

}

