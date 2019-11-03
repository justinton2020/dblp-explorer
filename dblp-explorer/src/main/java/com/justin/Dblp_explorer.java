package com.justin;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;


import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.stream.Stream;

public final class Dblp_explorer {

    public static void printCitationOrder(List<JSONObject> ob, List<String> listOfId, int numberOfOder, int counter) {
        if (numberOfOder == 0) {
            return;
        }
        
       
         
        List<JSONObject> oBook = new ArrayList<JSONObject>();
        for (String id : listOfId) {
            // print all the books in the id list
               ob.stream().filter(y -> ((String) y.get("id")).equals(id)).map(x -> x.get("title")).forEach(System.out::println);
        }
        for (String id : listOfId) {
            List<String> listOfStringId = new ArrayList<String>();
            // print all the books in the id list
               //ob.stream().filter(y -> ((String) y.get("id")).equals(id)).map(x -> x.get("title")).forEach(System.out::println);
               // get the list of jsonObject with the same id
               oBook = ob.stream().filter(y -> ((String) y.get("id")).equals(id)).collect(Collectors.toList());
               // check if the book has references
               
               if (!oBook.isEmpty() && oBook.get(0).get("references") != null ) {
                   System.out.println("The citation number:" + counter + " order");
                   counter++;
                   
                   JSONArray listReference = (JSONArray) oBook.get(0).get("references");
                   
                  
                   for (int i = 0 ; i < listReference.size(); i++) {
                       int in = i;
                       listOfStringId.add((String) listReference.get(in));
                       //System.out.println(listReference.get(in));
                   }
                   
                 
               }
                printCitationOrder(ob, listOfStringId, numberOfOder - 1, counter);
           }
        
    }
    @SuppressWarnings("unchecked")
   public static void main(String[] args)  {
           //System.out.println("Hello World!");
           try {
               int k = 3;
               Stream<String> lines = Files.lines(Paths.get("dblp_papers_v11_first_100_lines.txt"));
              
               JSONParser parser = new JSONParser();
               String keyword = "Remote";
               List<JSONObject> jo = new ArrayList<JSONObject>();
               List<String> filteredList = lines.parallel().filter(x -> x.contains(keyword)).collect(Collectors.toList());
               for (String str : filteredList) {
                   Object obj = (Object) parser.parse(str);
                   jo.add((JSONObject) obj);
               }
               List<String> listBooks = jo.stream().filter(y -> ((String) y.get("title")).contains(keyword))
                       .map(x -> (String) x.get("id")).collect(Collectors.toList());
               System.out.println("The book with keyword: " + keyword);
               int counter = 1;
               //System.out.println("The citation number:" + counter + " order");
               printCitationOrder(jo, listBooks, k, counter);
               
           
               lines.close();  
           }
               catch (Exception e){
                   e.printStackTrace();
                   System.out.println("error!");
               }
               
              // listBooks.stream().map(x -> x.get("id")).forEach(System.out::println);
           }
    
}