package com.ps;

import com.ps.domain.AnswerDo;
import com.ps.domain.SorlQuestion;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.QueryRequest;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.MapSolrParams;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/23 9:56
 */
public class Test {
    public static void main(String[] args) {
        int i=0;
        if(10==10 | i !=0){
            System.out.println("真的");
        }
    }

    public static void main1(String[] args) throws IOException, SolrServerException {
        HttpSolrClient build = new HttpSolrClient.Builder("http://192.168.3.210:8983/solr/#/qa/query").build();

        Map<String, String> map = new HashMap<>();

        map.put("q","keyword:*");
        map.put("start","1");
        map.put("rows","1");

        QueryResponse response=build.query("test",new MapSolrParams(map));

        System.out.println(response.getResults());

    }

    public static void main2(String[] args) throws IOException, SolrServerException {
        HttpSolrClient build = new HttpSolrClient.Builder("http://192.168.3.210:8983/solr").build();
        Map<String, String> map = new HashMap<>();

        map.put("q","title:会");

        QueryResponse response=build.query("qa",new MapSolrParams(map));
        List<SorlQuestion> beans = response.getBeans(SorlQuestion.class);
        System.out.println(beans);
    }
}
