<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%> <%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html>
  <head>
    <meta charset="UTF-8" />
    <title>User Profile</title>
    <style>
      body {
        font-family: Arial, sans-serif;
        margin: 0;
        padding: 20px;
        background-color: #f5f5f5;
      }
      .container {
        max-width: 800px;
        margin: 0 auto;
        background-color: white;
        padding: 20px;
        border-radius: 5px;
        box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
      }
      .profile-header {
        display: flex;
        margin-bottom: 20px;
      }
      .avatar {
        width: 200px;
        height: 200px;
        background-color: #ddd;
        margin-right: 20px;
        display: flex;
        align-items: center;
        justify-content: center;
      }
      .user-info {
        flex-grow: 1;
      }
      table {
        width: 100%;
        border-collapse: collapse;
        margin: 20px 0;
      }
      th,
      td {
        padding: 8px;
        text-align: left;
        border-bottom: 1px solid #ddd;
      }
      th {
        background-color: #f2f2f2;
      }
      .upload-form {
        margin: 20px 0;
      }
      .upload-btn {
        padding: 8px 16px;
        background-color: #f2f2f2;
        border: 1px solid #ddd;
        border-radius: 4px;
        cursor: pointer;
      }
    </style>
  </head>
  <body>
    <div class="container">
      <div class="profile-header">
        <div class="avatar">
          <!-- Placeholder for avatar image -->
          <img
            src=""
            alt="Profile Picture"
            id="avatar-img"
            style="max-width: 100%; max-height: 100%; display: none"
          />
          <span id="avatar-placeholder">No Image</span>
        </div>
        <div class="user-info">
          <h1>It's Me</h1>
          <table class="user-details">
            <tr>
              <td><strong>First Name:</strong></td>
              <td>${user.first_name}</td>
            </tr>
            <tr>
              <td><strong>Last Name:</strong></td>
              <td>${user.last_name}</td>
            </tr>
            <tr>
              <td><strong>Email:</strong></td>
              <td>${user.email}</td>
            </tr>
            <tr>
              <td><strong>Phone:</strong></td>
              <td>+${user.phone_number}</td>
            </tr>
          </table>

          <div class="upload-form">
            <form
              action="${pageContext.request.contextPath}/images"
              method="post"
              enctype="multipart/form-data"
            >
              <input type="file" name="avatar" id="avatar" accept="image/*" />
              <button class="upload-btn" type="submit">Upload</button>
            </form>
          </div>
        </div>
      </div>

      <!-- Authentication History Table -->
      <h2>Authentication History</h2>
      <table>
        <thead>
          <tr>
            <th>Date</th>
            <th>Time</th>
            <th>IP</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty authHistory}">
              <tr>
                <td colspan="3">No authentication history available</td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="authEvent" items="${authHistory}">
                <tr>
                  <td>${authEvent.formattedDate}</td>
                  <td>${authEvent.formattedTime}</td>
                  <td>${authEvent.ipAddress}</td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
      <!-- Uploaded Files Table -->
      <h2>Uploaded Files</h2>
      <table>
        <thead>
          <tr>
            <th>File name</th>
            <th>Size</th>
            <th>MIME</th>
          </tr>
        </thead>
        <tbody>
          <c:choose>
            <c:when test="${empty userFiles}">
              <tr>
                <td colspan="3">No files uploaded yet</td>
              </tr>
            </c:when>
            <c:otherwise>
              <c:forEach var="file" items="${userFiles}">
                <tr>
                  <td>
                    <a
                      href="${pageContext.request.contextPath}/images/${file.storedFilename}"
                      target="_blank"
                      >${file.originalFilename}</a
                    >
                  </td>
                  <td>${file.formattedSize}</td>
                  <td>${file.mimeType}</td>
                </tr>
              </c:forEach>
            </c:otherwise>
          </c:choose>
        </tbody>
      </table>
    </div>
  </body>
</html>
