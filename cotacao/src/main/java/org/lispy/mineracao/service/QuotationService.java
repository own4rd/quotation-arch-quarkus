package org.lispy.mineracao.service;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.lispy.mineracao.client.CurrencyPriceClient;
import org.lispy.mineracao.dto.CurrencyPriceDto;
import org.lispy.mineracao.dto.QuotationDto;
import org.lispy.mineracao.entity.QuotationEntity;
import org.lispy.mineracao.message.KafkaEvents;
import org.lispy.mineracao.repository.QuotationRepository;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
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

    public void getCurrencyPrice() {

    }

    private boolean updateCurrencyInfoPrice(CurrencyPriceDto currencyPriceInfo) {

        BigDecimal currentPrice = new BigDecimal(currencyPriceInfo.getUsdbrl().getBid());
        boolean updatePrice = false;

        List<QuotationEntity> quotationEntityList = quotationRepository.findAll().list();
        if(quotationEntityList.isEmpty()) {
            saveQuotation(currencyPriceInfo);
            updatePrice = true;
        } else {
            QuotationEntity lastDollarPrice = quotationEntityList.get(quotationEntityList.size() - 1);
            if(currentPrice.floatValue() > lastDollarPrice.getCurrencyPrice().floatValue()) {
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
