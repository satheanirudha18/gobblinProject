package com.gobblin.core;

/**
 * Created by Anirudha Sathe on 9/11/18.
 */

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import gobblin.configuration.WorkUnitState;
import gobblin.converter.Converter;
import gobblin.converter.DataConversionException;
import gobblin.converter.SchemaConversionException;

public abstract class ToAvroBaseConverter<SI, DI> extends Converter<SI, Schema, DI, GenericRecord> {

    @Override
    public abstract Schema convertSchema(SI schema, WorkUnitState workUnitState)
        throws SchemaConversionException;

    @Override
    public abstract Iterable<GenericRecord> convertRecord(Schema outputSchema, DI inputRecord, WorkUnitState workUnitState)
        throws DataConversionException;
}
