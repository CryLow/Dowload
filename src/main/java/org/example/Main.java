package org.example;

import org.json.JSONObject;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;


public class Main {
    public static void main(String[] args) throws IOException {
        String kpid = "";
        String name = null;
        try {
            Document document = Jsoup.connect("https://lordserial.run/zarubezhnye-serialy/707-miss-marvel-v1.html")
                    .userAgent("Chrome/4.0.249.0 Safari/532.5")
                    .referrer("https://yandex.ru")
                    .get();
            Elements elements = document.select("iframe");
            for(var i : elements){
                if(i.attr("src").contains("//arcchid.link/sow/")){
                    String temp = i.attr("src");
                    int index = 19;
                    kpid = temp.substring(index); //Получаем kpid
                }

            }
            Elements elements1 = document.select("li > span ");
            for(var i : elements1){
                if(i.attr("itemprop").equals("alternativeHeadline")) name = i.text();
            }

            int episode = 1;
            int season = 1;
            int voice = 6;

            String urlResp = "https://arcchid.link/player/responce.php?kpid="+ kpid +"&season="+ season+ "&episode="+ episode +"&voice="+ voice +"&type=undefined&uniq";
            String urlMainIndex = null;
            InputStream responsible = null;

            URL resp = new URL(urlResp);
            HttpURLConnection httpURLConnection = (HttpURLConnection) resp.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            httpURLConnection.setReadTimeout(10000);

            if(HttpURLConnection.HTTP_OK == httpURLConnection.getResponseCode()){
                responsible = httpURLConnection.getInputStream();
            }
            StringBuilder stringBuilder = new StringBuilder();
            int bytesRead;
            while ((bytesRead=responsible.read()) != -1) {
                stringBuilder.append((char) bytesRead);
            }
            JSONObject jsonObject1 = new JSONObject(stringBuilder.toString());
            urlMainIndex = jsonObject1.getString("src");
            responsible.close();


            InputStream indexMainM3u8 = null;

            URL indexm3u8 = new URL(urlMainIndex);
            HttpURLConnection httpURLConnectionM3U8 = (HttpURLConnection) indexm3u8.openConnection();
            httpURLConnectionM3U8.setRequestMethod("GET");
            httpURLConnectionM3U8.setConnectTimeout(10000);
            httpURLConnectionM3U8.setReadTimeout(10000);

            if(HttpURLConnection.HTTP_OK == httpURLConnectionM3U8.getResponseCode()){
                indexMainM3u8 = httpURLConnectionM3U8.getInputStream();
            }
            StringBuilder stringBuilderM3U8 = new StringBuilder();
            OutputStream mainM3U8 = new FileOutputStream("M3U8.m3u8");
            while ((bytesRead=indexMainM3u8.read()) != -1) {
                stringBuilderM3U8.append((char) bytesRead);
                mainM3U8.write((char) bytesRead);
            }
            String index = stringBuilderM3U8.toString();
            mainM3U8.close();
            indexMainM3u8.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        boolean isNull = true;
        InputStream inputStream;
        OutputStream outputStream = new FileOutputStream(name + ".mp4");
        for(int i = 1000;isNull;i++){
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