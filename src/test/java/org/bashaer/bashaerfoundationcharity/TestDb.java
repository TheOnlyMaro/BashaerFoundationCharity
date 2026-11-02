package org.bashaer.bashaerfoundationcharity;

import org.bashaer.bashaerfoundationcharity.model.DatabaseConnection;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

public class TestDb {
    @Test
    void shouldReturnTheSameDatabaseConnectionInstance() {
        DatabaseConnection db1 = DatabaseConnection.getInstance();
        DatabaseConnection db2 = DatabaseConnection.getInstance();

        assertSame(db1, db2);
        assertNotNull(db1.getConnection());
    }
}