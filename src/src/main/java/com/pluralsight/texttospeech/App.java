package com.pluralsight.texttospeech;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.*;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static void main(String[] args) throws UnirestException, IOException {
        String bearer = "Bearer " + Unirest.post("https://eastus.api.cognitive.microsoft.com/sts/v1.0/issuetoken")
                .header("Ocp-Apim-Subscription-Key","{{ Your Subscription Key }}")
                .asString().getBody();

        String ssml = "<speak version='1.0' xmlns=\"http://www.w3.org/2001/10/synthesis\" xml:lang='en-US'>" +
                        "<voice  name='Microsoft Server Speech Text to Speech Voice (en-US, Jessa24kRUS)'>" +
                        "Welcome to Microsoft Cognitive Services <break time=\"100ms\" /> Text-to-Speech API." +
                        "</voice> </speak>";

        Map<String,String> audio_headers = new HashMap<String, String>();
        audio_headers.put("Content-Type", "application/ssml+xml");
        audio_headers.put("X-Microsoft-OutputFormat", "audio-24khz-48kbitrate-mono-mp3");
        audio_headers.put("User-Agent", "PluralsightDemo");
        audio_headers.put("Authorization", bearer );

        InputStream audio = Unirest.post("https://eastus.tts.speech.microsoft.com/cognitiveservices/v1")
                .headers(audio_headers)
                .body(ssml)
                .asBinary().getBody();

        Path path = FileSystems.getDefault().getPath("speech.mpga");
        Files.copy(audio,path);

    }
}
