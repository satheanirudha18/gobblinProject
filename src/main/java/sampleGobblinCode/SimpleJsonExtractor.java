package sampleGobblinCode;

/**
 * Created by Anirudha Sathe on 9/11/18.
 */

import com.google.common.io.Closer;
import org.apache.commons.vfs2.*;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.impl.DefaultFileSystemConfigBuilder;
import org.apache.gobblin.configuration.ConfigurationKeys;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.password.PasswordManager;
import org.apache.gobblin.source.extractor.DataRecordException;
import org.apache.gobblin.source.extractor.Extractor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class SimpleJsonExtractor implements Extractor<String, String> {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleJsonExtractor.class);

    private static final String SOURCE_FILE_KEY = "source.file";

    private final WorkUnitState workUnitState;

    private final FileObject fileObject;

    private final BufferedReader bufferedReader;

    private final Closer closer = Closer.create();

    public SimpleJsonExtractor(WorkUnitState workUnitState) throws FileSystemException {
        this.workUnitState = workUnitState;

        //Resolve the file to pull
        if (workUnitState.getPropAsBoolean(ConfigurationKeys.SOURCE_CONN_USE_AUTHENTICATION, false)) {
            //add authentication credential if authentication is needed
            UserAuthenticator auth = new StaticUserAuthenticator(workUnitState.getProp(ConfigurationKeys.SOURCE_CONN_DOMAIN, ""), workUnitState.getProp(ConfigurationKeys.SOURCE_CONN_USERNAME), PasswordManager.getInstance(workUnitState).readPassword(workUnitState.getProp(ConfigurationKeys.SOURCE_CONN_PASSWORD)));
            FileSystemOptions opts = new FileSystemOptions();
            DefaultFileSystemConfigBuilder.getInstance().setUserAuthenticator(opts, auth);
            this.fileObject = VFS.getManager().resolveFile(workUnitState.getProp(SOURCE_FILE_KEY), opts);
        }else{
            this.fileObject = VFS.getManager().resolveFile(workUnitState.getProp(SOURCE_FILE_KEY));
        }

        //Open the file for reading
        LOGGER.info("Opening file " + this.fileObject.getURL().toString());
        this.bufferedReader = this.closer.register(new BufferedReader(new InputStreamReader(this.fileObject.getContent().getInputStream(), ConfigurationKeys.DEFAULT_CHARSET_ENCODING)));
    }

    public String getSchema() {
        return this.workUnitState.getProp(ConfigurationKeys.SOURCE_SCHEMA);
    }

    public String readRecord(@Deprecated String reuse) throws DataRecordException, IOException {
        //read the next line
        return this.bufferedReader.readLine();
    }

    public long getExpectedRecordCount() {
        //We don't know how many records are actually in the file before actually reading them
        return 0;
    }

    public long getHighWatermark() {
        //Watermark is not applicable for this type of Extractor
        return 0;
    }

    public void close() throws IOException {
        try {
            this.closer.close();
        }catch (IOException ie) {
            LOGGER.error("Failed to close the file object - ",ie);
        }

        try {
            this.fileObject.close();
        }catch (IOException ie) {
            LOGGER.error("Failed to close the file object - ", ie);
        }
    }
}
