package org.powerimo.http.serialization;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StringOrListDeserializer extends JsonDeserializer<List<String>> {

        @Override
        public List<String> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonNode node = p.getCodec().readTree(p);

            if (node.isTextual()) {
                return Collections.singletonList(node.asText());
            } else if (node.isArray()) {
                List<String> result = new ArrayList<>();
                for (JsonNode item : node) {
                    if (item.isTextual()) {
                        result.add(item.asText());
                    }
                }
                return result;
            }

            return Collections.emptyList(); // fallback
        }

}
