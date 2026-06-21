package com.basisttha.Kreta.Model;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsageRecords {//this table is for couting usage of APIs
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID recordId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId_usageRecord")
    private User user;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
    private Long callCount;//api call count
    private Boolean reportedToStripe;//we report usage to stripe, which auto-calculates usage based bill
    private LocalDateTime lastFlushedAt;//redis handles the api rate limit enforcement, and periodically flushes data to db
}
