package com.bankbabu.balance.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.bankbabu.balance.R;
import com.bankbabu.balance.adapters.blog.ChildData;
import com.bankbabu.balance.adapters.blog.ExpandableAdapter;
import com.bankbabu.balance.adapters.blog.ParentData;
import com.bankbabu.balance.models.Articles;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsArticleActivity extends AppCompatActivity {

    @BindView(R.id.text_title_details_article) TextView txtTitleDetailsArticles;
    @BindView(R.id.text_content_details_article) TextView txtContentDetailsArticles;
    @BindView(R.id.recyclerView_Info) RecyclerView recyclerViewInfoExpandable;
   // @BindView(R.id.text_info_details_article) TextView txtInfoDetailsArticles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_articles);
        ButterKnife.bind(this);
        Context context = getApplicationContext();

        String title,content;

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if(extras == null) {
                title= null;
                content= null;

            } else {
                title= extras.getString("ARTICLE_TITLE");
                content= extras.getString("ARTICLE_CONTENT");
                txtTitleDetailsArticles.setText(title);
                txtContentDetailsArticles.setText(content);


            }


        } else {
        }
        List<String> info = getIntent().getStringArrayListExtra("ARTICLE_INFO");
        List<String> moreinfo = getIntent().getStringArrayListExtra("ARTICLE_MORE_INFO");




        List<ParentData> list = getList();


        recyclerViewInfoExpandable.setLayoutManager(new LinearLayoutManager(this));

        ExpandableAdapter expandableAdapter = new ExpandableAdapter(DetailsArticleActivity.this,list);
        recyclerViewInfoExpandable.setAdapter(expandableAdapter);
        recyclerViewInfoExpandable.addItemDecoration(new DividerItemDecoration(this,LinearLayoutManager.VERTICAL));
        recyclerViewInfoExpandable.setAdapter(expandableAdapter);



    }
    private List<ParentData> getList() {

        List<String> info = getIntent().getStringArrayListExtra("ARTICLE_INFO");
        List<String> moreinfo = getIntent().getStringArrayListExtra("ARTICLE_MORE_INFO");

        List<ParentData> list_parent =new ArrayList<>();
        List<ChildData> list_data_child = new ArrayList<>();

        for (int i = 0; i < info.size(); i++) {

            String information = info.get(i);
            String moreinformation = moreinfo.get(i);


            list_data_child.add(new ChildData(moreinformation));

            list_parent.add(new ParentData(information,list_data_child));
        }

        return list_parent;
    }

}
