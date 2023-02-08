package com.example.utils;

import java.io.IOException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalTimeDeserializer extends JsonDeserializer<LocalTime> {

	private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("HH:mm:ss'Z'");

	@Override
	public LocalTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		return LocalTime.parse(p.getValueAsString(), FORMATTER);
	}

}
