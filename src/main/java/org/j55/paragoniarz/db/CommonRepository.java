package org.j55.paragoniarz.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * @author johnnyFiftyFive
 */
@Service
public abstract class CommonRepository {
    private static final Sql2o db = new Sql2o("jdbc:sqlite:paragoniarz.sqlite", "", "");
    protected static final Logger logger = LoggerFactory.getLogger(CommonRepository.class);

    /**
     * @return opened connection
     */
    protected Connection openConnection() {
        return db.open();
    }

    protected int save(Object entity, String sql) {
        try (Connection conn = openConnection()) {
            return (Integer) conn.createQuery(sql, true)
                    .bind(entity)
                    .executeUpdate()
                    .getKey();
        }
    }
}
