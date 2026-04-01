package com.github.kuminkyu9.limiteddropserver.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "raffle_products")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class RaffleProduct {

    @Id
    @Column(name = "product_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "start_at", nullable = false)
    private LocalDateTime startAt;

    @Column(name = "end_at", nullable = false)
    private LocalDateTime endAt;

    @Column(name = "winner_count", nullable = false)
    private Integer winnerCount;

    @Column(name = "draw_at", nullable = false)
    private LocalDateTime drawAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "raffle_status", nullable = false)
    private RaffleStatus raffleStatus;

    @Column(name = "draw_completed", nullable = false)
    private boolean drawCompleted;

    @CreatedDate
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    public void updateRaffleStatus(RaffleStatus raffleStatus) {
        this.raffleStatus = raffleStatus;
    }

    public void markDrawCompleted() {
        this.drawCompleted = true;
    }
}