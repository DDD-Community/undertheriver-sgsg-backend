package com.undertheriver.sgsg.user.domain;

import java.util.Objects;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.RequiredArgsConstructor;

@Converter
@RequiredArgsConstructor
public class PasswordConverter implements AttributeConverter<String, String> {

	private final BCryptPasswordEncoder bCryptPasswordEncoder;

	@Override
	public String convertToDatabaseColumn(String attribute) {
		if (Objects.isNull(attribute) || attribute.isEmpty()) {
			return "";
		}
		return bCryptPasswordEncoder.encode(attribute);
	}

	@Override
	public String convertToEntityAttribute(String dbData) {
		return dbData;
	}
}
