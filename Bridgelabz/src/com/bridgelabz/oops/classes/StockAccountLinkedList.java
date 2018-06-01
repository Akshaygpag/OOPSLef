package com.bridgelabz.oops.classes;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import com.bridgelabz.algorithm.impl.OrderedListImpl;
import com.bridgelabz.algorithm.impl.QueueImpl;
import com.bridgelabz.algorithm.impl.StackPureImpl;
import com.bridgelabz.algorithm.interfaces.OrderedList;
import com.bridgelabz.algorithm.interfaces.Queue;
import com.bridgelabz.algorithm.interfaces.Stack;
import com.bridgelabz.datastructures.programs.Node;
import com.bridgelabz.functionalprogramming.util.Utility;
import com.bridgelabz.oops.interfaces.StockType;

/**
 * @author Akshay
 * @version 1.0.0
 * @since 31-May-2018
 */
public class StockAccountLinkedList implements StockType {
    private String fileName;
    OrderedList<CompanyShares> myShares;
    OrderedList<CompanyShares> allShares;
    Stack<String> symbolTransactions=new StackPureImpl<String>();
    Queue<Date> transactionDates=new QueueImpl<Date>();

    public StockAccountLinkedList(String fileName) throws IOException, ParseException {
	this.fileName = fileName;
	this.getAllShares();
	this.getMyShares();
    }

    public OrderedList<CompanyShares> getMyShares() {
	if (this.myShares == null) {
	    System.out.println("creating  my new list");

	    this.myShares = new OrderedListImpl<CompanyShares>().list();

	}
	return myShares;
    }

    public OrderedList<CompanyShares> getAllShares() throws IOException, ParseException {
	if (allShares == null) {
	    File file = new File(fileName);
	    FileReader reader = new FileReader(file);
	    JSONParser parser = new JSONParser();
	    List<JSONObject> fileStockList = (List<JSONObject>) parser.parse(reader);
	    OrderedList<CompanyShares> allShare = new OrderedListImpl<CompanyShares>();
	    AtomicInteger index = new AtomicInteger(1);
	    String[] data = new String[5];
	    data[0] = "ID";
	    data[1] = "STOCK NAME";
	    data[2] = "LAST TRANSACTION";
	    data[3] = "AVAILABLE";
	    data[4] = "STOCK PRICE";
	    System.out.println(String.format("%-2s  %-20s  %-40s  %-10s  %-10s", data));
	    for (JSONObject jsonObject : fileStockList) {
		CompanyShares stock = new CompanyShares();
		stock.setId(index.getAndIncrement());
		stock.setStockName(jsonObject.get("StockName").toString());
		stock.setStockPrice(Double.parseDouble(jsonObject.get("Price").toString()));
		stock.setNoOfStocks(Integer.parseInt(jsonObject.get("Available").toString()));
		System.out.println(stock);
		Node<CompanyShares> node = new Node<CompanyShares>();
		node.setInfo(stock);
		allShare.add(node);

	    }

	    this.allShares = allShare;
	}
	return allShares;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#StockAccount(java.lang.String)
     */
    @Override
    public StockType StockAccount(String name) {

	try {

	    return new StockAccountLinkedList(name);

	} catch (Exception e) {

	    System.out.println("error " + e.getMessage());
	}
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#valueOf()
     */
    @Override
    public double valueOf() {
	// TOTAL VALUATION OF THE STOCKS PURCHASED
	double valuation = 0.0;

	for (int i = 0; i < this.getMyShares().size(); i++) {

	    CompanyShares company = this.getMyShares().get(i).getInfo();

	    valuation += (company.getStockPrice()) * (company.getNoOfStocks());
	}
	return valuation;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#save(java.lang.String)
     */
    final String PATH = "/home/bridgeit/";

    @Override
    public void save(String fileName) throws IOException {
	// SAVE THE ALLSHARES INTO THE FILE
	if (fileName == null) {
	    fileName = this.fileName;
	}
	File f = new File(PATH + fileName);

	if (f.exists()) {
	    f.delete();
	}
	f.createNewFile();

	FileWriter fileWriter = new FileWriter(f);

	BufferedWriter writer = new BufferedWriter(fileWriter);
	while (!this.getMyShares().isEmpty()) {
	    writer.write(this.myShares.pop(0).getInfo().toString());
	    writer.newLine();
	    writer.flush();

	}
	writer.close();
	fileWriter.close();
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#printReport()
     */
    @Override
    public void printReport() {
	// PRINTS THE FORMATTED REPORT
	AtomicInteger index = new AtomicInteger(1);

	for (int i = 0; i < this.getMyShares().size(); i++) {
	    CompanyShares shares = null;

	    CompanyShares company = this.getMyShares().get(i).getInfo();
	    try {
		shares = (CompanyShares) company.clone();
	    } catch (CloneNotSupportedException e) {
		e.printStackTrace();
	    }
	    shares.setId(index.getAndIncrement());
	    System.out.println(shares);
	}

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#buy(int, java.lang.String)
     */
    @Override
    public void buy(int amount, String symbol) throws IOException, ParseException, CloneNotSupportedException {
	// BUY THE STOCKS
	// CHECK IF THE STOCKS ARE AVILABLE TO BUY
	CompanyShares selectedShare = null;
	for (int i = 0; i < this.getAllShares().size(); i++) {

	    CompanyShares shares = this.getAllShares().get(i).getInfo();

	    if (shares.getStockName().equalsIgnoreCase(symbol)) {
		selectedShare = shares;
		break;
	    }
	}
	if (selectedShare != null) {
	    // FOUND BUY THEM
	    if (selectedShare.getNoOfStocks() < amount) {
		System.out.println(
			"Enter a number lesser or equal to " + selectedShare.getNoOfStocks() + " to buy this stock");
		amount = Utility.getIntergerValue();
		if (amount <= 0) {
		    return;
		}
		this.buy(amount, symbol);
		return;
	    }
	    CompanyShares myShare = null;
	    // ENTERED CORRECT DATA TO BUY
	    for (int i = 0; i < this.getMyShares().size(); i++) {
		CompanyShares shares = this.getMyShares().get(i).getInfo();
		if (shares.getStockName().equalsIgnoreCase(symbol)) {

		    System.out.println("Found your element " + shares);
		    myShare = shares;
		    break;
		}
	    }
	    Date transaction = new Date();
	    Node<String> companySymbol = new Node<String>();
	    companySymbol.setInfo(symbol);
	    Node<Date> transactionTimes = new Node<Date>();
	    transactionTimes.setInfo(transaction);
	    if (myShare != null) {
		System.out.println("Updating your stocks ..");

		this.symbolTransactions.push(companySymbol);
		this.transactionDates.enqueue(transactionTimes);
		myShare.setNoOfStocks(myShare.getNoOfStocks() + amount);
		myShare.setTransaction(transaction);
		return;
	    }

	    System.out.println("You dont have these stocks ");
	    this.symbolTransactions.push(companySymbol);
	    this.transactionDates.enqueue(transactionTimes);
	    Node<CompanyShares> newShares = new Node<CompanyShares>();
	    CompanyShares newSharesAdding = (CompanyShares) selectedShare.clone();
	    newSharesAdding.setNoOfStocks(amount);
	    newSharesAdding.setTransaction(transaction);
	    newShares.setInfo(newSharesAdding);
	    this.getMyShares().add(newShares);
	    // FINALLY UPDATE MASTER CHARTS
	    selectedShare.setNoOfStocks(selectedShare.getNoOfStocks() - amount);
	    selectedShare.setTransaction(transaction);
	    return;
	}

	System.out.println("Invalid name entered...");
	return;
	// NOT FOUND RETURN

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#sell(int, java.lang.String)
     */
    @Override
    public void sell(int amount, String symbol) throws IOException, ParseException, CloneNotSupportedException {
	CompanyShares selectedShare = null;
	for (int i = 0; i < this.getMyShares().size(); i++) {

	    CompanyShares shares = this.getMyShares().get(i).getInfo();

	    if (shares.getStockName().equalsIgnoreCase(symbol)) {
		selectedShare = shares;
		break;
	    }
	}

	// INVALID SYMBOL
	if (selectedShare == null) {

	    System.out.println("Invalid symbol entered ...");

	    return;

	}

	// IF ELEMENT IS FOUND

	if (selectedShare.getNoOfStocks() < amount) {
	    System.out.println("Enter lesser or equivalent to " + selectedShare.getNoOfStocks());
	    int userInput = Utility.getIntergerValue();
	    if (userInput <= 0) {
		return;
	    }
	    sell(userInput, symbol);
	    return;
	}
	Date transaction = new Date();
	Node<String> companySymbol = new Node<String>();
	companySymbol.setInfo(symbol);
	Node<Date> transactionTimes = new Node<Date>();
	transactionTimes.setInfo(transaction);
	selectedShare.setNoOfStocks(selectedShare.getNoOfStocks() - amount);
	selectedShare.setTransaction(transaction);

	CompanyShares sellShare = null;
	for (int i = 0; i < this.getAllShares().size(); i++) {

	    CompanyShares shares = this.getAllShares().get(i).getInfo();

	    if (shares.getStockName().equalsIgnoreCase(symbol)) {
		sellShare = shares;
		break;
	    }
	}
	this.symbolTransactions.push(companySymbol);
	this.transactionDates.enqueue(transactionTimes);
	sellShare.setNoOfStocks(sellShare.getNoOfStocks() + amount);
	sellShare.setTransaction(transaction);
	return;

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#printStack()
     */
    @Override
    public void printStack() {
	System.out.println("The symbol transcation descending order");
	AtomicInteger index = new AtomicInteger(1);
	while (!this.symbolTransactions.isEmpty()) {

	    System.out.println(index.getAndIncrement() + " " + this.symbolTransactions.pop().getInfo());

	}
	System.out.println("----------END--------------");

    }

    /*
     * (non-Javadoc)
     * 
     * @see com.bridgelabz.oops.interfaces.StockType#printQueue()
     */
    @Override
    public void printQueue() {
	System.out.println("The transaction history is ");
	AtomicInteger index = new AtomicInteger(1);

	while (!this.transactionDates.isEmpty()) {

	    System.out.println(index.getAndIncrement() + " " + this.transactionDates.dequeue().getInfo());

	}

	System.out.println("----------END--------------");

    }

}
