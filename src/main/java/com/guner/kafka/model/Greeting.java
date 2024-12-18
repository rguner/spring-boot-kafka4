package com.guner.kafka.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Setter
@Getter
public class Greeting {

    private String msg;
    private String name;
}