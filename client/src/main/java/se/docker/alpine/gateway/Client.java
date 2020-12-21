package se.docker.alpine.gateway;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.ResteasyWebTarget;
import se.docker.alpine.api.v1.RestfulPackageApi;
import se.docker.alpine.api.v1.RestfulPackagesApi;
import se.docker.alpine.api.v1.RestfulSystemApi;
import se.docker.alpine.compress.Tar;

import javax.ws.rs.InternalServerErrorException;
import javax.ws.rs.core.Response;
import java.io.*;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.atomic.AtomicInteger;

import org.jboss.logging.Logger;

public class Client
{
    private static final Logger LOG = Logger.getLogger(Client.class);
    final static String BASE_URI = "http://127.0.0.1:64014";
    final ResteasyClient client;
    final AtomicInteger internalReleaseNumber = new AtomicInteger(-1);

    public Client()
    {
        ResteasyClientBuilder resteasyClientBuilder = new ResteasyClientBuilder();
        client = resteasyClientBuilder.build();
    }

    public void send(ClientDto clientDto) throws IOException
    {
        String uri;
        LOG.debugf("Client DTO %s", clientDto.toString());
        uri = createCollection();


        if (uri.startsWith(RestfulPackageApi.V_1_PACKAGES))
        {
            String id = uri.substring(RestfulPackageApi.V_1_PACKAGES.length());
            putName(id,clientDto.getName());
            putVersion(id,clientDto.getVersion());

            putReleaseNumber(clientDto, id);
            putArch(id,clientDto.getArch());
            putLicence(id,clientDto.getLicense());
            putDescription(id,clientDto.getDescription());
            putUrl(id,clientDto.getUrl());
            putSource(id,clientDto.getSource(), clientDto.getName(), clientDto.getVersion());
            putPackageFunction(id,clientDto.getPackageFunction());
            byte[] packageAkp = getPackage(id);

            if ( Files.notExists(clientDto.getTarget()))
            {
                Files.createDirectory(clientDto.getTarget());
            }
            final String fileApk = clientDto.getName() + "-" + clientDto.getVersion() + "-r" + internalReleaseNumber.get() + ".apk";
            Path apkFile = Paths.get(clientDto.getTarget().toAbsolutePath().toString(), fileApk);
         //   Path apkFile = Paths.get(clientDto.getTarget().toAbsolutePath().toString(), clientDto.getName() + "-" + clientDto.getVersion() + ".apk");
            try (FileOutputStream stream = new FileOutputStream(apkFile.toAbsolutePath().toString())) {
                stream.write(packageAkp);
            }

        }
        else
        {
            throw new InternalServerErrorException("Wrong path uri:" + uri);
        }
    }

    public void getKeys(Path folder) throws IOException
    {
        byte[] packageAkp  =  getTarKeys();
        if ( Files.notExists(folder))
        {
            Files.createDirectories(folder);
        }

        Path apkFile = Paths.get(folder.toAbsolutePath().toString(), "key.rsa.pub");
        Files.deleteIfExists(apkFile);
        try (FileOutputStream stream = new FileOutputStream(apkFile.toString())) {
            stream.write(packageAkp);
        }
    }

    private void putReleaseNumber(ClientDto clientDto, String id)
    {
        Integer releaseNumber = clientDto.getReleaseNumber() ;
        if (releaseNumber == null)
        {
            putReleaseNumber(id, internalReleaseNumber.incrementAndGet());
        }
        else
        {
            putReleaseNumber(id, releaseNumber);
            internalReleaseNumber.set(releaseNumber);
        }
    }

    private void putUrl(String path, String description)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setUrl(Long.valueOf(path),description);
        LOG.debugf("HTTP code: {0}", response.getStatus());
        response.close();
    }

    private void putDescription(String path, String description)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setDescription(Long.valueOf(path),description);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private void putLicence(String path, String license)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setLicense(Long.valueOf(path),license);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private void putArch(String path, String arch)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setArch(Long.valueOf(path),arch);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private String getArch(String path)
    {
        String arch;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.getArch(Long.valueOf(path));
        arch = response.readEntity(String.class);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
        return arch;
    }

    private void putVersion(String path, String version)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setVersion(Long.valueOf(path),version);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private String getVersion(String path)
    {
        String version;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.getVersion(Long.valueOf(path));
        version = response.readEntity(String.class);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
        return version;
    }

    private void putReleaseNumber(String path, Integer version)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setReleaseNumber(Long.valueOf(path),version);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private Integer getReleaseNumber(String path)
    {
        Integer releaseNumber;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.getReleaseNumber(Long.valueOf(path));
        releaseNumber = response.readEntity(Integer.class);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
        return releaseNumber;
    }

    private void putPackageFunction(String path, String function)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setPackageFunction(Long.valueOf(path),function);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private String getPackageFunction(String path)
    {
        String name;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.getPackageFunction(Long.valueOf(path));
        name = response.readEntity(String.class);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
        return name;
    }

    private String createCollection() throws IOException
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackagesApi proxy = target.proxy(RestfulPackagesApi.class);
        Response response = proxy.packageId();
        System.out.println("HTTP code: " + response.getStatus());
        URI location = response.getLocation();
        String path = location.getPath();

        response.close();
        return path;
    }
    
    private void putName(String path, String name)
    {
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.setName(Long.valueOf(path),name);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private String getName(String path)
    {
        String name;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.getName(Long.valueOf(path));
        name = response.readEntity(String.class);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
        return name;
    }

    private void putSource(String id, Path sourceFolder,String packName, String version) throws IOException
    {
        byte[] tarFileContent = Tar.createApkTarContent(sourceFolder,packName, version);
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.putSource(Long.valueOf(id),tarFileContent);
        System.out.println("HTTP code: " + response.getStatus());
        response.close();
    }

    private byte[] getPackage(String path) throws IOException
    {
        byte[] tarContent = null;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulPackageApi proxy = target.proxy(RestfulPackageApi.class);
        Response response = proxy.getPackage(Long.valueOf(path));
   //     if ( response.getStatus() == Response.Status.OK.getStatusCode())
        {
            tarContent = response.readEntity(byte[].class);
            System.out.println("HTTP code: " + response.getStatus());
            response.close();
        }
        return tarContent;
    }

    private byte[] getTarKeys() throws IOException
    {
        byte[] keysTar = null;
        ResteasyWebTarget target = client.target(BASE_URI);
        RestfulSystemApi proxy = target.proxy(RestfulSystemApi.class);
        Response response = proxy.downloadKeys();
        if ( response.getStatus() == Response.Status.OK.getStatusCode())
        {
            keysTar = response.readEntity(byte[].class);
            System.out.println("HTTP code: " + response.getStatus());
            response.close();
        }
        return keysTar;
    }
    
}
