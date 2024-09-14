package org.baixador;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProcessRunner {

    public byte[] runDownloadCommand(String url) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("yt-dlp", "-o", "-", url);
        Process process = processBuilder.start();

        try(InputStream inputStream = process.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
            process.waitFor();
            return outputStream.toByteArray();
        } catch (InterruptedException e) {
            throw new IOException("Process was interrupted", e);
        }
    }

    public String runTitleCommand(String url) throws IOException {
        List<String> command = new ArrayList<>();
        command.add("yt-dlp");
        command.add("--print");
        command.add("title");
        command.add(url);

        ProcessBuilder builder = new ProcessBuilder(command);
        Process process = builder.start();

        try(BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
            String videoTitle = reader.readLine();
            process.waitFor();
            return videoTitle;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
