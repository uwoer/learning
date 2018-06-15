package com.uwola.learning.streaming;

import java.io.Serializable;

public class Orders implements Serializable {
    public String getOrder_id() {
        return order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getEval_set() {
        return eval_set;
    }

    public void setEval_set(String eval_set) {
        this.eval_set = eval_set;
    }

    public String getOrder_number() {
        return order_number;
    }

    public void setOrder_number(String order_number) {
        this.order_number = order_number;
    }

    public String getOrder_dow() {
        return order_dow;
    }

    public void setOrder_dow(String order_dow) {
        this.order_dow = order_dow;
    }

    public String getOrder_hour_of_day() {
        return order_hour_of_day;
    }

    public void setOrder_hour_of_day(String order_hour_of_day) {
        this.order_hour_of_day = order_hour_of_day;
    }

    public String getDays_since_prior_order() {
        return days_since_prior_order;
    }

    public void setDays_since_prior_order(String days_since_prior_order) {
        this.days_since_prior_order = days_since_prior_order;
    }

    public String order_id;
    public String user_id;
    public String eval_set;
    public String order_number;
    public String order_dow;
    public String order_hour_of_day;
    public String days_since_prior_order;
}
