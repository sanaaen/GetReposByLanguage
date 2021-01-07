package com.company;

import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Main {

    static void GetReposByLanguage(JSONArray array) {
        String x;
        int count = 0;
        Scanner s = new Scanner(System.in);
        System.out.print("Enter the language of which you want to count number of repos:");
        x = s.nextLine();
        ArrayList<String> url_repo = new ArrayList<>();

        for (int i = 0; i < array.size(); i++) {

            JSONObject obj = (JSONObject) array.get(i);
            if(obj.get("language")!= null && obj.get("language").equals(x)) {
                count++;
                url_repo.add(obj.get("url").toString());
            }
        }

        System.out.println("Number of repos by this language:" + count);
        System.out.println("Repo URL for this language:");
        for (String url : url_repo) {
            System.out.println(url + "\n");
        }
    }

    public static void main (String[]args){
        try {
            Integer number = 0;
            if (args.length != 0)
                number = Integer.parseInt(args[0]);
            else
                number = 3;
            // Substract a number of day from sysdate to get a list of recent repos
            System.out.println("subtract " + number + " day from a Date");
            Calendar cal = Calendar.getInstance();
            cal.setTime(new Date());
            cal.add(Calendar.DATE, -number);
            Date dateBefore_numberDay = cal.getTime();

            SimpleDateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");
            String sysdate = simpleFormat.format(dateBefore_numberDay);
            URL url = new URL("https://api.github.com/search/repositories?q=created:%3E"+sysdate+"&sort=stars&order=desc");

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            //Getting the response code
            int responsecode = conn.getResponseCode();

            if (responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            } else {

                String inline = "";
                Scanner scanner = new Scanner(url.openStream());

                //Write all the JSON data into a string using a scanner
                while (scanner.hasNext()) {
                    inline += scanner.nextLine();
                }

                //Close the scanner
                scanner.close();

                //Using the JSON simple library parse the string into a json object
                JSONParser parse = new JSONParser();
                JSONObject data_obj = (JSONObject) parse.parse(inline);

                //Get the required object from the above created object
                JSONArray array = (JSONArray) data_obj.get("items");

                GetReposByLanguage(array);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}

