package se.docker.alpine.build.abuild;

import org.jboss.logging.Logger;
import se.docker.alpine.build.model.PackageData;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Supplier;

/**
 * Build an APK
 */
public class BuildApkFile
{
    private static final Logger LOG = Logger.getLogger(BuildApkFile.class);
    private final ProcessBuilder processBuilder;
    private final PackageData packageData;
    private final Path aPortsFolder;
    private final Path packageFolder;


    public BuildApkFile(ProcessBuilder processBuilder, PackageData packageData) throws IOException
    {
        this.processBuilder = processBuilder;
        this.packageData = packageData;
        Path tempDir = Files.createTempDirectory("aports");
        aPortsFolder = Paths.get(tempDir.toAbsolutePath().toString(), "aports");
        Files.createDirectory(aPortsFolder);

        // Create package folder
        packageFolder = Paths.get(aPortsFolder.toAbsolutePath().toString(), packageData.getName());
    }


    public void updateApkBuildFile() throws IOException
    {
        if (buildApkFile() == 0)
        {
            UpdateApkBuildFile updateApkBuildFile = new UpdateApkBuildFile(packageData);
            Path apkBuildFile = Paths.get(aPortsFolder.toAbsolutePath().toString(), packageData.getName(), "APKBUILD");
            updateApkBuildFile.updateApkBuildFile(apkBuildFile, apkBuildFile);
            copyTarFileToArkBuild(packageData);
        }
    }
    
    public int buildApkPackage()
    {
        String[] commandNewApkBuild = {"abuild", "checksum"};
        Supplier<String[]> command = () -> commandNewApkBuild;
        int exitVal = command(packageFolder, command);

        if (exitVal == 0)
        {
            String[] commandABuild = {"abuild", "-r"};
            command = () -> commandABuild;
            exitVal = command(packageFolder, command);
            if (exitVal != 0)
            {
                LOG.errorf("Command %s %s", commandABuild[0], commandABuild[1]);
            }
        }
        else
        {
            LOG.errorf("Command %s %s", commandNewApkBuild[0], commandNewApkBuild[1]);
        }
        return exitVal;
    }

    static public ProcessBuilder createProcessBuilder()
    {
        return new ProcessBuilder();
    }

    private void copyTarFileToArkBuild(PackageData packageData) throws IOException
    {
        String tarFile = packageData.getName() + "-" + packageData.getVersion() + ".tar";
        Path targetFile = Paths.get(aPortsFolder.toAbsolutePath().toString(), packageData.getName(), tarFile);
        Path sourceTarFile = Paths.get(packageData.getSource().toAbsolutePath().toString(), "source.tar");
        Files.copy(sourceTarFile, targetFile);
    }

    private int buildApkFile()
    {
        String[] commandNewApkBuild = {"newapkbuild", this.packageData.getName()};
        Supplier<String[]> command = () -> commandNewApkBuild;
        return command(aPortsFolder, command);
    }

    private int command(Path workFolder, Supplier<String[]> supplier)
    {
        int exitVal;
        processBuilder.command(supplier.get());
        try
        {
            processBuilder.directory(workFolder.toFile());
            Process process = processBuilder.start();
            StringBuilder output = new StringBuilder();
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null)
            {
                output.append(line).append("\n");
            }
            exitVal = process.waitFor();
            if (exitVal == 0)
            {
                LOG.debugv("Success: %s ", output);
            }
            else
            {
                LOG.errorf("ERROR: %s ", output);
            }
        }
        catch (IOException | InterruptedException e)
        {
            LOG.errorf("ERROR: %s ", e.getMessage());
            exitVal = 2;
        }
        return exitVal;
    }


}
