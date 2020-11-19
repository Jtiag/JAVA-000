package com.jasper.jdbc.mvp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author jasper
 */
@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class Student {
    private int id;
    private String name;
}
