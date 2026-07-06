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

package org.yooreeka.examples.banktrx;

import org.yooreeka.util.P;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.*;
import java.util.stream.Collectors;

public class TransactionAnalyzer {

    DateTimeFormatter dtf;

    public ArrayList<BankTransaction> transactions;
    public ArrayList<BankTransaction> credits;
    public ArrayList<BankTransaction> debits;

    public List<String> distinctDebitDescriptions;
    public Map<String, Double> debitTotals;
    public List<String> distinctCreditDescriptions;
    public Map<String, Double> creditTotals;

    public TransactionAnalyzer() {

        // These are array lists of bank transactions
        credits = new ArrayList<>();
        debits = new ArrayList<>();

        // The String in these maps are the categories of spend
        creditTotals = new HashMap<>();
        debitTotals = new HashMap<>();

        dtf = new DateTimeFormatterBuilder()
                .appendOptional(DateTimeFormatter.ofPattern("M/dd/yy"))
                .appendOptional(DateTimeFormatter.ofPattern("MM/dd/yy"))
                .appendOptional(DateTimeFormatter.ofPattern("M/d/yy"))
                .appendOptional(DateTimeFormatter.ofPattern("MM/d/yy"))
                .toFormatter();

    }

    Map<String, Double> sortByTotalAmount(Map<String, Double> map) {

        return map.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue())
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (e1, e2) -> e1,
                        LinkedHashMap::new));
    }
    double getTotalCredits() {
        double totalCredits = 0;
        for (BankTransaction t : credits) {
            totalCredits += t.getAmount();
        }
        return totalCredits;
    }

    double getTotalDebits() {
        double  totalDebits = 0;
        for (BankTransaction t : debits) {
            totalDebits += t.getAmount();
        }
        return totalDebits;
    }
}
