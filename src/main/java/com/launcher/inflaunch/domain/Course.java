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
@EqualsAndHashCode(callSuper = true)
@Entity(name = "course")
public class Course extends BaseEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @ToString.Exclude
    private User user;

    @ManyToOne
    @ToString.Exclude
    private Type type;

    @Column(unique = true, nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int price;

    @OneToMany(mappedBy = "course" ,  cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Video> videoList = new ArrayList<>();

    @OneToMany(mappedBy = "course",  cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "course",  cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Cart> cartList = new ArrayList<>();
}
