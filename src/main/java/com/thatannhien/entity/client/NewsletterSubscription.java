package com.thatannhien.entity.client;

import com.thatannhien.entity.BaseEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
@Entity
@Table(name = "newsletter_subscription", uniqueConstraints = @UniqueConstraint(name = "uc_newsletter_subscription_email", columnNames = {"email"}))
public class NewsletterSubscription extends BaseEntity {
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "status", nullable = false, columnDefinition = "TINYINT")
    private Integer status; // 1: Active, 0: Unsubscribed
}

