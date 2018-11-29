package com.turing.turingsdksample.bean;

import java.io.Serializable;
import java.util.List;

public class MediaListData implements Serializable {

    private static final long serialVersionUID = -8624136952516584353L;
    /**
     * count : 10
     * total : 10
     * items : [{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�01 鍑烘潵璧拌蛋璺�","url":"r1L6dt93lEjGev5Jnuff0WRV","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�02 琚嬮紶濡堝鏈変釜琚嬭","url":"r1L6dt93lEjGev5JnuffnRmt","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�03 濡堝鐨�","url":"r1L6dt93lEjGev5JnuffrFf5","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�04 姘存灉鍋ュ悍姝�","url":"r1L6dt93lEjGev5Jnufg0Wvt","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�05 灏惧反璋�","url":"r1L6dt93lEjGev5JnufgnFf5","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�06 鎴戠埍娲楁尽","url":"r1L6dt93lEjGev5JnufhjXWV","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�07 灏忔墜鎷嶆媿","url":"r1L6dt93lEjGev5Jnufh0XEy","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�08 灏忛奔娓告父娓�","url":"r1L6dt93lEjGev5JnujfjWvt","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�09 闊抽樁姝�","url":"r1L6dt93lEjGev56i2EfrRRV","i":""},{"t":"璐濈摝鍎挎瓕绮鹃�夐泦锛�10 鍋氬仛鍋�","url":"r1L6dt93lEjGev56i2EgjYpv","i":""}]
     */

    private int count;
    private int total;
    private List<ItemsBean> items;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<ItemsBean> getItems() {
        return items;
    }

    public void setItems(List<ItemsBean> items) {
        this.items = items;
    }

    public static class ItemsBean implements Serializable {
        private static final long serialVersionUID = -4969469940164224644L;
        /**
         * t : 璐濈摝鍎挎瓕绮鹃�夐泦锛�01 鍑烘潵璧拌蛋璺�
         * url : r1L6dt93lEjGev5Jnuff0WRV
         * i :
         */

        private String t;
        private String url;
        private String i;

        public String getT() {
            return t;
        }

        public void setT(String t) {
            this.t = t;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getI() {
            return i;
        }

        public void setI(String i) {
            this.i = i;
        }
    }
}
