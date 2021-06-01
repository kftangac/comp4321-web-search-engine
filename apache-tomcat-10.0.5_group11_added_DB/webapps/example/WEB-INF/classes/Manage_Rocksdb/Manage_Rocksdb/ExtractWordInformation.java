/* --
COMP4321 Lab1 Exercise
Student Name:
Student ID:
Section:
Email:
*/
package Manage_Rocksdb.Manage_Rocksdb;
import org.jsoup.nodes.Document;
import java.util.*;



public class ExtractWordInformation
{

    private static int wordID = 1;
    private static LinkedHashMap<Integer, String> wordIdMappingTable = new LinkedHashMap<>();

    public static LinkedHashMap<Integer, String> getWordIdMappingTable() {
        return wordIdMappingTable;
    }

    public static void UpdateWordMappingTable(LinkedHashMap<String, Integer> stopStemResult) {
        for (Map.Entry<String, Integer> stopStemResultEntry : stopStemResult.entrySet()) {
            String current = stopStemResultEntry.getKey();
            boolean exists = false;
            for (Map.Entry<Integer, String> MappingTableEntry : wordIdMappingTable.entrySet()) {
                if (MappingTableEntry.getValue().equals(current)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                wordIdMappingTable.put(wordID++, current);
            }
        }
    }

    public static LinkedHashMap<Integer, String> extractTitleWords(Document doc) {
        LinkedHashMap<Integer, String> TitleResultWithPos = new LinkedHashMap<>();

        String title = doc.title();
        int pos = 0;
        StringTokenizer stTitle = new StringTokenizer(title);
        while (stTitle.hasMoreTokens()) {
            String currentWord = stTitle.nextToken();
            pos++;
            TitleResultWithPos.put(pos, currentWord);
        }

        // preprocess StopStem
        LinkedHashMap<Integer, String> stopStemResult = new LinkedHashMap<>();
        StopStem stopStem = new StopStem("stopwords-en.txt");
        for (Map.Entry<Integer, String> entry : TitleResultWithPos.entrySet()) {
            String word = entry.getValue();
            if (word.matches("\\A\\p{ASCII}*\\z") && !stopStem.isStopWord(word) && !stopStem.stem(word).equals("")) {
                stopStemResult.put(entry.getKey(), stopStem.stem(word));
            }
        }
        return stopStemResult;
    }

    public static LinkedHashMap<Integer, String> extractContentWords(Document doc) {
        LinkedHashMap<Integer, String> ContentResultWithPos = new LinkedHashMap<>();

        String contents = doc.body().text();
        int pos = 0;
        StringTokenizer stContent = new StringTokenizer(contents);
        while (stContent.hasMoreTokens()) {
            String currentWord = stContent.nextToken();
            pos++;
            ContentResultWithPos.put(pos, currentWord);
        }

        // preprocess StopStem
        LinkedHashMap<Integer, String> stopStemResult = new LinkedHashMap<>();
        StopStem stopStem = new StopStem("stopwords-en.txt");
        for (Map.Entry<Integer, String> entry : ContentResultWithPos.entrySet()) {
            String word = entry.getValue();
            if (word.matches("\\A\\p{ASCII}*\\z") && !stopStem.isStopWord(word) && !stopStem.stem(word).equals("")) {
                stopStemResult.put(entry.getKey(), stopStem.stem(word));
            }
        }
        return stopStemResult;
    }

    public static LinkedHashMap<Integer, String> extractTitleWordsIdAndPos(Document doc) {

        LinkedHashMap<Integer, String> stopStemResult = extractTitleWords(doc);

        // wordid, many pos
        LinkedHashMap<Integer, ArrayList<Integer>> titleWordsIdAndPos = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> stemEntry : stopStemResult.entrySet()) {
            String current = stemEntry.getValue();
            for (Map.Entry<Integer, String> MappingTableEntry : wordIdMappingTable.entrySet()) {
                if (MappingTableEntry.getValue().equals(current)) {
                    if (titleWordsIdAndPos.containsKey(MappingTableEntry.getKey())) {
                        titleWordsIdAndPos.get(MappingTableEntry.getKey()).add(stemEntry.getKey());
                    } else {
                        ArrayList<Integer> list = new ArrayList<>();
                        list.add(stemEntry.getKey());
                        titleWordsIdAndPos.put(MappingTableEntry.getKey(), list);
                    }
                }
            }
        }

        LinkedHashMap<Integer, String> titleWordsIdAndPosString = new LinkedHashMap<>();
        for (Map.Entry<Integer, ArrayList<Integer>> MappingTableEntry : titleWordsIdAndPos.entrySet()) {
            String positionString = "";
            for (Integer c : MappingTableEntry.getValue()) {
                positionString += c;
                positionString += "-";
            }
            positionString = positionString.substring(0, positionString.length() - 1);
            titleWordsIdAndPosString.put(MappingTableEntry.getKey(), positionString);
        }

        return titleWordsIdAndPosString;
    }

    public static LinkedHashMap<Integer, String> extractContentWordsIdAndPos(Document doc) {

        LinkedHashMap<Integer, String> stopStemResult = extractContentWords(doc);

        // wordid, many pos
        LinkedHashMap<Integer, ArrayList<Integer>> ContentWordsIdAndPos = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> stemEntry : stopStemResult.entrySet()) {
            String current = stemEntry.getValue();
            for (Map.Entry<Integer, String> MappingTableEntry : wordIdMappingTable.entrySet()) {
                if (MappingTableEntry.getValue().equals(current)) {
                    if (ContentWordsIdAndPos.containsKey(MappingTableEntry.getKey())) {
                        ContentWordsIdAndPos.get(MappingTableEntry.getKey()).add(stemEntry.getKey());
                    } else {
                        ArrayList<Integer> list = new ArrayList<>();
                        list.add(stemEntry.getKey());
                        ContentWordsIdAndPos.put(MappingTableEntry.getKey(), list);
                    }
                }
            }
        }

        LinkedHashMap<Integer, String> contentWordsIdAndPosString = new LinkedHashMap<>();
        for (Map.Entry<Integer, ArrayList<Integer>> MappingTableEntry : ContentWordsIdAndPos.entrySet()) {
            String positionString = "";
            for (Integer c : MappingTableEntry.getValue()) {
                positionString += c;
                positionString += "-";
            }
            positionString = positionString.substring(0, positionString.length() - 1);
            contentWordsIdAndPosString.put(MappingTableEntry.getKey(), positionString);
        }

        return contentWordsIdAndPosString;
    }

    public static LinkedHashMap<String, Integer> extractWords(Document doc) {
        LinkedHashMap<Integer, String> stemTitleWords = extractTitleWords(doc);
        LinkedHashMap<Integer, String> stemContentWords = extractContentWords(doc);

        // count keywords
        LinkedHashMap<String, Integer> keywordsCount = new LinkedHashMap<>();
        for (Map.Entry<Integer, String> entry : stemTitleWords.entrySet()) {
            keywordsCount.compute(entry.getValue(), (k, v) -> v == null ? 1 : v + 1);
        }
        for (Map.Entry<Integer, String> entry : stemContentWords.entrySet()) {
            keywordsCount.compute(entry.getValue(), (k, v) -> v == null ? 1 : v + 1);
        }
        return keywordsCount;
    }
}
