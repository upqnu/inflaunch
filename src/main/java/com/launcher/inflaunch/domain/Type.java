package com.launcher.inflaunch.domain;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(callSuper = true)
@Entity(name = "type")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true,nullable = false)
    private String type;
    @ManyToOne
    @ToString.Exclude
    private Category category;

    @Column(nullable = false)
    private int sequence;

    @OneToMany(mappedBy = "type",  cascade = CascadeType.ALL)
    @ToString.Exclude
    @Builder.Default
    private List<Course> courseList = new ArrayList<>();

    public void addCourse(Course... course){Collections.addAll(courseList, course);}

}