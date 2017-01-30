package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.EntityTest;
import org.itquasar.multiverse.jagrosk.persistence.Repository;
import org.junit.jupiter.api.Test;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPAPersistenceProviderTest {

    private static final Repository<Integer, EntityTest.TestEntity> repository = JPAPersistenceProvider.create("test").buildRepository();

    @Test
    public void initTest(){
        System.out.println("JPA");


    }
}
