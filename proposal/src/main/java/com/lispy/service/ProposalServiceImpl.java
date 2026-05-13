package com.lispy.service;

import com.lispy.dto.ProposalDetailsDto;
import com.lispy.dto.ProposalDto;
import com.lispy.entity.ProposalEntity;
import com.lispy.message.KafkaEvent;
import com.lispy.repository.ProposalRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Date;

@ApplicationScoped
public class ProposalServiceImpl implements ProposalService{

    @Inject
    ProposalRepository proposalRepository;

    @Inject
    KafkaEvent kafkaEvent;

    @Override
    public ProposalDetailsDto findFullProposal(long id) {
        ProposalEntity proposalEntity = proposalRepository.findById(id);
        return ProposalDetailsDto.builder()
                .proposalId(proposalEntity.getId())
                .proposalValidityDays(proposalEntity.getProposalValidityDays())
                .country(proposalEntity.getCountry())
                .priceTonne(proposalEntity.getPriceTonne())
                .customer(proposalEntity.getCustomer())
                .tonnes(proposalEntity.getTonnes())
                .build();
    }

    @Override
    @Transactional
    public void createNewProposal(ProposalDetailsDto proposalDetailsDto) {
        ProposalDto proposalDto = buildAndSaveNewProposal(proposalDetailsDto);
        kafkaEvent.sendNewKafkaEvent(proposalDto);
    }

    private ProposalDto buildAndSaveNewProposal(ProposalDetailsDto proposalDetailsDto) {
        try {
            ProposalEntity proposalEntity = new ProposalEntity();
            proposalEntity.setCreated(new Date());
            proposalEntity.setProposalValidityDays(proposalDetailsDto.getProposalValidityDays());
            proposalEntity.setCountry(proposalDetailsDto.getCountry());
            proposalEntity.setCustomer(proposalDetailsDto.getCustomer());
            proposalEntity.setPriceTonne(proposalDetailsDto.getPriceTonne());
            proposalEntity.setTonnes(proposalDetailsDto.getTonnes());

            proposalRepository.persist(proposalEntity);

            return ProposalDto.builder()
                    .proposalId(proposalEntity.getId())
                    .priceTonne(proposalEntity.getPriceTonne())
                    .customer(proposalEntity.getCustomer())
                    .build();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
    }

    @Override
    @Transactional
    public void removeProposal(long id) {
        proposalRepository.deleteById(id);
    }
}
