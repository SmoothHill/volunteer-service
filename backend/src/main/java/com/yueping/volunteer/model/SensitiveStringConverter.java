package com.yueping.volunteer.model;

import com.yueping.volunteer.service.SensitiveDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class SensitiveStringConverter implements AttributeConverter<String, String> {

    private static final Logger log = LoggerFactory.getLogger(SensitiveStringConverter.class);

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return SensitiveDataService.encryptIfNeeded(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        try {
            return SensitiveDataService.decryptIfNeeded(dbData);
        } catch (IllegalStateException ex) {
            log.warn("Skip decrypting legacy sensitive value because it cannot be decrypted with current key");
            return "";
        }
    }
}
