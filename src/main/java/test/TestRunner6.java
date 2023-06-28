package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bean1.AudioFeedData;
import org.bean1.AudioStreamFeedData;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static test.TestRunner5.*;

public class TestRunner6 {

    private static List<AudioFeedData> getListAudioData(List<Integer> audioBytesList) throws IOException {
        List<AudioFeedData> audioFeedData = new ArrayList<>();

        AudioFeedData audioFeedData1 = AudioFeedData.newBuilder()
                .setPartnerId(132324244)
                .setStreamId("stream id")
                .setConversationId("conversation id111")
                .setAppId("app id101")
                .setCaseNumber(123243455)
                .setSeqNumber(12244)
                .setStreamStartTime(32324342)
                .setIsFinal(true)
                .setParticipantId("participant ID1010")
                .setParticipantType("part. type")
                .addAllAudioBytes(audioBytesList)
                .setCreatedTime(12324424)
                .setDisableTranscript(false)
                .setDisableRecording(true)
                .setLang("set lang")
                .setDuration(33435453)
                .setCumulativeAudioDuration(12343545)
                .setStreamingData(true)
                .build();

        for (int i=0; i<10; i++){
            audioFeedData.add(audioFeedData1);
        }
        return audioFeedData;
    }

    private static test.bean1.AudioStreamFeedData getAudioStreamObj(byte[] audioBytes){
        test.bean1.AudioStreamFeedData audioStreamFeedData = new test.bean1.AudioStreamFeedData(1234544, "stream id", "app id 101");

        test.bean1.AudioFeedData audioFeedData = getObject(audioBytes);
        audioStreamFeedData.setFinalTrue();
        for (int i=0; i<10; i++){
            audioStreamFeedData.add(audioFeedData);
        }
        return audioStreamFeedData;
    }

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serializedData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();

        System.out.println("Serialization Json: " + (t2-t1)/mega);
        return serializedData;
    }

    private static void deserializeJson(String serializedData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.bean1.AudioStreamFeedData audioStreamFeedData = objectMapper.readValue(serializedData, test.bean1.AudioStreamFeedData.class);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Json: " + (t2-t1)/mega);
    }

    private static byte[] serializeProto(AudioStreamFeedData audioStreamFeedData){
        double t1 = System.nanoTime();
        byte[] serializedData = audioStreamFeedData.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + (t2-t1)/mega);
        return serializedData;
    }

    private static void deserializeProto(byte[] serializedProto) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        AudioStreamFeedData audioFeedData = AudioStreamFeedData.parseFrom(serializedProto);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Proto: " + (t2-t1)/mega);
    }


    public static void main(String[] args) throws IOException {

        byte[] audioBytesArr = readFileToByteArray(path83);
        List<Integer> audioBytesList = new ArrayList<>();

        for (byte b:audioBytesArr){
            audioBytesList.add((int)b);
        }

        List<AudioFeedData> audioFeedDataList = getListAudioData(audioBytesList);

        // proto object
        AudioStreamFeedData streamFeedData = AudioStreamFeedData.newBuilder()
                .setPartnerId(1234544)
                .setStreamId("stream id")
                .setAppId("app id 122")
                .setIsFinal(true)
                .addAllAudioFeed0(audioFeedDataList)
                .build();

        // Simple object for Json
        test.bean1.AudioStreamFeedData streamFeedData1 = getAudioStreamObj(audioBytesArr);

        String serializedJson = serializeJson(streamFeedData1);
        deserializeJson(serializedJson);

        byte[] serializedProto = serializeProto(streamFeedData);
        deserializeProto(serializedProto);

        System.out.println("Size in Json: " + ((double)serializedJson.getBytes().length)/1024);
        System.out.println("Size in proto: " + ((double)serializedProto.length)/1024);
    }
}
