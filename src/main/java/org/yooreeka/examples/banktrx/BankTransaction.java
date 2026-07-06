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

import java.time.LocalDate;

/**
 * This class models a general bank transaction so that all bank transactions can be modeled
 * with the same class.
 *
 * Here are some examples
 * Barclay's
 * Transaction Date,Description,Category,Amount
 *
 * Chase
 * Transaction Date,Post Date,Description,Category,Type,Amount,Memo
 *
 * Citizens
 * "Transaction Type","Date","Account Type","Description","Amount","Reference No.","Credits","Debits"
 *
 */
public class BankTransaction {
    private String bank;
    private String account;
    private String accountType;

    private java.time.LocalDate transactionDate;
    private java.time.LocalDate transactionPostDate;
    private String description;
    private String transactionType;
    private String category;
    private String memo;
    private String refNumber;

    // The transaction is a:
    //     credit when amount > 0
    //     debit  when amount < 0
    private double amount;

    public BankTransaction(String bank, LocalDate transactionDate, String description, double amount, String category) {
        this.bank = bank;
        this.transactionDate = transactionDate;
        this.description = description;
        this.amount = amount;
        this.category = category;
    }

    public BankTransaction(String bankName) {
        this.bank = bankName;
    }

    //
    // Getters & Setters
    //
    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public LocalDate getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(LocalDate transactionDate) {
        this.transactionDate = transactionDate;
    }

    public LocalDate getTransactionPostDate() {
        return transactionPostDate;
    }

    public void setTransactionPostDate(LocalDate transactionPostDate) {
        this.transactionPostDate = transactionPostDate;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getMemo() {
        return memo;
    }

    public void setMemo(String memo) {
        this.memo = memo;
    }

    public String getRefNumber() {
        return refNumber;
    }

    public void setRefNumber(String refNumber) {
        this.refNumber = refNumber;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String print() {
        StringBuffer sb = new StringBuffer();
        sb.append(bank).append(", ");
        sb.append(account).append(", ");
        sb.append(accountType).append(", ");
        sb.append(transactionDate).append(", ");
        // sb.append(transactionPostDate).append(", ");
        sb.append(description).append(", ");
        sb.append(transactionType).append(", ");
        sb.append(category).append(", ");
        // sb.append(memo).append(", ");
        sb.append(refNumber).append(", ");
        sb.append(amount);
        return sb.toString();
    }
}
