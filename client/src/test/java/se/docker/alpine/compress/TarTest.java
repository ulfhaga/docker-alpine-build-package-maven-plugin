package se.docker.alpine.compress;


import org.junit.jupiter.api.*;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;


@Tag("tag2")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TarTest
{
    public static final String SOURCE_TAR = "source.tar";

    @Test
    @Order(1)
    void createTarFolder() throws IOException
    {
        Path sourceDirectory = Paths.get("src", "test", "resources", "testData");
        Path archive = Paths.get("target", SOURCE_TAR);
        Tar.createTarContent(sourceDirectory, archive);
        assertTrue(Files.exists(archive));
    }

    @Test
    @Order(2)
    void decompressTarFile() throws IOException
    {
        Path target = Paths.get("target", "testData");
        Path source = Paths.get("target", SOURCE_TAR);
        Tar.extractTarFile(source, target);
        Path hello = Paths.get("target", "testData", "source", "hello.sh");
        assertTrue(Files.exists(hello));
    }

    @Test
    @Order(10)
    void createTarContent() throws IOException
    {
        Path sourceDirectory = Paths.get("src", "test", "resources", "testData");
        byte[] archiveContent = Tar.createTarContent(sourceDirectory);
        int len = archiveContent.length;
        assertNotEquals(len, 0);
        assertTrue(len > 100);

        final String pathTar = "target/archive.tar";
        final Path pathTarFile = Paths.get(pathTar);
        Files.deleteIfExists(pathTarFile);
        writeToFile(archiveContent, pathTar);
        assertTrue(Files.isRegularFile(pathTarFile));
    }
// createApkTarContent

    @Test
    @Order(11)
    void createApkTarContent() throws IOException
    {
        Path sourceDirectory = Paths.get("src", "test", "resources", "testData","source");
        Path archive = Paths.get("target", SOURCE_TAR);
        Tar.createApkTarContent(sourceDirectory,archive,"MyApplication","2.0");
        assertTrue(Files.exists(archive.toAbsolutePath()));
    }

    @Test
    @Order(12)
    void createApkTarByteContent() throws IOException
    {
        Path sourceDirectory = Paths.get("src", "test", "resources", "testData","source");
        byte[] archiveContent = Tar.createApkTarContent(sourceDirectory,"MyApplication","2.0");
        assertTrue(archiveContent.length > 0 );

        final String pathTar = "target/archive.tar";
        final Path pathTarFile = Paths.get(pathTar);
        Files.deleteIfExists(pathTarFile);
        writeToFile(archiveContent, pathTar);
        assertTrue(Files.isRegularFile(pathTarFile));
    }

    private void writeToFile(byte[] content, String path) throws IOException
    {
        File file = new File(path);
        try (FileOutputStream fileOutputStream = new FileOutputStream(file))
        {
            fileOutputStream.write(content);
        }
    }
}