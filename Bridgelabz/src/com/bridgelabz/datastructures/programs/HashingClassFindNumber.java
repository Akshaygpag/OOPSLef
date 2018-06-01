package com.bridgelabz.datastructures.programs;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import com.bridgelabz.algorithm.impl.OrderedListImpl;
import com.bridgelabz.algorithm.interfaces.OrderedList;
import com.bridgelabz.functionalprogramming.util.Utility;

/**
 * THIS CLASS IS USED TO CREATE HASHAND STORES THE VALUES AND PERFORMES THE
 * OPERATIONS ON THE LIST OF NUMBERS STORED
 * 
 * @author Akshay
 * @version 1.0.0
 * @since 24-May-2018
 */
public class HashingClassFindNumber {
    private static final String FILE_PATH = "/home/bridgeit/fileHolder/HashingList.txt";// GET FILE PATH

    /**
     * MAIN METHOD WILL CAUSE THE FETCHING THE FILE DATA AND THEN STORING THEM IN
     * THE RESPECTIVE AREA
     * 
     * @param args
     * 
     */
    public static void main(String[] args) {
	// THIS METHOD WILL READ NUMBERS FROM FILES AND TAKE USER VALUE
	@SuppressWarnings("unchecked")
	OrderedList<Integer>[] chain = new OrderedListImpl[11];// CREATE A ARRAY OF ORDERED LIST WITH CASTED TO
							       // INTERFACE
	for (int s = 0; s < chain.length; s++) {
	    chain[s] = new OrderedListImpl<Integer>();
	}
	File file = new File(FILE_PATH);// FETCH AND CREATE A OBJECT FOR THE FILE
	FileReader read = null;// FILEREADER
	if (!file.exists()) {
	    System.out.println("File resource could not be found Error :404");
	    return;
	}
	FileWriter fw = null;
	try {
	    read = new FileReader(file);
	    int c = -1;
	    StringBuffer sb = new StringBuffer();
	    try {
		while ((c = read.read()) != -1) {
		    sb.append((char) c);
		}
		System.out.println("File contents starts.......");
		System.out.println(sb.toString());
		System.out.println("File contents ends......");
		if (file.length() != 0) {
		    String[] data = sb.toString().split(",");
		    for (String insertData : data) {
			if (insertData.trim().length() > 0) {
			    Node<Integer> n = new Node<Integer>();
			    System.out.println(insertData.trim());
			    n.setInfo(Integer.parseInt(insertData.trim()));
			    Integer remainder = n.getInfo() % 11;
			    chain[remainder].add(n);
			}
		    }
		}
		System.out.println("Enter the number to be searched");
		Node<Integer> n = Utility.getNodeValue();
		Integer remainder = n.getInfo() % 11;
		if (chain[remainder].search(n)) {
		    System.out.println("key found ");
		    System.out.println("deleting.....");
		    chain[remainder].remove(n);
		    System.out.println("deletion completed .");
		} else {
		    chain[remainder].add(n);
		    System.out.println("Element is added.");
		}
		file.delete();
		file.createNewFile();
		fw = new FileWriter(file);
		StringBuffer listContents = new StringBuffer();
		System.out.println("--------------------");

		for (int i = 0; i < chain.length; i++) {

		    while (!chain[i].isEmpty()) {
			System.out.println("inside the loop");
			String dataListSingle = chain[i].pop().getInfo().toString();

			System.out.println("The chain size is : " + chain[i].size());
			listContents.append("," + dataListSingle);

		    }
		}

		if (listContents.toString().trim().length() == 0) {
		    fw.write("");
		} else {
		    fw.write(listContents.toString().substring(1, listContents.toString().length()));
		}
		fw.flush();
		System.out.println("Writing to file completed");
	    } catch (Exception e) {
		System.out.println("cannot do the file read and write operations " + e.getMessage());
		e.printStackTrace();
	    }
	} catch (Exception e) {
	    System.out.println("cannot do the file read and write operations " + e.getMessage());
	    e.printStackTrace();
	} finally {

	    try {
		fw.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	}

    }
}
