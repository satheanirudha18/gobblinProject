package sampleGobblinCode;

/**
 * Created by Anirudha Sathe on 9/11/18.
 */

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import org.apache.gobblin.configuration.ConfigurationKeys;
import org.apache.gobblin.configuration.SourceState;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.source.Source;
import org.apache.gobblin.source.extractor.Extractor;
import org.apache.gobblin.source.workunit.Extract;
import org.apache.gobblin.source.workunit.WorkUnit;

import java.io.IOException;
import java.util.List;

public class SimpleJsonSource implements Source<String, String>{

    public static final String SOURCE_FILE_KEY = "source.file";

    public List<WorkUnit> getWorkunits(SourceState state) {
        List<WorkUnit> workUnits = Lists.newArrayList();

        if (!state.contains(ConfigurationKeys.SOURCE_FILEBASED_FILES_TO_PULL)) {
            return workUnits;
        }

        //create a simple snapshot-type extract for all files
        Extract extract = new Extract(Extract.TableType.SNAPSHOT_ONLY, state.getProp(ConfigurationKeys.EXTRACT_NAMESPACE_NAME_KEY, "ExampleNamespace"), "ExampleTable");

        String filesToPull = state.getProp(ConfigurationKeys.SOURCE_FILEBASED_FILES_TO_PULL);

        for (String file : Splitter.on(",").omitEmptyStrings().split(filesToPull)) {
            //create one workunit for each file to pull
            WorkUnit workUnit = WorkUnit.create(extract);
            workUnit.setProp(SOURCE_FILE_KEY, file);
            workUnits.add(workUnit);
        }

        return workUnits;
    }

    public Extractor<String, String> getExtractor(WorkUnitState state) throws IOException {
        return new SimpleJsonExtractor(state);
    }

    public void shutdown(SourceState state) {
        System.out.println("The system has shut down.");
        System.exit(0);
    }


    public static void main(String[] args) {
        System.out.println("This is Source's main method. You better get that!.");
    }
}
