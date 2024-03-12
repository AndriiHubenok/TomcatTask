package org.example;

import java.io.IOException;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;

//http://localhost:8080/Servlet/time?timezone=UTC+2
@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {
    private TemplateEngine engine;

    @Override
    public void init() throws ServletException {
        engine = new TemplateEngine();
        FileTemplateResolver resolver = new FileTemplateResolver();
        resolver.setPrefix(getServletContext().getRealPath("/WEB-INF/templates/"));
        resolver.setSuffix(".html");
        resolver.setTemplateMode("HTML5");
        resolver.setOrder(engine.getTemplateResolvers().size());
        resolver.setCacheable(false);
        engine.addTemplateResolver(resolver);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");

        String timezoneParam = request.getParameter("timezone");
        ZoneId zoneId = null;

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            try {
                zoneId = ZoneId.of(timezoneParam.replace(" ", "+"));
                Cookie lastTimezoneCookie = new Cookie("lastTimezone", zoneId.toString());
                lastTimezoneCookie.setMaxAge(86400);
                response.addCookie(lastTimezoneCookie);
            } catch (Exception e) {
                response.getWriter().println("Invalid timezone parameter");
                return;
            }
        } else {
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("lastTimezone".equals(cookie.getName())) {
                        try {
                            zoneId = ZoneId.of(cookie.getValue());
                        } catch (Exception e) {
                            zoneId = ZoneId.of("UTC");
                        }
                        break;
                    }
                }
            }
            if (zoneId == null) {
                zoneId = ZoneId.of("UTC");
            }
        }

        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);

        Context context = new Context();
        context.setVariable("currentTime", "Current time in " + zoneId + ": " + currentTime.format(DateTimeFormatter.ISO_ZONED_DATE_TIME));

        engine.process("time", context, response.getWriter());
    }
}
