package com.ozzyozdil.retrofitjava.model;

import com.google.gson.annotations.SerializedName;

public class CryptoModel {

    @SerializedName("currency")    // I need to type the same name found in the JSON file.
    public String currency;

    @SerializedName("price")
    public String price;
}
