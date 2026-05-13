package com.lispy.controller;

import com.lispy.dto.ProposalDetailsDto;
import com.lispy.service.ProposalService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Path("/api/proposal")
public class ProposalController {

    @Inject
    ProposalService proposalService;

    private final Logger LOG = LoggerFactory.getLogger(ProposalController.class);

    @GET
    @Path("/{id}")
    public ProposalDetailsDto findDetailsProposal(@PathParam("id") long id) {
        return proposalService.findFullProposal(id);
    }

    @POST
    public Response createProposal(ProposalDetailsDto proposalDetailsDto) {
        LOG.info("Receiving purchase order");

        try {
            proposalService.createNewProposal(proposalDetailsDto);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }

    @DELETE
    @Path("/{id}")
    public Response removeProposal(@PathParam("id") long id) {
        try {
            proposalService.removeProposal(id);
            return Response.noContent().build();
        } catch (Exception e) {
            return Response.serverError().build();
        }
    }
}
