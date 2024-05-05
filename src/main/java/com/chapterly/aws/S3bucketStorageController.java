package com.chapterly.aws;

import java.io.ByteArrayOutputStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

/***
 * Main S3bucketStorageController RestController
 */
@CrossOrigin
@RestController
public class S3bucketStorageController {

    @Autowired
    private AmazonClient amazonClient;

    /***
     * download file by filename
     * @param filename that we want to download
     * @return byte[] array
     */
    @GetMapping(value = "/download/{filename}")
    public ResponseEntity<byte[]> downloadFile23(@PathVariable String filename) {
        ByteArrayOutputStream downloadInputStream = amazonClient.downloadFile(filename);

        return ResponseEntity.ok().contentType(contentType(filename))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(downloadInputStream.toByteArray());
    }

    /***
     * contentType is a method that is used by the download file api above that defines the type of content
     * @param filename
     * @return MediaType
     */
    private MediaType contentType(String filename) {
        String[] fileArrSplit = filename.split("\\.");
        String fileExtension = fileArrSplit[fileArrSplit.length - 1];
        switch (fileExtension) {
            case "txt":
                return MediaType.TEXT_PLAIN;
            case "png":
                return MediaType.IMAGE_PNG;
            case "jpg":
                return MediaType.IMAGE_JPEG;
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}