package com.gobblin.core;

/**
 * Created by Anirudha Sathe on 13/11/18.
 */

import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import org.apache.commons.vfs2.*;
import org.apache.gobblin.configuration.ConfigurationKeys;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.password.PasswordManager;
import org.apache.gobblin.source.extractor.Extractor;
import org.apache.gobblin.source.extractor.DataRecordException;

import com.google.common.io.Closer;

import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class EventExtractor implements Extractor<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(EventExtractor.class);

    private static final String SOURCE_PAGE_KEY = "source.page";

    private final WorkUnitState workUnitState;

    private BufferedReader bufferedReader;

    private final HttpClient client;

    private final HttpGet request;

    private final HttpResponse response;

    private final Closer closer = Closer.create();

    public EventExtractor(WorkUnitState workUnitState) throws IOException {
        this.workUnitState = workUnitState;

        //Resolve this file to pull
        /*if (workUnitState.getPropAsBoolean(ConfigurationKeys.SOURCE_CONN_USE_AUTHENTICATION, false)) {
            //add authentication credential if authentication is needed
            UserAuthenticator auth;
            auth = new StaticUserAuthenticator(workUnitState.getProp(ConfigurationKeys.SOURCE_CONN_DOMAIN, ""), workUnitState.getProp(ConfigurationKeys.SOURCE_CONN_USERNAME), PasswordManager.getInstance((WorkUnitState) workUnitState).readPassword(workUnitState.getProp(ConfigurationKeys.SOURCE_CONN_PASSWORD)));
            FileSystemOptions opts = new FileSystemOptions();
            DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
            this.fileObject = VFS.getManager().resolveFile(workUnitState.getProp(SOURCE_PAGE_KEY));
        }*/
        /*
        Custom logic for the extraction of data starts
         */
        this.client = new DefaultHttpClient();

        String url= workUnitState.getProp(SOURCE_PAGE_KEY);

        if (!workUnitState.getPropAsBoolean(ConfigurationKeys.SOURCE_CONN_USE_AUTHENTICATION, false)) {
            this.request = new HttpGet(url);
        }else{
            this.request = new HttpGet(url);
        }

        this.response = this.client.execute(this.request);

        //StringBuffer result = new StringBuffer();

        InputStreamReader ir = new InputStreamReader(this.response.getEntity().getContent(), ConfigurationKeys.DEFAULT_CHARSET_ENCODING);

        this.bufferedReader = new BufferedReader(ir);

        if (this.response.getStatusLine().getStatusCode() == 200) {
            this.bufferedReader = this.closer.register(this.bufferedReader);
        }else{
            if (response.getStatusLine().getStatusCode() == 404){
                LOGGER.error("Error : " + response.getStatusLine().getStatusCode() + " Page not found error.");
            }else if (response.getStatusLine().getStatusCode() == 500) {
                LOGGER.error("Error : " + response.getStatusLine().getStatusCode() + " Internal Server error.");
            }else {
                LOGGER.error("Error : " + response.getStatusLine().getStatusCode() + " Unidentified.");
            }
        }

    }

    public String getSchema() {
        return this.workUnitState.getProp(ConfigurationKeys.SOURCE_SCHEMA);
    }

    public String readRecord(@Deprecated String reuse) throws DataRecordException, IOException{
        //read the next line
        return this.bufferedReader.readLine();
    }

    public long getExpectedRecordCount() {
        //We actually don't know how many records are actually in the file before actually reading them
        return 0;
    }

    public long getHighWatermark() {
        //Watermark is not required for this type of Extractor
        return 0;
    }

    public void close() throws IOException {
        try {
            this.closer.close();
        }catch (IOException ie) {
            LOGGER.error("Failed to close the file object - ", ie);
        }

        this.request.abort();

    }
}
