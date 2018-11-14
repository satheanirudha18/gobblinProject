package com.gobblin.api;

/**
 * Created by Anirudha Sathe on 15/11/18.
 */

import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.configuration.SourceState;

import java.io.IOException;
import java.util.List;

import org.apache.gobblin.source.extractor.Extractor;
import org.apache.gobblin.source.workunit.WorkUnit;

/*
* An implement for classes that the end users implement to work with a data source from which
* schema and records can be extracted.
*
* <p>
*     An implementation of this interface should contain all the logic required to work with a
*     specific data source. This usually include work determination and partitioning, and details
*     of the connection protocol to work with the data source.
* </p>
*
* @param <S> output Schema type
* @param <D> output Record type
 */

public interface Source<S, D> {

    /*
    * Get a list of Workunits, each for which is for extracting a portion of data.
    *
    * <p>
    *     Each {@link Workunit} will be used to instantiate a {@link org.apache.gobblin.configuration.WorkUnitState}
    *     that gets passed to the {@link #getExtractor(org.apache.gobblin.configuration.WorkUnitState)}
    *     method to get an {@link Extractor} for extracting schema and data records from the source.
    *     The {@link Workunit} instance should have all the properties needed for the {@link Extractor} to work.
    * </p>
    *
    * <p>
    *     Typically the list of {@link Workunit}s for the current run is determined by taking into
    *     account the list of {@link Workunit}s from the previous run so data gets extracted incrementally.
    *     The method {@link org.apache.gobblin.configuration.SourceState#getPreviousWorkUnitState} can be
    *     used to get the list of {@link Workunit}s from the previous run.
    * </p>
    *
    * @param state see {@link org.apache.gobblin.configuration.SourceState}
    * @return a list of {@link Workunit}s
     */
    public abstract List<WorkUnit> getWorkunits(SourceState state);

    /*
    * Get an {@link Extractor} based on a given {@link org.apache.gobblin.configuration.WorkUnitState}
    *
    * <p>
    *     The {@link Extractor} returned can use {@link org.apache.gobblin.configuration.WorkUnitState}
    *     to store arbitrary key-value pairs that will be persisted to the state and loaded it in
    *     the next scheduled job run.
    * </p>
    *
    * @param state a {@link org.apache.gobblin.configuration.WorkUnitState} carrying properties needed by
    * by the returned {@link Extractor}
    * @return an {@link Extractor} used to extract schema and data records from the data source.
    * @throws IOException if it fails to create an {@link Extractor}
     */
    public abstract Extractor<S, D> getExtractor(WorkUnitState state) throws IOException;

    /*
    * Shutdown this {@link Source} instance.
    *
    * <p>
    *     This method is called once when the job completes. Properties (key-value pairs) added
    *     to the input {@link SourceState) instance will be persisted and available the next scheduled
    *     job run through the method {@link #getWorkUnits(SourceState)}. If there is a cleanup or reporting
    *     required for a particular implementation of this interface, then it is acceptable to have a
    *     default implementation of this method.
    * </p>
    *
    * @param state see {@link SourceState}
     */
    public abstract void shutdown(SourceState state);

}
