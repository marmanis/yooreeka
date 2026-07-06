package org.yooreeka.test;

import org.yooreeka.config.YooreekaConfigurator;
import org.yooreeka.algos.clustering.model.DataPoint;
import org.yooreeka.algos.clustering.model.Dendrogram;
import org.yooreeka.algos.clustering.test.SFData;
import org.yooreeka.algos.clustering.test.SFDataset;
import org.yooreeka.algos.clustering.utils.SortedArrayClustering;
import org.yooreeka.algos.clustering.hierarchical.SingleLinkAlgorithm;
import org.yooreeka.algos.clustering.hierarchical.MSTSingleLinkAlgorithm;
import org.yooreeka.algos.clustering.hierarchical.AverageLinkAlgorithm;
import org.yooreeka.algos.clustering.partitional.KMeansAlgorithm;
import org.yooreeka.algos.clustering.test.MyDiggSpaceData;
import org.yooreeka.algos.clustering.test.MyDiggSpaceDataset;
import org.yooreeka.util.metrics.CosineDistance;
import org.yooreeka.algos.clustering.dbscan.DBSCANAlgorithm;
import org.yooreeka.algos.clustering.rock.ROCKAlgorithm;
import org.yooreeka.util.P;

public class Chapter_04 {

	private long t = 0;

	public Chapter_04() {
	}

	public void run() throws Exception {
		t = System.currentTimeMillis();

		// Sorted Array Clustering
		script_04_01();

		// Average Link (Hierarchical)
		script_04_02();

		// Partitional (KMeans)
		script_04_03();

		// DBSCAN
		script_04_04_DBSCAN();

		// ROCK
		script_04_04_Rock();

		P.timePassedSince(t);
	}

	public void script_04_01() {
		SFDataset ds = SFData.createDataset();
		SortedArrayClustering.cluster(ds.getData());
	}

	public void script_04_02() {
		SFDataset ds = SFData.createDataset();
		DataPoint[] dps = ds.getData();
		double[][] adjMatrix = ds.getAdjacencyMatrix();

		SingleLinkAlgorithm sla = new SingleLinkAlgorithm(dps, adjMatrix);
		Dendrogram dendroSLA = sla.cluster();
		dendroSLA.print(4);

		MSTSingleLinkAlgorithm sla2 = new MSTSingleLinkAlgorithm(dps, adjMatrix);
		Dendrogram dendroSLA2 = sla2.cluster();
		dendroSLA2.print(4);

		AverageLinkAlgorithm ala = new AverageLinkAlgorithm(dps, adjMatrix);
		Dendrogram dendroALA = ala.cluster();
		dendroALA.print(4);
	}

	public void script_04_03() {
		SFDataset ds = SFData.createDataset();
		DataPoint[] dps = ds.getData();

		KMeansAlgorithm kMeans = new KMeansAlgorithm(8, dps);
		kMeans.cluster();
		kMeans.print();
	}

	public void script_04_04_DBSCAN() {
		MyDiggSpaceDataset ds = MyDiggSpaceData.createDataset(15);
		DataPoint[] dps = ds.getData();

		CosineDistance cosD = new CosineDistance();
		DBSCANAlgorithm dbscan = new DBSCANAlgorithm(dps, cosD, 0.8, 2, true);
		dbscan.printResults(dbscan.cluster());
	}

	public void script_04_04_Rock() {
		MyDiggSpaceDataset ds = MyDiggSpaceData.createDataset(15);
		DataPoint[] dps = ds.getData();

		ROCKAlgorithm rock = new ROCKAlgorithm(dps, 5, 0.2);
		Dendrogram dnd = rock.cluster();
		dnd.print(21);
	}
}
