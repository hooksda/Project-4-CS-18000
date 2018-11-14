package src;

import java.io.*;
import java.util.ArrayList;

public class ChatFilter {

    public ChatFilter(String badWordsFileName) {
        try {
            File file = new File(badWordsFileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            ArrayList<String> badwords = new ArrayList<>();
            for (String line; (line = br.readLine()) != null;) {
                String[] bad = line.split(" ");
                for (int i = 0; i < bad.length; i++) {
                    badwords.add(bad[i]);

                }
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public String filter(String msg) {
        return msg;
    }
}
