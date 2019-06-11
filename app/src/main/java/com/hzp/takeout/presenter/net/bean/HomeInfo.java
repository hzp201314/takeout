package com.hzp.takeout.presenter.net.bean;

import java.util.List;

/**
 * Created by HASEE on 2016/12/13.
 */
public class HomeInfo {
    private Head head;// jsonobject {}

    private List<HomeItem> body;//jsonArray [{}{}{}{}{}]

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public List<HomeItem> getBody() {
        return body;
    }

    public void setBody(List<HomeItem> body) {
        this.body = body;
    }
}
