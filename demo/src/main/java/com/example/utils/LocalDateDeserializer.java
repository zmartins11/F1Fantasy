package com.example.utils;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

public class LocalDateDeserializer extends StdDeserializer<LocalDate> {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

	  public LocalDateDeserializer() {
	        super(LocalDate.class);
	    }

	    @Override
	    public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
	        return LocalDate.parse(p.getValueAsString(), FORMATTER);
	    }
}
