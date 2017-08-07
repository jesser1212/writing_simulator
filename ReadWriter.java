import java.io.*;
import java.util.Arrays;

/**
 * This class reads from text files and writes to them.
 * Created by Jesse on 7/25/2017.
 */
public class ReadWriter {
    public ReadWriter()
    {

    }

    /**
     * Splits a file's contents into sentences.
     * @param path - a string to the path of the file that we want split
     * @return an array of all the sentences found in the file given by the path
     */
    public String[] readAndSplit(String path)
    {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader textReader = new BufferedReader(fr);

            StringBuilder fullText = new StringBuilder();
            String line;
            while ((line = textReader.readLine()) != null) {
                fullText.append(line);
            }

            String fullTextString = fullText.toString();
            //String[] fullSentences = fullTextString.split("!?\\.");
            //String fullSentences = Arrays.toString(fullTextString)
            String[] fullSentences = fullTextString.split("((?<=\\.)|(?<=!))");
            return fullSentences;

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return null;

    }


    /**
     * Writes the given text to a file with the path. If a file already fits the name given in the path, we increment a digit on the end until it's a unique file.
     * This ensures new files (instead of replacing one story with another).
     * @param path
     * @param text
     * @return
     */
    public boolean writeToPath(String path, String text)
    {
        boolean success = false;
        int count = 0;
        File newFile = new File(path + Integer.toString(count) +".txt");

        while(newFile.exists())
        {
            count++;
            newFile = new File(path + Integer.toString(count) + ".txt");

        }
        try{
            PrintWriter writer = new PrintWriter(path + Integer.toString(count) + ".txt", "UTF-8");
            writer.print(text);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }



        return true;

    }

}
