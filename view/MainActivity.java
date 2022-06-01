package in.com.demo.myrecycleviewerapplication.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import in.com.demo.myrecycleviewerapplication.R;
import in.com.demo.myrecycleviewerapplication.adapter.RecylerViewAdapter;
import in.com.demo.myrecycleviewerapplication.Constants.GeneralConstant;
import in.com.demo.myrecycleviewerapplication.database.MySQLiteHelper;
import in.com.demo.myrecycleviewerapplication.model.ArticleInfo;
import in.com.demo.myrecycleviewerapplication.model.SourceInfo;

public class MainActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecylerViewAdapter adapter;
    private ArrayList models;
    private ProgressBar progressBar;
    private MySQLiteHelper database;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        progressBar = findViewById(R.id.progressBar);
        database = new MySQLiteHelper(this);
        preferences = PreferenceManager.getDefaultSharedPreferences(this);

        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);

        models = new ArrayList<ArticleInfo>();
        adapter = new RecylerViewAdapter( models);
        mRecyclerView.setAdapter(adapter);

        ArrayList<ArticleInfo> cache = new ArrayList<ArticleInfo>();
        cache = database.getArticleInfo();
        boolean isCacheExpire = false;
        long cacheTime = preferences.getLong("cache", 0);

        if (cacheTime > 0) {
            long currentTime = new Date().getTime();
            long difference = currentTime - cacheTime;
            long seconds = difference / 1000;

            if (seconds > 20) {
                isCacheExpire = true;
            }
        }

        if(null!=cache) {
            if (cache.size() > 0 && !isCacheExpire) {
                models = cache;
                showData();
            } else {
                getData();
            }
        }else{
            getData();
        }
    }

    private void getData() {

        String url="https://newsapi.org/v2/everything";

//        String url = GeneralConstant.LOAD_ARTICLES_URL;


        ProgressDialog pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading...PLease wait");
        pDialog.show();

        RequestQueue requestQueue = Volley.newRequestQueue(this);
/*
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONArray>() {

                    @Override
                    public void onResponse(JSONArray response) {

                        Log.e("######","response :"+response.toString());

                        int cout= response.length();
                        for(int i=0; i<response.length(); i++){
                            try{
                                JSONObject articleObj =response.getJSONObject(i);

                                ArticleInfo aInfo = new ArticleInfo();
                                aInfo.setAuthor(articleObj.getString("author"));
                                aInfo.setTitle(articleObj.getString("title"));
                                aInfo.setDesc(articleObj.getString("description"));
                                aInfo.setUrl(articleObj.getString("url"));
                                aInfo.setUrlToImage(articleObj.getString("urlToImage"));
                                aInfo.setPublishedAt(articleObj.getString("publishedAt"));
                                SourceInfo sInfo =new SourceInfo();
                                sInfo.setId(articleObj.getJSONObject("source").getInt("id"));
                                sInfo.setName(articleObj.getJSONObject("source").getString("name"));
                                aInfo.setSourceInfo(sInfo);

                                database.insertArticleInfo(aInfo);

                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }


*/
                        StringRequest sRequest = new StringRequest
                                (Request.Method.GET, url,  new Response.Listener<String>() {

                                    @Override
                                    public void onResponse(String response) {

                                        Log.e("######","response :"+response.toString());

                                        try {
                                            pDialog.dismiss();
                                            JSONObject jsonObject=new JSONObject(response);
                                            String status=jsonObject.getString("status");
                                            Log.e("######","response :"+status);
                                            if(status.equals(GeneralConstant.COMM_OPERATION_SUCCESS)){
                                                JSONArray jsonArray = new JSONArray(jsonObject.getJSONArray("articles"));
                                                int cout = response.length();
                                                for (int i = 0; i < jsonArray.length(); i++) {
                                                    try {
                                                        JSONObject articleObj = jsonArray.getJSONObject(i);

                                                        ArticleInfo aInfo = new ArticleInfo();
                                                        aInfo.setAuthor(articleObj.getString("author"));
                                                        aInfo.setTitle(articleObj.getString("title"));
                                                        aInfo.setDesc(articleObj.getString("description"));
                                                        aInfo.setUrl(articleObj.getString("url"));
                                                        aInfo.setUrlToImage(articleObj.getString("urlToImage"));
                                                        aInfo.setPublishedAt(articleObj.getString("publishedAt"));
                                                        SourceInfo sInfo = new SourceInfo();
                                                        sInfo.setId(articleObj.getJSONObject("source").getInt("id"));
                                                        sInfo.setName(articleObj.getJSONObject("source").getString("name"));
                                                        aInfo.setSourceInfo(sInfo);

                                                        database.insertArticleInfo(aInfo);

                                                    } catch (Exception e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        pDialog.dismiss();
                        Toast.makeText(getApplicationContext(),"Please try again. Error :"+error,Toast.LENGTH_SHORT).show();
                        Log.e("######","error :"+error);
                    }
                }){
                            @Override
                            protected Map getParams()
                            {
                                //q=bitcoin"+"&"+"apiKey="+GeneralConstant.API_KEY+"&"+"page=1"
                                Map params = new HashMap();
                                params.put("q", "bitcoin");
//                                params.put("apiKey", "GeneralConstant.API_KEY");
                                params.put("page", "1");

                                return params;
                            }

                            @Override
                            public Map<String, String> getHeaders() throws AuthFailureError {
                                HashMap<String, String> headers = new HashMap<String, String>();
                                headers.put("Content-Type", "application/json");
//                                headers.put("Authorization", "GeneralConstant.API_KEY");
                                headers.put("Authorization", "Bearer " + "GeneralConstant.API_KEY");

                                return headers;
                            }
                        };

        sRequest.setShouldCache(false);
        requestQueue.add(sRequest);
    }

    private void showData() {
        adapter.setFilter(models);
        progressBar.setVisibility(View.GONE);
    }
}