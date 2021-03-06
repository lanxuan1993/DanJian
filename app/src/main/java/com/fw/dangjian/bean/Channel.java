package com.fw.dangjian.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/1/5 0005.
 */

public class Channel extends MultiItemEntity implements Serializable {
    public static final int TYPE_MY = 1;
    public static final int TYPE_OTHER = 2;
    public static final int TYPE_MY_CHANNEL = 3;
    public static final int TYPE_OTHER_CHANNEL = 4;
    public String Title;
    public String TitleCode;

    public Channel(String title, String titleCode) {
        this(TYPE_MY_CHANNEL, title, titleCode);
    }

    public Channel(int type, String title, String titleCode) {
        Title = title;
        TitleCode = titleCode;
        itemType = type;
    }

}
