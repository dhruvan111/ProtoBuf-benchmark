package test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.protobuf.InvalidProtocolBufferException;
import org.bean1.AudioFeedData;

import java.util.Random;

public class FinalTestRunner1 {
    private static final long mega = 1000000;
    private static final Random random = new Random();

    public static void main(String[] args) throws InvalidProtocolBufferException, JsonProcessingException {

        int fre = 10;
        testing(fre);
    }

    private static void testing(int fre) throws InvalidProtocolBufferException, JsonProcessingException {

        for (int i = 0; i < fre; i++) {
            long randomNum = random.nextLong();
            String randomStr = "random string " + (17 * i) + " random str";
            jsonOpr(randomNum, randomStr, i);
            protoOpr(randomNum, randomStr, i);
            System.out.print("\n");
        }
    }

    private static void protoOpr(long randomNum, String randomStr, int i) throws InvalidProtocolBufferException {
        AudioFeedData audioFeedDataProto = buildProtoObject(randomNum + i, randomStr + 12 * i, randomStr + 10 * i, randomStr + 15 * i, randomNum + 20L * i, 130 * i, randomNum + 1000L * i);
        byte[] serProto = serializeProto(audioFeedDataProto);
        deserializeProto(serProto);
    }

    private static void jsonOpr(long randomNum, String randomStr, int i) throws JsonProcessingException {
        test.bean1.AudioFeedData audioFeedDataJson = buildJsonObject(randomNum + i, randomStr + 12 * i, randomStr + 10 * i, randomStr + 15 * i, randomNum + 20L * i, 130 * i, randomNum + 1000L * i);
        String serJSON = serializeJson(audioFeedDataJson);
        deserializeJson(serJSON);
    }

    private static AudioFeedData buildProtoObject(long patId, String strId, String conId, String appId, long caseNum, int seqNum,
                                                  long strTime) {
        boolean setFinal = true;
        // object for protoBuf
        long startTime = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.newBuilder()
                .setPartnerId(patId)
                .setStreamId(strId)
                .setConversationId(conId)
                .setAppId(appId)
                .setCaseNumber(caseNum)
                .setSeqNumber(seqNum)
                .setStreamStartTime(strTime)
                .setIsFinal(setFinal)
                .setParticipantId(strId)
                .setParticipantType(appId)
                .setCreatedTime(caseNum)
                .setDisableTranscript(setFinal)
                .setDisableRecording(!setFinal)
                .setLang(appId)
                .setDuration(strTime)
                .setCumulativeAudioDuration(caseNum)
                .setStreamingData(setFinal)
                .build();

        long endTime = System.nanoTime();
        System.out.println("Proto Build time: " + ((endTime - startTime) / 1000) + " μs");
        return audioFeedData;
    }

    private static byte[] serializeProto(AudioFeedData audioFeedData) {
        double t1 = System.nanoTime();
        byte[] serializedData = audioFeedData.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + ((t2 - t1) / mega) + " ms");
        return serializedData;
    }

    private static void deserializeProto(byte[] serializedProto) throws InvalidProtocolBufferException {
        double t1 = System.nanoTime();
        AudioFeedData audioFeedData = AudioFeedData.parseFrom(serializedProto);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Proto: " + ((t2 - t1) / mega) + " ms");
    }

    public static test.bean1.AudioFeedData buildJsonObject(long patId, String strId, String conId, String appId, long caseNum, int seqNum,
                                                           long strTime) {
        boolean setFinal = true;
        long startTime = System.nanoTime();
        test.bean1.AudioFeedData audioFeedData1 = new test.bean1.AudioFeedData();
        audioFeedData1.setPartnerId(patId);
        audioFeedData1.setStreamId(strId);
        audioFeedData1.setConversationId(conId);
        audioFeedData1.setAppId(appId);
        audioFeedData1.setCaseNumber(caseNum);
        audioFeedData1.setSeqNumber(seqNum);
        audioFeedData1.setStreamStartTime(strTime);
        audioFeedData1.setFinal(setFinal);
        audioFeedData1.setParticipantId(strId);
        audioFeedData1.setParticipantType(appId);
        audioFeedData1.setCreatedTime(caseNum);
        audioFeedData1.setDisableTranscript(setFinal);
        audioFeedData1.setDisableRecording(!setFinal);
        audioFeedData1.setLang(appId);
        audioFeedData1.setDuration(strTime);
        audioFeedData1.setCumulativeAudioDuration(caseNum);
        audioFeedData1.setStreamingData(setFinal);

        long endTime = System.nanoTime();

        System.out.println("JSON Build time: " + ((endTime - startTime) / 1000) + " μs");
        return audioFeedData1;
    }

    private static String serializeJson(Object object) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        String serializedData = objectMapper.writeValueAsString(object);
        double t2 = System.nanoTime();

        System.out.println("Serialization Json: " + ((t2 - t1) / mega) + " ms");
        return serializedData;
    }

    private static void deserializeJson(String serializedData) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        double t1 = System.nanoTime();
        test.bean1.AudioFeedData audioFeedData = objectMapper.readValue(serializedData, test.bean1.AudioFeedData.class);
        double t2 = System.nanoTime();

        System.out.println("Deserialization Json: " + ((t2 - t1) / mega) + " ms");
    }
}
