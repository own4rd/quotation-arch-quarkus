package org.lispy.mineracao.scheduler;

import io.quarkus.scheduler.Scheduled;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.lispy.mineracao.message.KafkaEvents;
import org.lispy.mineracao.service.QuotationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class QuotationScheduler {
    private final Logger LOG = LoggerFactory.getLogger(KafkaEvents.class);

    @Inject
    QuotationService quotationService;

    @Scheduled(every = "35s", identity = "task-job")
    void schedule() {
        LOG.info("Executing Scheduler");
        quotationService.getCurrencyPrice();
    }
}
