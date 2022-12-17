package com.ozzyozdil.retrofitjava.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ozzyozdil.retrofitjava.adapter.RecyclerViewAdapter;
import com.ozzyozdil.retrofitjava.model.CryptoModel;
import com.ozzyozdil.retrofitjava.R;
import com.ozzyozdil.retrofitjava.service.CryptoAPI;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    ArrayList<CryptoModel> cryptoModels;
    private String BASE_URL = "https://raw.githubusercontent.com/";
    Retrofit retrofit;

    RecyclerView recyclerView;
    RecyclerViewAdapter recyclerViewAdapter;

    CompositeDisposable compositeDisposable;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //https://raw.githubusercontent.com/atilsamancioglu/K21-JSONDataSet/master/crypto.json

        recyclerView = findViewById(R.id.recyclerView);

        // Retrofit & JSON
        Gson gson = new GsonBuilder().setLenient().create(); // json oluşturmak için
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))  // CryptoModelde Jsondan bilgiyi nasıl aldığımızı bildiriyoruz
                .build();


        loadData();
    }

    private void loadData(){

        CryptoAPI cryptoAPI = retrofit.create(CryptoAPI.class);

        Call<List<CryptoModel>> call = cryptoAPI.getData();
        call.enqueue(new Callback<List<CryptoModel>>() {  // Gelen cevabı asenkron bir şekilde almamızı sağlar
            @Override
            public void onResponse(Call<List<CryptoModel>> call, Response<List<CryptoModel>> response) {  // Veri gelirse

                if (response.isSuccessful()){

                    List<CryptoModel> responseList = response.body();  // .body() API deki verileri alır.
                    cryptoModels = new ArrayList<>(responseList);

                    // RecyclerView
                    recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    recyclerViewAdapter = new RecyclerViewAdapter(cryptoModels);
                    recyclerView.setAdapter(recyclerViewAdapter);

                    /*
                    // forEach
                    for (CryptoModel cryptoModel : cryptoModels) {
                        System.out.println(cryptoModel.currency + ": " + cryptoModel.price);
                    }
                     */

                }
            }

            @Override
            public void onFailure(Call<List<CryptoModel>> call, Throwable t) {   // Hata olursa

                t.printStackTrace();
            }
        });
    }
}