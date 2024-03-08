package org.example;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

//http://localhost:8080/Servlet/time?timezone=UTC+2
@WebServlet(value = "/time")
public class TimeServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String timezoneParam = request.getParameter("timezone");
        ZoneId zoneId;

        if (timezoneParam != null && !timezoneParam.isEmpty()) {
            try {
                zoneId = ZoneId.of(timezoneParam.replace(" ", "+"));
            } catch (Exception e) {
                out.println("Invalid timezone parameter");
                return;
            }
        } else {
            zoneId = ZoneId.of("UTC");
        }

        ZonedDateTime currentTime = ZonedDateTime.now(zoneId);
        out.println("Current time in " + zoneId + ": " + currentTime);
    }
}
