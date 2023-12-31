package test.sizetesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.graph.Graph;
import com.google.protobuf.InvalidProtocolBufferException;
import org.beans2.*;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;

import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static test.TestRunner5.*;

public class TestRunner2 {

    private static TwilioEvent getObjProto(List<Integer> byteData){

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

        List<String> bigDataList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String data = "Data_" + i;
            bigDataList.add(data);
        }

        StartData startData = StartData.newBuilder()
                .setAccountSid("acc id 101")
                .addAllTracks0(bigDataList)
                .setMediaFormat(mediaFormat)
                .setStreamMetadata(metadata)
                .build();

        MediaData mediaData = MediaData.newBuilder()
                .setPayload("pay load 101")
                .addAllBytes(byteData)
                .setDurationMillis(122424)
                .build();

        return TwilioEvent.newBuilder()
                .setEvent("setting event 101")
                .setStart(startData)
                .setMedia(mediaData)
                .build();
    }

    private static test.beans2.TwilioEvent getObjJson(byte[] byteData){

        test.beans2.StreamMetadata metadata = new test.beans2.StreamMetadata();
        metadata.setAppId("app id 101");
        metadata.setCaseNumber(1234355L);
        metadata.setParticipantId("part id 1001");
        metadata.setPartnerId(String.valueOf(24345324));

        test.beans2.MediaFormat mediaFormat = new test.beans2.MediaFormat("encoding",12 , 354);
        List<String> bigDataList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            String data = "Data_" + i;
            bigDataList.add(data);
        }

        test.beans2.StartData startData = new test.beans2.StartData();
        startData.setAccountSid("acc id 101");
        startData.setTracks(bigDataList);
        startData.setMediaFormat(mediaFormat);
        startData.setStreamMetadata(metadata);

        test.beans2.MediaData mediaData = new test.beans2.MediaData();
        mediaData.setPayload("pay load 101");
        mediaData.setBytes(byteData);
        mediaData.setDurationMillis(122424);

        test.beans2.TwilioEvent twilioEvent = new test.beans2.TwilioEvent();
        twilioEvent.setEvent("setting event 101");
        twilioEvent.setStart(startData);
        twilioEvent.setMedia(mediaData);

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

        byte[] audioBytesArr = getAudio(path1);
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

        System.out.println("Shallow size: ");
        System.out.println(ClassLayout.parseInstance(twilioEvent1).toPrintable());
        System.out.println(ClassLayout.parseInstance(twilioEvent).toPrintable());

        System.out.println("Deep size: ");
        System.out.println(GraphLayout.parseInstance(twilioEvent1).totalSize());
        System.out.println(GraphLayout.parseInstance(twilioEvent).totalSize());
    }
}
