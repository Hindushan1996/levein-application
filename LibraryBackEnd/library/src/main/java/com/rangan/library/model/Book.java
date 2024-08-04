package com.rangan.library.model;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigInteger;

@Data
@Entity
@Table(name = "book")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "book_name")
    private String bookName;

    @Column(name = "isbn")
    private BigInteger isbn;

    @ManyToOne
    @JoinColumn(name = "auth_id", referencedColumnName = "id")
    private Author author;
}
