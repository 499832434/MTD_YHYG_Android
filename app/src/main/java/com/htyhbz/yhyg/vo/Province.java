package com.htyhbz.yhyg.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/11.
 */
public class Province {
    private int id;
    private String name;
    private List<City> list=new ArrayList<City>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<City> getList() {
        return list;
    }

    public void setList(List<City> list) {
        this.list = list;
    }
}
