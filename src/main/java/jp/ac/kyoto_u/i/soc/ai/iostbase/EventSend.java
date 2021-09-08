package jp.ac.kyoto_u.i.soc.ai.iostbase;

import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.ParseException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicHeader;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class EventSend {
    public static void send() throws IOException {


        CloseableHttpClient client = HttpClients.createDefault();
//        String urlString = "http://localhost:8088/08_HttpClientTest_war_exploded/get";
        String urlString = "http://54.150.99.88:8080/jsServices/EventManagement";
        HttpPost httpPost = new HttpPost(urlString);

        // json
        JSONObject jsonObj = new JSONObject();
        JSONObject params = new JSONObject();
        JSONArray paramArray = new JSONArray();

        JSONArray header =new JSONArray();
        jsonObj.put("callback","");
        jsonObj.put("headers",header);
        jsonObj.put("id","3");
        jsonObj.put("method","notifyEvent");
        params.put("created",1619663885000L);
        params.put("dataType","temperature");
        params.put("deviceId","1");
        params.put("latitude","");
        params.put("longitude","");
        params.put("placeTag","1");
        params.put("value",30);
        paramArray.put(params);
        jsonObj.put("params",paramArray);


        //
        StringEntity jsonEntity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
        jsonEntity.setContentType(new BasicHeader("Content-Type","application/json-rpc;"));
        jsonEntity.setContentEncoding(Consts.UTF_8.name());


        httpPost.setEntity(jsonEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();

        try {
            String resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EntityUtils.consume(entity);
        response.close();
        client.close();
    }
    
    
    public static void sendTest(Long createdStr, String dataType, String deviceId, String placeTag, double value) throws IOException {
           
        CloseableHttpClient client = HttpClients.createDefault();
//        String urlString = "http://localhost:8088/08_HttpClientTest_war_exploded/get";
        String urlString = "http://54.150.99.88:8080/jsServices/EventManagement";
        HttpPost httpPost = new HttpPost(urlString);

        // json
        JSONObject jsonObj = new JSONObject();
        JSONObject params = new JSONObject();
        JSONArray paramArray = new JSONArray();

        JSONArray header =new JSONArray();
        jsonObj.put("callback","");
        jsonObj.put("headers",header);
        jsonObj.put("id","3");
        jsonObj.put("method","notifyEvent");
        
        Long created = Long.valueOf(createdStr).longValue();
        params.put("created",created);
        // params.put("created",1619663885000L);
        params.put("dataType",dataType);
        // params.put("dataType","temperature");
        params.put("deviceId",deviceId);
        // params.put("deviceId","1");
        params.put("latitude","");
        params.put("longitude","");
        
        params.put("placeTag",placeTag);
        // params.put("placeTag","1");
        params.put("value",value);
        // params.put("value",30);
        paramArray.put(params);
        jsonObj.put("params",paramArray);


        //
        StringEntity jsonEntity = new StringEntity(jsonObj.toString(), Consts.UTF_8);
        jsonEntity.setContentType(new BasicHeader("Content-Type","application/json-rpc;"));
        jsonEntity.setContentEncoding(Consts.UTF_8.name());


        httpPost.setEntity(jsonEntity);

        CloseableHttpResponse response = client.execute(httpPost);
        HttpEntity entity = response.getEntity();

        try {
            String resultString = EntityUtils.toString(entity, StandardCharsets.UTF_8);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        EntityUtils.consume(entity);
        response.close();
        client.close();
    }
    
    public static void sendTest1_1(Long created, String dataType, String deviceId, String placeTag, double value) throws IOException{
        
        CloseableHttpClient client = HttpClients.createDefault();
        //String urlString = "http://54.150.99.88:8080/jsServices/EventManagement";
        String urlString = deviceId;
        HttpPost httpPost = new HttpPost(urlString);
        
        StringEntity requestEntity = dataSetting(created, "event", "request:comfortability", placeTag, 0);
        httpPost.setEntity(requestEntity);
        client.execute(httpPost);
        
        StringEntity dataEntity = dataSetting(created, dataType, deviceId, placeTag, value);
        httpPost.setEntity(dataEntity);
        CloseableHttpResponse dataResponse = client.execute(httpPost);
        HttpEntity entity = dataResponse.getEntity();
        
        EntityUtils.consume(entity);
        dataResponse.close();
        client.close();
    }
    
    public static void sendTest2(String serverIp, Long created, String dataType, String deviceId, String placeTag, double value) throws IOException{
        
        CloseableHttpClient client = HttpClients.createDefault();
        //String urlString = "http://54.150.99.88:8080/jsServices/EventManagement";
        String urlString = serverIp;
        HttpPost httpPost = new HttpPost(urlString);
        
        // StringEntity requestEntity = dataSetting(created, "event", "request:comfortability", placeTag, 0);
        // httpPost.setEntity(requestEntity);
        // client.execute(httpPost);
        
        StringEntity dataEntity = dataSetting(created, dataType, deviceId, placeTag, value);
        httpPost.setEntity(dataEntity);
        CloseableHttpResponse dataResponse = client.execute(httpPost);
        HttpEntity entity = dataResponse.getEntity();
        
        EntityUtils.consume(entity);
        dataResponse.close();
        client.close();
    }
    
    public static void sendTest3(String serverIp, Long created, String dataType, String deviceId, String placeTag, String eventType, String eventPlaceTag, double value) throws IOException{
        
        CloseableHttpClient client = HttpClients.createDefault();
        //String urlString = "http://54.150.99.88:8080/jsServices/EventManagement";
        String urlString = serverIp;
        HttpPost httpPost = new HttpPost(urlString);
        
        StringEntity requestEntity = dataSetting(created, "event", eventType, eventPlaceTag, 0);
        httpPost.setEntity(requestEntity);
        client.execute(httpPost);
        
        StringEntity dataEntity = dataSetting(created, dataType, deviceId, placeTag, value);
        httpPost.setEntity(dataEntity);
        CloseableHttpResponse dataResponse = client.execute(httpPost);
        HttpEntity entity = dataResponse.getEntity();
        
        EntityUtils.consume(entity);
        dataResponse.close();
        client.close();
    }
    
    
    public static StringEntity dataSetting(Long createdStr, String dataType, String deviceId, String placeTag, double value) throws IOException{
        JSONObject dataJson = new JSONObject();
        JSONObject params = new JSONObject();
        JSONArray paramArray = new JSONArray();
        JSONArray header =new JSONArray();
        
        dataJson.put("callback","");
        dataJson.put("headers",header);
        dataJson.put("id","3");
        dataJson.put("method","notifyEvent");
        
        Long created = Long.valueOf(createdStr).longValue();
        params.put("created",created);
        // params.put("created",1619663885000L);
        params.put("dataType",dataType);
        // params.put("dataType","temperature");
        params.put("deviceId",deviceId);
        // params.put("deviceId","1");
        params.put("latitude","");
        params.put("longitude","");
        
        params.put("placeTag",placeTag);
        // params.put("placeTag","1");
        params.put("value",value);
        // params.put("value",30);
        paramArray.put(params);
        dataJson.put("params",paramArray);
        
        //
        StringEntity dataEntity = new StringEntity(dataJson.toString(), Consts.UTF_8);
        dataEntity.setContentType(new BasicHeader("Content-Type","application/json-rpc;"));
        dataEntity.setContentEncoding(Consts.UTF_8.name());
        
        return dataEntity;
    }

}
