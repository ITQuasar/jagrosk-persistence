package org.itquasar.multiverse.jagrosk.persistence;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

/**
 * Created by guilherme on 16/10/16.
 */
public class GenericRepository<I, M extends IdentifableEntity<I, M>> implements Repository<I, M> {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericRepository.class);

    private final Class<M> modelClass;
    private final EntityManager em;

    public GenericRepository(Class<M> modelClass, EntityManager em){
        this.modelClass = modelClass;
        this.em = em;
    }

    @Override
    public List<M> listAll(){
        CriteriaQuery<M> query = em.getCriteriaBuilder().createQuery(this.modelClass);
        query.from(this.modelClass);
        return em.createQuery(query).getResultList();
    }

    // FIXME: use option
    @Override
    public M findById(I id){
        return em.find(this.modelClass, id);
    }

    @Override
    public void close() {
        LOGGER.info("Closing {}", this.getClass().getSimpleName());
        em.close();
    }
}
