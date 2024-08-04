package com.rangan.library.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor  // Added default constructor
public class BookDTO {
    private Long id;
    private String bookName;
    private BigInteger isbn;
    private Long authorId;
}
