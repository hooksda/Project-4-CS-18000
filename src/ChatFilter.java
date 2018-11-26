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
        ArrayList<String> aster = new ArrayList<>();
        for (int j = 0; j < badWords.size(); j++) {
            String filter = "";
            for (int i = 0; i < badWords.get(j).length(); i++) {

                filter += "*";
            }
            aster.add(filter);
        }
        for (int i = 0; i < badWords.size(); i++) {
            msg = msg.replaceAll("(?i)" + badWords.get(i), aster.get(i));
        }
        return msg;
    }
}
