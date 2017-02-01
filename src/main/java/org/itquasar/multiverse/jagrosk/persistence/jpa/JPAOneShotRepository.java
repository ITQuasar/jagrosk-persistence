package org.itquasar.multiverse.jagrosk.persistence.jpa;

import org.itquasar.multiverse.jagrosk.persistence.JagroskEntity;
import org.itquasar.multiverse.jagrosk.persistence.OneShotRepository;
import org.itquasar.multiverse.jagrosk.persistence.RepositoryAction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityTransaction;

/**
 * Created by guilherme on 29/01/17.
 */
public class JPAOneShotRepository<I, E extends JagroskEntity<I>> extends OneShotRepository<I, E> {

    private static final Logger LOGGER = LoggerFactory.getLogger(JPAOneShotRepository.class);

    public JPAOneShotRepository(JPARepository<I, E> repository) {
        super(repository);
    }

    private JPARepository<I, E> getRepository() {
        return (JPARepository) this.repository;
    }

    @Override
    protected <T> T performAndReturn(RepositoryAction<T> action) {
        T result = null;
        EntityTransaction trx = getRepository().getEntityManager().getTransaction();
        try {
            trx.begin();
            result = action.execute();
            trx.commit();
        } catch (Exception ex) {
            if (trx.isActive()) {
                trx.rollback();
            }
            LOGGER.error("Couldn't perform JPA Transaction.", ex);
        } finally {
            this.close();
        }
        return result;
    }
}
