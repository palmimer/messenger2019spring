package com.progmatic.messenger2019spring.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Varga János
 */
public class MessageDto {

    @NotNull
    @NotBlank
    @Size(min = 10, max = 100)
    private String text;

    public MessageDto(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    
}
