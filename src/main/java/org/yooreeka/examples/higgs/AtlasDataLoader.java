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
 *   Copyright (c) 2007-2009    Haralambos Marmanis & Dmitry Babenko
 *   Copyright (c) 2009-2014 Marmanis Group LLC and individual contributors as indicated by the @author tags.  
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
package org.yooreeka.examples.higgs;

import java.io.IOException;

import org.yooreeka.util.C;
import org.yooreeka.util.parsing.common.DataField;
import org.yooreeka.util.parsing.common.DataType;
import org.yooreeka.util.parsing.csv.CSVFile;
import org.yooreeka.util.parsing.csv.CSVSchema;

/**
 * @author <a href="mailto:babis@marmanis.com">Babis Marmanis</a>
 *
 */
public class AtlasDataLoader {

	private CSVFile f;
	
	public AtlasDataLoader(String dataFileName) throws IOException {
	
		CSVSchema s = new CSVSchema();

		DataField f1 = new DataField("EventId", DataType.LONG);
		s.addField(f1,true);
		
		DataField f2 = new DataField("DER_mass_MMC", DataType.DOUBLE);
		s.addField(f2);

		DataField f3 = new DataField("DER_mass_transverse_met_lep", DataType.DOUBLE);
		s.addField(f3);

		DataField f4 = new DataField("DER_mass_vis", DataType.DOUBLE);
		s.addField(f4);

		DataField f5 = new DataField("DER_pt_h", DataType.DOUBLE);
		s.addField(f5);

		DataField f6 = new DataField("DER_deltaeta_jet_jet", DataType.DOUBLE);
		s.addField(f6);

		DataField f7 = new DataField("DER_mass_jet_jet", DataType.DOUBLE);
		s.addField(f7);

		DataField f8 = new DataField("DER_prodeta_jet_jet", DataType.DOUBLE);
		s.addField(f8);

		DataField f9 = new DataField("DER_deltar_tau_lep", DataType.DOUBLE);
		s.addField(f9);

		DataField f10 = new DataField("DER_pt_tot", DataType.DOUBLE);
		s.addField(f10);

		DataField f11 = new DataField("DER_sum_pt", DataType.DOUBLE);
		s.addField(f11);

		DataField f12 = new DataField("DER_pt_ratio_lep_tau", DataType.DOUBLE);
		s.addField(f12);

		DataField f13 = new DataField("DER_met_phi_centrality", DataType.DOUBLE);
		s.addField(f13);

		DataField f14 = new DataField("DER_lep_eta_centrality", DataType.DOUBLE);
		s.addField(f14);

		DataField f15 = new DataField("PRI_tau_pt", DataType.DOUBLE);
		s.addField(f15);

		DataField f16 = new DataField("PRI_tau_eta", DataType.DOUBLE);
		s.addField(f16);

		DataField f17 = new DataField("PRI_tau_phi", DataType.DOUBLE);
		s.addField(f17);

		DataField f18 = new DataField("PRI_lep_pt", DataType.DOUBLE);
		s.addField(f18);

		DataField f19 = new DataField("PRI_lep_eta", DataType.DOUBLE);
		s.addField(f19);

		DataField f20 = new DataField("PRI_lep_phi", DataType.DOUBLE);
		s.addField(f20);

		DataField f21 = new DataField("PRI_met", DataType.DOUBLE);
		s.addField(f21);

		DataField f22 = new DataField("PRI_met_phi", DataType.DOUBLE);
		s.addField(f22);

		DataField f23 = new DataField("PRI_met_sumet", DataType.DOUBLE);
		s.addField(f23);

		DataField f24 = new DataField("PRI_jet_num", DataType.DOUBLE);
		s.addField(f24);

		DataField f25 = new DataField("PRI_jet_leading_pt", DataType.DOUBLE);
		s.addField(f25);

		DataField f26 = new DataField("PRI_jet_leading_eta", DataType.DOUBLE);
		s.addField(f26);

		DataField f27 = new DataField("PRI_jet_leading_phi", DataType.DOUBLE);
		s.addField(f27);

		DataField f28 = new DataField("PRI_jet_subleading_pt", DataType.DOUBLE);
		s.addField(f28);

		DataField f29 = new DataField("PRI_jet_subleading_eta", DataType.DOUBLE);
		s.addField(f29);

		DataField f30 = new DataField("PRI_jet_subleading_phi", DataType.DOUBLE);
		s.addField(f30);

		DataField f31 = new DataField("PRI_jet_all_pt", DataType.DOUBLE);
		s.addField(f31);
		
		DataField f32 = new DataField("Weight", DataType.DOUBLE);
		s.addField(f32);

		DataField f33 = new DataField("Label", DataType.STRING);
		s.addField(f33);

		f = new CSVFile(dataFileName, true, s);
		f.setSeparator(C.COMMA);
		f.isVerbose(true);
		f.read();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			AtlasDataLoader loader = new AtlasDataLoader("/home/babis/code/HiggsBoson/data/training.csv");
			loader.getF().printHeaders();
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * @return the f
	 */
	public CSVFile getF() {
		return f;
	}

}
