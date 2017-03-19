package org.itquasar.multiverse.jagrosk.persistence;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

/**
 * Created by guilherme on 29/01/17.
 */
public class PersistenceTest {

    @Test
    public void jpaPersistenceCommitTest() {
        System.out.println("JPA persistence test");
        persistenceTest(JagroskPersistenceFactory.create(JagroskPersistenceType.JPA), false);
    }

    @Test
    @Disabled
    public void jpaPersistenceRollbackTest() {
        System.out.println("JPA persistence test");
        Assertions.assertThrows(TrxError.class
                , () -> persistenceTest(
                        JagroskPersistenceFactory.create(
                                JagroskPersistenceType.JPA,
                                new HashMap<String, String>() {{
                                    put(JagroskPersistenceOptions.JPA_PU_NAME, "test-roolback");
                                }}
                        ),
                        true
                )
        );
    }

    @Test
    @Disabled
    public void memoryPersistenceCommitTest() {
        System.out.println("Memory persistence test");
        persistenceTest(JagroskPersistenceFactory.create(JagroskPersistenceType.MEMORY), false);
    }

    @Test
    @Disabled
    public void memoryPersistenceRollbackTest() {
        System.out.println("Memory persistence test");
        Assertions.assertThrows(TrxError.class
                , () -> persistenceTest(
                        JagroskPersistenceFactory.create(JagroskPersistenceType.MEMORY),
                        true
                )
        );

    }

    public void persistenceTest(JagroskPersistence persistence, boolean fail) {
        System.out.println(persistence.repositoryOneShot(FooBarEntity.class).listAll());
        persistence.repositoryOneShot(FooBarEntity.class).add(new FooBarEntity(1));
        System.out.println(persistence.repositoryOneShot(FooBarEntity.class).listAll());
        Assertions.assertEquals(
                new FooBarEntity(1),
                persistence.repositoryOneShot(FooBarEntity.class)
                        .findById(1).get()
        );

        Optional<FooBarEntity> opt = persistence.transaction((repoProvider) -> {
            Repository<Integer, FooBarEntity> repo = repoProvider.repository(FooBarEntity.class);
            List<FooBarEntity> foos = repo.findBy("id", 1);
            if (foos.isEmpty()) {
                Assertions.fail("No entity found, but it should be there!");
            }
            FooBarEntity foo = foos.get(0);
            foo.setFooBar("foo bar");
            repo.update(foo);
            if (fail) {
                throw new TrxError("This should fail for test purposes!");
            }
            return foo;
        });
        if(!fail) {
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

    static class TrxError extends Error {
        public TrxError(String message) {
            super(message);
        }
    }
}
