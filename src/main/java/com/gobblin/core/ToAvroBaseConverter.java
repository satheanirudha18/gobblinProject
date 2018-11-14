package com.gobblin.core;

/**
 * Created by Anirudha Sathe on 9/11/18.
 */

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.converter.Converter;
import org.apache.gobblin.converter.DataConversionException;
import org.apache.gobblin.converter.SchemaConversionException;

public abstract class ToAvroBaseConverter<SI, DI> extends Converter<SI, Schema, DI, GenericRecord> {

    @Override
    public abstract Schema convertSchema(SI schema, WorkUnitState workUnitState)
        throws SchemaConversionException;

    @Override
    public abstract Iterable<GenericRecord> convertRecord(Schema outputSchema, DI inputRecord, WorkUnitState workUnitState)
        throws DataConversionException;
}
