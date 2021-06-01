package Manage_Rocksdb.Manage_Rocksdb;

import java.io.BufferedReader;
import java.util.*;
import org.rocksdb.RocksDB;
import org.rocksdb.Options;
import org.rocksdb.RocksDBException;  
import org.rocksdb.RocksIterator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;


public class Manage_Rocksdb
{
	private RocksDB db;
	private Options options;

	Manage_Rocksdb(String dbPath) throws RocksDBException
	{
		// the Options class contains a set of configurable DB options
		// that determines the behaviour of the database.
		this.options = new Options();
		this.options.setCreateIfMissing(true);

		// creat and open the database
		this.db = RocksDB.open(options, dbPath);
	}

	//key: int      value: string
	public void add_int_to_string(int integer, String str) throws RocksDBException
	{
		if(str == null)
		{
			return;
		}
		byte[] k = (String.valueOf(integer)).getBytes();
		byte[] v = str.getBytes();
		db.put(k,v);
	}

	//key: int      value: string
	public String get_url_from_pageid(int index) throws RocksDBException
	{
		return new String(db.get(String.valueOf(index).getBytes()));
	}

	//key: string     value:int
	public void add_string_to_int(String str, int integer) throws RocksDBException
	{
		if(str == null)
		{
			return;
		}
		byte[] k = str.getBytes();
		byte[] v = (String.valueOf(integer)).getBytes();
		db.put(k,v);
	}

	//key: string     value:int
	public int get_int_from_string(String str) throws RocksDBException
	{
		if(str == null)
		{
			return -1;
		}
		if(db.get(str.getBytes()) == null) {return 0;}
		return Integer.parseInt(new String(db.get(str.getBytes())));
	}

	//key: int    value: string
	public void add_int_to_1string(int index, String content) throws RocksDBException
	{
		if(content == null)
		{
			return;
		}
		byte[] k = (String.valueOf(index)).getBytes();
		byte[] v = content.getBytes();
		db.put(k,v);
	}

	//key: int    value: string
	public String get_string_from_int(int integer) throws RocksDBException
	{
		if(integer <0)
		{
			return "";
		}
		byte[] temp = (db.get(String.valueOf(integer).getBytes()));
		if(temp == null)
		{
			return "";
		}
		return new String(db.get(String.valueOf(integer).getBytes()));
	}



	//key: int     value: int int int (add)...
	public void int_to_int_extend(int key, int extend_value) throws RocksDBException
	{
		if( key < 0 )
		{
			return;
		}
		byte[] k = (String.valueOf(key)).getBytes();
		byte[] v = db.get(String.valueOf(key).getBytes());
		if (v == null) {
			v = (String.valueOf(extend_value)).getBytes();
		} else {
			v = (new String(v) +" "+ String.valueOf(extend_value)).getBytes();
		}
		db.put(k,v);
	}
	public String[] get_stringarray(int key) throws RocksDBException
	{
		if(db.get(String.valueOf(key).getBytes()) == null)
		{
			String[] temp = {"null"};
			return temp ; 
		}
		String[] array = new String(db.get(String.valueOf(key).getBytes())).split(" ");
		return array;
	}

	public String get_pageid(String url) throws RocksDBException
	{
		RocksIterator iter = db.newIterator();

		for(iter.seekToFirst(); iter.isValid(); iter.next()) {
			if((new String(iter.key()).equals(url)))
			{
				return new String (iter.value());
			}
		}

		return "-1";
	}
	//
	public void addWord_Frequency(int pageid, String word, int frequency) throws RocksDBException
	{
		if(word == null)
		{
			return;
		}

		byte[] v = ("pageid: " + pageid + ", frequency: " + frequency).getBytes();
		byte[] k = word.getBytes();
		db.put(k, v);
	}

	public String getWord_and_Frequency(int pageid) throws RocksDBException
	{
		String content = (" ");
		if(pageid<=0) {return "null";}
		else
		{
			RocksIterator iter = db.newIterator();

			for(iter.seekToFirst(); iter.isValid(); iter.next()) {
				String[] temp = new String(iter.value()).split(",");
				String[] temp1 = new String(temp[0]).split(" ");
				if (Integer. parseInt(temp1[1]) == pageid )
				{
					content = ( content + new String(iter.key()) + temp[1]+" ");
				}
			}
		}

		return content;	
	}

	public void addWord_id_table(int word_id, String word) throws RocksDBException
	{
		if(word == null || word_id <= 0 )
		{
			return;
		}
		byte[] k = (String.valueOf(word_id)).getBytes();
		byte[] v = word.getBytes();
		db.put(k,v);
	}

	public void addWord_position(int pageid, int word_id, String word) throws RocksDBException
	{
		if(word == null || word_id <= 0 )
		{
			return;
		}
		byte[] k = (String.valueOf(word_id)).getBytes();
		byte[] v = word.getBytes();
		db.put(k,v);
	}

	//key: int     value: string string string (add)...
	public void int_to_string_extend(int key, String value) throws RocksDBException
	{
		if(value == null || key < 0 )
		{
			return;
		}
		byte[] k = (String.valueOf(key)).getBytes();
		byte[] v = db.get(String.valueOf(key).getBytes());
		if (v == null) {
			v = (value).getBytes();
		} else {
			v = (new String(v) +" "+ value).getBytes();
		}
		db.put(k,v);
	}

	//key: int    value: string int, string int, string int (add)...
	public void int_to_stringandint_enxtend(int key, String string, int integer) throws RocksDBException
	{
		if( string == null || key < 0 )
		{
			return;
		}
		byte[] k = (String.valueOf(key)).getBytes();
		byte[] v = db.get(String.valueOf(key).getBytes());
		if (v == null) {
			v = (string + " " + String.valueOf(integer)).getBytes();
		} else {
			v = (new String(v) + "," + string + " " + String.valueOf(integer)).getBytes();
		}
		db.put(k,v);
	}

	//key: int    value: int string, int string, int string (add)...
	public void int_to_intandstring_enxtend(int key, int integer, String string) throws RocksDBException
	{
		if( string == null || key < 0 )
		{
			return;
		}
		byte[] k = (String.valueOf(key)).getBytes();
		byte[] v = db.get(String.valueOf(key).getBytes());
		if (v == null) {
			v = ( String.valueOf(integer) + " " +string ).getBytes();
		} else {
			v = (new String(v) + "," + String.valueOf(integer) + " " + string).getBytes();
		}
		db.put(k,v);
	}

	//key: str    value: int(pageid) string(pos-pos-pos), int string, int string (add)...
	public void string_to_intandstring_enxtend(String str, int integer, String string) throws RocksDBException
	{
		//    	System.out.println(str);
		if( string == null || str == null )
		{
			return;
		}
		byte[] k = (str).getBytes();
		byte[] v = db.get((str).getBytes());
		if (v == null) {
			v = ( String.valueOf(integer) + " " + string ).getBytes();
		} else {
			v = (new String(v) + "," + String.valueOf(integer) + " " + string).getBytes();
		}
		db.put(k,v);
	}

	//for properties only
	public void add_pageid_to_properties(int pageid, String title, String url, String mod_date, int size) throws RocksDBException
	{
		if( pageid < 0 )
		{
			return;
		}
		if(title == null) {title = "null";}
		if(mod_date == null) {mod_date = "null";}
		byte[] k = (String.valueOf(pageid)).getBytes();
		byte[] v = ( title + "," + url + "," + mod_date + "," + String.valueOf(size)+"bytes").getBytes();

		db.put(k,v);
	}

	public String get_properties_from_pageid(int pageid) throws RocksDBException
	{
		if(db.get(String.valueOf(pageid).getBytes()) == null)
		{
			return " ";
		}
		String result = "";
		byte [] content = db.get(String.valueOf(pageid).getBytes());
		String[] array = new String(content).split(",");

		if(array.length >=1)
		{
			result +=(array[0] + ",");
		}
		else
		{
			result +=("null" + ",");
		}
		if(array.length >=2)
		{
			result +=(array[1] + ",");
		}
		else
		{
			result +=("null" + ",");
		}
		if( array.length >=3)
		{
			result +=(array[2] + ",");
		}
		else
		{
			result +=("null" + ",");
		}
		if(array.length >=4)
		{
			result +=(array[3] + ",");
		}
		else
		{
			result +=("null" + ",");
		}
		return result;
	}

	//
	//key: word id     value: pageid pos-pos-pos...,pageid pos-pos-pos...,pageid pos-pos-pos...
	public int[] get_pageid_from_invertedindex(int word_id) throws RocksDBException
	{

		byte [] key = db.get(String.valueOf(word_id).getBytes());
		String[] pageid_and_its_pos = new String(key).split(",");

		int[] array;          
		array = new int[pageid_and_its_pos.length];  
		for(int i=0; i<pageid_and_its_pos.length; i++)
		{
			array[i] = Integer.parseInt((new String(pageid_and_its_pos[i]).split(" "))[0]);
		}


		return array;	
	}

	//key: word id     value: pageid pos-pos-pos...,pageid pos-pos-pos...,pageid pos-pos-pos...
	public int[] get_pos_from_invertedindex(int word_id, int page_id) throws RocksDBException
	{

		byte [] key = db.get(String.valueOf(word_id).getBytes());
		String[] pageid_and_its_pos = new String(key).split(",");


		String[] array2;
		//problem
		array2 = new String[100];
		for(int i=0; i<pageid_and_its_pos.length; i++)
		{
			if(Integer.parseInt((new String(pageid_and_its_pos[i]).split(" "))[0]) == page_id) {
				array2 = (new String(pageid_and_its_pos[i]).split(" "))[1].split("-");
				break;
			};
		}
		int[] array3;
		array3 = new int[array2.length];
		for(int j=0; j<array3.length; j++) {
			array3[j] = Integer.parseInt(array2[j]);
		}
		return array3;	
	}

	public void delEntry(String word) throws RocksDBException
	{
		// Delete the word and its list from the hashtable
		db.remove(word.getBytes());
	} 

	public void printAll() throws RocksDBException
	{
		// Print all the data in the hashtable
		// ADD YOUR CODES HERE
		RocksIterator iter = db.newIterator();
		int count = 0;            
		for(iter.seekToFirst(); iter.isValid(); iter.next()) {
			System.out.println("key : " + new String(iter.key()) + "\t\t\tvalue : " + new String(iter.value()));
			count ++;
		}
		System.out.println("count:"+count);
	}    


	public int getKeyCount() {
		RocksIterator iter = db.newIterator();
		int count = 0;            
		for(iter.seekToFirst(); iter.isValid(); iter.next()) {
			count ++;
		}
		return count;
	}	

	public void closeDB() {
		db.close();
	}

	//key: int     value:int
	public int get_int_from_int(int integer) throws RocksDBException
	{
		if(db.get(String.valueOf(integer).getBytes()) == null)
		{
			return 0 ;
		}
		return Integer.parseInt(new String(db.get(String.valueOf(integer).getBytes())));
	}

	//key: int    value: string
	public void add_int_to_int(int key_int, int value_int) throws RocksDBException
	{
		byte[] k = (String.valueOf(key_int)).getBytes();
		byte[] v = (String.valueOf(value_int)).getBytes();
		db.put(k,v);
	}

	public void add_childid_to_parentid(int child_id, int parent_id) throws RocksDBException
	{

		byte[] k = (String.valueOf(parent_id)).getBytes();
		byte[] v = (String.valueOf(child_id)).getBytes();
		db.put(k,v);
	}

	public String get_5_most_frequent_word(int pageid) throws RocksDBException
	{
		if(db.get(String.valueOf(pageid).getBytes()) == null)
		{
			return "null" ;
		}
		String content = new String(db.get(String.valueOf(pageid).getBytes()));
		List<String> words = Arrays.asList(content.split(","));

		LinkedHashMap<String, Integer> singleWords = new LinkedHashMap<>();
		for (int i = 0; i < words.size(); i++) {
			List<String> temp = Arrays.asList(words.get(i).split(" "));
			singleWords.put(temp.get(0), Integer.parseInt(temp.get(1)));
		}
		// sorting
		List<Map.Entry<String, Integer>> singleWordList = new LinkedList<Map.Entry<String, Integer>>(singleWords.entrySet());

		Collections.sort(singleWordList, new Comparator<Map.Entry<String, Integer>>() {
			public int compare(Map.Entry<String, Integer> word1, Map.Entry<String, Integer> word2) {
				return (word2.getValue()).compareTo(word1.getValue());
			}
		});

		String result = "";
		for (int i = 0; i < 5; i++) {
			result += "keyword: " + singleWordList.get(i).getKey() + " | " + "freq: " + singleWordList.get(i).getValue() + ";\n";
		}
		return result;
	}
}


