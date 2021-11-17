package se.docker.alpine.api.v1;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.IOException;
import org.jboss.resteasy.annotations.GZIP;

@Path(RestfulPackageApi.V_1_PACKAGES + "{id}")
public interface RestfulPackageApi
{
    String V_1_PACKAGES = "/v1/packages/";

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    Response putMember(@PathParam("id") Long id) throws IOException;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    Response getMember(@PathParam("id") Long id);

    @Path("source")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.APPLICATION_OCTET_STREAM)
    Response putSource(@PathParam("id") Long id, @GZIP byte[] tarStream) throws IOException;

    @Path("source")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.TEXT_PLAIN)
    @GZIP
    Response getSource(@PathParam("id") Long id) throws IOException;

    @Path("name")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setName(@PathParam("id") Long id, String body);

    @Path("name")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getName(@PathParam("id") Long id);

    @Path("version")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setVersion(@PathParam("id") Long id, String body);

    @Path("version")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getVersion(@PathParam("id") Long id);

    @Path("releaseNumber")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setReleaseNumber(@PathParam("id") Long id, Integer body);

    @Path("releaseNumber")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getReleaseNumber(@PathParam("id") Long id);


    @Path("arch")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setArch(@PathParam("id") Long id, String body);

    @Path("arch")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getArch(@PathParam("id") Long id);

    @Path("license")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setLicense(@PathParam("id") Long id, String body);

    @Path("license")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getLicense(@PathParam("id") Long id);

    @Path("description")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setDescription(@PathParam("id") Long id, String body);

    @Path("description")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getDescription(@PathParam("id") Long id);

    @Path("url")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setUrl(@PathParam("id") Long id, String body);

    @Path("url")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getUrl(@PathParam("id") Long id);

    @Path("packageFunction")
    @PUT
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response setPackageFunction(@PathParam("id") Long id, String body);

    @Path("packageFunction")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Consumes(MediaType.TEXT_PLAIN)
    Response getPackageFunction(@PathParam("id") Long id);

    @Path("package")
    @GET
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    @Consumes(MediaType.TEXT_PLAIN)
    @GZIP
    Response getPackage(@PathParam("id") Long id) throws IOException;

}
