package com.mateuswmachado.votesystem.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonTestUtil {
    public static String asJsonString(Object object) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}