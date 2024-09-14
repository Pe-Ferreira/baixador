package org.baixador.service;

import org.baixador.ProcessRunner;
import org.baixador.controller.request.YoutubeURL;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class DownloaderService {

    public byte[] getVideoBytes(YoutubeURL youtubeURL) throws IOException {
        ProcessRunner runner = new ProcessRunner();
        byte[] videoData = runner.runDownloadCommand(youtubeURL.url);
        return videoData;
    }

    public String getVideoTitle(String url) throws IOException {
        ProcessRunner runner = new ProcessRunner();
        return runner.runTitleCommand(url);
    }
}
