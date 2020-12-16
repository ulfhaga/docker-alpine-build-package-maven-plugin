package se.docker.alpine.gateway;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


class ClientTestIT
{
    @DisplayName("Restful tests")
    @Test
    public void test1() throws IOException
    {
        Path sourceDirectory = Paths.get("src","test","resources","testData","source");
        ClientDto clientDto = new ClientDto();
        clientDto.setName("MyGoodPackage");
        clientDto.setSource(sourceDirectory);

        clientDto.setVersion("1.0");
        clientDto.setReleaseNumber(0);
        clientDto.setArch("noarch");
        clientDto.setLicense("LGPL-2.1-or-later");
        clientDto.setUrl("http:\\/\\/www.github.com");
        clientDto.setDescription("Testing");
        clientDto.setTarget(Paths.get("/tmp"));
        clientDto.setPackageFunction("install -Dm755 hello.sh \"$pkgdir\"/usr/bin/hello1.sh");

        Client client = new Client();
        client.send(clientDto);

        client.getKeys(Paths.get("/tmp"));
    }
}