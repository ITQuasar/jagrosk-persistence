package org.itquasar.multiverse.jagrosk.persistence;


import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Created by guilherme on 08/01/17.
 */
@Tag("repository")
public class EntityTest {

    @Test
    public void entityIdTest() {
        int id = 12345;
        Entity e = new TestEntity(id);
        assertThat(e, is(notNullValue()));
        assertThat(e.getId(), is(id));
        assertThat(e.identityHashCode(), is(Objects.hashCode(e.getId())));
        assertTrue(e.identityEquals(new TestEntity(id)));
    }

    public static class TestEntity implements Entity<Integer> {

        private final Integer id;

        private String fooBar;

        public TestEntity(Integer id) {
            this.id = id;
        }

        @Override
        public Integer getId() {
            return id;
        }

        public String getFooBar() {
            return fooBar;
        }

        public void setFooBar(String fooBar) {
            this.fooBar = fooBar;
        }

        @Override
        public String toString() {
            return "TestEntity{" +
                    "id=" + id +
                    (fooBar == null ? "" : ", fooBar=" + fooBar) +
                    '}';
        }
    }
}
