package com.example.penta.login;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class DeleteRequest extends StringRequest {

    final static  private String URL = "http://test1329.ivyro.net/finish.php";
    private Map<String, String> map;

    public DeleteRequest(String userID, Response.Listener<String> listener){
        super(Request.Method.POST,URL,listener,null);
        map = new HashMap<>();
        map.put("userID", userID);
    }
    @Override

    public Map<String, String>getParams(){
        return map;
    }
}