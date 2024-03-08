package org.example;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.TimeZone;

public class TimezoneValidateFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String timezoneParam = httpRequest.getParameter("timezone");

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            if (!isValidTimezone(timezoneParam)) {
                httpResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                httpResponse.getWriter().println("Invalid timezone parameter. Please provide a valid timezone.");
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean isValidTimezone(String timezone) {
        String[] availableIDs = TimeZone.getAvailableIDs();
        for (String id : availableIDs) {
            if (id.equals(timezone)) {
                return true;
            }
        }
        return false;
    }
}
