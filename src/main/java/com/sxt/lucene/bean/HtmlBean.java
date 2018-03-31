package com.sxt.lucene.bean;

/**
 * @author: ZouTai
 * @date: 2018/3/30
 * @description:
 */
public class HtmlBean {

    /**
     * title :标题
     * context： 内容
     * url： 链接地址
     */
    private String title;
    private String context;
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
