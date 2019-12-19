package yasis.apps.sasi;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
//implementation 'com.android.volley:volley:1.1.1'
public class VSingleton {
    private static VSingleton instance;
    private static RequestQueue requestQueue;
    private static Context context;
    private VSingleton(Context ctx){
        context = ctx;
        requestQueue = getRequestQueue();
    }
    public  static synchronized  VSingleton getInstance(Context context){
        if(instance==null){
            instance = new VSingleton(context);
        }
        return instance;
    }
    public RequestQueue getRequestQueue(){
        if(requestQueue==null){
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return  requestQueue;
    }
    public<T> void addToRequestQueue(Request<T> request){
        getRequestQueue().add(request);
    }
}
