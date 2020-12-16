package se.docker.alpine.api.v1;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;


@Path("/v1/system")
public interface RestfulSystemApi
{
    @GET
    @Path("keys")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    Response downloadKeys() throws IOException;
}
