package se.docker.alpine.compress;

import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class Tar
{
    public static byte[] createTarContent(Path source) throws IOException
    {
        byte[] compressedData;
        if (!Files.isDirectory(source))
        {
            throw new IOException("Please provide a directory.");
        }
        try (
                ByteArrayOutputStream archive = new ByteArrayOutputStream();
                BufferedOutputStream buffOut = new BufferedOutputStream(archive);
                TarArchiveOutputStream tOut = new TarArchiveOutputStream(buffOut))
        {
            Files.walkFileTree(source, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attributes)
                {
                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink())
                    {
                        return FileVisitResult.CONTINUE;
                    }
                    // get filename
                    Path targetFile = source.relativize(file);
                    try
                    {
                        TarArchiveEntry tarEntry = new TarArchiveEntry(
                                file.toFile(), targetFile.toString());
                        tOut.putArchiveEntry(tarEntry);
                        Files.copy(file, tOut);
                        tOut.closeArchiveEntry();
                    }
                    catch (IOException e)
                    {
                        System.err.printf("Unable to tar.gz : %s%n%s%n", file, e);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    System.err.printf("Unable to tar.gz : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });

            tOut.flush();
            buffOut.flush();
            archive.flush();
            archive.close();
            compressedData = archive.toByteArray();
            tOut.finish();
        }  // try
        return compressedData;
    }

    public static byte[] createApkTarContent(Path source, String packageName, String version) throws IOException
    {
        final String prefix = packageName + "-" + version;
        Path tmpDir = Files.createTempDirectory(prefix);
        Path newSource = Paths.get(tmpDir.toAbsolutePath().toString(), prefix);
        Files.createDirectory(newSource.toAbsolutePath());
        FileUtils.copyDirectory(source.toAbsolutePath().toFile(), newSource.toAbsolutePath().toFile());
        return createTarContent(tmpDir);
    }

    public static void createApkTarContent(Path source, Path archive, String packageName, String version) throws IOException
    {
        Path tmpDir = Files.createTempDirectory(packageName + "-" + version);
        Path newSource = Paths.get(tmpDir.toAbsolutePath().toString(), packageName + "-" + version);
        Files.createDirectory(newSource.toAbsolutePath());
        FileUtils.copyDirectory(source.toAbsolutePath().toFile(), newSource.toAbsolutePath().toFile());
        createTarContent(tmpDir, archive);
    }

    public static void createTarContent(Path source, Path archive) throws IOException
    {
        if (!Files.isDirectory(source))
        {
            throw new IOException("Please provide a directory.");
        }

        String tarFileName = archive.toFile().getAbsolutePath();

        try (OutputStream fOut = Files.newOutputStream(Paths.get(tarFileName));
             BufferedOutputStream buffOut = new BufferedOutputStream(fOut);
             TarArchiveOutputStream tOut = new TarArchiveOutputStream(buffOut))
        {
            Files.walkFileTree(source, new SimpleFileVisitor<Path>()
            {
                @Override
                public FileVisitResult visitFile(Path file,
                                                 BasicFileAttributes attributes)
                {
                    // only copy files, no symbolic links
                    if (attributes.isSymbolicLink())
                    {
                        return FileVisitResult.CONTINUE;
                    }
                    // get filename
                    Path targetFile = source.relativize(file);
                    try
                    {
                        TarArchiveEntry tarEntry = new TarArchiveEntry(
                                file.toFile(), targetFile.toString());
                        tOut.putArchiveEntry(tarEntry);
                        Files.copy(file, tOut);
                        tOut.closeArchiveEntry();
                        System.out.printf("file : %s%n", file);
                    }
                    catch (IOException e)
                    {
                        System.err.printf("Unable to tar.gz : %s%n%s%n", file, e);
                    }
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult visitFileFailed(Path file, IOException exc)
                {
                    System.err.printf("Unable to tar.gz : %s%n%s%n", file, exc);
                    return FileVisitResult.CONTINUE;
                }
            });
            tOut.finish();
        }
    }

    public static void extractTarFile(Path source, Path target)
            throws IOException
    {
        if (Files.notExists(source))
        {
            throw new IOException("File doesn't exists!");
        }
        try (InputStream fi = Files.newInputStream(source);
             BufferedInputStream bi = new BufferedInputStream(fi);
             TarArchiveInputStream ti = new TarArchiveInputStream(bi))
        {
            ArchiveEntry entry;
            while ((entry = ti.getNextEntry()) != null)
            {
                Path newPath = zipSlipProtect(entry, target);
                if (entry.isDirectory())
                {
                    Files.createDirectories(newPath);
                }
                else
                {
                    // check parent folder again
                    Path parent = newPath.getParent();
                    if (parent != null)
                    {
                        if (Files.notExists(parent))
                        {
                            Files.createDirectories(parent);
                        }
                    }
                    // copy TarArchiveInputStream to Path newPath
                    Files.copy(ti, newPath, StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }

    private static Path zipSlipProtect(ArchiveEntry entry, Path targetDir)
            throws IOException
    {
        Path targetDirResolved = targetDir.resolve(entry.getName());
        Path normalizePath = targetDirResolved.normalize();

        if (!normalizePath.startsWith(targetDir))
        {
            throw new IOException("Bad entry: " + entry.getName());
        }
        return normalizePath;
    }

}
