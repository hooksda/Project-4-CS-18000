import java.io.*;
import java.util.ArrayList;

public class ChatFilter {
    ArrayList<String> badWords = new ArrayList<>();

    public ChatFilter(String badWordsFileName) {
        try {
            File file = new File(badWordsFileName);
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            for (String line; (line = br.readLine()) != null; ) {
                badWords.add(line);
            }
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public String filter(String msg) {
        String filteredMessage = msg;
        String cases;
        String filter = "";
        for (int i = 0; i < badWords.size(); i++) {
            filter += "*";
        }
        for (int i = 0; i < badWords.size(); i++) {
            cases = badWords.get(i);
            if (filteredMessage.charAt(i) == cases.charAt(i)) {
                filteredMessage = cases.replaceAll("(?i)" + cases, filter);
            } else {

            }
        }
        return filteredMessage;
    }
}
