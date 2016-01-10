package org.j55.paragoniarz.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.converters.Converter;
import org.sql2o.quirks.NoQuirks;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author johnnyFiftyFive
 */
@Service
public abstract class CommonRepository<T> {
    protected static final Logger logger = LoggerFactory.getLogger(CommonRepository.class);

    private static final Map<Class, Converter> CONVERTERS = new HashMap<Class, Converter>() {{
        put(LocalDate.class, new LocalDateConverter());
    }};

    protected static final Sql2o db = new Sql2o("jdbc:sqlite:paragoniarz.sqlite", "", "", new NoQuirks(CONVERTERS));

    /**
     * @return opened connection
     */
    protected Connection openConnection() {
        return db.open();
    }

    protected Integer save(Object entity, String sql) {
        try (Connection conn = openConnection()) {
            return (Integer) conn.createQuery(sql, true)
                    .bind(entity)
                    .executeUpdate()
                    .getKey();
        }
    }

    protected List<T> select(String sql, Class<T> clazz) {
        try (Connection conn = openConnection()) {
            return conn.createQuery(sql)
                    .executeAndFetch(clazz);
        }

    }


}
