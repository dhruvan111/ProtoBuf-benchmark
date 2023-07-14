package test.accuracy;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.bean1.AudioFeedData;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.*;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TestRunner1 {

    public static final double mega = 1000000;
    public static final String path1 = "/Users/dhruvankadavala/Documents/Protobuf2/src/main/java/test/5-hours-output/speech-1.wav";

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
        return new byte[(int) audioInputStream.getFrameLength() * format.getFrameSize()];
    }

    private static byte[] serializeProto(AudioFeedData audioFeedData){
        double t1 = System.nanoTime();
        byte[] serializedData = audioFeedData.toByteArray();
        double t2 = System.nanoTime();

        System.out.println("Serialization Proto: " + (t2-t1)/mega);
        return serializedData;
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


        return audioFeedData1;
    }


    public static boolean accuracyTest(test.bean1.AudioFeedData audioFeedData1, test.bean1.AudioFeedData audioFeedData2) throws IllegalAccessException {
        Field[] fields = audioFeedData1.getClass().getDeclaredFields();
        for (Field field: fields){
            if (!compareFields(audioFeedData1, audioFeedData2, field)){
                return false;
            }
        }
        return true;
    }

    public static boolean compareFields(Object obj1, Object obj2,Field field) throws IllegalAccessException {
        Object val1 = field.get(obj1);
        Object val2 = field.get(obj2);
        if (field.getType().equals(byte[].class)){
            return (Arrays.equals((byte[]) val1, (byte[]) val2));
        }
        return ((val1 != null) && val1.equals(val2));
    }

    public static void main(String[] args) throws IOException, UnsupportedAudioFileException, IllegalAccessException {

        byte[] audioBytesArr = getAudio(path1);
        List<Integer> audioBytesList = new ArrayList<>();

        for (byte b:audioBytesArr){
            audioBytesList.add((int)b);
        }

        // Object for Json
        test.bean1.AudioFeedData audioFeedData1 = getObject(audioBytesArr);

        // making proto obj with java bean data
        AudioFeedData audioFeedDataObj = AudioFeedData.newBuilder()
                .setPartnerId(audioFeedData1.getPartnerId())
                .setStreamId(audioFeedData1.getStreamId())
                .setConversationId(audioFeedData1.getConversationId())
                .setAppId(audioFeedData1.getAppId())
                .setCaseNumber(audioFeedData1.getCaseNumber())
                .setSeqNumber(audioFeedData1.getSeqNumber())
                .setStreamStartTime(audioFeedData1.getStreamStartTime())
                .setIsFinal(audioFeedData1.isFinal())
                .setParticipantId(audioFeedData1.getParticipantId())
                .setParticipantType(audioFeedData1.getParticipantType())
                .addAllAudioBytes(audioBytesList)
                .setCreatedTime(audioFeedData1.getCreatedTime())
                .setDisableTranscript(audioFeedData1.isDisableTranscript())
                .setDisableRecording(audioFeedData1.isDisableRecording())
                .setLang(audioFeedData1.getLang())
                .setDuration(audioFeedData1.getDuration())
                .setCumulativeAudioDuration(audioFeedData1.getCumulativeAudioDuration())
                .setStreamingData(audioFeedData1.isStreamingData())
                .build();

        byte[] serProtoObj = serializeProto(audioFeedDataObj);

        AudioFeedData desProtoObj = AudioFeedData.parseFrom(serProtoObj);

        byte[] audioBytesArrFromProto = new byte[desProtoObj.getAudioBytesList().size()];

        int idx = 0;
        for (Integer val:desProtoObj.getAudioBytesList()){
            audioBytesArrFromProto[idx] = val.byteValue();
            idx++;
        }

        test.bean1.AudioFeedData audioFeedDataJsonObj = new test.bean1.AudioFeedData();
        audioFeedDataJsonObj.setPartnerId(desProtoObj.getPartnerId());
        audioFeedDataJsonObj.setStreamId(desProtoObj.getStreamId());
        audioFeedDataJsonObj.setConversationId(desProtoObj.getConversationId());
        audioFeedDataJsonObj.setAppId(desProtoObj.getAppId());
        audioFeedDataJsonObj.setCaseNumber(desProtoObj.getCaseNumber());
        audioFeedDataJsonObj.setSeqNumber(desProtoObj.getSeqNumber());
        audioFeedDataJsonObj.setStreamStartTime(desProtoObj.getStreamStartTime());
        audioFeedDataJsonObj.setFinal(desProtoObj.getIsFinal());
        audioFeedDataJsonObj.setParticipantId(desProtoObj.getParticipantId());
        audioFeedDataJsonObj.setParticipantType(desProtoObj.getParticipantType());
        audioFeedDataJsonObj.setAudioBytes(audioBytesArrFromProto);
        audioFeedDataJsonObj.setCreatedTime(desProtoObj.getCreatedTime());
        audioFeedDataJsonObj.setDisableTranscript(desProtoObj.getDisableTranscript());
        audioFeedDataJsonObj.setDisableRecording(desProtoObj.getDisableRecording());
        audioFeedDataJsonObj.setLang(desProtoObj.getLang());
        audioFeedDataJsonObj.setDuration(desProtoObj.getDuration());
        audioFeedDataJsonObj.setCumulativeAudioDuration(desProtoObj.getCumulativeAudioDuration());
        audioFeedDataJsonObj.setStreamingData(desProtoObj.getStreamingData());

        if (accuracyTest(audioFeedData1, audioFeedDataJsonObj)){
            System.out.println("Accuracy test passed.");
        } else {
            System.out.println("Accuracy test failed.");
        }
    }
}
