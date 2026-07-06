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
package org.yooreeka.util.parsing.kaspersky;

import org.yooreeka.util.P;
import org.yooreeka.util.parsing.common.DataField;
import org.yooreeka.util.parsing.common.DataType;
import org.yooreeka.util.parsing.csv.CSVDocument;
import org.yooreeka.util.parsing.csv.CSVEntry;
import org.yooreeka.util.parsing.csv.CSVFile;
import org.yooreeka.util.parsing.csv.CSVSchema;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * This is a parser for TXT files that have been exported from Kaspersky Password Manager (KPM)
 * These TXT files contain the information about the Websites and the Notes in KPM
 * The purpose of this class is to parse and transform that information into a CSV file
 * for import in other password management software such as 1Password
 */
public class KasperskyParser {

    private static final String _1PASSWORD = "1Password";

    private Path kasperskyFile;
    private String targetPasswordManager;

    private CSVDocument csvDocument = new CSVDocument();

    List<String> inputLines;

    private CSVFile csvFile;

    private boolean verbose = false;

    /**
     * @param fileName
     * @param targetPasswordManager
     */
    public KasperskyParser(String fileName, String targetPasswordManager) throws IOException {

        this.kasperskyFile = Paths.get(fileName);
        this.targetPasswordManager = targetPasswordManager;
    }

    public static void main(String[] args) throws IOException {
        KasperskyParser p = new KasperskyParser("C:\\Users\\micro\\OneDrive\\Documents\\Kaspersky__24-08-2024.txt",_1PASSWORD);
        p.run();
    }

    private void run() throws IOException {

        String fileName = kasperskyFile.getFileName().toString();

        if (!kasperskyFile.toFile().exists()) {
            P.error("File not found: " + fileName);
            throw new IllegalArgumentException(fileName);

        } else {
            String websitesCsvName = kasperskyFile.toFile().getCanonicalPath() + "_Websites.csv";
            String notesCsvName = kasperskyFile.toFile().getCanonicalPath() + "_Notes.csv";

            P.println("websitesCsvName: "+websitesCsvName);
            P.println("notesCsvName: "+notesCsvName);

            CSVSchema[] schemata = defineCSV(targetPasswordManager);

            P.println("Schemata defined");

            CSVFile fWebsites = new CSVFile(websitesCsvName, true, schemata[0]);
            fWebsites.setSeparator(",");

            // TODO: Later we can write code for the notes
            //       This is not implemented yet
            // CSVFile fNotes = new CSVFile(notesCsvName, true, schemata[1]);
            // fNotes.setSeparator(",");

            P.println("Start parsing ...");
            parse(kasperskyFile);
            P.println("End parsing ...");

            P.println("Write CSV to disk");

            fWebsites.setDoc(csvDocument);
            fWebsites.write();
        }
    }

    /**
     * @param targetPasswordManager
     * @return an array of CSVSchema with two entries. The first for the Websites and the second for the Notes.
     */
    private CSVSchema[] defineCSV(String targetPasswordManager) {

        CSVSchema[] s = new CSVSchema[2];

        if (targetPasswordManager.equalsIgnoreCase(_1PASSWORD)) {

            s[0] = define1PasswordWebsitesSchema();

            s[1] = define1PasswordNotesSchema();

        } else {
            P.error("Valid target schemata are: " + _1PASSWORD);
            throw new RuntimeException("Unsupported target schema: " + targetPasswordManager);
        }
        return s;
    }

    private CSVSchema define1PasswordWebsitesSchema() {
        CSVSchema s = new CSVSchema();

        // Website name
        DataField f1 = new DataField("Title", DataType.STRING);
        s.addField(f1);

        // Website URL
        DataField f2 = new DataField("Website", DataType.STRING);
        s.addField(f2);

        // Login
        DataField f3 = new DataField("Username", DataType.STRING);
        s.addField(f3);

        DataField f4 = new DataField("Password", DataType.STRING);
        s.addField(f4);

        // Comment
        DataField f5 = new DataField("Notes", DataType.STRING);
        s.addField(f5);

        // Login name
        DataField f6 = new DataField("Login Name", DataType.STRING);
        s.addField(f6);

        return s;
    }

    private CSVSchema define1PasswordNotesSchema() {

        CSVSchema s = new CSVSchema();

        // Note Title
        DataField f1 = new DataField("Title", DataType.STRING);
        s.addField(f1);

        // Note Text
        DataField f5 = new DataField("Notes", DataType.STRING);
        s.addField(f5);

        return s;
    }

    private void parse(Path kasperskyFile) {
        try {
            inputLines = Files.readAllLines(kasperskyFile);
        } catch (IOException e) {
            P.error("Exception occurred while reading the file: " + kasperskyFile);
            throw new RuntimeException(e);
        }

        // Keep track of where we are in the list
        int lineIndex = 0;

        int siteStartIndex, siteEndIndex;

        for (String line : inputLines) {

//            P.println("Processing line #: " + lineIndex);
//            P.println(line);
//
            if (line.startsWith("---")) {
                WebsiteInfo info = parseWebsite(lineIndex);

                CSVEntry csvEntry = new CSVEntry();
                String[] data = new String[6];
                data[0] = info.getWebsiteName();
                data[1] = info.getWebsiteUrl();
                data[2] = info.getLoginName();
                data[3] = info.getUsername();
                data[4] = info.getPassword();
                data[5] = info.getNotes();
                csvEntry.setData(data);

                P.println(csvEntry.toString());

                csvDocument.addEntry(csvEntry);
            }
            lineIndex++;
        }
        P.println("Parsing complete");
    }

    private WebsiteInfo parseWebsite(int lineIndex) {

        boolean hasMore = true;
        WebsiteInfo info = new WebsiteInfo();

        int i= lineIndex;
        StringBuffer buffer = new StringBuffer();

        while (hasMore) {

            if (i < inputLines.size()-1) {

                String line = inputLines.get(i + 1);

                if (line.startsWith("---"))
                    hasMore = false;
                else {
                    if (!line.trim().isEmpty()) {

                        int sepIndex = line.indexOf(':');

                        if (sepIndex != -1) {
                            String lineStart = line.substring(0, sepIndex);

                            switch (lineStart) {
                                case "Website name":
                                    info.setWebsiteName(line.substring(line.indexOf(':') + 1));
                                    break;

                                case "Website URL":
                                    info.setWebsiteUrl(line.substring(line.indexOf(':') + 1));
                                    break;

                                case "Login name":
                                    info.setLoginName(line.substring(line.indexOf(':') + 1));
                                    break;

                                case "Login":
                                    info.setUsername(line.substring(line.indexOf(':') + 1));
                                    break;

                                case "Password":
                                    String passwd = line.substring(line.indexOf(':') + 1);
                                    if (passwd.contains(",")) P.println("Password contains comma: "+lineIndex);
                                    info.setPassword(passwd);
                                    break;

                                case "Comment":
                                    buffer.append(line.substring(line.indexOf(':') + 1));
                                    //info.setNotes(line.substring(line.indexOf(':') + 1));
                                    break;

                                default:
                                    P.error("Invalid website line: " + line);
                                    hasMore = false;
                                    // throw new RuntimeException("Invalid website line: " + line);
                            }
                        } else {
                            // Empty line
                        }
                    } else {
                        buffer.append(line).append("  ");
                    }
                    i++;
                }
            } else
                hasMore = false;
        }
        info.setNotes(buffer.toString());
        return info;
    }

    class WebsiteInfo {

        String websiteName;
        String websiteUrl;
        String loginName;
        String username;
        String password;
        String notes;

        public WebsiteInfo(String websiteName,
                           String websiteUrl,
                           String loginName,
                           String username,
                           String password,
                           String notes) {

            this.websiteName = websiteName;
            this.websiteUrl = websiteUrl;
            this.loginName = loginName;
            this.username = username;
            this.password = password;
            this.notes = notes;
        }

        public WebsiteInfo() {
            // Empty constructor
        }

        public String getWebsiteName() {
            return websiteName;
        }

        public String getWebsiteUrl() {
            return websiteUrl;
        }

        public String getLoginName() {
            return loginName;
        }

        public String getUsername() {
            return username;
        }

        public String getPassword() {
            return password;
        }

        public String getNotes() {
            return notes;
        }

        public void setWebsiteName(String websiteName) {
            this.websiteName = websiteName;
        }

        public void setWebsiteUrl(String websiteUrl) {
            this.websiteUrl = websiteUrl;
        }

        public void setLoginName(String loginName) {
            this.loginName = loginName;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }
    }

}
