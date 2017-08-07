/**
 * Created by Jesse on 7/27/2017.
 */
public class ModifiedSentence {
    private int originalLocation;
    private int usageCount;
    private String sentence;

    public ModifiedSentence(int location, int count, String sentence)
    {
        originalLocation = location;
        usageCount = count;
        this.sentence = sentence;
    }

    public int getOriginalLocation() {
        return originalLocation;
    }

    public void setOriginalLocation(int originalLocation) {
        this.originalLocation = originalLocation;
    }

    public int getUsageCount() {
        return usageCount;
    }

    public void setUsageCount(int usageCount) {
        this.usageCount = usageCount;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
