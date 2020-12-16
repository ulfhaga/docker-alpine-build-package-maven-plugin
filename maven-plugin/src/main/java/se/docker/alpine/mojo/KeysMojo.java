package se.docker.alpine.mojo;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import se.docker.alpine.gateway.Client;

import java.io.IOException;
import java.nio.file.Paths;

@Mojo(name = "keys")
public class KeysMojo implements org.apache.maven.plugin.Mojo
{
    private Log log;

    @Override
    public void execute()
    {
        Client client = new Client();
        try
        {
            client.getKeys(Paths.get("/tmp"));
        }
        catch (IOException e)
        {
            log.error(e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void setLog(Log log)
    {
        this.log = log;
    }

    @Override
    public Log getLog()
    {
        return log;
    }
}