package com.gobblin.core;

/**
 * Created by Anirudha Sathe on 14/11/18.
 */

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.gobblin.configuration.WorkUnitState;
import org.apache.gobblin.converter.DataConversionException;
import org.apache.gobblin.converter.SchemaConversionException;

import java.lang.reflect.Type;
import java.util.Map;

public class EventConverter extends ToAvroBaseConverter<String, String>{

    private static final Gson GSON = new Gson();

    /*
    Expect the input JSON string to be a key-value pairs
     */
    private static final Type FIELD_ENTRY_TYPE = new TypeToken<Map<String, Object>>() {}.getType();

    @Override
    public Schema convertSchema(String inputSchema, WorkUnitState workUnitState) throws SchemaConversionException {
        return new Schema.Parser().parse(inputSchema);
    }

    @Override
    public Iterable<GenericRecord> convertRecord(Schema schema, String inputRecord, WorkUnitState workUnitState) throws DataConversionException {
        JsonElement jsonElement = GSON.fromJson(inputRecord, JsonElement.class);
        Map<String, Object> fields = GSON.fromJson(jsonElement, FIELD_ENTRY_TYPE);
        GenericRecord record = new GenericData.Record(schema);
        for (Map.Entry<String, Object> entry : fields.entrySet()) {
            record.put(entry.getKey(), entry.getValue());
        }

        return new SingleRecordIterable<GenericRecord>(record);
    }
}
