package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Unit;
import techit.model.Update;
import techit.model.dao.UnitDao;
import techit.model.dao.UpdateDao;

@Repository
public class UpdateDaoImpl implements UpdateDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Update getUpdate( Long id )
    {
        return entityManager.find( Update.class, id );
    }

    @Override
    public List<Update> getUpdates()
    {
        return entityManager.createQuery( "from Update order by id", Update.class )
            .getResultList();
    }

    @Override
    @Transactional
    public Update saveUpdate( Update update )
    {
        return entityManager.merge( update );
    }

}
