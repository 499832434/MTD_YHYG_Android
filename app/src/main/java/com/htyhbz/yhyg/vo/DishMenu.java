package com.htyhbz.yhyg.vo;

import java.util.ArrayList;

/**
 * Created by zongshuo on 2017/7/12.
 */
public class DishMenu{
    private String menuName;
    private ArrayList<Dish> dishList;

    public DishMenu(){

    }

    public DishMenu(String menuName,ArrayList dishList){
        this.menuName = menuName;
        this.dishList = dishList;
    }

    public ArrayList<Dish> getDishList() {
        return dishList;
    }

    public void setDishList(ArrayList<Dish> dishList) {
        this.dishList = dishList;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

}
