package org.itquasar.multiverse.jagrosk.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 29/01/17.
 */
public class PersistenceTest {

    @Test
    public void jpaPersistenceTest() {
        System.out.println("JPA persistence test");
        persistenceTest(JagroskPersistenceFactory.create(JagroskPersistenceType.JPA));
    }

    @Test
    public void memoryPersistenceTest() {
        System.out.println("Memory persistence test");
        persistenceTest(JagroskPersistenceFactory.create(JagroskPersistenceType.MEMORY));
    }

    public void persistenceTest(JagroskPersistence persistence) {
        System.out.println(persistence.repositoryOneShot(FooBarEntity.class).listAll());
        persistence.repositoryOneShot(FooBarEntity.class).add(new FooBarEntity(1));
        System.out.println(persistence.repositoryOneShot(FooBarEntity.class).listAll());
        Assertions.assertEquals(
                new FooBarEntity(1),
                persistence.repositoryOneShot(FooBarEntity.class)
                        .findById(1).get()
        );

        Optional<FooBarEntity> opt = persistence.transaction((repoProvider) -> {
            Repository<Integer, FooBarEntity> repo = repoProvider.get(FooBarEntity.class);
            List<FooBarEntity> foos = repo.findBy("id", 1);
            if (foos.isEmpty()) {
                Assertions.fail("No entity found, but it should be there!");
            }
            FooBarEntity foo = foos.get(0);
            foo.setFooBar("foo bar");
            repo.update(foo);
            return foo;
        });

        Assertions.assertEquals(
                new FooBarEntity(1, "foo bar"),
                persistence.repositoryOneShot(FooBarEntity.class)
                        .findById(1).get()
        );

        Assertions.assertEquals(
                opt.get(),
                persistence.repositoryOneShot(FooBarEntity.class).findById(1).get()
        );
    }
}
