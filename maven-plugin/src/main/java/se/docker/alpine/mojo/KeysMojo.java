package se.docker.alpine.mojo;

import org.apache.maven.plugin.logging.Log;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import se.docker.alpine.gateway.Client;

import java.io.IOException;
import java.nio.file.Paths;

@Mojo(name = "keys")
public class KeysMojo implements org.apache.maven.plugin.Mojo
{
    private Log log;

    @Parameter(property = "keysTarget", required = false)
    private String keysTarget;

    @Override
    public void execute()
    {
        Client client = new Client();
        try
        {
            if ( keysTarget != null && !keysTarget.isEmpty())
            {
                client.getKeys(Paths.get(keysTarget));
            }
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