package io.icyfry.bitbucket.e2s.api;

import java.io.IOException;

import com.atlassian.bitbucket.emoticons.Emoticon;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;

/**
 * A serializer for Atlassian Emoticon class
 */
public class EmoticonSerializer extends StdSerializer<Emoticon>{

    private static final long serialVersionUID = 1L;

    public EmoticonSerializer() {
        this(null);
    }

    public EmoticonSerializer(Class<Emoticon> obj) {
        super(obj);
    }

    @Override
    public void serialize(Emoticon obj, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        String str = "?";
        if(obj.getValue().isPresent()) {
            str = obj.getValue().get();
        }
        jsonGenerator.writeString(str);
    }
    
}