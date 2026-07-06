
package org.yooreeka.algos.taxis.bayesian;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.yooreeka.algos.taxis.core.BaseInstance;
import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.algos.taxis.core.intf.Concept;
import org.yooreeka.algos.taxis.core.intf.Instance;

public class NaiveBayesTest {

	private TrainingSet tennisTrainingSet;

	@Before
	public void setUp() {
		// Mock PlayTennis dataset
		// Features: "Outlook" (Sunny, Overcast, Rain), "Wind" (Weak, Strong)
		// Concepts: "Yes", "No"
		Instance[] instances = new Instance[] {
			BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Sunny", "Weak"}),
			BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Overcast", "Weak"}),
			BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Rain", "Weak"}),
			BaseInstance.createInstance("No", new String[] {"Outlook", "Wind"}, new String[] {"Sunny", "Strong"}),
			BaseInstance.createInstance("No", new String[] {"Outlook", "Wind"}, new String[] {"Rain", "Strong"}),
			BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Overcast", "Strong"})
		};
		tennisTrainingSet = new TrainingSet(instances);
	}

	@Test
	public void testClassifierInitialization() {
		NaiveBayes nb = new NaiveBayes("TennisClassifier", tennisTrainingSet);
		Assert.assertEquals("TennisClassifier", nb.getName());
		Assert.assertEquals(tennisTrainingSet, nb.getTset());
	}

	@Test
	public void testClassifierTrainingRequiresAttributes() {
		NaiveBayes nb = new NaiveBayes("TennisClassifier", tennisTrainingSet);
		try {
			nb.train();
			Assert.fail("Should throw IllegalStateException because attributes are not specified");
		} catch (IllegalStateException e) {
			Assert.assertTrue(e.getMessage().contains("without specifying the attributes"));
		}
	}

	@Test
	public void testClassificationPlayTennis() {
		NaiveBayes nb = new NaiveBayes("TennisClassifier", tennisTrainingSet);
		nb.trainOnAttribute("Outlook");
		nb.trainOnAttribute("Wind");
		
		Assert.assertTrue(nb.train());

		// A Sunny and Weak wind day should be classified as Yes
		Instance testInstanceYes = BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Sunny", "Weak"});
		Concept classificationResult = nb.classify(testInstanceYes);
		Assert.assertNotNull(classificationResult);
		Assert.assertEquals("Yes", classificationResult.getName());

		// A Rain and Strong wind day should be classified as No
		Instance testInstanceNo = BaseInstance.createInstance("No", new String[] {"Outlook", "Wind"}, new String[] {"Rain", "Strong"});
		Concept classificationResultNo = nb.classify(testInstanceNo);
		Assert.assertNotNull(classificationResultNo);
		Assert.assertEquals("No", classificationResultNo.getName());
	}

	@Test
	public void testUnseenAttributeValueHandling() {
		NaiveBayes nb = new NaiveBayes("TennisClassifier", tennisTrainingSet);
		nb.trainOnAttribute("Outlook");
		nb.trainOnAttribute("Wind");
		nb.train();

		// "Snowy" is an unseen attribute value for Outlook
		Instance testInstanceUnseen = BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Snowy", "Weak"});
		Concept classificationResult = nb.classify(testInstanceUnseen);
		Assert.assertNotNull(classificationResult);
	}

	@Test
	public void testClassificationNullHandling() {
		NaiveBayes nb = new NaiveBayes("TennisClassifier", tennisTrainingSet);
		nb.trainOnAttribute("Outlook");
		nb.trainOnAttribute("Wind");
		nb.train();

		try {
			nb.classify(null);
			Assert.fail("Should throw IllegalArgumentException when classifying null instance");
		} catch (IllegalArgumentException e) {
			Assert.assertTrue(e.getMessage().contains("cannot be null"));
		}
	}

	private Concept getConcept(TrainingSet ts, String name) {
		for (Concept c : ts.getConceptSet()) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		return null;
	}

	@Test
	public void testCompareLaplacianToFallbackHeuristic() {
		Instance testInstance = BaseInstance.createInstance("Yes", new String[] {"Outlook", "Wind"}, new String[] {"Snowy", "Weak"});

		NaiveBayes nbDefault = new NaiveBayes("DefaultClassifier", tennisTrainingSet);
		nbDefault.trainOnAttribute("Outlook");
		nbDefault.trainOnAttribute("Wind");
		nbDefault.train();
		
		double defaultProbUnseenYes = nbDefault.getProbability(testInstance, getConcept(tennisTrainingSet, "Yes"));

		NaiveBayes nbLaplacian = new NaiveBayes("LaplacianClassifier", tennisTrainingSet);
		nbLaplacian.trainOnAttribute("Outlook");
		nbLaplacian.trainOnAttribute("Wind");
		nbLaplacian.setUseLaplacianSmoothing(true);
		nbLaplacian.train();
		
		double laplacianProbUnseenYes = nbLaplacian.getProbability(testInstance, getConcept(tennisTrainingSet, "Yes"));

		Assert.assertNotEquals(defaultProbUnseenYes, laplacianProbUnseenYes, 1e-6);
		Assert.assertEquals(0.1071428, defaultProbUnseenYes, 1e-4);
		Assert.assertEquals(0.095238, laplacianProbUnseenYes, 1e-4);
		
		nbLaplacian.setUseLogSum(true);
		double logSumProb = nbLaplacian.getProbability(testInstance, getConcept(tennisTrainingSet, "Yes"));
		Assert.assertEquals(Math.log(0.095238), logSumProb, 1e-4);
	}
}
