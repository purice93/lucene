package com.sxt.lucene.controller;

import com.sxt.lucene.bean.HtmlBean;
import com.sxt.lucene.index.CreateIndex;
import com.sxt.lucene.util.PageUtil;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.Scorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.util.ArrayList;

/**
 * @author: ZouTai
 * @date: 2018/3/30
 * @description: 控制器
 */

@Controller
public class luceneController {

    @Autowired
    private CreateIndex createIndex;


    @RequestMapping("/index.do")
    public String createIndex() {
        File file = new File(CreateIndex.indexDir);
        if (file.exists()) {
            file.delete();
            file.mkdirs(); //使用mkdirs是为了创建父目录，防止没有父目录
        }
        createIndex.createIndex();
        return "search.jsp";
    }

    @RequestMapping("/search.do")
    public String search(String keywords, int num, Model model) throws Exception {

        int pageSize = 10;
        Directory dir = FSDirectory.open(new File(CreateIndex.indexDir));
        IndexReader indexReader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);

        IKAnalyzer analyzer = new IKAnalyzer();
        MultiFieldQueryParser mqp = new MultiFieldQueryParser(Version.LUCENE_4_9, new String[]{"title", "context"}, analyzer);
        Query query = mqp.parse(keywords);

        TopDocs topDocs = indexSearcher.search(query, pageSize * num);
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;

        // 分页
        String pageNum = num+"";
        int count = topDocs.totalHits;
        // 当前条数、一页显示多少条、总条数
        PageUtil<HtmlBean> pageUtil = new PageUtil<HtmlBean>(pageNum
                , pageSize+"", count);

        ArrayList<HtmlBean> htmlBeanArrayList = new ArrayList<HtmlBean>();
        for (int i = (num - 1) * pageSize; i < Math.min(num * pageSize, count); i++) {
            Document document = indexReader.document(scoreDocs[i].doc);

            // 分别对标题和内容进行高亮处理
            Formatter formatter = new SimpleHTMLFormatter("<font color = \"red\">", "</font>");
            Scorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(formatter, scorer);
            String title = highlighter.getBestFragment(analyzer, "title", document.get("title"));
            String context = highlighter.getBestFragments(analyzer
                            .tokenStream("context", document.get("context")), document.get("context")
                    , 3, "...");
            String url = document.get("url");
            HtmlBean htmlBean = new HtmlBean();
            htmlBean.setTitle(title);
            htmlBean.setContext(context);
            htmlBean.setUrl(url);
            htmlBeanArrayList.add(htmlBean);
        }
        pageUtil.setList(htmlBeanArrayList);
        model.addAttribute("page", pageUtil);
        model.addAttribute("keywords", keywords);
        return "search.jsp";
    }
}
