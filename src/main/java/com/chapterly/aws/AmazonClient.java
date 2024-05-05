package com.chapterly.aws;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

/***
 * Main AmazonClient Service
 */
@Service
public class AmazonClient {
    private AmazonS3 s3client;
    @Value("${s3.endpointUrl}")
    private String endpointUrl;
    @Value("${s3.bucketName}")
    private String bucketName;
    @Value("${s3.accessKeyId}")
    private String accessKeyId;
    @Value("${s3.secretKey}")
    private String secretKey;
    @Value("${s3.region}")
    private String region;


    /***
     *This method runs just after the bean is created.
     * creates the AmazonClient Object so that we could perform any task
     * related to aws eg. upload, delete, get etc.
     */
    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKeyId, this.secretKey);
        this.s3client = AmazonS3ClientBuilder.standard().withRegion(region)
                .withCredentials(new AWSStaticCredentialsProvider(credentials)).build();
    }

    /***
     * Uploads the file to aws s3 bucket
     * @param multipartFile
     * @return the fileName and it's url
     * @throws Exception in case something goes wrong
     */
    public String uploadFile(MultipartFile multipartFile) throws Exception {
        String fileUrl = "";
        File file = convertMultiPartToFile(multipartFile);
        String fileName = generateFileName(multipartFile);
//		fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
        uploadFileTos3bucket(fileName, file);
//		file.delete();
        return fileName;
    }


    /***
     * deletes a file from s3 bucket
     * @param fileUrl is the reference to the file we want to delete
     * @return status of file deletion
     */
    public String deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3client.deleteObject(bucketName, fileName);
        return "Successfully deleted";
    }

    /**
     * This api uploads file to s3 bucket but doesn't return anything
     * @param fileName name of the file
     * @param file which we want to store
     */
    private void uploadFileTos3bucket(String fileName, File file) {
        s3client.putObject(bucketName, fileName, file);
    }

    /***
     * This api converts Multipart to File type
     * @param file which we want to convert
     * @return converted file
     * @throws IOException
     */
    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    /***
     * download file from s3 bucket
     * @param keyName is filename which we want to download
     * @return ByteArrayOutputStream
     * /throws exception if anything goes wrong
     */
    public ByteArrayOutputStream downloadFile(String keyName) {
        try {
            S3Object s3object = s3client.getObject(new GetObjectRequest(bucketName, keyName));

            InputStream is = s3object.getObjectContent();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            int len;
            byte[] buffer = new byte[4096];
            while ((len = is.read(buffer, 0, buffer.length)) != -1) {
                outputStream.write(buffer, 0, len);
            }
            return outputStream;
        } catch (IOException ioException) {
//            logger.error("IOException: " + ioException.getMessage());
        } catch (AmazonServiceException serviceException) {
            // logger.info("AmazonServiceException Message: " +
            // serviceException.getMessage());
            throw serviceException;
        } catch (AmazonClientException clientException) {
            // logger.info("AmazonClientException Message: " +
            // clientException.getMessage());
            throw clientException;
        }

        return null;
    }

    /***
     * it generates a specific file url and returns it so that in future it doesn't match with any other file
     * @param multiPart file of which we want to generate a filename
     * @return unique file name
     */
    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}