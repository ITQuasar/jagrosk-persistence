package org.itquasar.multiverse.jagrosk.persistence;

import java.io.Serializable;
import java.util.Objects;

/**
 *
 * TEst
 * =====
 *
 *
 * Created by guilherme on 16/10/16.
 *
 * @startuml entity-diagram.png
 *
 * A -> B: Hi
 * B -> A: Ho
 *
 * @enduml
 *
 * ```java
 * public class Entity {
 *
 * }
 * ```
 */
public interface JagroskEntity<I> extends Serializable {

    I getId();

    default boolean identityEquals(JagroskEntity<I> other) {
        return this.getClass().isInstance(other) && Objects.equals(this.getId(), other.getId());
    }

    default int identityHashCode(){
        return Objects.hashCode(this.getId());
    }

}
