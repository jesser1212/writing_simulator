import java.util.ArrayList;
import java.util.TreeMap;

/**
 * This class essentially outlines the entire rewriting process here.
 * First, a JSON file is parsed to find what priorities the program should have,
 * along with the categories and associated words of the categories for a large Public Domain work, a small Public Domain work, and a new book.
 * It then finds the order of categories found in the Small work,
 * modifies the sentences of the Large work and categorizes them, too,
 * and then organizes the optimal sentences of the Large work that fit the order of the Small work's categories
 * into a new book that the user has freely generated.
 * Created by Jesse on 7/25/2017.
 */
public class Main {

    public static void main(String args[])
    {
        String jsonFile;
        String largeStoryFileName;
        String smallStoryFileName;


        jsonFile = args[0];
        largeStoryFileName = args[1];
        smallStoryFileName = args[2];
        String outputFile = args[3];

        Parser theJSONParser = new Parser();
        theJSONParser.parseTheFile(jsonFile);

        ReadWriter theReadAndWriter = new ReadWriter();
        String[] splitSmallBook = theReadAndWriter.readAndSplit(smallStoryFileName);
        String[] splitLargeBook = theReadAndWriter.readAndSplit(largeStoryFileName);

        //"categories" is the map associating different sentence categories with their associated words
        TreeMap<String, ArrayList<String>> smallCategories;
        ArrayList<String> prioritizedCategories = theJSONParser.getPriorities();
        smallCategories = theJSONParser.getShortCategories();



        //new associations:
        TreeMap<String, ArrayList<String>> largeCategories;
        largeCategories = theJSONParser.getLongCategories();



        TreeMap<String, ArrayList<String>> newCategories;
        newCategories = theJSONParser.getNewCategories();


        Classifier theClassifier = new Classifier();
        ArrayList<String> categoryOrder;
        categoryOrder = theClassifier.gatherCategoryOrder(splitSmallBook, smallCategories, prioritizedCategories);



        //this is the map associating the modified sentences of the long book to their categories
        TreeMap<String, ArrayList<ModifiedSentence>> categorizedLongBook;
        theClassifier.setNewAssociations(newCategories);
        categorizedLongBook = theClassifier.modifyAndCategorize(splitLargeBook, largeCategories, prioritizedCategories);

        Organizer theOrganizer = new Organizer(splitSmallBook.length, splitLargeBook.length, prioritizedCategories);
        String organizedBook = theOrganizer.organizeFullBook(categoryOrder, categorizedLongBook);

        theReadAndWriter.writeToPath(outputFile, organizedBook);
    }
}
