package org.itquasar.multiverse.jagrosk.persistence;

import org.itquasar.multiverse.jagrosk.persistence.mem.MemoryRepository;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Created by guilherme on 08/01/17.
 */
public class MemoryRepositoryTest {


    private static final Repository<Integer, EntityTest.TestEntity> REPOSITORY = new MemoryRepository<>(EntityTest.TestEntity.class);


    @Test
    public void manipulateEntityID1(){
        REPOSITORY.add(new EntityTest.TestEntity(1));
        EntityTest.TestEntity e = REPOSITORY.findById(1).get();
        e.setFooBar("FOO BAR");
        REPOSITORY.update(e);
        List<EntityTest.TestEntity> list = REPOSITORY.findBy("fooBar", "FOO BAR");

        assertThat(e.getFooBar(), is("FOO BAR"));
        assertThat(list.size(), is(1));
        assertThat(list.get(0).getFooBar(), is("FOO BAR"));
        assertThat(REPOSITORY.findById(1).get().getId(), is(1));

        assertThat(REPOSITORY.findBy("nonExistent", "FOO BAR").size(), is(0));
    }
}
