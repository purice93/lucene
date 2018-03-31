package com.sxt.lucene.index;

import com.sxt.lucene.bean.HtmlBean;
import com.sxt.lucene.bean.HtmlBeanUtil;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

/**
 * @author: ZouTai
 * @date: 2018/3/28
 * @description: 创建索引
 */

@Service
public class CreateIndex {

    /*
     静态变量
     dataDir: 资源位置
     indexDir: 索引位置
      */

    public static String dataDir = "E:/JavaEE_IJ_WorkSpace/lucene/Data/www.bjsxt.com";
    public static String indexDir = "E:/JavaEE_IJ_WorkSpace/lucene/Data/index2";

    public void createIndex() {
        try {
            // 文件和分析器
            Directory dir = FSDirectory.open(new File(indexDir));
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);

            // 写入索引配置
            IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_9, analyzer);
            indexWriterConfig.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
            IndexWriter indexWriter = new IndexWriter(dir, indexWriterConfig);

            // 遍历所有文件夹及其子文件夹
            File file = new File(dataDir);
            // 不能有文件夹listFilesAndDirs
            Collection<File> files = FileUtils.listFiles(file, TrueFileFilter.INSTANCE, TrueFileFilter.INSTANCE);

            for(File f : files) {
                HtmlBean htmlBean = HtmlBeanUtil.parseHtml(f);
                Document document = new Document();
                // 文件名、内容、最后修改时间
                if (htmlBean == null) {
                    continue;
                }
                document.add(new StringField("title", htmlBean.getTitle(), Field.Store.YES));
                document.add(new TextField("context", htmlBean.getContext(), Field.Store.YES));
                document.add(new StringField("url", htmlBean.getUrl(), Field.Store.YES));
                indexWriter.addDocument(document);
            }
            indexWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
