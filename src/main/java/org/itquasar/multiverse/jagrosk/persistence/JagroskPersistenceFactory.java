package org.itquasar.multiverse.jagrosk.persistence;

import org.itquasar.multiverse.jagrosk.persistence.jpa.JPAPersistence;
import org.itquasar.multiverse.jagrosk.persistence.mem.MemoryPersistence;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Created by guilherme on 29/01/17.
 */
public class JagroskPersistenceFactory {

    public static JagroskPersistence create(JagroskPersistenceType type) {
        return create(type, Collections.emptyMap());
    }

    public static JagroskPersistence create(JagroskPersistenceType type, Map<String, String> options) {
        Objects.requireNonNull(options, "Options cannot be null, but can be empty.");
        switch (type) {
            case MEMORY:
                return createInMemory();
            case JPA:
                return options.containsKey(JagroskPersistenceOptions.JPA_PU_NAME)
                        ? createJPA(options.get(JagroskPersistenceOptions.JPA_PU_NAME))
                        : createJPA();
        }
        throw new Error("Type " + type + " not supported!");
    }

    public static JagroskPersistence createInMemory() {
        return new MemoryPersistence();
    }

    public static JagroskPersistence createJPA() {
        return JPAPersistence.create();
    }

    public static JagroskPersistence createJPA(String puName) {
        return JPAPersistence.create(puName);
    }


}
