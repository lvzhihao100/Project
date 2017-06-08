package com.eqdd.library.ui.select;

import com.eqdd.library.bean.ComparaEntity;
import com.eqdd.library.bean.Person;

import java.util.Comparator;

/**
 * Created by lvzhihao on 17-6-5.
 */

public  class PinyinComparator implements Comparator<ComparaEntity> {


    public static PinyinComparator instance = null;

    public static PinyinComparator getInstance() {
        if (instance == null) {
            instance = new PinyinComparator();
        }
        return instance;
    }

    public int compare(ComparaEntity o1, ComparaEntity o2) {
        if (o1.getComparaContent().equals("@") || o2.getComparaContent().equals("#")) {
            return -1;
        } else if (o1.getComparaContent().equals("#") || o2.getComparaContent().equals("@")) {
            return 1;
        } else {
            return o1.getComparaContent().compareTo(o2.getComparaContent());
        }
    }

}
