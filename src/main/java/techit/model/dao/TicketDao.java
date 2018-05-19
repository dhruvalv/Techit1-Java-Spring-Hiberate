package techit.model.dao;

import java.util.List;

import techit.model.Ticket;
import techit.model.Unit;
import techit.model.User;

public interface TicketDao {

    Ticket getTicket( Long id );

    List<Ticket> getTickets();

    List<Ticket> getTicketsCreatedBy( User user );

    List<Ticket> getTicketsCreatedFor( String email );

    List<Ticket> getTicketsAssignedTo( Unit unit );

    List<Ticket> getTicketsAssignedTo( User technician );

    Ticket saveTicket( Ticket ticket );

}
