package com.example.ewallet.domains.account.model.Account;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    // 账户余额
    @Column(name = "balance")
    private BigDecimal balance = BigDecimal.ZERO;

    // 冻结的账户金额
//    @Embedded
    // BigDecimal.ZERO
//    @Column(name = "int_val")
    private BigDecimal frozenBalance = BigDecimal.ZERO;

    // 账户本身的状态
    @Enumerated(EnumType.STRING)
    private AccountStatus accountStatus;

    //账户创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date create_time;

    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date update_time;

    public Account(Long userId) {
        this.userId = userId;
        this.accountStatus = AccountStatus.ACTIVE;
    }
}
