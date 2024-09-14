package org.baixador.view;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;
import org.baixador.controller.request.YoutubeURL;
import org.baixador.service.DownloaderService;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.ByteArrayInputStream;
import java.io.IOException;

@Route
public class DownloaderView extends VerticalLayout {

    private final DownloaderService downloaderService;
    private TextField urlField;
    private Button downloadButton;

    @Autowired
    public DownloaderView(DownloaderService downloaderService) {
        this.downloaderService = downloaderService;
        setupUI();
        configureListerners();
    }

    private void setupUI() {
        urlField = new TextField("Youtube video URL");
        urlField.setPlaceholder("My video url");

        downloadButton = new Button("Process video");
        add(urlField, downloadButton);
    }

    private void configureListerners() {
        downloadButton.addClickListener(event -> {
            try {
                handleDownload();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void handleDownload() throws IOException {
        String url = urlField.getValue();
        if (!url.isBlank()) {
            YoutubeURL ytUrl = new YoutubeURL();
            ytUrl.url = url;
            String videoTitle = downloaderService.getVideoTitle(url);
            byte[] videoData = downloaderService.getVideoBytes(ytUrl);

            serveVideoAsDownload(videoData, videoTitle);
            Notification.show("Downloaded.");
        }
    }

    private void serveVideoAsDownload(byte[] videoData, String videoTitle) {
        StreamResource streamResource = new StreamResource(videoTitle + ".mp4", () -> new ByteArrayInputStream(videoData));

        Anchor downloadLink = new Anchor(streamResource, "Download your video");
        downloadLink.getElement().setAttribute("download", true);

        add(downloadLink);
    }
}
