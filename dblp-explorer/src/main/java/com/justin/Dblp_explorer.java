package com.justin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.stream.Stream;

public final class Dblp_explorer {
	public static JSONObject parserObject(String d) throws ParseException {
		JSONObject job = null;
		JSONParser parser = new JSONParser(); 
		try {
		Object ob = (Object) parser.parse(d);
		job = (JSONObject) ob;
		return job;
		}
		catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	public static List<JSONObject> makeAJsonObjectForId(String name, String[] keyword) throws IOException, ParseException {


			List<String> master = new ArrayList<String>();
			for ( String k : keyword) {
				String t = "\"id\": \"" + k + "\", \"title\"";
				//System.out.println(t);
				Stream<String> lines = Files.lines(Paths.get(name));
				List<String> strOb = lines.parallel().filter(x -> x.contains(t)).map(x -> (String) x).collect(Collectors.toList());
				for (String str : strOb) {
					if(!master.contains(str))
						master.add(str);
		  		}
				lines.close();
			}
		  List<JSONObject> lstObj = new ArrayList<JSONObject>();
		 
		  for ( String st : master) {
			  JSONObject y = parserObject(st);
			  lstObj.add(y);
		  }
			 
		
			return lstObj;
		}
	public static List<JSONObject> makeAJsonObject(String name, String[] keyword) throws IOException, ParseException {

		List<String> master = new ArrayList<String>();
		for ( String k : keyword) {
			
			Stream<String> lines = Files.lines(Paths.get(name));
			List<String> strOb = lines.parallel().filter(x -> x.contains(k)).map(x -> (String) x).collect(Collectors.toList());
			for (String str : strOb) {
				if(!master.contains(str))
					master.add(str);
	  		}
			lines.close();
		}
	  List<JSONObject> lstObj = new ArrayList<JSONObject>();
	 
	  for ( String st : master) {
		  JSONObject y = parserObject(st);
		  lstObj.add(y);
	  }
		return lstObj;
	}
	public static List<String> shortListedStream(List<JSONObject> data, String[] id ) throws ParseException, IOException{

		List<String> filteredList = new ArrayList<String>();
		 for (String i : id) {
			 
			//System.out.println("The references:aaaa"  + i);
			filteredList = data.parallelStream().filter(x -> ((String) x.get("title")).contains(i)).map(x -> (String)x.get("id")).collect(Collectors.toList());
		 }
		 return filteredList;
	}
	
	public static void printCitationOrder( String name,List<JSONObject> mapdata, List<String> listOfId, int numberOfOder, int counter) throws ParseException, IOException{
		 if (numberOfOder == 0 || mapdata.isEmpty() || listOfId.isEmpty()) {
			 return;
		 }

		 List<JSONObject> lstBooks = new ArrayList<JSONObject>();
	 	//System.out.println("The references:11 " );
		 List<JSONArray> lstReference = new ArrayList<JSONArray>();
	     for (String id : listOfId) {
	    	 // print all the books in the id list
	    	 
	    	 List<JSONObject> reduceData =   mapdata.parallelStream().filter(x -> x.get("id").equals(id)).collect(Collectors.toList());
	    	 if (!reduceData.isEmpty()) {
	    	 System.out.println(reduceData.get(0).get("title"));
	    	 lstBooks.add(reduceData.get(0)); 
	    	 if (reduceData.get(0).containsKey("references")) {
	    		 lstReference.add((JSONArray) reduceData.get(0).get("references"));
	    	 }
	    	 }
	  
	     }
	    
	     if (lstReference.isEmpty())
	    	 return;
	     System.out.println("The citation number:" + counter + "\n" );
		 counter++;
		 if (numberOfOder - 1 == 0)
			 return;
		 for (int i = 0; i < lstReference.size(); i++) {
			  JSONArray arr = (JSONArray) lstReference.get(i);
			  List<String> listOfStringId = new ArrayList<String>();
			  for (int in =0; in < arr.size(); in++) {
				 
				  listOfStringId.add((String)arr.get(in));
			  }
			  List<JSONObject> newdata = makeAJsonObjectForId(name, listOfStringId.stream().toArray(String[]::new));
			
			
			  printCitationOrder(name, newdata, listOfStringId, numberOfOder - 1, counter); 
		  }
		   	
	 }
	 @SuppressWarnings("unchecked")
	public static void main(String[] args)  {
	        //System.out.println("Hello World!");
	        try {
	        	//number of k
	        	int k = 3;
	        	String name = "dblp_papers_v11.txt";
	        	//String name = "dblp_papers_v11_first_100_lines.txt";
	            //Stream<String> lines = Files.lines(Paths.get(name));
	           
	            // search keyword
	            String[] keyword = {"Security Enhancement on Mobile Commerce"};
	            int counter = 1;
		       
			    
		        List<JSONObject> bookDatabase = makeAJsonObject(name, keyword);
		     
	            List<String> jo = shortListedStream(bookDatabase, keyword);
	
		        System.out.println("The book with keyword: " + keyword[0]);
		     
		        printCitationOrder(name ,bookDatabase, jo, k, counter);
		        
			
		       System.out.println("end");
	        }
	            catch (Exception e){
	                e.printStackTrace();
	                System.out.println("error!");
	            }
	            
	         
	        }
	 
}