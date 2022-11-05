package ru.zamilov.library;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import javafx.util.Pair;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * Кастомный Класс сериализации объекта Pair<String, Integer>
 */
@JsonComponent
public class CustomJsonSerializer extends JsonSerializer<Pair<String, Integer>> {
    @Override
    public void serialize(Pair<String, Integer> pair, JsonGenerator gen, SerializerProvider ser) throws IOException {
//        gen.configure(JsonWriteFeature.QUOTE_FIELD_NAMES.mappedFeature(), false);
        gen.writeStartObject();
        gen.writeNumberField(pair.getKey(), pair.getValue());
        gen.writeEndObject();
    }
}