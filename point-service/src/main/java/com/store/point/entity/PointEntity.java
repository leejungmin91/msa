package com.store.point.entity;

import com.store.common.event.SignupEvent;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.Lock;

@Getter
@Table(name = "POINT")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
@Entity
public class PointEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "POINT_ID")
    private Long id;
    private Long userId;
    private Long point;

    public PointEntity(Long userId, Long point) {
        this.userId = userId;
        this.point = point;
    }

    public static PointEntity signUp(SignupEvent event, Long eventPoint) {
        return new PointEntity(
                event.id()
                , eventPoint
        );
    }

    public static PointEntity updatePoint(PointEntity currentPoint, Long updatePoint) {
        currentPoint.point += updatePoint;
        return currentPoint;
    }

}
