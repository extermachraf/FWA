package com.example.servlet.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.example.servlet.models.UploadedFile;
import com.example.servlet.models.User;
import com.example.servlet.services.FileStorageService;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;

@WebServlet("/images/*")
@MultipartConfig(
        fileSizeThreshold = 1024 * 1024, // 1 MB
        maxFileSize = 1024 * 1024 * 10, // 10 MB
        maxRequestSize = 1024 * 1024 * 20 // 20 MB
)
public class ImagesServlet extends HttpServlet {

    private static final Logger logger = LoggerFactory.getLogger(ImagesServlet.class);
    private FileStorageService fileStorageService;

    @Override
    public void init() throws ServletException {
        ApplicationContext springContext = (ApplicationContext) getServletContext().getAttribute("springContext");
        this.fileStorageService = springContext.getBean(FileStorageService.class);
        logger.info("ImagesServlet initialized with FileStorageService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must be logged in to upload files");
            return;
        }

        User user = (User) session.getAttribute("user");

        try {
            Part filePart = req.getPart("avatar");
            if (filePart == null) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "No file part in the request");
                return;
            }

            String contentType = filePart.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                resp.sendError(HttpServletResponse.SC_BAD_REQUEST, "Only image files are allowed");
                return;
            }

            String fileName = extractFileName(filePart);
            Long fileSize = filePart.getSize();

            try (InputStream fileContent = filePart.getInputStream()) {
                UploadedFile storedFile = fileStorageService.storeFile(
                        fileContent, fileName, contentType, fileSize, user);

                logger.info("File uploaded: {} (stored as {})", fileName, storedFile.getStoredFilename());
            }

            resp.sendRedirect(req.getContextPath() + "/profile");

        } catch (Exception e) {
            logger.error("Error uploading file: {}", e.getMessage());
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "File upload failed: " + e.getMessage());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String pathInfo = req.getPathInfo();

        // If no specific image is requested, redirect to profile
        if (pathInfo == null || pathInfo.equals("/")) {
            resp.sendRedirect(req.getContextPath() + "/profile");
            return;
        }

        // Remove leading slash
        String fileName = pathInfo.substring(1);

        Optional<UploadedFile> fileInfo = fileStorageService.getFileInfo(fileName);
        if (!fileInfo.isPresent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image not found");
            return;
        }

        Optional<Path> filePath = fileStorageService.getFilePath(fileName);
        if (!filePath.isPresent()) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Image file not found on disk");
            return;
        }

        // Set content type and headers
        resp.setContentType(fileInfo.get().getMimeType());
        resp.setHeader("Content-Disposition", "inline; filename=\"" + fileInfo.get().getOriginalFilename() + "\"");

        // Stream the file content to the response
        Files.copy(filePath.get(), resp.getOutputStream());
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        String[] items = contentDisp.split(";");

        for (String item : items) {
            if (item.trim().startsWith("filename")) {
                String fileName = item.substring(item.indexOf("=") + 2, item.length() - 1);
                // Get just the filename, not the whole path
                return fileName.substring(fileName.lastIndexOf('/') + 1)
                        .substring(fileName.lastIndexOf('\\') + 1);
            }
        }
        return "unknown";
    }
}
