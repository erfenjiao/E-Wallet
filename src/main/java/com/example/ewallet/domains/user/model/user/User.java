package com.example.ewallet.domains.user.model.user;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Date;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long userId;

    private String username;

    @Embedded
    @Column(name = "encrypted_password")
    private Password password;

    @Embedded
    @Column(name = "email")
    private Email email;

    private String phoneNumber;

    //创建时间
    @Temporal(TemporalType.TIMESTAMP)
    @CreationTimestamp
    private Date create_time;

    //更新时间
    @Temporal(TemporalType.TIMESTAMP)
    @UpdateTimestamp
    private Date update_time;

    private String token;

}
