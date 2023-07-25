package com.br.equaly.messenger.model.entity;

import com.br.equaly.messenger.model.enums.EmailTemplate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmailToken {

    private String id;

    private EmailTemplate templateType;

    private String timestamp;
}
