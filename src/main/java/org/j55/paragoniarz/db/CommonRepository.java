package org.j55.paragoniarz.db;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * @author johnnyFiftyFive
 */
public abstract class CommonRepository {
    private static final Sql2o db = new Sql2o("jdbc:sqlite://paragoniarz.sqlite", "", "");
    private Logger logger = LoggerFactory.getLogger(CommonRepository.class);

    /**
     * @return opened connection
     */
    protected Connection openConnection() {

        return db.open();
    }
}
