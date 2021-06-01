package Manage_Rocksdb.Manage_Rocksdb;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.rocksdb.RocksDBException;

public class RetrievalFunction1 {

	private int no_of_doc;
	private HashMap<String,Double> partial_sum = new HashMap<String,Double>();
	private List<String> retrieval_result;
	private String[] query_terms; 
	
	public RetrievalFunction1(String query) throws RocksDBException {
		//open all db
		Manage_Rocksdb url_to_pageid = new Manage_Rocksdb("./url_to_pageid_db");
		Manage_Rocksdb word_to_wordid = new Manage_Rocksdb("./word_to_wordid_db");
		Manage_Rocksdb body_inverted_index_db = new Manage_Rocksdb("./body_inverted_index");
		Manage_Rocksdb title_inverted_index_db = new Manage_Rocksdb("./title_inverted_index");
		Manage_Rocksdb tf_max_db = new Manage_Rocksdb("./tf_max_db");
		Manage_Rocksdb pageid_to_properties = new Manage_Rocksdb("./pageid_to_properties");
		Manage_Rocksdb pageid_to_keyword = new Manage_Rocksdb("./pageid_to_keyword");
		Manage_Rocksdb childid_to_parentid_db = new Manage_Rocksdb("./childid_to_parentid_db");
		Manage_Rocksdb parentid_to_childid_db = new Manage_Rocksdb("./parentid_to_childid_db");
		Manage_Rocksdb pageid_to_url = new Manage_Rocksdb("./pageid_to_url");
		
		
		no_of_doc = url_to_pageid.getKeyCount();
		
		//split phrase from query, store in pharse_terms
		this.query_terms = find_query_terms(query);
		for(String s:query_terms) {
			System.out.println(s);
		}
		
		//compute title partial sum of query terms
		double title_favor = 2;
		for(String query_term: query_terms) {
			HashMap<String, Double> temp_partial_sum;
			if(query_term.contains(" ")) {
				temp_partial_sum = compute_partial_sum_for_phrase_term(query_term,word_to_wordid,title_inverted_index_db);
			}else {
				temp_partial_sum = compute_partial_sum_for_single_term(query_term,word_to_wordid,title_inverted_index_db,tf_max_db);
			}
			System.out.println(temp_partial_sum);
			temp_partial_sum.forEach((k, v) -> partial_sum.merge(k, v, (v1, v2) -> (v1 + v2)*title_favor));
		}
		
		//compute body partial sum of query terms
		for(String query_term: query_terms) {
			HashMap<String, Double> temp_partial_sum;
			if(query_term.contains(" ")) {
				temp_partial_sum = compute_partial_sum_for_phrase_term(query_term,word_to_wordid,body_inverted_index_db);
			}else {
				temp_partial_sum = compute_partial_sum_for_single_term(query_term,word_to_wordid,body_inverted_index_db,tf_max_db);
			}
			System.out.println(temp_partial_sum);
			temp_partial_sum.forEach((k, v) -> partial_sum.merge(k, v, (v1, v2) -> v1 + v2));
		}
		
		//sort the result in descending order
		List<Map.Entry<String, Double>> partial_sum_sorted = partial_sum.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toList());
		
		//compute retrieval result for web interface
		//can add your codes here, now the string to be returned to gary website is = "score,pageid"
		for(Object obj:partial_sum_sorted) {
			String[] pageid_score = obj.toString().split("=");
			//format this string
			String parent_links = "";
			String child_links = "";
			String [] parent_array = childid_to_parentid_db.get_stringarray(Integer.parseInt(pageid_score[0]));
			for(int i=0; i< parent_array.length; i++)
			{
				parent_links += pageid_to_url.get_string_from_int(Integer.parseInt(parent_array[i])) ;
				parent_links += "\n";
			}
			
			String [] child_array = parentid_to_childid_db.get_stringarray(Integer.parseInt(pageid_score[0]));
			for(int i=0; i< child_array.length; i++)
			{
				child_links += pageid_to_url.get_string_from_int(Integer.parseInt(child_array[i])) ;
				child_links += "\n";
			}

			retrieval_result.add(pageid_score[1] + pageid_to_properties.get_properties_from_pageid(Integer.parseInt(pageid_score[0])) 
					+ pageid_to_keyword.get_5_most_frequent_word(Integer.parseInt(pageid_score[0])) + ","
						+ parent_links + ","
							+ child_links);		
		} 
		
		System.out.println(partial_sum_sorted);

		//close all db
		url_to_pageid.closeDB();
		word_to_wordid.closeDB();
		body_inverted_index_db.closeDB();
		title_inverted_index_db.closeDB();
		tf_max_db.closeDB();
		pageid_to_properties.closeDB();
		pageid_to_keyword.closeDB();
		childid_to_parentid_db.closeDB();
		parentid_to_childid_db.closeDB();
		pageid_to_url.closeDB();
		
		
	}

	
	private String[] find_query_terms(String query) {
		Pattern p = Pattern.compile("\"([^\"]*)\"");
		Matcher m = p.matcher(query);
		List<String> list = new ArrayList<String>();
		String[] query_terms;
		
		while (m.find()) {
		  String a = m.group().substring(1, m.group().length()-1);
		  list.add(a);
		}
		
		query_terms = query.split(" ");
		for(String s:query_terms) {
			if(s.startsWith("\"") || s.endsWith("\"")) {
				continue;
			}
			list.add(s);
		}
		
		return list.toArray(new String[0]);
	}

	private HashMap<String, Double> compute_partial_sum_for_single_term(String query_term, Manage_Rocksdb word_to_wordid , Manage_Rocksdb inverted_index, Manage_Rocksdb tf_max_db) throws RocksDBException{
		System.out.println("single");

		int df = 0;
		int tf = 0;
		int max_tf = 1;
		double idf = 0;
		double innerProduct = 0;
		StopStem s = new StopStem("stopwords-en.txt");
		
		HashMap<String, Double> temp_partial_sum = new HashMap<String, Double>();
		
		int wordid = word_to_wordid.get_int_from_string(s.stem(query_term));
		String res = inverted_index.get_string_from_int(wordid);
		String[] pageid_pos = res.split(",");
		df = pageid_pos.length; 
		for(String data: pageid_pos) {
			String[] temp = data.split(" ");
			String pageid = temp[0];
			if(pageid == null || pageid.equals("")) continue;
			String[] pos;
			if(temp.length > 1) {
 				pos = temp[1].split("-");
				tf = pos.length;
			}else {
				tf = 1;
			}

			idf = Math.log(no_of_doc/df) / Math.log(2);
			max_tf = tf_max_db.get_int_from_int(Integer.parseInt(pageid));
			innerProduct = tf*idf/max_tf; 
			temp_partial_sum.put(pageid,innerProduct);
		}
		
		return temp_partial_sum;
	}
	
	private HashMap<String, Double> compute_partial_sum_for_phrase_term(String phrase,  Manage_Rocksdb word_to_wordid , Manage_Rocksdb inverted_index) throws RocksDBException {
		System.out.println("phrase");
		
		int df = 0;
		int tf = 0;
		int max_tf = 1;
		double idf = 0;
		double innerProduct = 0;
		StopStem s = new StopStem("stopwords-en.txt");
		
		HashMap<String, String> pageid_pos_map = new HashMap<>();
		HashMap<String, String> temp_map = new HashMap<>();
		HashMap<String, Double> temp_partial_sum = new HashMap<String, Double>(); 
		
		String[] terms = phrase.split(" ");
		int len = terms.length;
		
		for(String term: terms) {

			int wordid = word_to_wordid.get_int_from_string(s.stem(term));
			String res = inverted_index.get_string_from_int(wordid);
			String[] pageid_pos = res.split(",");
			
			for(String data:pageid_pos) {
				String[] temp = data.split(" "); 
				String pageid = temp[0];
				if(pageid == null || pageid.equals("")) continue;
				
				temp_map.put(temp[0], temp[1]);
				//System.out.println(temp_map);

			}
			
			temp_map.forEach(
				    (key, value) -> pageid_pos_map.merge(key, value, (v1, v2) -> v1.equalsIgnoreCase(v2) ? v1 : v1 + "-" + v2)
				);
			temp_map.clear();
		}
		
		pageid_pos_map.forEach( (k,v) -> {if(v.length()>len) temp_map.put(k, v);});
		//System.out.println(pageid_pos_map);
		
		
		for(String pageid:pageid_pos_map.keySet()) {
				List<String> pos_list = Arrays.asList(pageid_pos_map.get(pageid).split("-"));
				pos_list.sort(Comparator.naturalOrder());
				ListIterator<String> it = pos_list.listIterator();
				
				tf = 0;
				int count = 0; 
				int prev_pos = -1;
				while(it.hasNext()) {
					int cur_pos = Integer.parseInt(it.next());
					//System.out.println(prev_pos +"------------"+cur_pos);
					prev_pos++;
					if(cur_pos == prev_pos) {
						count++;
						if(count >= len-1) {
							tf++;
							if(tf > max_tf) {
								max_tf = tf;
							}
							count = 0;
						}
					}else {
						prev_pos = cur_pos;
						count = 0;
					}

				}
				if(tf >= 1) {
					df++;
					temp_partial_sum.put(pageid, (double) tf);
				}
		}
		if(df == 0) return temp_partial_sum;
		
		idf = Math.log(no_of_doc/df) / Math.log(2);
		
		for(String key:temp_partial_sum.keySet()) {
			innerProduct = temp_partial_sum.get(key)*idf/max_tf;
			temp_partial_sum.put(key, innerProduct);
		}

		//temp_partial_sum.forEach((key, value) -> System.out.println(key + ":" + value));
		return temp_partial_sum;
	}
	

	private HashMap<String,Double> get_partial_sum() {
		return partial_sum;
	}
	

	public List<String>get_retrieval_result() {
		return retrieval_result;
	}
	
	public static void main(String args[]) throws RocksDBException {
		RetrievalFunction1 rf = new RetrievalFunction1("computer science");
		
		int count = 0;
//		for(String key:rf.get_partial_sum().keySet()) {
//			System.out.println(key + "\t" + rf.get_partial_sum().get(key));
//			count++;
//		}
		
		for(Object obj:rf.get_retrieval_result() ) {
			System.out.println(obj);
			count++;
		}
		System.out.println(count);
		
	}

}
