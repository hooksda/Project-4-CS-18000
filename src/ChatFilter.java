import java.io.*;
import java.util.ArrayList;

public class ChatFilter {

    public ChatFilter(String badWordsFileName) {
        try {
            File file = new File(badWordsFileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(badWordsFileName);
            ArrayList<String> badwords = new ArrayList<>();
            for (String line; (line = br.readLine()) != null; ) {
                    badwords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public String filter(String msg) {
        String s = "";
        String x = "*";
        boolean loop = false;
        int lastWord = 0;
        String word = "";
        for (int j = 0; j < word.length(); j++) {
            s = s + x;
        }
        while (!loop) {
            int newWord = msg.indexOf(word, lastWord);
            if (newWord == -1) {
                loop = true;
            } else {
                lastWord = newWord + 1;
                if ((newWord == 0 || !(Character.isLetter(msg.charAt(newWord - 1)))) &&
                        (newWord + word.length() > msg.length() - 1 ||
                                !(Character.isLetter(msg.charAt(newWord + word.length()))))) {
                    String before = msg.substring(0, newWord);
                    String after = "";
                    if (newWord + word.length() > msg.length() - 1) {
                        after = "";
                    } else {
                        after = msg.substring(newWord + word.length());
                    }
                    msg = before + s + after;
                }
            }
        }
        return msg;
    }
}
