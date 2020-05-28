package org.yooreeka.examples.spamfilter;
public class ClassificationResult {
    
    private boolean isSpamEmail;
    
    public ClassificationResult() {
        // empty
    }

    /*
     * It is better to keep as much java code as possible outside of rule files. 
     * Unless of cause you enjoy working with scripting languages :-)
     */
    public boolean isSimilar(String x, String y) {
        return x.contains(y);
    }
    
    public boolean isSpamEmail() {
        return isSpamEmail;
    }

    public void setSpamEmail(boolean isSpamEmail) {
        System.out.println("Invoked " + this.getClass().getSimpleName() + ".setSpamEmail(" + isSpamEmail + ")");
        this.isSpamEmail = isSpamEmail;
    }
}