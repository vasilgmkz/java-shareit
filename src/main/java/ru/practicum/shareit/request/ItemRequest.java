package ru.practicum.shareit.request;

import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import ru.practicum.shareit.user.model.User;

import java.time.Instant;

/**
 * TODO Sprint add-item-requests.
 */
@Entity
@Table(name = "ITEMREQUEST")
@Data
@RequiredArgsConstructor
public class ItemRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "request_id")
    private Long id;
    @Column(name = "request_description")
    private String description;
    @OneToOne
    @JoinColumn(name = "user_id")
    private User requestor;
    @Column(name = "request_created")
    private Instant created = Instant.now();
}
