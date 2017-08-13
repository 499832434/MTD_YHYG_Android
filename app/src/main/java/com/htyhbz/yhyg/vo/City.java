package com.htyhbz.yhyg.vo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zongshuo on 2017/8/11.
 */
public class City {
    private int id;
    private int pid;
    private String name;
    private List<District> list=new ArrayList<District>();

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<District> getList() {
        return list;
    }

    public void setList(List<District> list) {
        this.list = list;
    }
}
