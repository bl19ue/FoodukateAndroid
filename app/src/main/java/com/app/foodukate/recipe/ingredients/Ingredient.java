package com.app.foodukate.recipe.ingredients;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by sumitvalecha on 4/22/16.
 */
public class Ingredient {
    public Ingredient(String _id, String name, ArrayList<String> labels) {
        this._id = _id;
        this.name = name;
        this.labels = labels;
        this.quantity = "1";
        this.label = (labels != null && labels.size() > 0 && labels.get(0) != null) ? labels.get(0) : "";
    }

    public String get_id() {
        return _id;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLabels() {
        return labels;
    }

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String string() {
        return quantity + " " + labels.get(0) + " " + name;
    }

    @Expose @SerializedName("_id") private String _id;
    @Expose @SerializedName("name") private String name;
    @Expose @SerializedName("labels") private ArrayList<String> labels;
    @Expose @SerializedName("label") private String label;
    @Expose @SerializedName("quantity") private String quantity;
}
