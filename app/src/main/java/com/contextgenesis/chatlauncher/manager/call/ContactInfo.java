package com.contextgenesis.chatlauncher.manager.call;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ContactInfo {

    private final long id;
    private final String contactName;
    private final String phoneNumber;

}
