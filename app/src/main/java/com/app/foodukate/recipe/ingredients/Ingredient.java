package com.app.foodukate.recipe.ingredients;

import java.util.ArrayList;

/**
 * Created by sumitvalecha on 4/22/16.
 */
public class Ingredient {
    public Ingredient(String _id, String name, ArrayList<String> labels) {
        this._id = _id;
        this.name = name;
        this.labels = labels;
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

    private String _id;
    private String name;
    private ArrayList<String> labels;
    private String quantity;
}
