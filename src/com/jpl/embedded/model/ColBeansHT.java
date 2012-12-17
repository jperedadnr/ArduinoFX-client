package com.jpl.embedded.model;

import java.util.ArrayList;

/**
 *
 * @author José Pereda Llamas
 * Created on 09-dic-2012 - 19:05:50
 */
public class ColBeansHT {

    private ArrayList<BeanHT> beanHT;

    public ArrayList<BeanHT> getCol() {
        return beanHT;
    }

    public void setCol(ArrayList<BeanHT> col) {
        this.beanHT = col;
    }

    @Override
    public String toString() {
        return "ColBeansHT{" + "col=" + beanHT + '}';
    }
    
}
