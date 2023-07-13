package test.sizetesting;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bean1.AudioFeedData;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.info.GraphLayout;


import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class TestRunner3 {

    public static final double mega = 1000000;
    public static final String path1 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-1.wav";
    public static final String path4 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-4.wav";
    public static final String path21 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-21.wav";
    public static final String path83 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-83.wav";
    public static final String path114 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-114.wav";

    public static byte[] readFileToByteArray(String filePath) throws IOException {
        File file = new File(filePath);
        try (FileInputStream fis = new FileInputStream(file);
             ByteArrayOutputStream bos = new ByteArrayOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }
            return bos.toByteArray();
        }
    }


    static byte[] getAudio(String name) throws IOException, UnsupportedAudioFileException {
        File file = new File(name);
        byte[] audioBytes = Files.readAllBytes(file.toPath());
        InputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Received audio : " + format + " frame length : " + audioInputStream.getFrameLength());
//        if (format.getEncoding() != AudioFormat.Encoding.PCM_SIGNED) {
//            // TODO: transform audio if needed
//        }
        byte[] data = new byte[(int) audioInputStream.getFrameLength() * format.getFrameSize()];
        int read = audioInputStream.read(data); //Audio Byte Data
        return data;
    }

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serializedData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();

        System.out.println("Serialization Json: " + (t2-t1)/mega);
        return serializedData;
    }

    private static byte[] serializeProto(AudioFeedData audioFeedData){
        double t1 = System.nanoTime();
        byte[] serializedData = audioFeedData.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + (t2-t1)/mega);
        return serializedData;
    }

    private static void deserializeJson(String serializedData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.bean1.AudioFeedData audioFeedData = objectMapper.readValue(serializedData, test.bean1.AudioFeedData.class);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Json: " + (t2-t1)/mega);
    }

    private static void deserializeProto(byte[] serializedProto) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.parseFrom(serializedProto);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Proto: " + (t2-t1)/mega);
    }

    public static test.bean1.AudioFeedData getObject(byte[] audioBytes){
        test.bean1.AudioFeedData audioFeedData1 = new test.bean1.AudioFeedData();

        audioFeedData1.setPartnerId(132324244);
        audioFeedData1.setStreamId("stream id");
        audioFeedData1.setConversationId("conversation id111");
        audioFeedData1.setAppId("app id101");
        audioFeedData1.setCaseNumber(123243455);
        audioFeedData1.setSeqNumber(12244);
        audioFeedData1.setStreamStartTime(32324342);
        audioFeedData1.setFinal(true);;
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
        return audioFeedData1;
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException {

        byte[] audioBytesArr = new byte[1];
        audioBytesArr[0] = 2;

        List<Integer> audioBytesList = new ArrayList<>();

        for (byte b:audioBytesArr){
            audioBytesList.add((int)b);
        }

        // object for protoBuf
        AudioFeedData audioFeedData = AudioFeedData.newBuilder()
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

        // Object for Json
        test.bean1.AudioFeedData audioFeedData1 = getObject(audioBytesArr);

        //Json
        String serializedJson = serializeJson(audioFeedData1);
        deserializeJson(serializedJson);

        // Proto
        byte[] serializedProto = serializeProto(audioFeedData);
        deserializeProto(serializedProto);

        System.out.println("Shallow size: ");
        System.out.println(ClassLayout.parseInstance(audioFeedData1).toPrintable());
        System.out.println(ClassLayout.parseInstance(audioFeedData).toPrintable());

        System.out.println("Deep size: ");
        System.out.println(GraphLayout.parseInstance(audioFeedData1).toFootprint());
        System.out.println(GraphLayout.parseInstance(audioFeedData).toFootprint());
    }
}
