package it.polimi.ingsw.view;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

public class ReadInput implements Callable<String> {
    private final BufferedReader reader;

    public ReadInput() {
        reader = new BufferedReader(new InputStreamReader(System.in));
    }

    public String call() throws  InterruptedException, IOException {
        String input;
        // wait until there is data to complete a readLine()
        while (!reader.ready()) {
            Thread.sleep(200);
        }
        input = reader.readLine();
        return input;
    }
}
