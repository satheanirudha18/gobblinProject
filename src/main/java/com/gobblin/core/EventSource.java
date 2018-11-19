package com.gobblin.core;

/**
 * Created by Anirudha Sathe on 13/11/18.
 */

import com.google.common.collect.Lists;
import org.apache.gobblin.source.Source;
import org.apache.gobblin.configuration.ConfigurationKeys;
import org.apache.gobblin.configuration.SourceState;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.source.extractor.Extractor;
import org.apache.gobblin.source.workunit.Extract;
import org.apache.gobblin.source.workunit.WorkUnit;

import java.io.IOException;
import java.util.List;

public class EventSource implements Source<String, String> {

    public static final String SOURCE_PAGE_KEY = "source.page";

    public List<WorkUnit> getWorkunits(SourceState state) {
        List<WorkUnit> workUnits = Lists.newArrayList();

        /*
        checking whether there are any files mention in the job or .pull file
        */
        if (!state.contains(ConfigurationKeys.SOURCE_FILEBASED_FILES_TO_PULL)) {
            return workUnits;
        }

        /*
        creating an object of Extract to create the snapshots of the data
         */
        Extract extract = new Extract(Extract.TableType.SNAPSHOT_ONLY, state.getProp(ConfigurationKeys.EXTRACT_NAMESPACE_NAME_KEY, "ExampleNamespace"), "ExampleTable");

        /*
        A variable to store the location where the files are kept
         */
        WorkUnitState workstate = new WorkUnitState();
        EventWrapper eventWrapper = new EventWrapper(workstate);
        String urlToPull = state.getProp(ConfigurationKeys.SOURCE_FILEBASED_FILES_TO_PULL);

        /*
        create a workunit for URL to pull
         */
        WorkUnit workUnit = (WorkUnit) WorkUnit.create(extract);
        workUnit.setProp(SOURCE_PAGE_KEY, urlToPull);
        workUnits.add(workUnit);

        return workUnits;
    }

    public Extractor<String, String> getExtractor(WorkUnitState state) throws IOException {
        return new EventExtractor(state);
    }

    public void shutdown(SourceState state) {
        System.out.println("The system has shut down.");
        System.exit(0);
    }

}
