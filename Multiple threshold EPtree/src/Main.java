/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
//package fpgrowth;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class Main {

    //static int threshold = 2;
    //static String file = "census.dat";
    static String file = "ianchang.txt";
	//static String file = "test_db.txt";
    //static String threshold_file = "ianchang-3.txt";
	static String threshold_file = "test_threshold-2.txt";

    public static void main(String[] args) throws FileNotFoundException {
        long start = System.currentTimeMillis();
        new FPGrowth(new File(file), new File(threshold_file));
        System.out.println((System.currentTimeMillis() - start));
    }
}