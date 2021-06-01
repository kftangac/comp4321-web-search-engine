package Manage_Rocksdb.Manage_Rocksdb;
import java.io.*;
import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;  
import org.rocksdb.RocksIterator;

public class TestProgram {

    public static void main(String args[])throws IOException, RocksDBException {

    	File file = new File("./spider_result.txt");
    	file.createNewFile();
    	FileWriter writer = new FileWriter(file);
    	
		Manage_Rocksdb a;
		String path = "./url_to_pageid_db";
		a = new Manage_Rocksdb(path);
		
		Manage_Rocksdb b;
		String path2 = "./word_to_frequency_db";
		b = new Manage_Rocksdb(path2);
		
		Manage_Rocksdb c;
		String path3 = "./wordid_to_word_db";
		c = new Manage_Rocksdb(path3);
		
		Manage_Rocksdb d;
		String path4 = "./title_wordid_to_pos_db";
		d = new Manage_Rocksdb(path4);		
		
		Manage_Rocksdb f;
		String path5 = "./body_wordid_to_pos_db";
		f = new Manage_Rocksdb(path5);	
		
		Manage_Rocksdb g;
		String path6 = "./parentid_to_childid_db";
		g = new Manage_Rocksdb(path6);
		
		Manage_Rocksdb h;
		String path7 = "./childid_to_parentid_db";
		h = new Manage_Rocksdb(path7);
		
        // Writes the content to the file
        for(int i=1; i<=30;i++)
        {
        	
        	writer.write(b.getWord_and_Frequency(i)+"\n");

        	writer.write("-------------------------------------------------------------------------------------------\n");
        }
        
        writer.flush();
        writer.close();
        
    }
}