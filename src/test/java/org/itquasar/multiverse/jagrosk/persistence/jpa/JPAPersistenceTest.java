package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.FooBarEntity;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistence;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistenceFactory;
import org.itquasar.multiverse.jagrosk.persistence.JagroskPersistenceType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Optional;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPAPersistenceTest {

    static final JagroskPersistence persistence = JagroskPersistenceFactory.create(JagroskPersistenceType.JPA);

    @Test
    public void initTest() {
        System.out.println("JPA");
        persistence.transaction(
                        (trx) -> trx.getRepository(FooBarEntity.class).add(
                                new FooBarEntity(1)
                        )
                );
        Optional foobar = persistence.transaction(
                (trx) -> trx.getRepository(FooBarEntity.class).findById(
                        1
                )
        );
        Assertions.assertEquals(1, (int) ((FooBarEntity) (foobar.get())).getId());
    }
}
