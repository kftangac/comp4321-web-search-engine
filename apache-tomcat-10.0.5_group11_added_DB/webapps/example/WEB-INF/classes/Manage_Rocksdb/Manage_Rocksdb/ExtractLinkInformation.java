package Manage_Rocksdb.Manage_Rocksdb;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ExtractLinkInformation {
	private static int pageID = 1;
	private static LinkedHashMap<Integer, String> pageIDMappingTable = new LinkedHashMap<>(); //page mapping table
	private static LinkedHashMap<String, Integer> pageUrlMappingTable = new LinkedHashMap<>(); //page mapping table
	
	private static LinkedHashMap<Integer, String> childtoParentMappingTable = new LinkedHashMap<>();
	
    public static LinkedHashMap<Integer, String> getpageIDMappingTable() {
        return pageIDMappingTable;
    }
    
    public static LinkedHashMap<Integer, String> getparentToChildMappingTable() {
        return childtoParentMappingTable;
    }
    
    public static void updatePageMappingTable(List<String> result){
    	Iterator<String> result_iter = result.iterator();
    	while (result_iter.hasNext()) {
            String current = result_iter.next();
            boolean exists = false;
            for (Map.Entry<Integer, String> MappingTableEntry : pageIDMappingTable.entrySet()) {
                if (MappingTableEntry.getValue().equals(current)) {
                    exists = true;
                    break;
                }
            }
            if (!exists) {
            	pageUrlMappingTable.put(current, pageID);
            	pageIDMappingTable.put(pageID, current); //can be further transform to page properties mapping table
            	pageID++;
            }
        }
    }
    
    public static void updatechildtoParentMappingTable(List<String> result){
    	Iterator<String> result_iter = result.iterator();
    	String current = result_iter.next(); // first link is the parent
    	String parent_pageID = String.valueOf(pageUrlMappingTable.get(current));
    	while (result_iter.hasNext()) {
            current = result_iter.next();
        	Integer child_pageID = pageUrlMappingTable.get(current);
        	String temp = parent_pageID;
        	if(childtoParentMappingTable.get(child_pageID) != null) {
        		temp = childtoParentMappingTable.get(child_pageID) + "-" + parent_pageID;
        	}
            childtoParentMappingTable.put(child_pageID, temp);
        }
    }
    
    public static void extractLinks(String basic_url, Document doc) {
		List<String> result = new ArrayList<String>();
		result.add(basic_url); 

        Elements links = doc.select("a[href]");
        for (Element link: links) {	
        	String linkString = link.attr("href");
        	// filter out emails
        	if (linkString.contains("mailto:") || linkString.contains("javascript") || linkString.contains(".jpg")) {
        		continue;
        	} 
//        	if (!(linkString.contains("cse")) || !linkString.contains("ust")|| !(linkString.contains("hk"))) {
//				continue;
//			} 
//        	if (!(linkString.contains("cse.ust.hk"))) {
//				continue;
//			} 
//        	
			if(linkString.matches("^((?!http(s?)://).)*$")) { //if url is malformed, add basic url in front of it
				String newstring = basic_url.substring(0, basic_url.length() - 1); 
				linkString = newstring + linkString;
			}
        	//result.add(link.attr("href"));
        	result.add(linkString);
        }
        
        result = result.stream().distinct().collect(Collectors.toList());
        
        updatePageMappingTable(result);
        updatechildtoParentMappingTable(result);
        
//        for (Object key : pageIDMappingTable.keySet()) {
//            System.out.println(key + " : " + pageIDMappingTable.get(key));
//        }
//        
//        for (Object key : childtoParentMappingTable.keySet()) {
//            System.out.println(key + " : " + childtoParentMappingTable.get(key));
//        }

    }

    
}

		
