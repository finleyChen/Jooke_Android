package com.jooketechnologies.network;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import fi.iki.elonen.NanoHTTPD;

public class HttpServer
  extends NanoHTTPD
{
  private static final Map<String, String> MIME_TYPES = new HashMap<String, String>()
  {
    private static final long serialVersionUID = 6339365927933648124L;
  };

  private int wsPort;
  
  public HttpServer(int paramInt1, int paramInt2, Context paramContext)
  {
    super(paramInt1);
    this.wsPort = paramInt2;
 
  }

  @SuppressLint({"DefaultLocale"})
  public NanoHTTPD.Response serve(String paramString, NanoHTTPD.Method paramMethod, Map<String, String> paramMap1, Map<String, String> paramMap2, Map<String, String> paramMap3)
  {
	 Log.e("paramString",paramString);
     JSONObject localJSONObject2 = new JSONObject();
     try {
		localJSONObject2.put("port", this.wsPort);
	} catch (JSONException e) {
		
		e.printStackTrace();
	}

     NanoHTTPD.Response localResponse6 = new NanoHTTPD.Response(NanoHTTPD.Response.Status.OK, (String)MIME_TYPES.get("json"), localJSONObject2.toString());
     return localResponse6;
   
  

  }
}
