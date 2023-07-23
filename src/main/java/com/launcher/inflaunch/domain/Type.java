package com.launcher.inflaunch.domain;
import jakarta.persistence.*;
import lombok.*;


    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    @ToString(callSuper = true)
    @EqualsAndHashCode(callSuper = true)
    @Entity(name = "type")
    public class Type extends BaseEntity{
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(nullable = false)
        private String category;
        @Column(unique = true, nullable = false)
        private String type;

        @Column(nullable = false)
        private int sequence;

}
