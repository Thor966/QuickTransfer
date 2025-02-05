package com.ag.file;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

@SuppressWarnings("serial")
@WebServlet("/SendFileServlet")
@MultipartConfig
public class SendFileServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Part filePart = request.getPart("file");
        String fileName = filePart.getSubmittedFileName();

        // Directory to store the shared file
        String uploadPath = getServletContext().getRealPath("") + File.separator + "shared_files";
        File directory = new File(uploadPath);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }

        // Clear the directory before saving the new file
        for (File file : directory.listFiles()) {
            file.delete(); // Delete all existing files
        }

        // Save the new file
        File file = new File(uploadPath + File.separator + fileName);
        System.out.println("Saving file to: " + file.getAbsolutePath());

        try (InputStream input = filePart.getInputStream();
             FileOutputStream output = new FileOutputStream(file)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buffer)) != -1) {
                output.write(buffer, 0, bytesRead);
            }
        }

        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write("File uploaded successfully.");
    }
}
