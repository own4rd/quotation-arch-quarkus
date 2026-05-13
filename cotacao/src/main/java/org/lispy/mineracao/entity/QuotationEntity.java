package org.lispy.mineracao.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name="quotation")
@Data
@NoArgsConstructor
public class QuotationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quotation_seq")
    @SequenceGenerator(name = "quotation_seq", sequenceName = "quotation_seq", allocationSize = 1)
    public Long id;
    private Date date;
    @Column(name = "currency_price")
    private BigDecimal currencyPrice;
    @Column(name = "pct_change")
    private String pctChange;
    private String pair;
}
