package com.store.point.entity;

import com.store.common.event.SignupEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Table(name = "POINT_HISTORY")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Entity
public class PointHistoryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_HISTORY_ID")
    private Long id;
    private Long userId;
    private Long point;
    private String description;
    private String type;

    public PointHistoryEntity(Long userId, Long point, String description, String type) {
        this.userId = userId;
        this.point = point;
        this.description = description;
        this.type = type;
    }
}
