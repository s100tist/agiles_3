package com.example.agiles_3;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "recetas_tabla")
public class Receipe {
    @NonNull
    public String getUrl() {
        return url;
    }

    @NonNull
    public Double getYield() {
        return yield;
    }

    @NonNull
    public Integer getCalories() {
        return calories;
    }

    @NonNull
    public Double getEnergy() {
        return energy;
    }

    @NonNull
    public Double getFat() {
        return fat;
    }

    @NonNull
    public Double getSaturated() {
        return saturated;
    }

    @NonNull
    public Double getTrans() {
        return trans;
    }

    @NonNull
    public Double getCarbos() {
        return carbos;
    }

    @NonNull
    public Double getFiber() {
        return fiber;
    }

    @NonNull
    public Double getSugar() {
        return sugar;
    }

    @NonNull
    public Double getProtein() {
        return protein;
    }

    @NonNull
    public Double getCholesterol() {
        return cholesterol;
    }

    @NonNull
    public Double getSodium() {
        return sodium;
    }

    @PrimaryKey
    @NonNull
    @ColumnInfo(name="url")
    private String url;

    @NonNull
    @ColumnInfo(name="yield")
    private Double yield;

    @NonNull
    @ColumnInfo(name="calories")
    private Integer calories;

    @NonNull
    @ColumnInfo(name="energy")
    private Double energy;

    @NonNull
    @ColumnInfo(name="fat")
    private Double fat;

    @NonNull
    @ColumnInfo(name="saturated")
    private Double saturated;

    @NonNull
    @ColumnInfo(name="trans")
    private Double trans;

    @NonNull
    @ColumnInfo(name="carbos")
    private Double carbos;

    @NonNull
    @ColumnInfo(name="fiber")
    private Double fiber;

    @NonNull
    @ColumnInfo(name="sugar")
    private Double sugar;

    @NonNull
    @ColumnInfo(name="protein")
    private Double protein;

    @NonNull
    @ColumnInfo(name="cholesterol")
    private Double cholesterol;

    @NonNull
    @ColumnInfo(name="sodium")
    private Double sodium;

    public Receipe(@NonNull String url, @NonNull Double yield, @NonNull Integer calories, @NonNull Double energy, @NonNull Double fat, @NonNull Double saturated, @NonNull Double trans, @NonNull Double carbos, @NonNull Double fiber, @NonNull Double sugar, @NonNull Double protein, @NonNull Double cholesterol, @NonNull Double sodium) {
        this.url = url;
        this.yield = yield;
        this.calories = calories;
        this.energy = energy;
        this.fat = fat;
        this.saturated = saturated;
        this.trans = trans;
        this.carbos = carbos;
        this.fiber = fiber;
        this.sugar = sugar;
        this.protein = protein;
        this.cholesterol = cholesterol;
        this.sodium = sodium;
    }


}
