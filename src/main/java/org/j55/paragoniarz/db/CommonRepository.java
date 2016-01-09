package org.j55.paragoniarz.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

import java.util.List;

/**
 * @author johnnyFiftyFive
 */
@Service
public abstract class CommonRepository<T> {
    protected static final Logger logger = LoggerFactory.getLogger(CommonRepository.class);
    private static final Sql2o db = new Sql2o("jdbc:sqlite:paragoniarz.sqlite", "", "");

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

    protected List<? extends T> select(String sql, Class<? extends T> clazz) {
        try (Connection conn = openConnection()) {
            return conn.createQuery(sql)
                    .executeAndFetch(clazz);
        }
    }
}
