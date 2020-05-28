package org.yooreeka.test;

import org.yooreeka.algos.taxis.core.TrainingSet;
import org.yooreeka.examples.credit.BaggingCreditClassifier;
import org.yooreeka.examples.credit.BoostingCreditClassifier;
import org.yooreeka.examples.credit.DTCreditClassifier;
import org.yooreeka.examples.credit.NBCreditClassifier;
import org.yooreeka.examples.credit.NNCreditClassifier;
import org.yooreeka.examples.credit.data.UseCaseData;
import org.yooreeka.examples.credit.data.UserDataset;
import org.yooreeka.examples.credit.data.UserLoader;
import org.yooreeka.examples.credit.data.users.UserType;
import org.yooreeka.examples.credit.util.CreditErrorEstimator;
import org.yooreeka.util.P;

public class Chapter_06 {

	private UserDataset ds;
	private long t=0;
	
	public Chapter_06() {
		
		//EMPTY
	}
	
	public void run() {

		t = System.currentTimeMillis();
		
		//Credit Data
		script_06_01();
		
		//Credit Score via Naive Bayes
		script_06_02();
		
		//Credit Score via Decision Tree
		script_06_03();
		
		//Credit Score via Neural Network
		script_06_04();
		
		//Comparisons
		script_06_05();
		
		// Bagging
		script_06_06_01();
		script_06_06_02();
		script_06_06_03();
		script_06_06_04();
		script_06_06_05();
		
		// Boosting
		script_06_07_01();
		script_06_07_02();

		P.timePassedSince(t);
	}
	
	private void script_06_07_02() {

		BoostingCreditClassifier arcx4 = new BoostingCreditClassifier(ds);

		arcx4.setClassifierType("decision tree");

		arcx4.setClassifierPopulation(1);
		        
		arcx4.setVerbose(false);
		  
		arcx4.train();

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator arcx4ee  = new CreditErrorEstimator(testDS, arcx4);

		arcx4ee.run();                                                                          

		arcx4.setClassifierPopulation(3);

		arcx4.train();

		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);

		arcx4ee.run();		
	}

	private void script_06_07_01() {

		BoostingCreditClassifier arcx4 = new BoostingCreditClassifier(ds);

		arcx4.setClassifierType("decision tree");
		arcx4.setClassifierPopulation(1);
		        
		// set verbose level to true to see more details.
		// ATTENTION: If set to true then every classification will be reported
		arcx4.setVerbose(false);
		  
		arcx4.train();

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator arcx4ee  = new CreditErrorEstimator(testDS, arcx4);

		arcx4ee.run();

		// Keep adding members
		arcx4.setClassifierPopulation(3);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

		arcx4.setClassifierPopulation(5);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

		arcx4.setClassifierPopulation(7);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

		arcx4.setClassifierPopulation(11);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

		arcx4.setClassifierPopulation(31);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

		arcx4.setClassifierPopulation(41);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

		arcx4.setClassifierPopulation(61);
		arcx4.train();
		arcx4ee  = new CreditErrorEstimator(testDS, arcx4);
		arcx4ee.run();

	}

	private void script_06_06_05() {

		BaggingCreditClassifier bagClassifier = new BaggingCreditClassifier(ds);
		        
		bagClassifier.setVerbose(false);
		        
		TrainingSet ts1 = bagClassifier.getBootstrapSet();

		DTCreditClassifier dt1 = new DTCreditClassifier(ts1);

		dt1.useDefaultAttributes();

		dt1.setPruneAfterTraining(true);

		bagClassifier.addMember(dt1);

		bagClassifier.train();

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator bagee1 = new CreditErrorEstimator(testDS, bagClassifier);

		bagee1.run();

		TrainingSet ts2 = bagClassifier.getBootstrapSet();

		DTCreditClassifier dt2 = new DTCreditClassifier(ts2);

		dt2.useDefaultAttributes();

		dt2.setPruneAfterTraining(true);

		bagClassifier.addMember(dt2);

		bagClassifier.train();

		CreditErrorEstimator bagee2 = new CreditErrorEstimator(testDS, bagClassifier);

		bagee2.run();		
	}

	private void script_06_06_04() {

		BaggingCreditClassifier bagClassifier = new BaggingCreditClassifier(ds);
		        
		// set verbose level to true to see more details.
		// ATTENTION: If set to true then every classification will be reported
		bagClassifier.setVerbose(false);
		        
		//Adding ensemble members

		TrainingSet ts1 = bagClassifier.getBootstrapSet();
		NNCreditClassifier NN1 = new NNCreditClassifier(ts1);
		NN1.useDefaultAttributes();
		bagClassifier.addMember(NN1);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		UserDataset testDS = UserLoader.loadTestDataset();
		CreditErrorEstimator bagee1 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee1.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts2 = bagClassifier.getBootstrapSet();
		NNCreditClassifier NN2 = new NNCreditClassifier(ts2);
		NN2.useDefaultAttributes();
		bagClassifier.addMember(NN2);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee2 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee2.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts3 = bagClassifier.getBootstrapSet();
		NNCreditClassifier NN3 = new NNCreditClassifier(ts3);
		NN3.useDefaultAttributes();
		bagClassifier.addMember(NN3);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee3 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee3.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts4 = bagClassifier.getBootstrapSet();
		NNCreditClassifier NN4 = new NNCreditClassifier(ts4);
		NN4.useDefaultAttributes();
		bagClassifier.addMember(NN4);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee4 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee4.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts5 = bagClassifier.getBootstrapSet();
		NNCreditClassifier NN5 = new NNCreditClassifier(ts5);
		NN5.useDefaultAttributes();
		bagClassifier.addMember(NN5);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee5 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee5.run();

		// -----------------------------------------------------------------------------		
	}

	private void script_06_06_03() {

		BaggingCreditClassifier bagClassifier = new BaggingCreditClassifier(ds);
		        
		// set verbose level to true to see more details.
		// ATTENTION: If set to true then every classification will be reported
		bagClassifier.setVerbose(false);
		        
		//Adding ensemble members

		TrainingSet ts1 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb1 = new NBCreditClassifier(ts1);
		nb1.useDefaultAttributes();
		bagClassifier.addMember(nb1);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		UserDataset testDS = UserLoader.loadTestDataset();
		CreditErrorEstimator bagee1 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee1.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts2 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb2 = new NBCreditClassifier(ts2);
		nb2.useDefaultAttributes();
		bagClassifier.addMember(nb2);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee2 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee2.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts3 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb3 = new NBCreditClassifier(ts3);
		nb3.useDefaultAttributes();
		bagClassifier.addMember(nb3);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee3 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee3.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts4 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb4 = new NBCreditClassifier(ts4);
		nb4.useDefaultAttributes();
		bagClassifier.addMember(nb4);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee4 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee4.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts5 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb5 = new NBCreditClassifier(ts5);
		nb5.useDefaultAttributes();
		bagClassifier.addMember(nb5);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee5 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee5.run();

		// -----------------------------------------------------------------------------		
	}

	private void script_06_06_02() {

		BaggingCreditClassifier bagClassifier = new BaggingCreditClassifier(ds);
		        
		// set verbose level to true to see more details.
		// ATTENTION: If set to true then every classification will be reported
		bagClassifier.setVerbose(false);
		        
		//Adding ensemble members

		TrainingSet ts1 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt1 = new DTCreditClassifier(ts1);
		dt1.useDefaultAttributes();
		dt1.setPruneAfterTraining(true);
		bagClassifier.addMember(dt1);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		UserDataset testDS = UserLoader.loadTestDataset();
		CreditErrorEstimator bagee1 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee1.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts2 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt2 = new DTCreditClassifier(ts2);
		dt2.useDefaultAttributes();
		dt2.setPruneAfterTraining(true);
		bagClassifier.addMember(dt2);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee2 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee2.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts3 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt3 = new DTCreditClassifier(ts3);
		dt3.useDefaultAttributes();
		dt3.setPruneAfterTraining(true);
		bagClassifier.addMember(dt3);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee3 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee3.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts4 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt4 = new DTCreditClassifier(ts4);
		dt4.useDefaultAttributes();
		dt4.setPruneAfterTraining(true);
		bagClassifier.addMember(dt4);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee4 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee4.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts5 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt5 = new DTCreditClassifier(ts5);
		dt5.useDefaultAttributes();
		dt5.setPruneAfterTraining(true);
		bagClassifier.addMember(dt5);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee5 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee5.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts6 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt6 = new DTCreditClassifier(ts6);
		dt6.useDefaultAttributes();
		dt6.setPruneAfterTraining(true);
		bagClassifier.addMember(dt6);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee6 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee6.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts7 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt7 = new DTCreditClassifier(ts7);
		dt7.useDefaultAttributes();
		dt7.setPruneAfterTraining(true);
		bagClassifier.addMember(dt7);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee7 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee7.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts8 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt8 = new DTCreditClassifier(ts8);
		dt8.useDefaultAttributes();
		dt8.setPruneAfterTraining(true);
		bagClassifier.addMember(dt8);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee8 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee8.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts9 = bagClassifier.getBootstrapSet();
		DTCreditClassifier dt9 = new DTCreditClassifier(ts9);
		dt9.useDefaultAttributes();
		dt9.setPruneAfterTraining(true);
		bagClassifier.addMember(dt9);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee9 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee9.run();

		// -----------------------------------------------------------------------------

		/*

		Finally, given adequately powerful constituent classifiers, there is a sweet 
		spot of training data size for which diversity through input variation is most 
		effective. For example, if there is too little data, the gains achieved via 
		a bagged ensemble cannot compensate for the decrease in accuracy of individual 
		models, each of which now sees an even smaller training set. On the other end, 
		if the data set is extremely large and computation time is not an issue, even 
		a single flexible classifier can be quite adequate

		*/		
	}

	private void script_06_06_01() {

		BaggingCreditClassifier bagClassifier = new BaggingCreditClassifier(ds);
		        
		// set verbose level to true to see more details.
		// ATTENTION: If set to true then every classification will be reported
		bagClassifier.setVerbose(false);
		        
		//Adding ensemble members
		TrainingSet ts1 = bagClassifier.getBootstrapSet();
		DTCreditClassifier tree1 = new DTCreditClassifier(ts1);
		tree1.useDefaultAttributes();
		tree1.setPruneAfterTraining(true);
		bagClassifier.addMember(tree1);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		UserDataset testDS = UserLoader.loadTestDataset();
		CreditErrorEstimator bagee1 = new CreditErrorEstimator(testDS, bagClassifier);
		bagee1.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts2 = bagClassifier.getBootstrapSet();
		NNCreditClassifier nn1 = new NNCreditClassifier(ts2);
		nn1.setLearningRate(0.05);
		nn1.useDefaultAttributes();
		 
		bagClassifier.addMember(nn1);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee2 = new CreditErrorEstimator(testDS, bagClassifier);   
		bagee2.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts3 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb1 = new NBCreditClassifier(ts3);
		nb1.useDefaultAttributes();
		bagClassifier.addMember(nb1);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee3 = new CreditErrorEstimator(testDS, bagClassifier);     
		bagee3.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts4 = bagClassifier.getBootstrapSet();
		DTCreditClassifier tree2 = new DTCreditClassifier(ts4);
		tree2.useDefaultAttributes();
		tree2.setPruneAfterTraining(true);
		bagClassifier.addMember(tree2);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee4 = new CreditErrorEstimator(testDS, bagClassifier);     
		bagee4.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts5 = bagClassifier.getBootstrapSet();
		NBCreditClassifier nb2 = new NBCreditClassifier(ts5);
		nb2.useDefaultAttributes();
		bagClassifier.addMember(nb2);
		bagClassifier.train();

		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee5 = new CreditErrorEstimator(testDS, bagClassifier);    
		bagee5.run();

		// -----------------------------------------------------------------------------

		TrainingSet ts6 = bagClassifier.getBootstrapSet();
		NNCreditClassifier nn2 = new NNCreditClassifier(ts6);
		nn2.setLearningRate(0.05);
		nn2.useDefaultAttributes();
		 
		bagClassifier.addMember(nn2);
		bagClassifier.train();
		// -----------------------------------------------------------------------------

		CreditErrorEstimator bagee6 = new CreditErrorEstimator(testDS, bagClassifier);    
		bagee6.run();

		// -----------------------------------------------------------------------------


		
	}

	private void script_06_05() {

		NNCreditClassifier neuralNet = new NNCreditClassifier(ds);

		neuralNet.setLearningRate(0.025);
		neuralNet.useDefaultAttributes();
		        
		neuralNet.train();

		//
		// Now, calculate error rate for test set
		//

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator nn_err = new CreditErrorEstimator(testDS, neuralNet);
		       
		nn_err.run();

		/*

		============================================================
		 Classification completed in 1.702 seconds.

		 Total test dataset txns: 20000
		    Classified correctly: 14330, Misclassified: 5670
		                Accuracy: 0.7165
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX    498      0    372      0      0
		  VG     91      0   4100     23      0
		  GD      0      0   8804    897      0
		  BD      0      0     33   4175    147
		  DN      0      0      0      7    853
		  
		============================================================

		 Classification completed in 5.261 seconds.

		 Total test dataset txns: 75000
		    Classified correctly: 58698, Misclassified: 16302
		                Accuracy: 0.78264
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX    473   2728     52      0      0
		  VG     80  10224   5580     12      0
		  GD      1   3779  29573   2991      0
		  BD      0      0    451  15273    595
		  DN      0      0      1     32   3155
		============================================================

		*/
		
	}

	private void script_06_04() {

		NNCreditClassifier neuralNet = new NNCreditClassifier(ds);

		neuralNet.setLearningRate(0.025);
		neuralNet.useDefaultAttributes();
		        
		neuralNet.train();

		//
		// Now, calculate error rate for test set
		//

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator nn_err = new CreditErrorEstimator(testDS, neuralNet);
		       
		nn_err.run();

		/*

		============================================================
		 Classification completed in 1.702 seconds.

		 Total test dataset txns: 20000
		    Classified correctly: 14330, Misclassified: 5670
		                Accuracy: 0.7165
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX    498      0    372      0      0
		  VG     91      0   4100     23      0
		  GD      0      0   8804    897      0
		  BD      0      0     33   4175    147
		  DN      0      0      0      7    853
		  
		============================================================

		 Classification completed in 5.261 seconds.

		 Total test dataset txns: 75000
		    Classified correctly: 58698, Misclassified: 16302
		                Accuracy: 0.78264
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX    473   2728     52      0      0
		  VG     80  10224   5580     12      0
		  GD      1   3779  29573   2991      0
		  BD      0      0    451  15273    595
		  DN      0      0      1     32   3155
		============================================================

		*/
		
	}

	private void script_06_03() {

		DTCreditClassifier decisionTree = new DTCreditClassifier(ds);

		decisionTree.useDefaultAttributes();

		decisionTree.train();

		//---> decisionTree.printTree();


		//
		// Now, calculate error rate for test set
		//

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator dt_err = new CreditErrorEstimator(testDS, decisionTree);
		       
		dt_err.run();

		/* 

		====================================================================
		 Total test dataset txns: 20000
		    Classified correctly: 16524, Misclassified: 3476
		                Accuracy: 0.8262
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX    831     24     15      0      0
		  VG    164   2321   1725      4      0
		  GD      0    585   8319    797      0
		  BD      0      0      8   4200    147
		  DN      0      0      0      7    853

		====================================================================

		 Classification completed in 0.672 seconds.

		 Total test dataset txns: 75000
		    Classified correctly: 61890, Misclassified: 13110
		                Accuracy: 0.8252
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX   3137     62     54      0      0
		  VG    595   8895   6391     15      0
		  GD     12   2280  30989   3063      0
		  BD      0      0     10  15714    595
		  DN      0      0      0     33   3155
		  
		*/
		
	}

	private void script_06_02() {

		NBCreditClassifier naiveBayes = new NBCreditClassifier(ds);

		naiveBayes.useDefaultAttributes();

		naiveBayes.train();


		//
		// Now, calculate error rate for test set
		//

		UserDataset testDS = UserLoader.loadTestDataset();

		CreditErrorEstimator nb_err = new CreditErrorEstimator(testDS, naiveBayes);
		       
		nb_err.run();


		/*


		==================================================================
		 Classification completed in 1.575 seconds.

		 Total test dataset txns: 20000
		    Classified correctly: 16520, Misclassified: 3480
		                Accuracy: 0.826
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX    828     24     18      0      0
		  VG    161   2149   1900      4      0
		  GD      1    418   8482    800      0
		  BD      0      0      0   4208    147
		  DN      0      0      0      7    853

		==================================================================
		 Classification completed in 20.622 seconds.

		 Total test dataset txns: 75000
		    Classified correctly: 61925, Misclassified: 13075
		                Accuracy: 0.8256666666666667
		___________________________________________________________

		                CONFUSION MATRIX
		___________________________________________________________

		         EX     VG     GD     BD     DN
		  EX   3160     39     54      0      0
		  VG    617   8131   7132     16      0
		  GD     12   1504  31756   3072      0
		  BD      0      0      1  15723    595
		  DN      0      0      0     33   3155

		*/
		
	}

	/**
	 * @param args
	 */
	private void script_06_01() {

		// --------------------------------------------------------
		// Credit Worthiness or Credit Score
		// --------------------------------------------------------

		UseCaseData useCaseData = new UseCaseData(200, 2000);

		// --------------------------------------------------------------------
		// DEFAULT NOISE LEVELS (defined in the class UserType)
		// --------------------------------------------------------------------
		// UserType.addNoiseLevel("EX",new Double[] {1.0d, 3.0d, 7.5d, 10.0d});
		// UserType.addNoiseLevel("VG",new Double[] {1.0d, 3.0d, 6.0d, 10.0d});
		// UserType.addNoiseLevel("GD",new Double[] {1.0d, 3.0d, 4.0d, 8.0d});
		// UserType.addNoiseLevel("BD",new Double[] {1.0d, 3.0d, 7.5d, 10.0d});
		// UserType.addNoiseLevel("DN",new Double[] {1.0d, 4.5d, 9.0d, 13.5d});
		// --------------------------------------------------------------------

		// CLEAN DATA
		UserType.addNoiseLevel("EX", new Double[] { 1.0d, 5.0d, 8.0d, 10.0d });
		UserType.addNoiseLevel("VG", new Double[] { 1.0d, 2.5d, 6.0d, 10.0d });
		UserType.addNoiseLevel("GD", new Double[] { 1.0d, 3.0d, 4.0d, 8.0d });
		UserType.addNoiseLevel("BD", new Double[] { 1.0d, 3.0d, 7.5d, 10.0d });
		UserType.addNoiseLevel("DN", new Double[] { 1.0d, 6.0d, 10.0d, 14.0d });

		// NOISY DATA
		UserType.addNoiseLevel("EX", new Double[] { 0.5d, 1.5d, 3.0d, 4.0d });
		UserType.addNoiseLevel("VG", new Double[] { 0.5d, 1.5d, 3.0d, 4.0d });
		UserType.addNoiseLevel("GD", new Double[] { 0.5d, 1.5d, 3.0d, 4.0d });
		UserType.addNoiseLevel("BD", new Double[] { 0.5d, 1.5d, 3.0d, 4.0d });
		UserType.addNoiseLevel("DN", new Double[] { 0.5d, 1.5d, 3.0d, 4.0d });

		useCaseData.create(true);

		ds = UserLoader.loadTrainingDataset();
	}

}
