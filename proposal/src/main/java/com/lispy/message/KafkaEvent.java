package com.lispy.message;

import com.lispy.dto.ProposalDto;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.eclipse.microprofile.reactive.messaging.Emitter;

@ApplicationScoped
public class KafkaEvent {

    private final Logger LOG = LoggerFactory.getLogger(KafkaEvent.class);

    @Channel("proposal")
    Emitter<ProposalDto> proposalDtoEmitter;

    public void sendNewKafkaEvent(ProposalDto proposalDto) {
        LOG.info("Sending new Proposal to topic!");
        proposalDtoEmitter.send(proposalDto).toCompletableFuture().join();
    }
}
