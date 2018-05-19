package techit.model.dao.jpa;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;
import techit.model.dao.TicketDao;

@Repository
public class TicketDaoImpl implements TicketDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Ticket getTicket( Long id )
    {
        return entityManager.find( Ticket.class, id );
    }

    @Override
    public List<Ticket> getTickets()
    {
        return entityManager
            .createQuery( "from Ticket order by id", Ticket.class )
            .getResultList();
    }

    @Override
    public List<Ticket> getTicketsCreatedBy( User user )
    {
        String query = "from Ticket where createdBy = :user";

        return entityManager.createQuery( query, Ticket.class )
            .setParameter( "user", user )
            .getResultList();
    }

    @Override
    public List<Ticket> getTicketsCreatedFor( String email )
    {
        String query = "from Ticket where createdForEmail = :email";

        return entityManager.createQuery( query, Ticket.class )
            .setParameter( "email", email )
            .getResultList();
    }

    @Override
    public List<Ticket> getTicketsAssignedTo( Unit unit )
    {
        String query = "from Ticket where unit = :unit";

        return entityManager.createQuery( query, Ticket.class )
            .setParameter( "unit", unit )
            .getResultList();
    }

    @Override
    public List<Ticket> getTicketsAssignedTo( User technician )
    {
        String query = "select t from Ticket t join t.technicians tt "
            + "where tt = :technician";

        return entityManager.createQuery( query, Ticket.class )
            .setParameter( "technician", technician )
            .getResultList();
    }

    @Override
    @Transactional
    public Ticket saveTicket( Ticket ticket )
    {
        return entityManager.merge( ticket );
    }

}
