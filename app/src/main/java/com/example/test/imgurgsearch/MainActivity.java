package com.example.test.imgurgsearch;

import android.app.ActionBar;
import android.app.SearchManager;
import android.graphics.Rect;
import android.opengl.Visibility;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.example.test.imgurgsearch.model.Photo;
import com.example.test.imgurgsearch.view.PhtoVHAdapter;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;



public class MainActivity extends AppCompatActivity {

    android.support.v7.widget.SearchView searchView;
    boolean countSwitch = false;
    Switch countSwitchView;
    TextView countSwitchViewTitle;
    PhtoVHAdapter adapter;
    List<Photo> photoArrayList;
    public static String TAG = "IMGSearch";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        countSwitchView = (Switch)findViewById(R.id.score_switch);
        countSwitchViewTitle = (TextView) findViewById(R.id.score_switch_title);
        countSwitchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                Log.d(TAG,"Switch State"+isChecked);
                countSwitch = isChecked;
                updateData(isChecked);
            }

        });
          findViewById(R.id.welcome_layout).setVisibility(View.VISIBLE);
          findViewById(R.id.source_layout).setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countSwitchView.setVisibility(View.INVISIBLE);
        countSwitchViewTitle.setVisibility(View.INVISIBLE);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.layout.menu, menu);
        final MenuItem searchItem = menu.findItem(R.id.action_search);

        if (searchItem != null) {
            searchView = (android.support.v7.widget.SearchView) MenuItemCompat.getActionView(searchItem);

            searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    return true;
                }
            });
            searchView.setOnSearchClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
            EditText searchPlate = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            searchPlate.setHint("Enter Search Tag");
            View searchPlateView = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            searchPlateView.setBackgroundColor(ContextCompat.getColor(this, android.R.color.transparent));
            // use this method for search process
            searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    // use this method when query submitted
                   // Toast.makeText(getApplicationContext(), query, Toast.LENGTH_SHORT).show();
                    fetchData(query);
                    countSwitchView.setChecked(false);

                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    // use this method for auto complete search process
                    return false;
                }
            });

        }
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public void onBackPressed() {
            searchView.onActionViewCollapsed();
            super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    private void fetchData(String searchTag) {
        adapter = new PhtoVHAdapter();
        Request request = MyConnection.MyConnectRequest.buildRequest(searchTag);
        Log.d(TAG,"searchTag "+searchTag);
        MyConnection.MyConnectRequest.getHttpClient().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e(TAG, "An error has occurred " + e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                ArrayList<Photo> photos;
                try {
                    Log.d(TAG,"onResponse");
                    JSONObject data = new JSONObject(response.body().string());
                    JSONArray items = data.getJSONArray("data");
                    photos = new ArrayList<Photo>();
                    Log.d(TAG,"Ites length "+items.length());
                    for (int i = 0; i < items.length(); i++) {

                        JSONObject item = items.getJSONObject(i);
                        Photo photo = new Photo();

                        if (item.getBoolean("is_album")) {
                            photo.setId(item.getString("cover"));
                        } else {
                            photo.setId(item.getString("id"));
                        }

                        photo.setTitle(item.getString("title"));
                        try{
                            if (item.getString("images_count") != null) {
                                photo.setImageCount(item.getString("images_count") + " more image");
                            }
                        }catch(JSONException exp) {
                            photo.setImageCount(" ");
                        }

                        String date = "";
                        try {
                            date = new java.text.SimpleDateFormat("dd/MM/yyyy h:mm a").format(new java.util.Date(Long.parseLong(item.getString("datetime")) * 1000));

                        } catch (Exception pe) {
                            Log.e(TAG, "Parse Exception" + pe);
                        }

                        photo.setPostDate(date);
                        photo.setScore(Integer.parseInt(item.getString("points"))
                                + Integer.parseInt(item.getString("score"))
                                + Integer.parseInt(item.getString("topic_id")));

                        photos.add(photo); // Add photo to list
                    }

                    photoArrayList = photos;
                    adapter.setData(photos);
                    Log.d(TAG," photos size "+photos.size());
                } catch (JSONException exp) {
                    Log.e(TAG, " exp " + exp);
                }

            }

        });

        Log.d(TAG,"Inside fetch data");

        runOnUiThread(new Runnable() {
           @Override
           public void run() {
               render();
           }
        });
    }

     public void render() {

         findViewById(R.id.welcome_layout).setVisibility(View.GONE);
         findViewById(R.id.source_layout).setVisibility(View.VISIBLE);

         countSwitchView.setVisibility(View.VISIBLE);
         countSwitchViewTitle.setVisibility(View.VISIBLE);

         RecyclerView rv = (RecyclerView)findViewById(R.id.rv_of_photos);
         rv.requestFocus();
         rv.setLayoutManager(new LinearLayoutManager(this));
         rv.setAdapter(adapter);
         rv.addItemDecoration(new RecyclerView.ItemDecoration() {
                @Override
                public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                    outRect.bottom = 16; // Gap of 16px
                }
            });
           rv.requestFocus();
        }

        void updateData(boolean countSum){

            Log.d(TAG," updateData countSum"+countSum);
            if(countSum){

                //Add Even sum Data on the list
                List<Photo> photos = adapter.getData();
                List<Photo> evenphotos = new ArrayList<Photo>();

                Log.d(TAG," updateData photos length "+photos.size());
                for (int i = 0; i < photos.size(); i++) {

                    Photo photo = photos.get(i);
                    if (photo.getScore() % 2 == 0) {
                        evenphotos.add(photo);
                    }
                }
                Log.d(TAG," updateData even photos length "+evenphotos.size());
                adapter.setData(evenphotos);
            } else {
                adapter.setData(photoArrayList);
                Log.d(TAG," updateData all photos length "+photoArrayList.size());

            }
        }
}

