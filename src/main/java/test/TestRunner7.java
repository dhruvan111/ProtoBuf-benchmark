package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.beans2.*;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static test.TestRunner5.*;

public class TestRunner7 {

    private static TwilioEvent getObjProto(List<Integer> byteData){

        List<String> bigDataList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String data = "Data_" + i;
            bigDataList.add(data);
        }

        long startTime = System.nanoTime();
        StreamMetadata metadata = StreamMetadata.newBuilder()
                .setAppId("app id 101")
                .setCaseNumber(1234355)
                .setParticipantId("part id 1001")
                .setPartnerId(24345324)
                .build();

        MediaFormat mediaFormat = MediaFormat.newBuilder()
                .setChannels(12)
                .setEncoding("encoding")
                .setSampleRate(354)
                .build();

        StartData startData = StartData.newBuilder()
                .setAccountSid("acc id 101")
                .addAllTracks0(bigDataList)
                .setMediaFormat(mediaFormat)
                .setStreamMetadata(metadata)
                .build();

        MediaData mediaData = MediaData.newBuilder()
                .setPayload("pay load 101")
//                .addAllBytes(byteData)
                .setDurationMillis(122424)
                .build();

        TwilioEvent twilioEvent = TwilioEvent.newBuilder()
                .setEvent("setting event 101")
                .setStart(startData)
                .setMedia(mediaData)
                .build();
        long endTime = System.nanoTime();
        System.out.println("Proto Build Time: " + (endTime-startTime));
        return twilioEvent;
    }

    private static test.beans2.TwilioEvent getObjJson(byte[] byteData){

        List<String> bigDataList = new ArrayList<>();

        for (int i = 0; i < 100; i++) {
            String data = "Data_" + i;
            bigDataList.add(data);
        }

        long startTime = System.nanoTime();
        test.beans2.StreamMetadata metadata = new test.beans2.StreamMetadata();
        metadata.setAppId("app id 101");
        metadata.setCaseNumber(1234355L);
        metadata.setParticipantId("part id 1001");
        metadata.setPartnerId(String.valueOf(24345324));

        test.beans2.MediaFormat mediaFormat = new test.beans2.MediaFormat("encoding",12 , 354);

        test.beans2.StartData startData = new test.beans2.StartData();
        startData.setAccountSid("acc id 101");
        startData.setTracks(bigDataList);
        startData.setMediaFormat(mediaFormat);
        startData.setStreamMetadata(metadata);

        test.beans2.MediaData mediaData = new test.beans2.MediaData();
        mediaData.setPayload("pay load 101");
//        mediaData.setBytes(byteData);
        mediaData.setDurationMillis(122424);


        test.beans2.TwilioEvent twilioEvent = new test.beans2.TwilioEvent();
        twilioEvent.setEvent("setting event 101");
        twilioEvent.setStart(startData);
        twilioEvent.setMedia(mediaData);
        long endTime = System.nanoTime();

        System.out.println("JSON build Time: " + (endTime-startTime));
        return twilioEvent;
    }

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serializedData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();

        System.out.println("Serialization Json: " + (t2-t1)/mega);
        return serializedData;
    }

    private static byte[] serializeProto(TwilioEvent twilioEvent){
        double t1 = System.nanoTime();
        byte[] serializedData = twilioEvent.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + (t2-t1)/mega);
        return serializedData;
    }

    private static void deserializeJson(String serializedData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.beans2.TwilioEvent twilioEvent = objectMapper.readValue(serializedData, test.beans2.TwilioEvent.class);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Json: " + (t2-t1)/mega);
    }

    private static void deserializeProto(byte[] serializedProto) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        TwilioEvent twilioEvent = TwilioEvent.parseFrom(serializedProto);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Proto: " + (t2-t1)/mega);
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        byte[] audioBytesArr = getAudio(path4);
        List<Integer> audioBytesList = new ArrayList<>();

        for (byte b:audioBytesArr){
            audioBytesList.add((int)b);
        }

        // Proto Obj
        TwilioEvent twilioEvent = getObjProto(audioBytesList);

        // Json Obj
        test.beans2.TwilioEvent twilioEvent1 = getObjJson(audioBytesArr);

        String serializedJson = serializeJson(twilioEvent1);
        deserializeJson(serializedJson);

        byte[] serializedProto = serializeProto(twilioEvent);
        deserializeProto(serializedProto);

        System.out.println("Size in Json: " + ((double)serializedJson.getBytes().length)/1024);
        System.out.println("Size in proto: " + ((double)serializedProto.length)/1024);
    }
}
