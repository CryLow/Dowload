package org.example;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

public class Main {
    public static void main(String[] args) throws IOException {
        boolean isNull = true;
        InputStream inputStream;
        OutputStream outputStream = new FileOutputStream("Test5.mp4");
        for(int i = 600;isNull;i++){
        String urlString = "\n" +
                "https://s5.arcchid.link/content/stream/films/the.witcher.s03e01.1080p.rus.lostfilm.tv_475138/hls/1080/segment" + i +".ts";
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(10000);
            connection.setReadTimeout(10000);

            connection.setDoOutput(true);
            if(HttpURLConnection.HTTP_OK == connection.getResponseCode()){
            inputStream = connection.getInputStream();

            byte[] buffer = new byte[8*1024];
            int bytesRead;
                while ((bytesRead=inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer,0,bytesRead);
                }

                inputStream.close();
            }
        else {
                System.out.println("The End");
                isNull= false;
            }
        }
        outputStream.close();
}}