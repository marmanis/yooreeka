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
package org.yooreeka.util.parsing.msword;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import org.apache.poi.hpsf.DocumentSummaryInformation;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.model.DocumentProperties;
import org.apache.poi.hwpf.usermodel.HeaderStories;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.AbstractDocument;
import org.yooreeka.util.parsing.common.DataEntry;
import org.yooreeka.util.parsing.common.DocumentParser;
import org.yooreeka.util.parsing.common.DocumentParserException;
import org.yooreeka.util.parsing.common.ProcessedDocument;

public class MSWordDocumentParser implements DocumentParser {

	ProcessedDocument wordDoc = new ProcessedDocument();

	@Override
	public DataEntry getDataEntry(int i) {
		// TODO Auto-generated method stub
		return null;
	}

	public ProcessedDocument parse(AbstractDocument doc)
			throws DocumentParserException {

		poiReadDocument(stripProtocolFromURL(doc.getDocumentURL()));
		
		wordDoc.setDocumentType(ProcessedDocument.TYPE_MSWORD);
		wordDoc.setDocumentId(doc.getDocumentId());
		wordDoc.setDocumentURL(doc.getDocumentURL());
		
		return wordDoc;
	}
	    
	public HWPFDocument poiReadDocument(String fileName){
		
		POIFSFileSystem fs = null;
        HWPFDocument hwpfDoc = null;
        try {
            fs = new POIFSFileSystem(new FileInputStream(fileName));
            hwpfDoc = new HWPFDocument(fs);
 
            /** Read the content **/
            
            String text = hwpfDoc.getDocumentText();
            
    		wordDoc.setDocumentTitle(getTitle(text));
            wordDoc.setText(text);
    		wordDoc.setContent(text);
    		
//    		P.hline();
//    		P.println(getTitle(text));
//    		printProperties(hwpfDoc.getDocProperties());
    		
            // readParagraphs(hwpfDoc);
  
        } catch (Exception e) {
            e.printStackTrace();
        }
        return hwpfDoc;
    }  
 
    /**
     * Pretty print of all the metadata
     * 
	 * @param docProperties
	 */
	public void printProperties(DocumentProperties dp) {
		P.hline();
		P.println("Adt          : "+dp.getAdt());
		P.println("CCh          : "+dp.getCCh());
		P.println("CChFtnEdn    : "+dp.getCChFtnEdn());
		P.println("CChWS        : "+dp.getCChWS());
		P.println("CChWSFtnEdn  : "+dp.getCChWSFtnEdn());
		P.println("CConsexHypLim: "+dp.getCConsexHypLim());
		P.println("CDBC         : "+dp.getCDBC());
		P.println("CDBCFtnEdn   : "+dp.getCDBCFtnEdn());
		P.println("CLines       : "+dp.getCLines());
		P.println("CLinesFtnEdn : "+dp.getCLinesFtnEdn());
		P.println("CParas       : "+dp.getCParas());
		P.println("CParasFtnEdn : "+dp.getCParasFtnEdn());
		P.println("CPg          : "+dp.getCPg());
		P.println("CPgFtnEdn    : "+dp.getCPgFtnEdn());
		P.println("CWords       : "+dp.getCWords());
		P.println("CWordsFtnEnd : "+dp.getCWordsFtnEnd());
		P.println("DttmCreated: "+dp.getDttmCreated());
		P.println("DttmLastPrint: "+dp.getDttmLastPrint());
		P.println("DttmRevised: "+dp.getDttmRevised());
		P.println("Doptypography: "+dp.getDoptypography());
		P.println("Edn: "+dp.getEdn());
		P.println("Edn1: "+dp.getEdn1());
		P.println("Epc: "+dp.getEpc());
		P.println("FootnoteInfo: "+dp.getFootnoteInfo());
		P.println("FormatFlags: "+dp.getFormatFlags());
		P.println("FOutlineDirtySave: "+dp.getFOutlineDirtySave());
		P.println("Fpc: "+dp.getFpc());
		P.println("GrfDocEvents: "+dp.getGrfDocEvents());
		P.println("GrfSupression: "+dp.getGrfSupression());
		P.println("HpsZoonFontPag: "+dp.getHpsZoonFontPag());
		P.println("KeyVirusSession30: "+dp.getKeyVirusSession30());
		P.println("LKeyProtDoc: "+dp.getLKeyProtDoc());
		P.println("Lvl: "+dp.getLvl());
		P.println("NEdn: "+dp.getNEdn());
		P.println("NfcEdnRef: "+dp.getNfcEdnRef());
		P.println("NfcEdnRef1: "+dp.getNfcEdnRef1());
		P.println("NfcFtnRef: "+dp.getNfcFtnRef());
		P.println("NfcFtnRef1: "+dp.getNfcFtnRef1());
		P.println("NFtn: "+dp.getNFtn());
		P.println("NRevision: "+dp.getNRevision());
		P.println("Reserved: "+dp.getReserved());
		P.println("Reserved1: "+dp.getReserved1());
		P.println("Reserved2: "+dp.getReserved2());
		P.println("RncEdn: "+dp.getRncEdn());
		P.println("RncFtn: "+dp.getRncFtn());
		P.println("TmEdited: "+dp.getTmEdited());
		P.println("Unused2: "+dp.getUnused2());
		P.println("Unused5: "+dp.getUnused5());
		P.println("View: "+dp.getView());
		P.println("Virusinfo: "+dp.getVirusinfo());
		P.println("WScaleSaved: "+dp.getWScaleSaved());
		P.println("WSpare: "+dp.getWSpare());
		P.println("WSpare2: "+dp.getWSpare2());
		P.println("WvkSaved: "+dp.getWvkSaved());
		P.println("ZkSaved: "+dp.getZkSaved());
		P.println("Asumyi: "+dp.getAsumyi());
		P.println("Spare: "+dp.getSpare());
		
	}

	public String[] readParagraphs(HWPFDocument doc) throws Exception {
    	
        WordExtractor we = new WordExtractor(doc);
 
        /**Get the total number of paragraphs**/
        String[] paragraphs = we.getParagraphText();

//        P.println("Total Paragraphs: "+paragraphs.length);
// 
//        for (int i = 0; i < paragraphs.length; i++) {
// 
//            P.println("Length of paragraph "+(i +1)+": "+ paragraphs[i].length());
//            P.println(paragraphs[i].toString());
// 
//        }
//        
        we.close();
        return paragraphs;
    }
 
    public String readHeader(HWPFDocument doc, int pageNumber) {
    	
        HeaderStories headerStore = new HeaderStories( doc);
        String header = headerStore.getHeader(pageNumber);
        //P.println("Header Is: "+header);
        return header;
    }
 
    public String readFooter(HWPFDocument doc, int pageNumber){
        
    	HeaderStories headerStore = new HeaderStories( doc);
        String footer = headerStore.getFooter(pageNumber);
        //P.println("Footer Is: "+footer);
        return footer;
    }
 
    public String[] readDocumentSummary(HWPFDocument doc) {
        
    	String[] summary = new String[5];
    	
    	DocumentSummaryInformation summaryInfo=doc.getDocumentSummaryInformation();
    	
        summary[0] = summaryInfo.getCategory();
        summary[1] = summaryInfo.getCompany();
        summary[2] = Integer.toString(summaryInfo.getLineCount());
        summary[3] = Integer.toString(summaryInfo.getSectionCount());
        summary[4] = Integer.toString(summaryInfo.getSlideCount());
 
//        P.hline();
//        P.println("Category: "+category);
//        P.println("Company: "+company);
//        P.println("Line Count: "+lineCount);
//        P.println("Section Count: "+sectionCount);
//        P.println("Slide Count: "+slideCount);
        return summary;
    }
    
    private String stripProtocolFromURL(String url) {
		String docURL= null;
		if (url.startsWith("file:")) {
			docURL = url.substring(6);
		}
		return docURL;
    }
    
	/*
	 * Finds the first non-empty line in the document.
	 */
	private String getTitle(String text) throws IOException {
		if (text == null) {
			return null;
		}
		String title = "";

		StringReader sr = new StringReader(text);
		BufferedReader r = new BufferedReader(sr);
		String line = null;
		while ((line = r.readLine()) != null) {
			if (line.trim().length() > 0) {
				title = line.trim();
				break;
			}
		}

		return title;
	}
}
