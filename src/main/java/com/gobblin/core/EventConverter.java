package com.gobblin.core;

/**
 * Created by Anirudha Sathe on 14/11/18.
 */

import java.util.List;


import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.generic.GenericData;
import org.apache.gobblin.configuration.ConfigurationKeys;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.converter.DataConversionException;
import org.apache.gobblin.converter.SchemaConversionException;


import com.google.common.base.Preconditions;
import com.google.gson.JsonObject;
import com.google.gson.JsonNull;
import com.google.gson.Gson;
import com.google.common.base.Splitter;

import org.apache.gobblin.converter.avro.JsonElementConversionWithAvroSchemaFactory;

public class EventConverter extends ToAvroBaseConverter<String, JsonObject>{

    private static final Splitter SPLITTER_ON_COMMA = Splitter.on(',').trimResults().omitEmptyStrings();
    private Schema schema;
    private static final Gson GSON = new Gson();

    /*
    Expect the input JSON string to be a key-value pairs
     */
    //private static final Type FIELD_ENTRY_TYPE = new TypeToken<Map<String, Object>>() {}.getType();
    @Override
    public Schema convertSchema(String inputSchema, WorkUnitState workUnitState) throws SchemaConversionException {
        //Preconditions.checkArgument(workUnitState.contains(ConfigurationKeys.SOURCE_SCHEMA));
        //this.schema = new Schema.Parser().parse(workUnitState.getProp(ConfigurationKeys.CONVERTER_AVRO_SCHEMA_KEY));
        this.schema = new Schema.Parser().parse(inputSchema);
        return this.schema;
    }

    @Override
    public Iterable<GenericRecord> convertRecord(Schema schema, JsonObject inputRecord, WorkUnitState workUnitState) throws DataConversionException {
        GenericRecord avroRecord = convertNestedRecord(schema, inputRecord, workUnitState);
        return new SingleRecordIterable<GenericRecord>(avroRecord);
    }

    public static GenericRecord convertNestedRecord(Schema outputSchema, JsonObject inputRecord, WorkUnitState workUnitState) throws DataConversionException {
        GenericRecord avroRecord = new GenericData.Record(outputSchema);
        JsonElementConversionWithAvroSchemaFactory.JsonElementConverter converter;

        for (Schema.Field field : outputSchema.getFields()) {
            Schema schema = field.schema();
            Schema.Type type = schema.getType();
            boolean nullable = false;

            if (type.equals(Schema.Type.UNION)) {
                nullable = true;
                List<Schema> types = schema.getTypes();
                if (types.size() != 2) {
                    throw new DataConversionException("Unions must be size 2, and contain one null");
                }
                if (schema.getTypes().get(0).getType().equals(Schema.Type.NULL)){
                    schema = schema.getTypes().get(1);
                    type = schema.getType();
                }else if (schema.getTypes().get(1).getType().equals(Schema.Type.NULL)) {
                    schema = schema.getTypes().get(0);
                    type = schema.getType();
                }else{
                    throw new DataConversionException("Unions must be size 2, and contain one null");
                }

                if (inputRecord.get(field.name()) == null){
                    inputRecord.add(field.name(), JsonNull.INSTANCE);
                }
            }

            if (inputRecord.get(field.name()) == null) {
                throw new DataConversionException("Field missing from record : " + field.name());
            }

            if (type.equals(Schema.Type.RECORD)){
                if (nullable && inputRecord.get(field.name()).isJsonNull()) {
                    avroRecord.put(field.name(), null);
                }else {
                    avroRecord.put(field.name(), convertNestedRecord(schema, inputRecord.get(field.name()).getAsJsonObject(), workUnitState));
                }
            }else{
                try{
                    JsonObject jsonSchema = GSON.fromJson(schema.toString(), JsonObject.class);
                    converter = JsonElementConversionWithAvroSchemaFactory.getConvertor(field.name(), type.getName(), jsonSchema, workUnitState, nullable);
                    avroRecord.put(field.name(), converter.convert(inputRecord.get(field.name())));
                }catch (Exception e) {
                    throw new DataConversionException("Could not convert field " + field.name(), e);
                }
            }
        }
        return avroRecord;
    }
}
