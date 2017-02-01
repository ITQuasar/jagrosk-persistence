package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistence;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistenceFactory;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistenceType;
import org.junit.jupiter.api.Test;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPAPersistenceTest {

    static final JagroskPersistence persistence = JagroskPersistenceFactory.create(JagroskPersistenceType.JPA);

    @Test
    public void initTest(){
        System.out.println("JPA");
        persistence.createTransaction();

    }
}
