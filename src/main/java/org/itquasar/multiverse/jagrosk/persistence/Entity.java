package org.itquasar.multiverse.jagrosk.persistence;

import java.util.Objects;

/**
 * Created by guilherme on 16/10/16.
 *
 */
public interface Entity<I> {

    I getId();

    default boolean identityEquals(Entity<I> other) {
        return this.getClass().isInstance(other) && Objects.equals(this.getId(), other.getId());
    }

    default int identityHashCode(){
        return Objects.hashCode(this.getId());
    }

}
