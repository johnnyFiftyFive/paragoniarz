package org.j55.paragoniarz.db;

import org.sql2o.Connection;
import org.sql2o.Sql2o;

/**
 * @author johnnyFiftyFive
 */
public abstract class CommonRepository {
    private static final Sql2o db = new Sql2o("jdbc:sqlite://paragoniarz.sqlite", "", "");

    /**
     * @return opened connection
     */
    protected Connection openConnection() {
        return db.open();
    }
}
