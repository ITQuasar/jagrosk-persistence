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
public class JagroskEntityTest {

    @Test
    public void entityIdTest() {
        int id = 12345;
        JagroskEntity e = new FooBarEntity(id);
        assertThat(e, is(notNullValue()));
        assertThat(e.getId(), is(id));
        assertThat(e.identityHashCode(), is(Objects.hashCode(e.getId())));
        assertTrue(e.identityEquals(new FooBarEntity(id)));
    }

}
