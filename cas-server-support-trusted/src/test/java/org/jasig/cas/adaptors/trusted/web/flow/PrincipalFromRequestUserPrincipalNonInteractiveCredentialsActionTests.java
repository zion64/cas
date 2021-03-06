package org.jasig.cas.adaptors.trusted.web.flow;

import org.jasig.cas.AbstractCentralAuthenticationService;
import org.jasig.cas.AbstractCentralAuthenticationServiceTests;
import org.jasig.cas.adaptors.trusted.authentication.handler.support.PrincipalBearingCredentialsAuthenticationHandler;
import org.jasig.cas.adaptors.trusted.authentication.principal.PrincipalBearingPrincipalResolver;
import org.jasig.cas.authentication.AuthenticationHandler;
import org.jasig.cas.authentication.AuthenticationManager;
import org.jasig.cas.authentication.PolicyBasedAuthenticationManager;
import org.jasig.cas.authentication.principal.DefaultPrincipalFactory;
import org.jasig.cas.authentication.principal.PrincipalResolver;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockServletContext;
import org.springframework.webflow.context.servlet.ServletExternalContext;
import org.springframework.webflow.test.MockRequestContext;

import java.security.Principal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;

/**
 * @author Scott Battaglia
 * @since 3.0.5
 */
public class PrincipalFromRequestUserPrincipalNonInteractiveCredentialsActionTests extends AbstractCentralAuthenticationServiceTests {

    private PrincipalFromRequestUserPrincipalNonInteractiveCredentialsAction action;


    @Before
    public void setUp() throws Exception {
        this.action = new PrincipalFromRequestUserPrincipalNonInteractiveCredentialsAction();
        this.action.setPrincipalFactory(new DefaultPrincipalFactory());

        final AuthenticationManager authenticationManager = new PolicyBasedAuthenticationManager(
                Collections.<AuthenticationHandler, PrincipalResolver>singletonMap(
                        new PrincipalBearingCredentialsAuthenticationHandler(),
                        new PrincipalBearingPrincipalResolver()));

        final AbstractCentralAuthenticationService centralAuthenticationService = (AbstractCentralAuthenticationService)
                getCentralAuthenticationService();
        centralAuthenticationService.setAuthenticationManager(authenticationManager);
        this.action.setCentralAuthenticationService(centralAuthenticationService);
    }

    @Test
    public void verifyRemoteUserExists() throws Exception {
        final MockHttpServletRequest request = new MockHttpServletRequest();
        request.setUserPrincipal(new Principal() {
            @Override
            public String getName() {
                return "test";
            }
        });

        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(
                new MockServletContext(), request, new MockHttpServletResponse()));

        assertEquals("success", this.action.execute(context).getId());
    }

    @Test
    public void verifyRemoteUserDoesntExists() throws Exception {
        final MockRequestContext context = new MockRequestContext();
        context.setExternalContext(new ServletExternalContext(
                new MockServletContext(), new MockHttpServletRequest(), new MockHttpServletResponse()));

        assertEquals("error", this.action.execute(context).getId());
    }

}
