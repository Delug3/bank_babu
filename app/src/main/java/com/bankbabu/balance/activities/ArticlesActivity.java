package com.bankbabu.balance.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters.blog.ArticlesAdapter;
import com.bankbabu.balance.models.Articles;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.parse.SaveCallback;

import org.json.JSONArray;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ArticlesActivity extends AppCompatActivity implements ArticlesAdapter.ItemClickListener {

    @BindView(R.id.recycler_view_blog) RecyclerView recyclerViewBlog;
    private ArticlesAdapter articlesAdapter;
    private static final String TAG = "BANKBABU";

    //private static List<ParseObject>dataArticles = new ArrayList<>();
    final List<Articles> dataArticles = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("iOKBT1I4Q2shGMztDYosmdAgRiNWVetIYA8PfSIH")
                .clientKey("umaCt0UiUtiPby3IpCuf05vX3RDqEy96Kct788jy")
                .server("https://parseapi.back4app.com")
                .build()
        );


        ButterKnife.bind(this);

        /*List<String> blogContent = new ArrayList<>();
        blogContent.add("Applying for Aadhaar card for Minors");
        blogContent.add("An Aadhaar card can be applied for minors too and in fact even a new-born can get an Aadhaar");
        */

        recyclerViewBlog.setLayoutManager(new LinearLayoutManager(this));
        articlesAdapter = new ArticlesAdapter(this,dataArticles);
        recyclerViewBlog.setHasFixedSize(true);
        articlesAdapter.setClickListener(this);

        //createObject();
       // readObject();
        findArticles();
    }
    @Override
    public void onItemClick(View view, int position) {
        //Toast.makeText(this, "You clicked " + articlesAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
        Intent i = new Intent(ArticlesActivity.this, DetailsArticleActivity.class);

        i.putExtra("ARTICLE_TITLE", dataArticles.get(position).getTitle());
        i.putExtra("ARTICLE_CONTENT", dataArticles.get(position).getContent());
        //i.putExtra("ARTICLE_INFO", dataArticles.get(position).getInformation());
       // i.putExtra("ARTICLE_MORE_INFO", dataArticles.get(position).getMoreinformation());
        i.putStringArrayListExtra("ARTICLE_INFO", (ArrayList<String>) dataArticles.get(position).getInformation());
        i.putStringArrayListExtra("ARTICLE_MORE_INFO", (ArrayList<String>) dataArticles.get(position).getMoreinformation());
        startActivity(i);


    }


    public void findArticles() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Articles");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> listarticles, ParseException e) {

                if (e == null) {

                    for (int i = 0; i < listarticles.size(); i++) {

                       Articles articles = new Articles();

                        articles.title = listarticles.get(i).getString("title");
                        articles.content = listarticles.get(i).getString("content");

                        articles.information = listarticles.get(i).getList("information");
                        articles.moreinformation = listarticles.get(i).getList("moreinformation");


                        String title = listarticles.get(i).getString("title");
                        String content = listarticles.get(i).getString("content");

                        Log.e(TAG, "Title: " + title);
                        Log.e(TAG, "Content: " + content);


                        //send listarticles data to adapter->recyclerview

                        dataArticles.add(articles);

                }


            } else {
                    // something went wrong
                }
                recyclerViewBlog.setAdapter(articlesAdapter);
            }
        });

    }

    public void createObject() {
        ParseObject entity = new ParseObject("Articles");

        entity.put("title", "titulo prueba");
        entity.put("content", "contenido prueba");
        entity.put("image", "A string");

        // Saves the new object.
        // Notice that the SaveCallback is totally optional!
        entity.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                // Here you can handle errors, if thrown. Otherwise, "e" should be null
            }
        });
    }

    public void readObject() {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Articles");
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject result, ParseException e) {
                if (e == null) {

                    String title = result.getString("title");
                    String content = result.getString("content");
                    String image =  result.getString("image");

                    Log.e(TAG, "Title: " + title);
                    Log.e(TAG, "Content: " + content);


                } else {
                    // something went wrong
                }

            }
        });
    }

}
