package com.lispy.service;

import com.lispy.dto.ProposalDetailsDto;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public interface ProposalService {

    ProposalDetailsDto findFullProposal(long id);

    void createNewProposal(ProposalDetailsDto proposalDetailsDto);

    void removeProposal(long id);
}
