package org.j55.paragoniarz.db;

import org.sql2o.converters.Converter;
import org.sql2o.converters.ConverterException;

import java.time.LocalDate;

/**
 * @author johnnyFiftyFive
 */
public class LocalDateConverter implements Converter<LocalDate> {
    @Override public LocalDate convert(Object val) throws ConverterException {
        return val == null ? null : LocalDate.parse((String) val);
    }

    @Override public Object toDatabaseParam(LocalDate val) {
        return val == null ? null : val.toString();
    }
}
