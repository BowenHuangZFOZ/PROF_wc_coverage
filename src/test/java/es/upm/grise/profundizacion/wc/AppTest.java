package es.upm.grise.profundizacion.wc;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import java.io.PrintStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

import static org.mockito.Mockito.mockConstruction;
import static org.mockito.Mockito.when;
import org.mockito.MockedConstruction;
import java.io.BufferedReader;

public class AppTest {

    private static Path testFile = Paths.get("ejemplo.txt");

    @BeforeAll
    public static void setup() throws IOException {
        Files.writeString(testFile, "kjdbvws wonvwofjw\n sdnfwijf ooj    kjndfohwouer 21374 vehf\n jgfosj\n\nskfjwoief ewjf\n\n\ndkfgwoihgpw vs wepfjwfin");
    }
    
    @AfterAll
    public static void teardown() {
        try {
            Files.deleteIfExists(testFile);
        } catch (IOException e) {
            System.err.println("Error deleting test file: " + e.getMessage());
            try {
                Thread.sleep(100);
                Files.deleteIfExists(testFile);
            } catch (IOException | InterruptedException ex) {
                System.err.println("Failed to delete test file on retry: " + ex.getMessage());
            }
        }
    }


    @Test
    public void testUsageMessageWhenNoArgs() {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        System.setOut(new PrintStream(output));
        App.main(new String[] {});
        
        assertEquals("Usage: wc [-clw file]\n".trim(), output.toString().trim());
    }
    
    @Test
    public void testUsageMessageWhenThreeArgs() {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(output));
    	App.main(new String[] {"-c","ejemplo_1.txt","ejemplo_2.txt"});
    	
    	assertEquals("Wrong arguments!\n".trim(), output.toString().trim());
    }
    
    @Test
    public void testFileNotFound() {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(output));
    	App.main(new String[] {"-c","fake-file.txt"});
    	
    	assertEquals("Cannot find file: fake-file.txt\n".trim(), output.toString().trim());
    }

    @Test
    public void testCommandNoDash() {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(output));
    	App.main(new String[] {"c","ejemplo.txt"});
    	
    	assertEquals("The commands do not start with -\n".trim(), output.toString().trim());
    }

    @Test
    public void testUnrecognizedCommand() {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(output));
    	App.main(new String[] {"-y","ejemplo.txt"});
    	
    	assertEquals("Unrecognized command: y\n".trim(), output.toString().trim());
    }
    
    @Test
    public void testCorrectPath() {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(output));
    	App.main(new String[] {"-clw","ejemplo.txt"});
    	
    	assertEquals("109\t7\t20\tejemplo.txt\n".trim(), output.toString().trim());
    }
    
    @Test
    public void testUsageMessageWhenReadError() {
    	ByteArrayOutputStream output = new ByteArrayOutputStream();
    	System.setOut(new PrintStream(output));
    	String file = "ejemplo.txt";
    	
    	try(MockedConstruction<BufferedReader> mocked = mockConstruction(BufferedReader.class,
    			(mock, context) ->{
    				when(mock.read()).thenThrow(new IOException("Read Error Test"));
    			})){
    		App.main(new String[] {"-c", file});
    	}
    	
    	assertEquals("Error reading file: " + file + "\n".trim(), output.toString().trim());
    }
    
}
