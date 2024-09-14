package org.baixador.controller;

import org.baixador.ProcessRunner;
import org.baixador.controller.request.YoutubeURL;
import org.baixador.service.DownloaderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@RequestMapping("/download")
@RestController
public class DownloaderController {

    @Autowired
    private DownloaderService downloaderService;

    @PostMapping("/video")
    public ResponseEntity<byte[]> download(@RequestBody YoutubeURL youtubeURL) {
        try {
            byte[] videoData = downloaderService.getVideoBytes(youtubeURL);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.valueOf("video/mp4"));
            headers.setContentLength(videoData.length);

            return new ResponseEntity<>(videoData, headers, HttpStatus.OK);
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("ERROR IN DOWNLOAD TO SERVER.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


}
