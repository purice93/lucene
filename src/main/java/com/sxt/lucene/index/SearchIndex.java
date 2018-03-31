package com.sxt.lucene.index;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import java.io.File;

/**
 * @author: ZouTai
 * @date: 2018/3/29
 * @description: 查询索引
 */
public class SearchIndex {

    public void searchIndex() {

        try {
            Directory directory = FSDirectory.open(new File(CreateIndex.indexDir));
            IndexReader indexReader = DirectoryReader.open(directory);
            IndexSearcher indexSearcher = new IndexSearcher(indexReader);

            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_4_9);
            QueryParser queryParser = new QueryParser(Version.LUCENE_4_9, "context", analyzer);
            Query query = queryParser.parse("form");

            TopDocs topDocs = indexSearcher.search(query, 10);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            for (ScoreDoc sd : scoreDocs) {
                int docId = sd.doc;
                Document document = indexReader.document(docId);
                System.out.println(document.get("filename"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
