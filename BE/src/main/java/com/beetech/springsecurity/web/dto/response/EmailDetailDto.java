package com.beetech.springsecurity.web.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

@Getter
@Setter
public class EmailDetailDto {
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
    private List<String> recipients;
}
