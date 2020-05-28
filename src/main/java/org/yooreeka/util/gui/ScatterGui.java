/*
 *   ________________________________________________________________________________________
 *   
 *   Y O O R E E K A
 *   A library for data mining, machine learning, soft computing, and mathematical analysis
 *   ________________________________________________________________________________________ 
 *    
 *   The Yooreeka project started with the code of the book "Algorithms of the Intelligent Web " 
 *   (Manning 2009). Although the term "Web" prevailed in the title, in essence, the algorithms 
 *   are valuable in any software application.
 *  
 *   Copyright (c) 2007-2009 Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-${year} Marmanis Group LLC and individual contributors as indicated by the @author tags.  
 * 
 *   Certain library functions depend on other Open Source software libraries, which are covered 
 *   by different license agreements. See the NOTICE file distributed with this work for additional 
 *   information regarding copyright ownership and licensing.
 * 
 *   Marmanis Group LLC licenses this file to You under the Apache License, Version 2.0 (the "License"); 
 *   you may not use this file except in compliance with the License.  
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software distributed under 
 *   the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, 
 *   either express or implied. See the License for the specific language governing permissions and
 *   limitations under the License.
 *   
 */
package org.yooreeka.util.gui;

import java.awt.event.WindowEvent;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;
import org.yooreeka.util.P;

/**
 * 
 * This is going to be a convenience class for doing basic XY plots. here is how
 * it would be used within the Bean Shell interpreter:
 * 
 * <pre> 
 *    bsh % double[] x = {1.0, 2.0, 3.0, 4.0, 5.0, 6.0, 7.0, 8.0, 9.0}; 
 *    bsh % double[] y = {1.0, 4.0, 9.0, 16.0, 20.0, 29.0, 35, 40., 42.0}; 
 *    bsh % gui = new org.yooreeka.util.gui.XyGui ("A plot",x,y); 
 *    bsh % gui.plot(); 
 * </pre>
 * 
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 * 
 */
public class ScatterGui extends ApplicationFrame {
	
	private int windowDimensionX = 700, windowDimensionY = 400;
	
	private String xLabel = "X";
	private String yLabel = "Y";

	/**
	 * 
	 */
	private static final long serialVersionUID = 6703945430655378237L;
	
	private StringBuilder errMsg;
	private int loopInt;

	public ScatterGui(String title) {

		super(title);
		errMsg = new StringBuilder();
	}

	/**
	 * @param title
	 *            chart title
	 * @param nameForData1
	 *            identifier for a data group/series
	 * @param nameForData2
	 *            identifier for a data group/series
	 * @param items
	 *            values/categories that correspond to data values
	 */
//	public ScatterGui(String title, String nameForData1, String nameForData2,
//			String[] items, double[] data1, double[] data2) {
//
//		super(title);
//	}

	public void plot(final JFreeChart chart) {
		plot(chart, getWindowDimensionX(),getWindowDimensionY());
	}
	
	public void plot(final JFreeChart chart, int wX, int wY) {

		ChartPanel chartPanel = new ChartPanel(chart);
		chartPanel.setPreferredSize(new java.awt.Dimension(wX, wY));
		setContentPane(chartPanel);					
		
		this.pack();
		RefineryUtilities.centerFrameOnScreen(this);
		this.setVisible(true);
	}

	public final JFreeChart createScatterPlot(double[] valX, double[] valY) {

		XYSeries xydata = null;
		
		if (checkX(valX) && checkY(valX.length, valY)) {

			xydata = new XYSeries(getTitle());

			for (int i = 0; i < loopInt; i++) {
				xydata.add(valX[i], valY[i]);
			}
		} else {
			P.println(errMsg.toString());
		}

		XYSeriesCollection xycollection = new XYSeriesCollection(xydata);
		
		//TODO: Externalize the hardcoded values later
		final JFreeChart chart = ChartFactory.createScatterPlot(getTitle()+" (Scatter Plot)", 
				getXLabel(), 
				getYLabel(), 
				xycollection,
				PlotOrientation.VERTICAL, 
				true, // does it have a legend?
				true, // does it have tooltips?
				false); // does it have URLs?

		return chart;
	}
	
	public JFreeChart createLineChart(String[] items, LineChartData data1, LineChartData data2) {
		DefaultCategoryDataset dataset = new DefaultCategoryDataset();
		for (int i = 0, n = items.length; i < n; i++) {
			dataset.addValue(data1.getData()[i], data1.getLabel(), items[i]);
			dataset.addValue(data2.getData()[i], data2.getLabel(), items[i]);
		}
		//TODO: Externalize the hardcoded values
		final JFreeChart chart = ChartFactory.createLineChart(getTitle(), 
				getXLabel(), 
				getYLabel(), 
				dataset,
				PlotOrientation.VERTICAL, 
				true, // does it have a legend?
				true, // does it have tooltips?
				false); // does it have URLs?
		
		return chart;
	}
	
	// ==================================================================================
	// VALIDATIONS, GETTERS & SETTERS
	// ==================================================================================
	
	private boolean checkX(double[] val) {

		boolean isOK = true;

		if (val == null || val.length <= 0) {

			errMsg.append("The array of data for the X-axis is null or does not contain data!");
			isOK = false;
		}

		setLoopInt(val.length);
		return isOK;
	}

	private boolean checkY(int n, double[] val) {

		boolean isOK = true;

		if (val == null || val.length <= 0) {
			errMsg.append("---------------------------------------------------------------------\n");
			errMsg.append("ERROR:\n");
			errMsg.append("The array of data for the Y-axis is null or does not contain data!");
			errMsg.append("---------------------------------------------------------------------\n");
			isOK = false;
		}

		if (val.length > n) {

			errMsg.append("---------------------------------------------------------------------\n");
			errMsg.append("WARNING: \n");
			errMsg.append("     The length of the array for the Y-axis data is greater than \n");
			errMsg.append(" the length of the array for the X-axis data. \n");
			errMsg.append(" Only the first " + n
					+ " points will be considered in the plot.");
			errMsg.append("---------------------------------------------------------------------\n");

		} else if (val.length < n) {

			errMsg.append("---------------------------------------------------------------------\n");
			errMsg.append("WARNING:\n");
			errMsg.append("     The length of the array for the Y-axis data is less than \n");
			errMsg.append(" the length of the array for the X-axis data. \n");
			errMsg.append(" Only the first " + n
					+ " points of the X-will be considered in the plot.");
			errMsg.append("---------------------------------------------------------------------\n");
			setLoopInt(val.length);
		}

		return isOK;
	}

	private void setLoopInt(int val) {
		loopInt = val;
	}

	/**
	 * Listens for the main window closing, and shuts down the application.
	 * 
	 * @param event
	 *            information about the window event.
	 */
	@Override
	public void windowClosing(WindowEvent event) {
		if (event.getWindow() == this) {
			dispose();

			// Overriding the ApplicationFrame behavior
			// Do not shutdown the JVM
			// System.exit(0);
			// -----------------------------------------
		}
	}

	/**
	 * @return the windowDimensionX
	 */
	public int getWindowDimensionX() {
		return windowDimensionX;
	}

	/**
	 * @param windowDimensionX the windowDimensionX to set
	 */
	public void setWindowDimensionX(int windowDimensionX) {
		this.windowDimensionX = windowDimensionX;
	}

	/**
	 * @return the windowDimensionY
	 */
	public int getWindowDimensionY() {
		return windowDimensionY;
	}

	/**
	 * @param windowDimensionY the windowDimensionY to set
	 */
	public void setWindowDimensionY(int windowDimensionY) {
		this.windowDimensionY = windowDimensionY;
	}

	/**
	 * @return the xLabel
	 */
	public String getXLabel() {
		return xLabel;
	}

	/**
	 * @param xLabel the xLabel to set
	 */
	public void setxLabel(String xLabel) {
		this.xLabel = xLabel;
	}

	/**
	 * @return the yLabel
	 */
	public String getYLabel() {
		return yLabel;
	}

	/**
	 * @param yLabel the yLabel to set
	 */
	public void setyLabel(String yLabel) {
		this.yLabel = yLabel;
	}

}
