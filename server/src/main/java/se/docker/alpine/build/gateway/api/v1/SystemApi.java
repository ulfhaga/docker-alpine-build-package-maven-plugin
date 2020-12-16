package se.docker.alpine.build.gateway.api.v1;

import org.jboss.logging.Logger;
import se.docker.alpine.api.v1.RestfulSystemApi;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@Path("/v1/system")
public class SystemApi implements RestfulSystemApi
{
    private static final Logger LOG = Logger.getLogger(RestfulSystemApi.class);

    @Override
    @GET
    @Path("keys")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadKeys()
    {
        Response.ResponseBuilder response;
        File fileDownload = new File("/home/dev/keys.tar");
        byte[] sourceTar = new byte[0];
        try
        {
            sourceTar = Files.readAllBytes(fileDownload.toPath());
            response = Response.ok().entity(sourceTar);
            response.header("Content-Disposition", "attachment;filename=" + "key.tar");
        }
        catch (IOException e)
        {
            LOG.errorf("Restful fileDownload {0} ", e.getMessage());
            response = Response.serverError();
        }
        return response.build();
    }
}
