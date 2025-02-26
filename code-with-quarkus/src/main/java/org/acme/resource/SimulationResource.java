package org.acme.resource;

import org.acme.model.CorpsCeleste;
import org.acme.service.SimulationService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Path("/simulate")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class SimulationResource {

    @Inject
    SimulationService simulationService;

    private final List<CorpsCeleste> corps = new ArrayList<>();

    @POST
    @Path("/add")
    public Response addCorps(CorpsCeleste c) {
        if (c == null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Le corps céleste ne peut pas être null")
                    .build();
        }
        corps.add(c);
        return Response.ok("Corps céleste ajouté").build();
    }

    @POST
    @Path("/start")
    public Response startSimulation() {
        if (corps.isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("Ajoutez d'abord des corps célestes")
                    .build();
        }
        simulationService.startSimulation(corps, 0.05);
        return Response.ok("Simulation démarrée en continu").build();
    }

    @POST
    @Path("/stop")
    public Response stopSimulation() {
        simulationService.stopSimulation(corps);
        return Response.ok("Simulation arrêtée").build();
    }

    @GET
    @Path("/state")
    public List<CorpsCeleste> getState() {
        return corps;
    }
}
