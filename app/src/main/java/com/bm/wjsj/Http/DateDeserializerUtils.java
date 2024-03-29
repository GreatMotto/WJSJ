package com.bm.wjsj.Http;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.Date;

/**
 * Created by Nathan on 15/5/22.
 */
public class DateDeserializerUtils implements JsonDeserializer<Date> {

    @Override
    public Date deserialize(JsonElement json, Type type,
                                      JsonDeserializationContext context) throws JsonParseException {
        return new Date(json.getAsJsonPrimitive().getAsLong());
    }
}
