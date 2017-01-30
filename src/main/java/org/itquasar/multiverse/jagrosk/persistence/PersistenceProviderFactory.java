package org.itquasar.multiverse.jagrosk.persistence;

import org.itquasar.multiverse.jagrosk.persistence.jpa.JPAPersistenceProvider;
import org.itquasar.multiverse.jagrosk.persistence.mem.MemoryPersistenceProvider;

/**
 * Created by guilherme on 29/01/17.
 */
public class PersistenceProviderFactory {

    public static enum Type {
        MEMORY(MemoryPersistenceProvider.class),
        JPA(JPAPersistenceProvider.class);

        private final Class<? extends JagroskPersistenceProvider> providerClass;

        Type(Class<? extends JagroskPersistenceProvider> pŕoviderClass) {
            this.providerClass = pŕoviderClass;
        }

        public Class<? extends JagroskPersistenceProvider> getProviderClass() {
            return providerClass;
        }
    }

    public static JagroskPersistenceProvider create(Type type) throws InstantiationException, IllegalAccessException {
        return create(type.getProviderClass());
    }

    public static JagroskPersistenceProvider create(Class<? extends JagroskPersistenceProvider> persitenceProviderClass) throws IllegalAccessException, InstantiationException {
        return persitenceProviderClass.newInstance();
    }
}
