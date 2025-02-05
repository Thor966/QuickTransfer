package com.ag.file;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/file-notify")
public class FileNotificationServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // Queue to store client connections
    private static final Queue<PrintWriter> clientQueue = new ConcurrentLinkedQueue<>();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // Get message from client
        String message = request.getParameter("message");
        if (message != null) {
            // Notify all connected clients
            for (PrintWriter client : clientQueue) {
                client.println("data: " + message + "\n");
                client.flush();
            }
        }
        response.setStatus(HttpServletResponse.SC_OK);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        response.setContentType("text/event-stream");
        response.setCharacterEncoding("UTF-8");
        
        PrintWriter out = response.getWriter();
        clientQueue.add(out);

        // Keep the connection open
        while (true) {
            try {
                Thread.sleep(10000); // Check every 10 seconds for new data
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
