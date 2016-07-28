package com.ditemis.rs;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.mycila.junit.concurrent.Concurrency;
import com.mycila.junit.concurrent.ConcurrentJunitRunner;

@RunWith(ConcurrentJunitRunner.class)
@Concurrency(value = 6)
public class TestServiceTest {

    private static final String WEBAPP_URL = "http://localhost:8080/modeshape-test";
    private static final int TRANSACTION_AMOUNTS = 1000;

    @Test public void test0() throws Throwable { createAndUpdateNodeLoop(0); }
    @Test public void test1() throws Throwable { createAndUpdateNodeLoop(1); }
    @Test public void test2() throws Throwable { createAndUpdateNodeLoop(2); }
    @Test public void test3() throws Throwable { createAndUpdateNodeLoop(3); }
    @Test public void test4() throws Throwable { createAndUpdateNodeLoop(4); }
    @Test public void test5() throws Throwable { createAndUpdateNodeLoop(5); }
    @Test public void test6() throws Throwable { createAndUpdateNodeLoop(6); }

    public void createAndUpdateNodeLoop(int testCaseId) throws Exception {
        for (int i = 0; i < TRANSACTION_AMOUNTS; i++) {
            createAndUpdateNode(testCaseId, i);
        }
    }
    
    public void createAndUpdateNode(int testCaseId, int iteration) throws Exception {        
        URL url = new URL(WEBAPP_URL + "/rest/test");
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        assertEquals(200, connection.getResponseCode());
        BufferedReader br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        String createdNodeId = br.readLine();
        
        System.out.println(testCaseId + ":" + iteration + " Created node " + createdNodeId);

        url = new URL(WEBAPP_URL + "/rest/test/" + createdNodeId);
        connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("PUT");
        assertEquals(200, connection.getResponseCode());

        br = new BufferedReader(new InputStreamReader((connection.getInputStream())));
        String updatedNodeId = br.readLine();
        assertEquals(createdNodeId, updatedNodeId);
        
        System.out.println(testCaseId + ":" + iteration + " Updated node " + updatedNodeId);
    }
}
