package org.example;

import org.example.resolver.ProtoGenerator;

import java.io.IOException;

public class Main {

    private static final String path1 = "/Users/dhruvankadavala/Documents/ProtoVsJSON/src/main/java/test/bean1";
    private static final String path2 = "/Users/dhruvankadavala/Documents/ProtoVsJSON/src/main/java/test/beans2";
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        ProtoGenerator generator = new ProtoGenerator();
        generator.generateAllFiles(path1, "ProtoFiles");
        generator.generateAllFiles(path2, "ProtoFiles");
    }
}
