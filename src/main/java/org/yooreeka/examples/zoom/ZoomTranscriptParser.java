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
package org.yooreeka.examples.zoom;

import org.yooreeka.util.P;

import java.io.*;
import java.util.ArrayList;

/**
 * Zoom transcripts result in a file with extension ".vtt"
 * This class provides convenient methods that can help extract different data from
 * that file
 *
 */
public class ZoomTranscriptParser {

    private boolean isVerbose=true;

    private long recordsParsed = 0;

    private File file;
    private FileReader fReader;
    private BufferedReader bR;

    private ArrayList<ZoomVttEntry> records;

    public ZoomTranscriptParser(String fileName) throws FileNotFoundException {
        file = new File(fileName);
        fReader = new FileReader(file);
        bR = new BufferedReader(fReader);

        records = new ArrayList<>();
    }

    public void parse() throws IOException {
        long t0 = System.currentTimeMillis();

        StringBuilder msg = null;
        if (isVerbose())
            msg= new StringBuilder("\nProcessed ");

        recordsParsed = 0;

        boolean hasMoreLines = true;
        String line;

        while (hasMoreLines) {

            line = bR.readLine();

            if (line.trim().equalsIgnoreCase("WEBVTT")) {
                //Ignore this
            } else if (line.trim().isEmpty()) {
                // This condition acts as separator for entries
                ZoomVttEntry e = new ZoomVttEntry();
                String s = bR.readLine();
                if (s == null) {
                    hasMoreLines = false;
                    break;
                } else {
                    e.setRecordId(Integer.parseInt(s));
                }
                e.setTimestamp(bR.readLine());
                e.setComment(bR.readLine());
                records.add(e);
                recordsParsed++;
            } else {
                hasMoreLines = false;
            }
        }
        bR.close();

        if (isVerbose()) {
            msg.append(recordsParsed).append(" lines from file: ").append(this.file.getAbsolutePath());
            msg.append("\n in "+(System.currentTimeMillis()-t0)+"ms\n");
            P.println(msg.toString());
        }
    }

    public static void main(String[] args) {
        String fName = "C:\\Data\\Text2Parse.vtt";
        try {
            ZoomTranscriptParser p = new ZoomTranscriptParser(fName);
            p.parse();
            P.println(p.print());

            p.save("C:\\Data\\Text2Parse.txt");

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public void save(String s) throws FileNotFoundException {
        PrintWriter out = new PrintWriter(s);
        out.println(print());
        out.close();
    }

    public String print() {
        StringBuilder txt = new StringBuilder();
        boolean printTimestamp = true;
        int numberOfRecords = records.size();

        for (int i=0;i < numberOfRecords; i++) {
            ZoomVttEntry e = records.get(i);
            int t1 = e.getTimestamp().lastIndexOf("-->");
            int t2 = e.getComment().indexOf(":");
            String speaker = e.getComment().substring(0,t2);

            if (printTimestamp)
                txt.append("\n")
                        .append(e.getRecordId()+" | "+e.getTimestamp().substring(0,t1)+" | "+speaker)
                        .append("\n");

            txt.append(e.getComment().substring(t2+1)).append("\n");

            if ((i+1) < numberOfRecords) {
                ZoomVttEntry nextE = records.get(i + 1);
                int t3 = nextE.getComment().indexOf(":");
                String nextSpeaker = nextE.getComment().substring(0, t3);
                if (speaker.equalsIgnoreCase(nextSpeaker)) {
                    printTimestamp = false;
                } else {
                    printTimestamp = true;
                }
            }
        }

        return txt.toString();
    }

    public boolean isVerbose() {
        return isVerbose;
    }

    public void setVerbose(boolean verbose) {
        isVerbose = verbose;
    }
}
