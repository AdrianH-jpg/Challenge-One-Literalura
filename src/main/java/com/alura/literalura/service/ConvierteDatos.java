package com.alura.literalura.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ConvierteDatos implements IConvierteDatos {
    private ObjectMapper objectMapper = new ObjectMapper();

    public ConvierteDatos() {
    }

    public <T> T obtenerDatos(String json, Class<T> clase) {
        try {
            return this.objectMapper.readValue(json, clase);
        } catch (JsonProcessingException var4) {
            JsonProcessingException e = var4;
            throw new RuntimeException(e);
        }
    }
}