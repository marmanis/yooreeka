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

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import org.jgraph.JGraph;
import org.jgraph.graph.DefaultCellViewFactory;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphLayoutCache;
import org.jgraph.graph.GraphModel;

public class GraphGui {

	private double nodeWidth = 500;
	private double nodeHeight = 20;

	private JGraph graph = null;

	private Map<String, DefaultGraphCell> nodeCells = new HashMap<String, DefaultGraphCell>();
	private List<DefaultGraphCell> edgeCells = new ArrayList<DefaultGraphCell>();

	public GraphGui() {
		createGraph();
	}

	public void addEdge(String sourceNodeName, String targetNodeName) {
		DefaultGraphCell sourceNodeCell = getNodeForEdge(sourceNodeName);
		DefaultGraphCell targetNodeCell = getNodeForEdge(targetNodeName);
		DefaultGraphCell edgeCell = createEdge(sourceNodeCell, targetNodeCell);
		edgeCells.add(edgeCell);
	}

	public void addNode(String name, String extraText, double x, double y) {
		String nodeLabel = name;
		if (extraText != null) {
			nodeLabel += " (" + extraText + ")";
		}
		DefaultGraphCell nodeCell = createCell(nodeLabel, x, y);
		nodeCells.put(name, nodeCell);
	}

	private DefaultGraphCell createCell(String name, double x, double y) {
		DefaultGraphCell cell = new DefaultGraphCell(name);
		GraphConstants.setBounds(cell.getAttributes(), new Rectangle2D.Double(
				x, y, nodeWidth, nodeHeight));
		GraphConstants.setBorder(cell.getAttributes(),
				BorderFactory.createRaisedBevelBorder());
		GraphConstants.setOpaque(cell.getAttributes(), true);
		GraphConstants.setGradientColor(cell.getAttributes(), Color.orange);
		cell.addPort(new Point2D.Double(0, 0));
		return cell;
	}

	private DefaultGraphCell createEdge(DefaultGraphCell source,
			DefaultGraphCell target) {
		DefaultEdge edge = new DefaultEdge();
		source.addPort();
		edge.setSource(source.getChildAt(source.getChildCount() - 1));
		target.addPort();
		edge.setTarget(target.getChildAt(target.getChildCount() - 1));
		GraphConstants.setLabelAlongEdge(edge.getAttributes(), true);
		GraphConstants.setLineEnd(edge.getAttributes(),
				GraphConstants.ARROW_CLASSIC);
		// GraphConstants.setRouting(edge.getAttributes(),
		// GraphConstants.ROUTING_DEFAULT);
		// GraphConstants.setRouting(edge.getAttributes(),
		// GraphConstants.ROUTING_SIMPLE);
		return edge;
	}

	private void createGraph() {
		GraphModel model = new DefaultGraphModel();
		GraphLayoutCache view = new GraphLayoutCache(model,
				new DefaultCellViewFactory());
		graph = new JGraph(model, view);
	}

	private DefaultGraphCell getNodeForEdge(String nodeName) {
		DefaultGraphCell nodeCell = nodeCells.get(nodeName);
		if (nodeCell == null) {
			throw new RuntimeException("Node doesn't exist " + "(nodeName="
					+ nodeName + ").");
		}
		return nodeCell;
	}

	private void insertAllCells() {
		List<DefaultGraphCell> allCells = new ArrayList<DefaultGraphCell>();
		allCells.addAll(nodeCells.values());
		allCells.addAll(edgeCells);

		DefaultGraphCell[] cells = allCells
				.toArray(new DefaultGraphCell[nodeCells.size()]);

		graph.getGraphLayoutCache().insert(cells);
		graph.setEditable(false);
	}

	public void showGraph() {
		insertAllCells();
		JFrame frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(graph));
		// frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
