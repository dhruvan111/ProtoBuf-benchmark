package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bean1.AudioFeedData;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FinalTestRunner {
    public static long mega = 1000000;
    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, InterruptedException {

        String t1 = "t1";
        Runnable runnable = () -> {
            try {
                extracted(t1);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread = new Thread(runnable);

        String t2 = "t2";
        Runnable runnable2 = () -> {
            try {
                extracted(t2);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread2 = new Thread(runnable2);

        thread.start();
        thread2.start();
        Thread.sleep(2_000);

        String t3 = "t3";
        Runnable runnable3 = () -> {
            try {
                extracted(t3);
            } catch (InvalidProtocolBufferException e) {
                throw new RuntimeException(e);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
        Thread thread3 = new Thread(runnable3);
        thread3.start();
        Thread.sleep(2_000);
    }

    private static void extracted(String thread) throws InvalidProtocolBufferException, JsonProcessingException {
        for (int i = 0; i < 2; i++) {

            // Object for proto
            System.out.println("Threads = " + thread);
            AudioFeedData audioFeedData = buildProtoObject(123 + i, "hell" + thread, "nw" + thread);
            // Proto
            byte[] serializedProto = serializeProto(audioFeedData);
            deserializeProto(serializedProto);

            // Object for Json
            test.bean1.AudioFeedData audioFeedData1 = buildJsonObject();
            //Json
            String serializedJson = serializeJson(audioFeedData1);
            deserializeJson(serializedJson);
        }
    }

    private static AudioFeedData buildProtoObject(long partnerId, String streamId, String convId) {
        // object for protoBuf
        long startTime = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.newBuilder().setPartnerId(partnerId).setStreamId(streamId).setConversationId(convId).setAppId(convId).setCaseNumber(partnerId).setSeqNumber((int) partnerId).setStreamStartTime(partnerId).setIsFinal(true).setParticipantId(streamId).setParticipantType(convId).setCreatedTime(partnerId).setDisableTranscript(false).setDisableRecording(true).setLang(streamId).setDuration(partnerId).setCumulativeAudioDuration(partnerId).setStreamingData(true).build();
        long endTime = System.nanoTime();
        System.out.println("Proto Build time: " + (endTime - startTime) / 1000);
        return audioFeedData;
    }

    private static byte[] serializeProto(AudioFeedData audioFeedData) {
        double t1 = System.nanoTime();
        byte[] serializedData = audioFeedData.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + (t2 - t1) / mega);
        return serializedData;
    }

    private static void deserializeProto(byte[] serializedProto) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.parseFrom(serializedProto);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Proto: " + (t2 - t1) / mega);
    }

    public static test.bean1.AudioFeedData buildJsonObject() {
        long startTime = System.nanoTime();
        test.bean1.AudioFeedData audioFeedData1 = new test.bean1.AudioFeedData();
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
        audioFeedData1.setCreatedTime(12324424);
        audioFeedData1.setDisableTranscript(false);
        audioFeedData1.setDisableRecording(true);
        audioFeedData1.setLang("set lang");
        audioFeedData1.setDuration(33435453);
        audioFeedData1.setCumulativeAudioDuration(12343545);
        audioFeedData1.setStreamingData(true);

        long endTime = System.nanoTime();

        System.out.println("JSON Build time: " + (endTime - startTime) / mega);
        return audioFeedData1;
    }

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serializedData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();

        System.out.println("Serialization Json: " + (t2 - t1) / mega);
        return serializedData;
    }

    private static void deserializeJson(String serializedData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.bean1.AudioFeedData audioFeedData = objectMapper.readValue(serializedData, test.bean1.AudioFeedData.class);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Json: " + (t2 - t1) / mega);
    }

    public static byte[] getAudio(String name) throws IOException, UnsupportedAudioFileException {
        File file = new File(name);
        byte[] audioBytes = Files.readAllBytes(file.toPath());
        InputStream byteArrayInputStream = new ByteArrayInputStream(audioBytes);
        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(byteArrayInputStream);
        AudioFormat format = audioInputStream.getFormat();
        System.out.println("Received audio : " + format + " frame length : " + audioInputStream.getFrameLength());
        return new byte[(int) audioInputStream.getFrameLength() * format.getFrameSize()];
    }


}