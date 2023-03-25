package ru.practicum.shareit.item.model;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@Entity
@Table(name = "items")
@NoArgsConstructor
@Builder
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, length = 100)
    private String name;
    @Column(nullable = false, length = 300)
    private String description;
    @Column(name = "is_available", nullable = false)
    private Boolean available;
    @Column(name = "owner_id")
    private Long owner;
    @Column(name = "request_id")
    private Long itemRequest;
}