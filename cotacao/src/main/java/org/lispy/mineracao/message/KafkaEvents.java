package org.lispy.mineracao.message;

import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.lispy.mineracao.dto.QuotationDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class KafkaEvents {

    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);

    @Channel("quotation-channel")
    Emitter<QuotationDto> quotationRequestEmitter;

    public void sendNewKafkaEvent(QuotationDto quotationDto) {

        LOG.info("Sending to Kafka TOPIC");
        quotationRequestEmitter.send(quotationDto).toCompletableFuture().join();
    }
}
