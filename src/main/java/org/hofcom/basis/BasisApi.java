package org.hofcom.basis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.Cookie;
import com.ning.http.client.ListenableFuture;
import com.ning.http.client.Response;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * Login to the basis webapp and download your data. 
 * 
 * @author hof
 */
public class BasisApi {

    /* HTTP client performing the requests */ 
    private final AsyncHttpClient httpClient = new AsyncHttpClient(); 
    
    /* the access token after a succesful login */ 
    private String accessToken = ""; 
    
    /* the basis-id, take from the user profile */ 
    private String basisId = ""; 

    /** parser of JSON data */ 
    private final ObjectMapper mapper = new ObjectMapper();
    
    /** execute a GET or POST request with the given headers and parameters */ 
    private Response executeRequest(String method, String url, 
                Map<String, String> headerLines, Map<String, String> arguments)
                throws IOException, ExecutionException, InterruptedException { 
        
        String postData = ""; 
        for(Map.Entry<String, String> argument : arguments.entrySet()) {
            if( postData.length() > 0) {
                postData += "&";
            }
            postData += argument.getKey() + "=" + argument.getValue();
        }        
               
        AsyncHttpClient.BoundRequestBuilder builder; 
        if (method.equals("POST")) { 
            builder = httpClient.preparePost(url);
        } else { 
            builder = httpClient.prepareGet(url+"?"+postData); 
        }
        
        /* set headers */  
        for (Map.Entry<String, String> entry : headerLines.entrySet()) {             
            builder.setHeader(entry.getKey(), entry.getValue()); 
        }

        /* parameters */  
        for (Map.Entry<String, String> entry : arguments.entrySet()) {             
            builder.addParameter(entry.getKey(), entry.getValue()); 
        }
        
        /* execute */  
        ListenableFuture<Response> response; 
        response = builder.execute();
        Response r = response.get();           
        return r; 
    }
    
    /** 
     * Login to the basis web application 
     * 
     * @param username
     * @param password
     * @throws IOException
     * @throws ExecutionException
     * @throws InterruptedException 
     */
    public void authenticate(String username, String password) throws IOException, 
            ExecutionException, InterruptedException { 
        
        Map<String, String> headerLines = new HashMap<String, String>();
        Map<String, String> arguments = new HashMap<String, String>();

        arguments.put("next", "https://app.mybasis.com");
        arguments.put("username",  username);
        arguments.put("password", password); 
        arguments.put("submit", "Login"); 
        
        System.out.print("Logging in: ");
        Response r = executeRequest("POST", "https://app.mybasis.com/login", headerLines, arguments);                         
        System.out.print(r.getStatusCode()+" "+r.getStatusText()+" "); 
        
        /* find the access token */ 
        for (Cookie cookie : r.getCookies()) { 
            if (cookie.getName().equals("access_token")) { 
                System.out.println(cookie.getName()+" --> "+cookie.getValue());
                accessToken = cookie.getValue(); 
            }
        }                       
    }        

    public String getData(String day, String startOffset, String endOffset) throws IOException, 
            ExecutionException, InterruptedException { 

        Map<String, String> headerLines = new HashMap<String, String>();
        Map<String, String> arguments = new HashMap<String, String>();

        headerLines.put("X-Basis-Authorization", "OAuth "+accessToken); 
        
        arguments.put("summary", "true");
        arguments.put("interval", "60");
        arguments.put("units", "ms");
        arguments.put("start_date", day);
        arguments.put("start_offset", startOffset);
        arguments.put("end_offset", endOffset);
        arguments.put("heartrate", "true");
        arguments.put("steps", "true");
        arguments.put("calories", "true");
        arguments.put("gsr", "true");
        arguments.put("skin_temp", "true");
        arguments.put("air_temp", "true");
        arguments.put("bodystates", "true");
  
        System.out.print("Data: "); 
        Response r = executeRequest("GET", "https://app.mybasis.com/api/v1/chart/"+basisId+".json", headerLines, arguments);         
        System.out.println(r.getStatusCode()+" "+r.getStatusText());
        
        /* save file */ 
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("basis-data-"+day+".json"));
            out.print(r.getResponseBody());
        }
        finally {
            if (out != null) out.close();
        }

        return r.getResponseBody();         
    }
    
    public String getMe() throws IOException, 
            ExecutionException, InterruptedException { 

        Map<String, String> headerLines = new HashMap<String, String>();
        Map<String, String> arguments = new HashMap<String, String>();

        headerLines.put("X-Basis-Authorization", "OAuth "+accessToken); 
        
        System.out.print("Profile: ");
        Response r = executeRequest("GET", "https://app.mybasis.com/api/v1/user/me.json", headerLines, arguments);         
        System.out.print(r.getStatusCode()+" "+r.getStatusText()+" ");
        
        /* save file */ 
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("me.json"));
            out.print(r.getResponseBody());
        }
        finally {
            if (out != null) out.close();
        } 
        
        /* figure out the basis-id */ 
        JsonNode root = mapper.readTree(r.getResponseBody());
        basisId = root.get("id").asText(); 
        System.out.println("Basis-ID: "+basisId);
        
        return r.getResponseBody(); 
    }
   
    public String getActivities(String day) throws IOException, 
            ExecutionException, InterruptedException { 

        Map<String, String> headerLines = new HashMap<String, String>();
        Map<String, String> arguments = new HashMap<String, String>();

        headerLines.put("X-Basis-Authorization", "OAuth "+accessToken); 
        
        arguments.put("expand", "activities"); 
        arguments.put("type", "run,walk,bike"); 
        
        System.out.print("Activities: ");
        Response r = executeRequest("GET", "https://app.mybasis.com/api/v2/users/me/days/"+day+"/activities", headerLines, arguments);         
        System.out.println(r.getStatusCode()+" "+r.getStatusText());

        /* save file */ 
        PrintStream out = null;
        try {
            out = new PrintStream(new FileOutputStream("activities-"+day+".json"));
            out.print(r.getResponseBody());
        }
        finally {
            if (out != null) out.close();
        }                
        return r.getResponseBody(); 
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getBasisId() {
        return basisId;
    }        

    public AsyncHttpClient getHttpClient() {
        return httpClient;
    }    
}
