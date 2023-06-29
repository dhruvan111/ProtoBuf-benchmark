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

    private static final int fre = 1;

    private static List<AudioFeedData> getListAudioData() throws IOException {
        List<AudioFeedData> audioFeedData = new ArrayList<>();

        audioFeedData.add(getAudioFeed(path1));
        audioFeedData.add(getAudioFeed(path4));

        return audioFeedData;
    }

    private static AudioFeedData getAudioFeed(String path) throws IOException {

        byte[] audioBytesArr1 = readFileToByteArray(path);

        List<Integer> list1 = new ArrayList<>();

        for (byte b:audioBytesArr1){
            list1.add((int)b);
        }

        return AudioFeedData.newBuilder()
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
                .addAllAudioBytes(list1)
                .setCreatedTime(12324424)
                .setDisableTranscript(false)
                .setDisableRecording(true)
                .setLang("set lang")
                .setDuration(33435453)
                .setCumulativeAudioDuration(12343545)
                .setStreamingData(true)
                .build();
    }

    private static test.bean1.AudioFeedData getFeedFromPath(String path) throws IOException {
        byte[] audioBytesArr1 = readFileToByteArray(path);

        test.bean1.AudioFeedData audioFeedData = new test.bean1.AudioFeedData();
                audioFeedData.setPartnerId(132324244);
                audioFeedData.setStreamId("stream id");
                audioFeedData.setConversationId("conversation id111");
                audioFeedData.setAppId("app id101");
                audioFeedData.setCaseNumber(123243455);
                audioFeedData.setSeqNumber(12244);
                audioFeedData.setStreamStartTime(32324342);
                audioFeedData.setFinal(true);
                audioFeedData.setParticipantId("participant ID1010");
                audioFeedData.setParticipantType("part. type");
                audioFeedData.setAudioBytes(audioBytesArr1);
                audioFeedData.setCreatedTime(12324424);
                audioFeedData.setDisableTranscript(false);
                audioFeedData.setDisableRecording(true);
                audioFeedData.setLang("set lang");
                audioFeedData.setDuration(33435453);
                audioFeedData.setCumulativeAudioDuration(12343545);
                audioFeedData.setStreamingData(true);
        return audioFeedData;
    }

    private static test.bean1.AudioStreamFeedData getObjWithMultiFeed() throws IOException {

        List<test.bean1.AudioFeedData> list = new ArrayList<>();
        list.add(getFeedFromPath(path1));
        list.add(getFeedFromPath(path4));

        test.bean1.AudioStreamFeedData audioStreamFeedData = new test.bean1.AudioStreamFeedData(1234544, "stream id", "app id 101");
        for (test.bean1.AudioFeedData data : list){
            audioStreamFeedData.add(data);
        }
        return audioStreamFeedData;
    }


    private static test.bean1.AudioStreamFeedData getAudioStreamObj(byte[] audioBytes){
        test.bean1.AudioStreamFeedData audioStreamFeedData = new test.bean1.AudioStreamFeedData(1234544, "stream id", "app id 101");

        test.bean1.AudioFeedData audioFeedData = getObject(audioBytes);
        audioStreamFeedData.setFinalTrue();

        for (int i=0; i<fre; i++){
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

        byte[] audioBytesArr = readFileToByteArray(path114);

        List<AudioFeedData> audioFeedDataList = getListAudioData();

        // proto object
        AudioStreamFeedData streamFeedData = AudioStreamFeedData.newBuilder()
                .setPartnerId(1234544)
                .setStreamId("stream id")
                .setAppId("app id 122")
                .setIsFinal(true)
                .addAllAudioFeed0(audioFeedDataList)
                .build();

        // Simple object for Json
        test.bean1.AudioStreamFeedData streamFeedData1 = getObjWithMultiFeed();

        String serializedJson = serializeJson(streamFeedData1);
        deserializeJson(serializedJson);

        byte[] serializedProto = serializeProto(streamFeedData);
        deserializeProto(serializedProto);

        System.out.println("Size in Json: " + ((double)serializedJson.getBytes().length)/1024);
        System.out.println("Size in proto: " + ((double)serializedProto.length)/1024);
    }
}
