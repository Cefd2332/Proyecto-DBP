package org.e2e.e2e.Email;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class EmailEvent {
    private String to;
    private String subject;
    private String text;
}
