package org.acme.app;

import io.quarkus.logging.Log;
import io.quarkus.runtime.QuarkusApplication;
import java.util.Scanner;

public class MyApp implements QuarkusApplication {

    public int run(String... args) throws Exception {
        Log.info("Inside run function");
        return 0;
    }

    public String title(String title, int p) {
        int position = Math.max(p, 2);
        if (p>5) position=5;
        return "#".repeat(position) + title;
    }
    public String title() {
        return title("carmageddo", 5);
    }

    public String animeTitle() {
        try (Scanner myObj = new Scanner(System.in)) {
            String title = myObj.nextLine();
            if (title == "") {
                return title();
            }
            else {
                return title(title, 3);
            }
        }
    }
}