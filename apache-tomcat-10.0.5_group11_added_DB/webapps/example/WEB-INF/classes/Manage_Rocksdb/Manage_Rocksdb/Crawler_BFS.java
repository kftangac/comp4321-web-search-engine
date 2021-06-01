package Manage_Rocksdb.Manage_Rocksdb;

import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.Connection;
import org.jsoup.Connection.Response;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import org.jsoup.HttpStatusException;
import java.lang.RuntimeException;
import java.net.UnknownHostException;

import org.rocksdb.Options;
import org.rocksdb.RocksDB;
import org.rocksdb.RocksDBException;

import java.util.*;



/** The data structure for the crawling queue.
 */

class Link{
	String url;
	int level;
	Link (String url, int level) {  
		this.url = url;
		this.level = level; 
	}  
}

class RevisitException extends RuntimeException {
	public RevisitException() {
		super();
	}
}


public class Crawler_BFS {

	private HashSet<String> urls;     // the set of urls that have been visited before
	private String current_lastModified;
	private int current_size;
	private int last_parentid = -1;
	public List<Link> todos; // the queue of URLs to be crawled
	private int max_crawl_depth = 3;  // feel free to change the depth limit of the spider.

	Crawler_BFS(String _url) {
		this.todos = new ArrayList<Link>();
		this.todos.add(new Link(_url, 1));
		this.urls = new HashSet<String>();
	}

	/**
	 * Send an HTTP request and analyze the response.
	 * @return {Response} res
	 * @throws HttpStatusException for non-existing pages
	 * @throws IOException
	 */
	public Response getResponse(String url) throws HttpStatusException, IOException, NullPointerException {
		if (this.urls.contains(url)) {
			throw new RevisitException(); // if the page has been visited, break the function
		}

		Connection conn = Jsoup.connect(url).followRedirects(false);
		// the default body size is 2Mb, to attain unlimited page, use the following.
		// Connection conn = Jsoup.connect(this.url).maxBodySize(0).followRedirects(false);
		Response res;
		try {
			/* establish the connection and retrieve the response */
			res = conn.execute();
			/* if the link redirects to other place... */
			if(res.hasHeader("location")) {
				String actual_url = res.header("location");
				if (this.urls.contains(actual_url)) {
					throw new RevisitException();
				}
				else {
					this.urls.add(actual_url);
				}
			}
			else {
				this.urls.add(url);
			}
			String lastModified = res.header("last-modified");
			current_lastModified = lastModified;
			int size = res.bodyAsBytes().length;
			current_size = size;
			String htmlLang = res.parse().select("html").first().attr("lang");

			String bodyLang = res.parse().select("body").first().attr("lang");
			String lang = htmlLang + bodyLang; 

		} catch (HttpStatusException e) {
			throw e;
		}
		catch (UnknownHostException e) {
			throw e;
		}
		catch (NullPointerException e) {
			throw e;
		}

		/* Get the metadata from the result */

		//		System.out.printf("Last Modified: %s\n", lastModified);
		//		System.out.printf("Size: %d Bytes\n", size);
		//		System.out.printf("Language: %s\n", lang);
		return res;
	}

	/** Extract words in the web page content.
	 * note: use StringTokenizer to tokenize the result
	 * @param {Document} doc
	 * @return {Vector<String>} a list of words in the web page body
	 */
	public Vector<String> extractWords(Document doc) {
		Vector<String> result = new Vector<String>();
		// ADD YOUR CODES HERE
		String contents = doc.body().text(); 

		StringTokenizer st = new StringTokenizer(contents);
		while (st.hasMoreTokens()) {
			result.add(st.nextToken());
		}
		return result;		
	}

	/** Extract useful external urls on the web page.
	 * note: filter out images, emails, etc.
	 * @param {Document} doc
	 * @return {Vector<String>} a list of external links on the web page
	 */
	public List<String> extractLinks(Document doc) {
		List<String> result = new ArrayList<String>();
		// ADD YOUR CODES HERE

		Elements links = doc.select("a[href]");

		for (Element link: links) {	
			String linkString = link.attr("href");
			// filter out emails
			if (linkString.contains("mailto:") || linkString.contains("javascript")) {
				continue;
			} 

			//            	if(!linkString.contains("http")){
			//          		linkString = url + linkString; 
			//           	}
			//result.add(link.attr("href"));
			result.add(linkString);
		}

		result =  result.stream().distinct().collect(Collectors.toList());

		//            Iterator<String> result_iter = result.iterator();
		//            while(result_iter.hasNext()) { 
		//            	result_iter.next();
		//            }

		return result;
	}

	public void update_a_and_o(Manage_Rocksdb a, Manage_Rocksdb o)throws RocksDBException {
		LinkedHashMap<Integer,String> words_a = ExtractLinkInformation.getpageIDMappingTable();
		for (Map.Entry<Integer,String> entry : words_a.entrySet()) {
			if(!(entry.getValue().contains("cse.ust.hk")))
			{
				continue;
			}
			a.add_string_to_int(entry.getValue(),entry.getKey());
			o.add_int_to_1string(entry.getKey(), entry.getValue());
		}
	}


	public void update_g(Manage_Rocksdb g)throws RocksDBException {
		LinkedHashMap<Integer, String> childtoParentMappingTable =ExtractLinkInformation.getparentToChildMappingTable();


		for (Map.Entry<Integer,String> entry : childtoParentMappingTable.entrySet()) {
			String[] array = entry.getValue().split("-");
			for(int i=0; i<array.length; i++) {
			g.int_to_int_extend(Integer.parseInt(array[i]), entry.getKey());
			}
		
		}

	}

	public void update_h(Manage_Rocksdb h)throws RocksDBException {
		LinkedHashMap<Integer, String> childtoParentMappingTable =ExtractLinkInformation.getparentToChildMappingTable();


		for (Map.Entry<Integer,String> entry : childtoParentMappingTable.entrySet()) {
				h.int_to_string_extend(entry.getKey(),entry.getValue());
		
		}
		
	}

	public void update_i(Manage_Rocksdb i)throws RocksDBException {
		LinkedHashMap<Integer,String> words_i = ExtractWordInformation.getWordIdMappingTable();
		for (Map.Entry<Integer,String> entry : words_i.entrySet()) {
			i.add_string_to_int(entry.getValue(),entry.getKey());
		}
	}

	public void update_j(Manage_Rocksdb j, Document doc, Link focus, Manage_Rocksdb a )throws RocksDBException {
		LinkedHashMap<String, Integer> words_j = ExtractWordInformation.extractWords(doc);
		for (Map.Entry<String, Integer> entry : words_j.entrySet()) {

			j.int_to_stringandint_enxtend(a.get_int_from_string(focus.url),entry.getKey(),entry.getValue());
		}
	}

	public void update_k(Manage_Rocksdb k,  Document doc, Link focus, Manage_Rocksdb a )throws RocksDBException {
		String title = doc.title();
		k.add_pageid_to_properties(a.get_int_from_string(focus.url), title, focus.url, current_lastModified, current_size);
	}

	public void update_l(Manage_Rocksdb l,  Document doc, Link focus, Manage_Rocksdb a,  Manage_Rocksdb i)throws RocksDBException {
		LinkedHashMap<Integer,String> words_l = ExtractWordInformation.extractTitleWordsIdAndPos(doc);

		for (Map.Entry<Integer,String> entry : words_l.entrySet()) {
			l.int_to_intandstring_enxtend(entry.getKey(), a.get_int_from_string(focus.url),entry.getValue());
		}
	}

	public void update_m(Manage_Rocksdb m,  Document doc, Link focus, Manage_Rocksdb a,  Manage_Rocksdb i)throws RocksDBException {
		LinkedHashMap<Integer,String> words_m = ExtractWordInformation.extractContentWordsIdAndPos(doc);
		System.out.print(words_m);
		for (Map.Entry<Integer,String> entry : words_m.entrySet()) {
			m.int_to_intandstring_enxtend(entry.getKey(), a.get_int_from_string(focus.url),entry.getValue());
		}
	}

	public void update_n(Manage_Rocksdb n,  Document doc, Link focus, Manage_Rocksdb a)throws RocksDBException {
		LinkedHashMap<String, Integer> words_n = ExtractWordInformation.extractWords(doc);
		System.out.println(words_n);
		//		n.add_int_to_int(a.get_int_from_string(focus.url), Collections.max(words_n.values()));

	}


	/** Use a queue to manage crawl tasks.
	 */
	public void crawlLoop() throws RocksDBException {

		Manage_Rocksdb a;
		String path = "./url_to_pageid_db";
		a = new Manage_Rocksdb(path);

		//		Manage_Rocksdb b;
		//		String path2 = "./word_to_frequency_db";
		//		b = new Manage_Rocksdb(path2);
		//
		//		Manage_Rocksdb c;
		//		String path3 = "./wordid_to_word_db";
		//		c = new Manage_Rocksdb(path3);

		//		Manage_Rocksdb d;
		//		String path4 = "./title_wordid_to_pos_db";
		//		d = new Manage_Rocksdb(path4);		
		//
		//		Manage_Rocksdb f;
		//		String path5 = "./body_wordid_to_pos_db";
		//		f = new Manage_Rocksdb(path5);	

		Manage_Rocksdb g;
		String path6 = "./parentid_to_childid_db";
		g = new Manage_Rocksdb(path6);

		Manage_Rocksdb h;
		String path7 = "./childid_to_parentid_db";
		h = new Manage_Rocksdb(path7);

		Manage_Rocksdb i;
		String path8 = "./word_to_wordid_db";
		i = new Manage_Rocksdb(path8);

		Manage_Rocksdb j;
		String path9 = "./pageid_to_keyword";
		j = new Manage_Rocksdb(path9);	

		Manage_Rocksdb k;
		String path10 = "./pageid_to_properties";
		k = new Manage_Rocksdb(path10);	

		Manage_Rocksdb l;
		String path11 = "./title_inverted_index";
		l = new Manage_Rocksdb(path11);	

		Manage_Rocksdb m;
		String path12 = "./body_inverted_index";
		m = new Manage_Rocksdb(path12);	

		Manage_Rocksdb n;
		String path13 = "./tf_max_db";
		n = new Manage_Rocksdb(path13);	

		Manage_Rocksdb o;
		String path14 = "./pageid_to_url_db";
		o = new Manage_Rocksdb(path14);


		//	
		System.out.println("database before:\n");
		a.printAll();


		while(!this.todos.isEmpty()) {
			Link focus = this.todos.remove(0);
			if (focus.level > this.max_crawl_depth) break; // stop criteria
			if (this.urls.contains(focus.url)) continue;   // ignore pages that has been visited
			/* start to crawl on the page */
			try {
				Response res = this.getResponse(focus.url);

				Document doc = res.parse();
				if(focus.url.contains("cemar.fytri.cn")) {continue;}

				//                             From gary
				LinkedHashMap<String, Integer> words = ExtractWordInformation.extractWords(doc);
				ExtractWordInformation.UpdateWordMappingTable(words);
				//				System.out.println("\nWords:");
				//
				//				System.out.println("\n\ntf_max:");
				////				System.out.println(Collections.max(words.values()));
				//				System.out.println("\n\nWord Mapping table (i.e., Word ID = word):");
				//				System.out.println(ExtractWordInformation.getWordIdMappingTable());	
				//				
				//				
				//				System.out.println("\n\nTitle word position and word ID (i.e., word ID = [Title Word positions]): ");
				//				System.out.println(ExtractWordInformation.extractTitleWordsIdAndPos(doc));
				//				System.out.println("\n\nContent word position and word ID (i.e., word ID = [Content Word positions]): ");
				//				System.out.println(ExtractWordInformation.extractContentWordsIdAndPos(doc));
				//									from gary

								update_a_and_o(a,o);
//								update_g(g);

				//				update_i(i);
				//				update_j(j, doc, focus, a);
				//				update_k(k, doc, focus, a);
				//				update_l(l, doc, focus, a, i);
				//				update_m(m, doc, focus, a, i);	
				//				update_n(n, doc, focus, a);				

				List<String> links = this.extractLinks(doc);
				
				//System.out.printf("\n\nLinks:");
				ExtractLinkInformation.extractLinks(focus.url,doc);

				for(String link: links) {
					Link temp = new Link(link, focus.level + 1);
					if(temp.url.matches("^((?!http(s?)://).)*$")) { //if url is malformed, add basic url in front of it
						String newstring = focus.url.substring(0, focus.url.length() - 1); 
						temp.url = newstring + temp.url;
					}
					this.todos.add(temp); // add links

				}
				
				System.out.print(ExtractLinkInformation.getparentToChildMappingTable());

			} catch (HttpStatusException e) {
				// e.printStackTrace ();
				System.out.printf("\nLink Error: %s\n", focus.url);
			} catch (IOException e) {
				e.printStackTrace();

			}
			catch (NoSuchElementException e) {
				e.printStackTrace();
			}
			catch (NullPointerException e) {
				throw e;
			}
		}
		
		LinkedHashMap<Integer, String> childtoParentMappingTable =ExtractLinkInformation.getparentToChildMappingTable();

//		update_h(h);
//		update_g(g);
		System.out.println("database after:\n");
		a.printAll();
		

	}

	public static void destroyAllDB() throws RocksDBException {
		RocksDB.destroyDB("./url_to_pageid_db", new Options());
		RocksDB.destroyDB("./pageid_to_properties", new Options());
		RocksDB.destroyDB("./childid_to_parentid_db", new Options());
		RocksDB.destroyDB("./pageid_to_keyword_db", new Options());
		RocksDB.destroyDB("./word_to_wordid_db", new Options());
		RocksDB.destroyDB("./body_inverted_index", new Options());
		RocksDB.destroyDB("./title_inverted_index", new Options());
		RocksDB.destroyDB("./tf_max_db", new Options());

	}

	public static void main (String[] args) throws RocksDBException {
//		RocksDB.destroyDB("./childid_to_parentid_db", new Options());
		String url = "https://www.cse.ust.hk/";
		Crawler_BFS crawler = new Crawler_BFS(url);
		crawler.crawlLoop();
		System.out.println("\nSuccessfully Returned");

	}
}

