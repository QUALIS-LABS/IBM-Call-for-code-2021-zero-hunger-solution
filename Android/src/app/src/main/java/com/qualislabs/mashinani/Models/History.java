package com.qualislabs.mashinani.Models;

public class History {

    private String produceId, produceName, date;

    public History() {
    }

    public History(String produceId, String produceName, String date) {
        this.produceId = produceId;
        this.produceName = produceName;
        this.date = date;
    }

    public String getProduceId() {
        return produceId;
    }

    public void setProduceId(String produceId) {
        this.produceId = produceId;
    }

    public String getProduceName() {
        return produceName;
    }

    public void setProduceName(String produceName) {
        this.produceName = produceName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
