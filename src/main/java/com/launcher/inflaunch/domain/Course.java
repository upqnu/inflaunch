package com.launcher.inflaunch.domain;
import com.launcher.inflaunch.enum_status.CourseStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
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

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @ToString.Exclude
    private List<Video> videoList = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    @Builder.Default
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    @ToString.Exclude
    @Builder.Default
    private List<Cart> cartList = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    private CourseStatus courseStatus;

    public CourseStatus getCourseStatus() {
        return this.courseStatus;
    }

    public Course(Long id, Type type, String title, String description, int price) {
        this.id = id;
        this.type = type;
        this.title = title;
        this.description = description;
        this.price = price;
    }
}