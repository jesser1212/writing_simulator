import com.sun.org.apache.xpath.internal.operations.Mod;

import java.util.ArrayList;
import java.util.TreeMap;

/**This is designed to Classify all the split sentences into different categories.
 * Created by Jesse on 7/27/2017.
 */
public class Classifier {

    TreeMap<String, ArrayList<String>> newAssociations;

    public TreeMap<String, ArrayList<String>> getNewAssociations() {
        return newAssociations;
    }

    public void setNewAssociations(TreeMap<String, ArrayList<String>> newAssociations) {
        this.newAssociations = newAssociations;
    }

    /**
    returns the categories of the sentences in the book in the order that they come.
     */
    public ArrayList<String> gatherCategoryOrder(String[] smallBook, TreeMap<String, ArrayList<String>> categories, ArrayList<String> priorities)
    {
        ArrayList<String> finalOrder = new ArrayList<>();
        for(int i = 0; i < smallBook.length; i++)
        {
            finalOrder.add(i, "");
            for(int j = 0; j < priorities.size(); j++)
            {
                if(areAssociationsInSentence(smallBook[i], categories.get(priorities.get(j))))
                {
                    finalOrder.set(i, priorities.get(j));
                    break;

                }
            }
            if(finalOrder.get(i).equals(""))
            {
                finalOrder.set(i, "Uncategorized");
            }
        }
        return finalOrder;
    }

    /**
     *
     * @return the LongBook's sentences, modified with the new words and associated with their classifications
     * TODO: this is adding one sentence to multiple categories.
     */
    public TreeMap<String, ArrayList<ModifiedSentence>> modifyAndCategorize(String[] largeBook, TreeMap<String, ArrayList<String>> categories, ArrayList<String> priorities)
    {
        TreeMap<String, ArrayList<ModifiedSentence>> returnTree = new TreeMap<>();

        for(int i = 0; i < priorities.size(); i ++)
        {
            ArrayList<ModifiedSentence> emptyList = new ArrayList<>();
            returnTree.put(priorities.get(i), emptyList);
        }
        ArrayList<ModifiedSentence> emptyList = new ArrayList<>();
        returnTree.put("Uncategorized", emptyList);


         for(int i = 0; i < largeBook.length; i++)
        {
            boolean categorized = false;
            String highestPriorityCategory = "Uncategorized";
            String modifyingSentence = largeBook[i];
            for(int j = 0; j < priorities.size(); j++)
            {
                if(areAssociationsInSentence(largeBook[i], categories.get(priorities.get(j))))
                {

                    if(!categorized) {
                        highestPriorityCategory = priorities.get(j);
                    }
                    categorized = true;
                    modifyingSentence = replaceAssociationsInSentence(modifyingSentence, categories.get(priorities.get(j)), priorities.get(j));


                    //ModifiedSentence newSentence = new ModifiedSentence(i, 0, replacedSentence);


                }
            }
            ModifiedSentence newSentence = new ModifiedSentence(i, 0, modifyingSentence);
            returnTree.get(highestPriorityCategory).add(newSentence);


        }

        return returnTree;
    }


    private boolean areAssociationsInSentence(String sentence, ArrayList<String> associations)
    {
        boolean found = false;
        for(int i = 0; i < associations.size(); i++)
        {
            if(sentence.contains(" " + associations.get(i) + " ")
                    || sentence.contains(" " + associations.get(i) + "\\.")
                    || sentence.contains(" " + associations.get(i) + ",")
                    || sentence.contains(" " + associations.get(i) + "!")
                    || sentence.contains(" " + associations.get(i) + "?")
                    || sentence.contains(" " + associations.get(i) + "—")
                    || sentence.contains("—" + associations.get(i) + " ")
                    || sentence.contains("\"" + associations.get(i) + ",")
                    || sentence.contains(" " + associations.get(i) + "\"")
                    || sentence.contains("\"" + associations.get(i) + " ")


                    )

            {
                found = true;
            }

        }
        return found;
    }


    private String replaceAssociationsInSentence(String sentence, ArrayList<String> associations, String category)
    {
        //String newSentence = "";
        ArrayList<String> replacers = newAssociations.get(category);

        for(int i = 0; i < associations.size(); i++)
        {
            String replaceWord = "";
            if(replacers.size() < 1)
            {
                replaceWord = associations.get(0);
            }
            else
            {
                replaceWord = replacers.get(0);
            }
            //determine the replacing word
            if(replacers.size() -1 >= i)
            {
                replaceWord = replacers.get(i);
            }

            sentence = replaceWordsInSentence(sentence, (associations.get(i)), (replaceWord));
            //replace any containments


        }
        return sentence;
    }

    private String replaceWordsInSentence(String oldSentence, String oldTerm, String newTerm)
    {

        oldSentence = oldSentence.replaceAll(" " + oldTerm + " ", " " + newTerm + " ");
        oldSentence = oldSentence.replaceAll(" " + oldTerm + ",", " " + newTerm + ",");
        oldSentence = oldSentence.replaceAll(" " + oldTerm + "\\.", " " + newTerm + "\\.");
        oldSentence = oldSentence.replaceAll(" " + oldTerm + "!", " " + newTerm + "!");
        oldSentence = oldSentence.replaceAll(" " + oldTerm + "—", " " + newTerm + "—");
        oldSentence = oldSentence.replaceAll("—" + oldTerm + " ", "—" + newTerm + " ");
        oldSentence = oldSentence.replaceAll("\"" + oldTerm + ",", "\"" + newTerm + ",");
        oldSentence = oldSentence.replaceAll(" " + oldTerm + "\"", " " + newTerm + "\"");
        oldSentence = oldSentence.replaceAll("\"" + oldTerm + " ", "\"" + newTerm + " ");






        return oldSentence;
    }
}
