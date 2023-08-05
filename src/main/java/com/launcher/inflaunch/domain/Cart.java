package com.launcher.inflaunch.domain;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity(name = "cart")
public class Cart{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private User user;

    @ManyToOne
    @ToString.Exclude
    private Course course;

    @OneToMany(mappedBy = "cart",  cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Payments> paymentsList = new ArrayList<>();





}