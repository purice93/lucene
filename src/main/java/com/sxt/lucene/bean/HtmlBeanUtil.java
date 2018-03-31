package com.sxt.lucene.bean;

import net.htmlparser.jericho.Element;
import net.htmlparser.jericho.HTMLElementName;
import net.htmlparser.jericho.Source;

import java.io.File;
import java.io.IOException;

/**
 * @author: ZouTai
 * @date: 2018/3/30
 * @description:
 */
public class HtmlBeanUtil {

    public static HtmlBean parseHtml(File file) {
        try {
            HtmlBean htmlBean = new HtmlBean();
            Source source = new Source(file);
            Element firstElement = source.getFirstElement(HTMLElementName.TITLE);

            if (firstElement == null || firstElement.getTextExtractor() == null) {
                return null;
            }
            htmlBean.setTitle(firstElement.getTextExtractor().toString());
            htmlBean.setContext(source.getTextExtractor().toString());
            htmlBean.setUrl("http://"+file.getAbsolutePath().substring(34));

            return htmlBean;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
