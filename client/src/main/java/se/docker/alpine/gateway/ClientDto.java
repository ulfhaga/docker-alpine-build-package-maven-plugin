package se.docker.alpine.gateway;

import java.nio.file.Path;

public class ClientDto
{
    private Path source;
    private String name;
    private String version;
    private Integer releaseNumber;
    private String arch;
    private String license;
    private String description;
    private String url;
    private Path target;
    private String packageFunction;

    public String getVersion()
    {
        return version;
    }

    public void setArch(String arch)
    {
        this.arch = arch;
    }

    public String getArch()
    {
        return arch;
    }

    public String getLicense()
    {
        return license;
    }

    public void setLicense(String license)
    {
        this.license = license;
    }

    public String getUrl()
    {
        return url;
    }

    public void setUrl(String url)
    {
        this.url = url;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public Path getSource()
    {
        return source;
    }

    public void setSource(Path source)
    {
        this.source = source;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    public void setReleaseNumber(Integer releaseNumber)
    {
        this.releaseNumber = releaseNumber;
    }

    public Integer getReleaseNumber()
    {
        return releaseNumber;
    }

    public Path getTarget()
    {
        return target;
    }

    public void setTarget(Path source)
    {
        this.target = source;
    }

    public void setPackageFunction(String packageFunction)
    {
        this.packageFunction = packageFunction;
    }

    public String getPackageFunction()
    {
        return packageFunction;
    }

    public String toString()
    {
        return (String.join(";", "name:", getName(), "version:", getVersion(), "releaseNumber:", String.valueOf(getReleaseNumber()), "source:", getSource().toString(),
                "arch:", getArch(), "license:", getLicense(), "description", getDescription(), "url:", getUrl(), "target", getTarget().toString(), "packageFunction:", getPackageFunction()));
    }

}
