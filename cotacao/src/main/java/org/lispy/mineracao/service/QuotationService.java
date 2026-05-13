package org.lispy.mineracao.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.lispy.mineracao.client.CurrencyPriceClient;
import org.lispy.mineracao.dto.CurrencyPriceDto;
import org.lispy.mineracao.dto.QuotationDto;
import org.lispy.mineracao.entity.QuotationEntity;
import org.lispy.mineracao.message.KafkaEvents;
import org.lispy.mineracao.repository.QuotationRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class QuotationService {

    @Inject
    @RestClient
    CurrencyPriceClient currencyPriceClient;

    @Inject
    QuotationRepository quotationRepository;

    @Inject
    KafkaEvents kafkaEvents;

    @Transactional
    public void getCurrencyPrice() {
        CurrencyPriceDto currencyPriceDto = currencyPriceClient.getPrice("USD-BRL");

        if(updateCurrencyInfoPrice(currencyPriceDto)) {
            kafkaEvents.sendNewKafkaEvent(QuotationDto
                    .builder()
                    .currencyPrice(new BigDecimal(currencyPriceDto.getUsdbrl().getBid()))
                    .date(new Date())
                    .build());
        }
    }

    private boolean updateCurrencyInfoPrice(CurrencyPriceDto currencyPriceInfo) {

        BigDecimal currentPrice = new BigDecimal(currencyPriceInfo.getUsdbrl().getBid());
        boolean updatePrice = false;

        QuotationEntity lastDollarPrice = quotationRepository.find("ORDER BY id DESC").firstResult();
        if(lastDollarPrice == null) {
            saveQuotation(currencyPriceInfo);
            updatePrice = true;
        } else {
            if(currentPrice.compareTo(lastDollarPrice.getCurrencyPrice()) > 0) {
                updatePrice = true;
                saveQuotation(currencyPriceInfo);
            }
        }
        return updatePrice;
    }

    private void saveQuotation(CurrencyPriceDto currencyPriceDto) {
        QuotationEntity quotation = new QuotationEntity();

        quotation.setDate(new Date());
        quotation.setCurrencyPrice(new BigDecimal(currencyPriceDto.getUsdbrl().getBid()));
        quotation.setPctChange(currencyPriceDto.getUsdbrl().getPctChange());
        quotation.setPair("USD-BRL");

        quotationRepository.persist(quotation);
    }
}
