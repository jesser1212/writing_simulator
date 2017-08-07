import org.json.JSONArray;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.TreeMap;

/**
 * This class parses the JSON file and gathers the program's priorities and the categories of other stories.
 * Created by Jesse on 7/25/2017.
 */
public class Parser {
    public Parser()
    {

    }
    private TreeMap<String, ArrayList<String>> longCategories = new TreeMap<>();
    private TreeMap<String, ArrayList<String>> shortCategories = new TreeMap<>();
    private TreeMap<String, ArrayList<String>> newCategories = new TreeMap<>();
    private ArrayList<String> priorities;

    /*
    This function breaks apart the JSON file. The file itself should always have a NewBook, LongBook, and ShortBook object.
    This results in this Parser object taking priorities, and the categories and associated words for each book.
     */
    public void parseTheFile(String theJSON)
    {


        String fullTextString = "";
        try {
            FileReader fr = new FileReader(theJSON);
            BufferedReader textReader = new BufferedReader(fr);

            StringBuilder fullText = new StringBuilder();
            String line;
            while ((line = textReader.readLine()) != null) {
                fullText.append(line);
            }

            fullTextString = fullText.toString();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



        String s = fullTextString;

        if(!s.equals("")) {

            try {
                JSONObject object = new JSONObject(s);

                longCategories = gatherCategories(object.getJSONObject("LongBook"));
                shortCategories = gatherCategories(object.getJSONObject("ShortBook"));
                newCategories = gatherCategories(object.getJSONObject("NewBook"));

                gatherPriorities(object.getJSONArray("Priorities"));



            } catch (Exception pe) {

                pe.printStackTrace();

            }
        }

    }

    /*
    This gathers the priorities of whatever book we pass in. Look for the usage of this function (in parseTheFile())
     */
    public void gatherPriorities(JSONArray prioritiesObject)
    {
        ArrayList<String> newPriorities = new ArrayList<>();
        for(int i = 0; i < prioritiesObject.length(); i++)
        {
            newPriorities.add(prioritiesObject.getString(i));
        }
        priorities = newPriorities;

    }

    /*
    This gathers the categories and their associated words for whatever object is passed in. This should be done on all 3 books.
     */
    public TreeMap<String, ArrayList<String>> gatherCategories(JSONObject originalObject)
    {
        TreeMap<String, ArrayList<String>> freshCategories = new TreeMap<>();
        Iterator<String> keysItr = originalObject.keys();
        while(keysItr.hasNext()) {
            String key = keysItr.next();
            JSONArray value = originalObject.getJSONArray(key);

            ArrayList<String> freshArrayList = new ArrayList<>();
            for(int i = 0; i < value.length(); i++)
            {
                freshArrayList.add(value.getString(i));
            }

            freshCategories.put(key, freshArrayList);

        }



        return freshCategories;
    }


    public TreeMap<String, ArrayList<String>> getLongCategories() {
        return longCategories;
    }

    public TreeMap<String, ArrayList<String>> getShortCategories() {
        return shortCategories;
    }

    public TreeMap<String, ArrayList<String>> getNewCategories() {
        return newCategories;
    }

    public ArrayList<String> getPriorities() {
        return priorities;
    }
}
