import java.util.ArrayList;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;

/**
 * Made to organize the given sentences into a book following the given categories.
 * Created by Jesse on 7/25/2017.
 */
public class Organizer {
    private double CATEGORY_PLOT_NARROWNESS = .2;
    private int smallBookSize, largeBookSize;
    private ArrayList<String> possibleCategories;
    public Organizer(int smallBookSize, int largeBookSize, ArrayList<String> possibleCategories)
    {
        this.smallBookSize = smallBookSize;
        this.largeBookSize = largeBookSize;
        this.possibleCategories = possibleCategories;

    }

    /**
     * Iterates through the entire order of the Small book's categories and finds the most relevant modified sentences
     * from the Large book.
     * @param categoryOrder
     * @param categorizedSentences
     * @return the compiled String of a full book using the Small book's categorical order with the Large book's prose and the new book's key terms.
     */
    public String organizeFullBook(ArrayList<String> categoryOrder, TreeMap<String, ArrayList<ModifiedSentence>> categorizedSentences)
    {
        StringBuilder newBook = new StringBuilder();

        for(int i = 0; i < categoryOrder.size(); i ++)
        {
            String relevantCategory = categoryOrder.get(i);

            //the following 'if statement' essentially checks if the category at this sentence is uncategorized.
            // If so, it picks a random category and uses the nearest sentence to that one (so it could be uncategorized, but this is unlikely
            if(relevantCategory.equals("Uncategorized") ) {

                int randomIndex = ThreadLocalRandom.current().nextInt(0, possibleCategories.size());
                relevantCategory = possibleCategories.get(randomIndex);
                while(categorizedSentences.get(relevantCategory).size() <= 0)
                {
                    randomIndex = ThreadLocalRandom.current().nextInt(0, possibleCategories.size());
                    relevantCategory = possibleCategories.get(randomIndex);
                }
            }
            newBook.append(findRelevantSentence(relevantCategory, categorizedSentences, i));
            int randomIndex = ThreadLocalRandom.current().nextInt(0, 3 + 1);
            if(randomIndex == 3)
            {
                newBook.append(System.getProperty("line.separator"));
            }
        }

        return newBook.toString();
    }

    /**
     * Looks for the optimal sentence in a particular category given the index of the sentence we're matching from the small old book.
     * This utilizes binarySearchForSentence().
     * @param currentCategory
     * @param categorizedSentences
     * @param smallBookIndex
     * @return a sentence relating to the given currentCategory that is closest to a range within the large book corresponding to the relative location of the original sentence in the small book
     */
    private String findRelevantSentence(String currentCategory, TreeMap<String, ArrayList<ModifiedSentence>> categorizedSentences, int smallBookIndex)
    {
        String newSentence = "";
        ArrayList<ModifiedSentence> sentenceGroup = categorizedSentences.get(currentCategory);
        if(sentenceGroup == null)
        {
            String monkeys = "mankeys";
        }

        //below is gathering the location of the current sentence (e.g. sentence 44 out of a 200 sentence work would be at .22, or 22%)
        double currentSmallPercentLocation = (double) smallBookIndex /  smallBookSize;
        int currentTiedLargeLocation = (int) (currentSmallPercentLocation * largeBookSize);

        int rangeBottom = currentTiedLargeLocation - (int) (CATEGORY_PLOT_NARROWNESS * .5 * largeBookSize);
        if(rangeBottom < 0)
        {
            rangeBottom = 0;
        }

        int rangeTop = currentTiedLargeLocation + (int) (CATEGORY_PLOT_NARROWNESS * .5 * largeBookSize);
        if(rangeTop > largeBookSize)
        {
            rangeTop = largeBookSize;
        }

        int randomIndex = ThreadLocalRandom.current().nextInt(rangeBottom, rangeTop + 1);

        newSentence = binarySearchForSentence(randomIndex, sentenceGroup);


        return newSentence;
    }

    /**
     * This search looks for the sentence closest to the seekingIndex in terms of its actual position in the original story,
     * ensuring that a sentence at the beginning of the new story is more likely to be from the old story's beginning, too.
     * It can do so in log(n) time, though it calls linearLeastUsedSearch() if the found sentence has already been used.
     * @param seekingIndex
     * @param sentenceGroup
     * @return the String of the sentence found to be closest (or least used and closest) to the seekingIndex
     */
    private String binarySearchForSentence(int seekingIndex, ArrayList<ModifiedSentence> sentenceGroup)
    {
        String bestSentence = "";

        int previousIndex = -1;
        int halfIndex = sentenceGroup.size() / 2;
        int halfMovement = sentenceGroup.size() / 2;
        int previousDistance = largeBookSize;
        ModifiedSentence previousSentence = new ModifiedSentence(1, 0, "");

        boolean found = false;
        int bestIndex = -1;
        while(!found)
        {
            if(halfIndex < 0)
            {
                found = true;
                bestIndex = 0;

            }
            else if(halfIndex > sentenceGroup.size()-1)
            {
                found = true;
                bestIndex = sentenceGroup.size() -1;
            }
            else if(sentenceGroup.get(halfIndex).getOriginalLocation() == seekingIndex)
            {
                found = true;
                bestIndex = halfIndex;
            }
            else if(halfMovement == 1 && previousDistance <= Math.abs(sentenceGroup.get(halfIndex).getOriginalLocation() - seekingIndex))
            {
                found = true;


                bestIndex = previousIndex;
            }
            else if(sentenceGroup.get(halfIndex).getOriginalLocation() < seekingIndex)
            {
                previousIndex = halfIndex;
                previousSentence = sentenceGroup.get(halfIndex);
                previousDistance = Math.abs(sentenceGroup.get(halfIndex).getOriginalLocation() - seekingIndex);
                halfMovement /= 2;
                if(halfMovement == 0)
                {
                    halfMovement = 1;
                }
                halfIndex += halfMovement;
            }
            else if(sentenceGroup.get(halfIndex).getOriginalLocation() > seekingIndex)
            {
                previousIndex = halfIndex;
                previousSentence = sentenceGroup.get(halfIndex);
                previousDistance = Math.abs(sentenceGroup.get(halfIndex).getOriginalLocation() - seekingIndex);
                halfMovement /= 2;
                if(halfMovement == 0)
                {
                    halfMovement = 1;
                }
                halfIndex -= halfMovement;
            }



        }


        if(sentenceGroup.get(bestIndex).getUsageCount() > 0)
        {
            bestIndex = linearLeastUsedSearch(bestIndex, sentenceGroup);
        }
        sentenceGroup.get(bestIndex).setUsageCount(sentenceGroup.get(bestIndex).getUsageCount() + 1);
        bestSentence = sentenceGroup.get(bestIndex).getSentence();

        return bestSentence;
    }


    /**
     * This function looks for the lowest used sentence in the offered sentence group. If there are multiple use values
     * that are the lowest value in the group, this function prioritizes those closest to the original index.
     * @param currentIndex
     * @param sentenceGroup
     * @return the index of the least used sentence in the given group
     */
    public int linearLeastUsedSearch(int currentIndex, ArrayList<ModifiedSentence> sentenceGroup)
    {
        int lowest = sentenceGroup.get(currentIndex).getUsageCount();
        int droppingIndex = -1;
        int droppingDistance = Integer.MAX_VALUE;
        for(int i = currentIndex; i > -1; i--)
        {
            if(sentenceGroup.get(i).getUsageCount() == 0)
            {
                droppingIndex = i;
                droppingDistance = Math.abs(currentIndex - i);
                lowest = 0;
                break;
            }
            else if(sentenceGroup.get(i).getUsageCount() < lowest)
            {
                droppingIndex = i;
                droppingDistance = Math.abs(currentIndex - i);
                lowest = sentenceGroup.get(i).getUsageCount();
            }
        }

        boolean alreadyFoundRisingDistance = false;
        int risingIndex = -1;
        int risingDistance = Integer.MAX_VALUE;
        for(int j = currentIndex + 1; j < sentenceGroup.size(); j++)
        {
            if(sentenceGroup.get(j).getUsageCount() == 0)
            {
                risingIndex = j;
                risingDistance = Math.abs(currentIndex - j);
                lowest = 0;
                break;
            }
            else if(sentenceGroup.get(j).getUsageCount() < lowest)
            {
                risingIndex = j;
                risingDistance = Math.abs(currentIndex - j);
                lowest = sentenceGroup.get(j).getUsageCount();
            }
            else if(sentenceGroup.get(j).getUsageCount() == lowest && !alreadyFoundRisingDistance)
            {
                risingIndex = j;
                risingDistance = Math.abs(currentIndex - j);
                alreadyFoundRisingDistance = true;
            }
        }

        //if no better sentence was found, return the original
        if(lowest == sentenceGroup.get(currentIndex).getUsageCount())
        {
            return currentIndex;
        }
        else if(risingDistance < droppingDistance)
        {
            return risingIndex;
        }
        else
        {
            return droppingIndex;
        }
    }

}
