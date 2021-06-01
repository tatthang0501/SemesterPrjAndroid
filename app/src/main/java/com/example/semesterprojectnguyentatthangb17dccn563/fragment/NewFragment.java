package com.example.semesterprojectnguyentatthangb17dccn563.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.semesterprojectnguyentatthangb17dccn563.R;
import com.example.semesterprojectnguyentatthangb17dccn563.adapter.ArticleRecyclerViewAdapter;
import com.example.semesterprojectnguyentatthangb17dccn563.model.Article;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class NewFragment extends Fragment {
    private ArticleRecyclerViewAdapter adapter;
    private RecyclerView view;
    private Button nextBtn;
    private Button backBtn;
    private int page;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView =  inflater.inflate(R.layout.fragment_new, container, false);
        init(rootView);

        adapter = new ArticleRecyclerViewAdapter(getActivity());
        LinearLayoutManager manager = new LinearLayoutManager(getActivity());
        view.setLayoutManager(manager);
        view.setAdapter(adapter);

        loadArticle();

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page++;
                loadArticle();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                page--;
                loadArticle();
            }
        });


        return rootView;
    }

    public void init(View v){
        page = 1;
        view = v.findViewById(R.id.revViewNews);
        nextBtn = v.findViewById(R.id.btnNextNews);
        backBtn = v.findViewById(R.id.btnPreNews);

    }
    public void loadArticle(){
        if(page == 1) backBtn.setEnabled(false);
        else backBtn.setEnabled(true);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                List<Article> list = new ArrayList<>();
                adapter.setListArticle(list);
                view.setAdapter(adapter);
            }
        });

        OkHttpClient client = new OkHttpClient();

        Moshi moshi = new Moshi.Builder().build();
        Type usersType = Types.newParameterizedType(Article.class);
        final JsonAdapter<Article> jsonAdapter = moshi.adapter(usersType);

        Request request = new Request.Builder()
                .url("https://ptit-crawler-app.herokuapp.com/tintuc/page/" + page)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("Error", "Network Error");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    JSONObject json = new JSONObject(response.body().string());
                    JSONArray arr =  json.getJSONArray("56");
                    List<Article> list = new ArrayList<>();

                    for(int i = 0; i < arr.length(); i++){
                        Article article = jsonAdapter.fromJson(arr.get(i).toString());
                        list.add(article);
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            adapter.setListArticle(list);
                            view.setAdapter(adapter);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}