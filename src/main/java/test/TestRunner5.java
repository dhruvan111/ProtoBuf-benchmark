package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bean1.AudioFeedData;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestRunner5 {

    public static final double mega = 1000000;
    public static final String path1 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-1.wav";
    public static final String path4 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-4.wav";
    public static final String path21 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-21.wav";
    public static final String path83 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-83.wav";
    public static final String path114 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-114.wav";

    public static List<Double> serProto = new ArrayList<>();
    public static List<Double> serJson = new ArrayList<>();
    public static List<Double> deserProto = new ArrayList<>();
    public static List<Double> deserJson = new ArrayList<>();
    public static List<Long> buildProto = new ArrayList<>();
    public static List<Long> buildJson = new ArrayList<>();


    public static byte[] getAudio(String name) throws IOException, UnsupportedAudioFileException {
        File file = new File(name);
        byte[] audioBytes = Files.readAllBytes(file.toPath());
        InputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Received audio : " + format + " frame length : " + audioInputStream.getFrameLength());
        return new byte[(int) audioInputStream.getFrameLength() * format.getFrameSize()];
    }

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serializedData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();

        System.out.println("Serialization Json: " + (t2-t1)/mega);
        serJson.add((t2-t1)/mega);
        return serializedData;
    }

    private static byte[] serializeProto(AudioFeedData audioFeedData){
        double t1 = System.nanoTime();
        byte[] serializedData = audioFeedData.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + (t2-t1)/mega);
        serProto.add((t2-t1)/mega);
        return serializedData;
    }

    private static void deserializeJson(String serializedData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.bean1.AudioFeedData audioFeedData = objectMapper.readValue(serializedData, test.bean1.AudioFeedData.class);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Json: " + (t2-t1)/mega);
        deserJson.add((t2-t1)/mega);
    }

    private static void deserializeProto(byte[] serializedProto) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.parseFrom(serializedProto);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Proto: " + (t2-t1)/mega);
        deserProto.add((t2-t1)/mega);
    }

    private static AudioFeedData buildProtoObject(List<Integer> audioBytesList, long patId, String strId, String conId){
        // object for protoBuf
        long startTime = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.newBuilder()
                .setPartnerId(patId)
                .setStreamId(strId)
                .setConversationId(conId)
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
        long endTime = System.nanoTime();
        System.out.println("Proto Build time: " + (endTime-startTime)/1000);
        buildProto.add(((endTime-startTime)/1000));
        return audioFeedData;
    }

    public static test.bean1.AudioFeedData buildJsonObject(byte[] audioBytes){
        test.bean1.AudioFeedData audioFeedData1 = new test.bean1.AudioFeedData();

        long startTime = System.nanoTime();

        audioFeedData1.setPartnerId(132324244);
        audioFeedData1.setStreamId("stream id");
        audioFeedData1.setConversationId("conversation id111");
        audioFeedData1.setAppId("app id101");
        audioFeedData1.setCaseNumber(123243455);
        audioFeedData1.setSeqNumber(12244);
        audioFeedData1.setStreamStartTime(32324342);
        audioFeedData1.setFinal(true);
        audioFeedData1.setParticipantId("participant ID1010");
        audioFeedData1.setParticipantType("part. type");
        audioFeedData1.setAudioBytes(audioBytes);
        audioFeedData1.setCreatedTime(12324424);
        audioFeedData1.setDisableTranscript(false);
        audioFeedData1.setDisableRecording(true);
        audioFeedData1.setLang("set lang");
        audioFeedData1.setDuration(33435453);
        audioFeedData1.setCumulativeAudioDuration(12343545);
        audioFeedData1.setStreamingData(true);

        long endTime = System.nanoTime();

        System.out.println("JSON Build time: " + (endTime-startTime)/1000);
        buildJson.add(((endTime-startTime)/1000));
        return audioFeedData1;
    }

    public static void displayList(List<?> list){
        for (Object val:list){
            System.out.println(val);
        }
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        byte[] audioBytesArr = getAudio(path1);
        List<Integer> audioBytesList = new ArrayList<>();

        for (byte b:audioBytesArr){
            audioBytesList.add((int)b);
        }


        for (int i=0; i<2; i++){

            // Object for proto
            AudioFeedData audioFeedData = buildProtoObject(audioBytesList, 123545+ 100*i, "fwihf " + 100*i, "fwknf " + 343*i);

            // Object for Json
            test.bean1.AudioFeedData audioFeedData1 = buildJsonObject(audioBytesArr);

            // Proto
            byte[] serializedProto = serializeProto(audioFeedData);
            deserializeProto(serializedProto);

            //Json
            String serializedJson = serializeJson(audioFeedData1);
            deserializeJson(serializedJson);
        }

        System.out.println("\nSerJson: ");
        displayList(serJson);

        System.out.println("\nSerProto: ");
        displayList(serProto);

        System.out.println("\nDeser Json: ");
        displayList(deserJson);

        System.out.println("\nDeser Proto: ");
        displayList(deserProto);

        System.out.println("\nbuild Json: ");
        displayList(buildJson);

        System.out.println("\nbuild Proto: ");
        displayList(buildProto);
    }
}
