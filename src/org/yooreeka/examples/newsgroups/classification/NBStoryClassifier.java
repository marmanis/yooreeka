package org.yooreeka.examples.newsgroups.classification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.yooreeka.algos.taxis.bayesian.NaiveBayes;
import org.yooreeka.algos.taxis.core.BaseConcept;
import org.yooreeka.algos.taxis.core.BaseInstance;
import org.yooreeka.algos.taxis.core.StringAttribute;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Classifier;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;
import org.yooreeka.examples.newsgroups.core.NewsCategory;
import org.yooreeka.examples.newsgroups.core.NewsDataset;
import org.yooreeka.examples.newsgroups.core.NewsStory;

public class NBStoryClassifier implements Classifier {

    private List<ClassificationResult> conceptScores;
    private NaiveBayes nbClassifier;
    private boolean verbose = true;

    public NBStoryClassifier(String name, NewsDataset ds) {
        TrainingSet tSet = createTrainingSet(ds);
        nbClassifier = new NaiveBayes(name, tSet);

    }

    public List<ClassificationResult> getConceptScores() {
        return conceptScores;
    }

    public List<ClassificationResult> getTopNScores(int topN) {
        return ClassificationResult.getTopResults(conceptScores, topN);
    }

    /**
     * Overriding default implementation to collect classification scores for
     * all concepts.
     */
    public Concept classify(Instance instance) {

        conceptScores = new ArrayList<ClassificationResult>();

        Concept bestConcept = null;
        double bestP = 0.0;

        TrainingSet tSet = nbClassifier.getTset();

        if( tSet == null || tSet.getConceptSet().size() == 0) {
            throw new RuntimeException("You have to train classifier first.");
        }

        if( verbose ) {
            System.out.println("\n*** Classifying instance: " + instance.toString() + "\n");
        }

        for (Concept c : tSet.getConceptSet()) {
            double p = nbClassifier.getProbability(c, instance);

            ClassificationResult cR = new ClassificationResult(c, p);
            conceptScores.add(cR);

            if( verbose ) {
                System.out.printf("P(%s|%s) = %.15f\n", c.getName(), instance.toString(), p);
            }
            if( p >= bestP ) {
                bestConcept = c;
                bestP = p;
            }
        }
        return bestConcept;
    }

    /*
     * Converts all documents from the dataset into training set.
     */
    public TrainingSet createTrainingSet(NewsDataset ds) {
        int nStories = ds.getSize();
        List<Instance> allTrainingInstances =
            new ArrayList<Instance>(nStories);
        Iterator<NewsStory> iter = ds.getIteratorOverStories();
        while( iter.hasNext() ) {
            NewsStory newsStory = iter.next();
            Instance instance = toInstance(newsStory);
            allTrainingInstances.add(instance);
        }

        Instance[] instances = allTrainingInstances.toArray(new Instance[nStories]);

        return new TrainingSet(instances);
    }

    /*
     * Creating an Instance from the NewsStory.
     */
    public Instance toInstance(NewsStory newsStory) {
        Concept concept = toConcept(newsStory.getTopic());
        String[] terms = newsStory.getTopNTerms();
        StringAttribute[] attributes = new StringAttribute[terms.length];
        for(int i = 0; i < terms.length; i++) {
            String name = terms[i];
            String value = "Y";
            attributes[i] = new StringAttribute(name, value);
        }
        return new BaseInstance(concept, attributes);
    }

    private Map<String, NewsCategory> allTopics = new HashMap<String, NewsCategory>();
    private Map<String, Concept> allConcepts = new HashMap<String, Concept>();

    /*
     * Converts Concept into NewsCategory.
     */
    public NewsCategory toTopic(Concept c) {
        if( c == null ) {
            return null;
        }

        String name = c.getName();
        NewsCategory t = allTopics.get(name);
        if( t == null ) {
            t = new NewsCategory(name);
            allTopics.put(name, t);
        }
        return t;
    }

    /*
     * Converts NewsCategory into Concept
     */
    public Concept toConcept(NewsCategory t) {
        if( t == null ) {
            return null;
        }

        String topicName = t.getName();
        Concept c = allConcepts.get(topicName);
        if( c == null ) {
            c = new BaseConcept(t.getName());
            allConcepts.put(topicName, c);
        }
        return c;
    }


    public String getName() {
        return nbClassifier.getName();
    }


    public boolean train() {
        TrainingSet tSet = nbClassifier.getTset();
        for(String attributeName : tSet.getAttributeNameSet() ) {
            nbClassifier.trainOnAttribute(attributeName);
        }

        return nbClassifier.train();
    }

}