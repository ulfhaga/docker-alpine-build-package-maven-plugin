package se.docker.alpine.build.abuild;

import org.jboss.logging.Logger;
import se.docker.alpine.build.model.PackageData;

import javax.ws.rs.InternalServerErrorException;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.regex.Pattern;

public class UpdateApkBuildFile
{
    private static final Logger LOG = Logger.getLogger(UpdateApkBuildFile.class);
    public static final String APKBUILD = "APKBUILD";
    public static final Pattern COMPILE_VER = Pattern.compile("(pkgver=.*)");
    public static final Pattern COMPILE_RELEASE_NUMBER = Pattern.compile("(pkgrel=.*)");
    public static final Pattern COMPILE_SOURCE = Pattern.compile("(source=.*)");
    public static final Pattern COMPILE_DESC = Pattern.compile("(pkgdesc=.*)");
    public static final Pattern COMPILE_URL = Pattern.compile("(url=.*)");
    public static final Pattern COMPILE_ARCH = Pattern.compile("(arch=.*)");
    public static final Pattern COMPILE_LICENSE = Pattern.compile("(license=.*)");
    public static final Pattern COMPILE_SUBPACKAGES = Pattern.compile("(subpackages=.*)");
    public static final Pattern COMPILE_BUILD_DIR = Pattern.compile("(builddir=.*)");

    private final PackageData packageData;

    public UpdateApkBuildFile(PackageData packageData)
    {
        this.packageData = packageData;
    }


    public void updateApkBuildFile(Path apkBuildFile, Path updatedApkBuildFile) throws IOException
    {
        String updatedContent;
        if (Files.exists(apkBuildFile))
        {
            String content = new String(Files.readAllBytes(apkBuildFile));
            updatedContent = buildDir(releaseNumber(subpackages(url(license(description(arch(source(version(content)))))))));

            int indexBuild = updatedContent.indexOf("build()" );
            updatedContent = updatedContent.substring(0, indexBuild);

            String packageFunction = packageData.getPackageFunction();

            updatedContent = updatedContent + "build() {\n" +
                    "\t# Replace with proper build command(s)\n" +
                    "\tcd \"${builddir}\";\n" +
                    "\tmkdir -p \"${pkgdir}\";\n" +
                    "}\n" +
                    "check() {\n" +
                    "\t# Replace with proper check command(s)\n" +
                    "\tcd \"${builddir}\";\n" +
                    "}\n" +
                    "package() {\n" +
                    "    # Replace with proper package command(s)\n" +
                    "\tcd \"${builddir}\";\n" +
                    "\tmkdir -p \"${pkgdir}\";\n" +
                    "\n" + packageFunction +
                    "\n" +
                    "}\n";

            writeToFile(updatedContent.getBytes(), updatedApkBuildFile);
        }
        else
        {
            LOG.errorf("File %s is missing", apkBuildFile.toAbsolutePath().toString());
            throw new InternalServerErrorException();
        }
    }

    private String version(String content)
    {
        String replacement = "pkgver=\"" + packageData.getVersion() + "\"";
        return COMPILE_VER.matcher(content).replaceFirst(replacement);
    }

    private String releaseNumber(String content)
    {
        String replacement = "pkgrel=\"" + packageData.getReleaseNumber() + "\"";
        return COMPILE_RELEASE_NUMBER.matcher(content).replaceFirst(replacement);
    }

    private String source(String content)
    {
        String srcName = "\\$pkgname-\\$pkgver.tar";
        String replacement = "source=\"" + srcName + "\"";
        return COMPILE_SOURCE.matcher(content).replaceFirst(replacement);
    }

    private String arch(String content)
    {
        String replacement = "arch=\"" + packageData.getArch() + "\"";
        return COMPILE_ARCH.matcher(content).replaceFirst(replacement);
    }

    private String license(String content)
    {
        String replacement = "license=\"" + packageData.getLicense() + "\"";
        return COMPILE_LICENSE.matcher(content).replaceFirst(replacement);
    }

    private String description(String content)
    {
        String replacement = "pkgdesc=\"" + packageData.getDescription() + "\"";
        return COMPILE_DESC.matcher(content).replaceFirst(replacement);
    }

    private String url(String content)
    {
        String replacement = "url=\"" + packageData.getUrl() + "\"";
        return COMPILE_URL.matcher(content).replaceFirst(replacement);
    }

    private String subpackages(String content)
    {
        String replacement = "subpackages=\"" + "\"";
        return COMPILE_SUBPACKAGES.matcher(content).replaceFirst(replacement);
    }

    private String buildDir(String content)
    {
        String replacement = "builddir=\"" + "\"";
        return COMPILE_BUILD_DIR.matcher(content).replaceFirst(replacement);
    }


    private void writeToFile(byte[] content, Path path) throws IOException
    {
        Files.deleteIfExists(path);
        File file = path.toFile();
        try (FileOutputStream fileOutputStream = new FileOutputStream(file))
        {
            fileOutputStream.write(content);
        }
    }

}
