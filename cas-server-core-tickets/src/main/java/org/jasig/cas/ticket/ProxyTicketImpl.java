package org.jasig.cas.ticket;

import org.jasig.cas.authentication.principal.Service;
import org.jasig.cas.ticket.proxy.ProxyTicket;

import javax.validation.constraints.NotNull;

/**
 * The {@link ProxyTicketImpl} is a concrete implementation of the {@link ProxyTicket}.
 *
 * @author Misagh Moayyed
 * @since 4.2
 */
public class ProxyTicketImpl extends ServiceTicketImpl implements ProxyTicket {
    private static final long serialVersionUID = -4469960563289285371L;

    /**
     * Instantiates a new Proxy ticket.
     */
    public ProxyTicketImpl() {
    }

    /**
     * Instantiates a new Proxy ticket.
     *
     * @param id           the id
     * @param ticket       the ticket
     * @param service      the service
     * @param fromNewLogin the from new login
     * @param policy       the policy
     */
    public ProxyTicketImpl(final String id, @NotNull final TicketGrantingTicketImpl ticket, @NotNull final Service service,
                           final boolean fromNewLogin, final ExpirationPolicy policy) {
        super(id, ticket, service, fromNewLogin, policy);
    }
}
