/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.ciphercontentgenerator;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author iwuvhugs
 */
public class ContentGenerator {

    public static void main(String[] args) {

        HttpResponse<JsonNode> jsonResponse;
        try {
            jsonResponse = Unirest.get(
                    "http://api.glassdoor.com/api/api.htm?"
                    + "v=1&"
                    + "format=json&"
                    + "t.p=55133&"
                    + "t.k=i2JQ8D1bthk&"
                    + "userip=64.229.147.203&"
                    + "useragent=Mozilla/%2F4.0&"
                    + "action=jobs-stats&"
                    + "country=Canada&"
                    + "jt=co-op&"
                    //                    + "jobType=internship&"
                    + "jc=29&"
                    + "returnStates=true&"
                    //                    + "returnJobTitles=true&"
                    + "admLevelRequested=1"
            ).header("useragent", "Mozilla/%2F4.0").asJson();
//            System.out.println(jsonResponse.getBody().toString());

            JSONObject states = jsonResponse.getBody().getObject().getJSONObject("response").getJSONObject("states");
            Iterator keys = states.keys();

            JSONArray jsonArray = new JSONArray();
            for (Iterator iterator = keys; iterator.hasNext();) {
                String key = iterator.next().toString();
                int numJobs = states.getJSONObject(key).getInt("numJobs");

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("province", key);
                jsonObject.put("numJobs", numJobs);
                jsonArray.put(jsonObject);

            }

            System.out.println(jsonArray.toString());
            
            Files.write(Paths.get("./coopJobStats.json"), jsonArray.toString().getBytes());

        } catch (UnirestException | IOException ex) {
            Logger.getLogger(ContentGenerator.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
