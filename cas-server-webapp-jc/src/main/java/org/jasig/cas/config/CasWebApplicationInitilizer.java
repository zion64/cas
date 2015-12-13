package org.jasig.cas.config;

import java.util.List;

import org.jasig.cas.pojo.ServletSpec;

/**
 * @author JongDeok Ahn
 * @since 4.1.1.2 Created at 2015. 12. 11. 18:53
 */
public class CasWebApplicationInitilizer extends AbstractCasWebApplicationInitializer {

    @Override
    protected List<ServletSpec> getAdditionalServlet() {
        return null;
    }

    @Override
    protected String getCasHome() {
        return "/opt/cas2";
    }

    @Override
    protected String[] getAdditionalServletMappingsForDispatcher() {
        return new String[0];
    }

    @Override
    protected String[] getSpringProfiles() {
        return new String[0];
    }

    @Override
    protected String getDisplayName() {
        return "CAS 4.2.0";
    }
}
