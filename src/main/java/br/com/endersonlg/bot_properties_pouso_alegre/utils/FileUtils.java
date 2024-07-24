package br.com.endersonlg.bot_properties_pouso_alegre.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

public class FileUtils {

  public static File downloadImage(String imageUrl) throws Exception {
    URL url = new URL(imageUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    InputStream inputStream = connection.getInputStream();
    File tempFile = File.createTempFile(UUID.randomUUID().toString(), ".jpg");
    try (FileOutputStream outputStream = new FileOutputStream(tempFile)) {
      byte[] buffer = new byte[1024];
      int bytesRead;
      while ((bytesRead = inputStream.read(buffer)) != -1) {
        outputStream.write(buffer, 0, bytesRead);
      }
    }
    return tempFile;
  }
}