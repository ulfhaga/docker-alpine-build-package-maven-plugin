package se.docker.alpine.build.service;

import org.apache.commons.io.FileUtils;
import se.docker.alpine.build.abuild.BuildApkFile;
import se.docker.alpine.build.model.PackageData;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Transactional
@ApplicationScoped
public class PackagesService
{
    private final ConcurrentHashMap<Long, PackageData> packages
            = new ConcurrentHashMap<>();

    private final AtomicLong index = new AtomicLong();

    private final Path tempDir;

    public PackagesService() throws IOException
    {
        tempDir = Files.createTempDirectory("packages");
    }

    public long createPackage() throws IOException
    {
        PackageData packageData = new PackageData();

        long counter = getCounter();
        packages.put(counter, packageData);

        Path folder = Paths.get(tempDir.toAbsolutePath().toString(), String.valueOf(counter));
        FileUtils.deleteDirectory(folder.toFile());
        packageData.setSource(Files.createDirectory(folder));

        return counter;
    }

    public PackageData getPackageById(String id)
    {
        return packages.get(Long.valueOf(id));
    }

    public PackageData getPackageById(Long id)
    {
        return packages.get(id);
    }

    public List<Long> getIds()
    {
        return (new ArrayList<>(packages.keySet()));
    }

    synchronized private long getCounter()
    {
        long counter = index.incrementAndGet();
        while (packages.containsKey(counter))
        {
            counter = index.incrementAndGet();
            if (counter >= Long.MAX_VALUE)
            {
                this.index.set(0);
            }
        }
        return counter;
    }

    public boolean createAlpinePackage(PackageData packageData) throws IOException
    {
        BuildApkFile buildApkFile = new BuildApkFile(BuildApkFile.createProcessBuilder(), packageData);
        buildApkFile.updateApkBuildFile();
        int result = buildApkFile.buildApkPackage();
        return (result == 0);
    }
}
