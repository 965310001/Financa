package com.ph.financa.activity.bean;

import java.util.Comparator;

/**
 * 专用于按首字母排序
 */
public class LetterComparator implements Comparator<ContactModel> {

    @Override
    public int compare(ContactModel contactModel, ContactModel t1) {
        if (contactModel == null || t1 == null){
            return 0;
        }
        String lhsSortLetters = contactModel.getIndex().substring(0, 1).toUpperCase();
        String rhsSortLetters = t1.getIndex().substring(0, 1).toUpperCase();
        return lhsSortLetters.compareTo(rhsSortLetters);
    }
}
