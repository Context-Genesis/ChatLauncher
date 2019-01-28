package com.contextgenesis.chatlauncher.models.attachments;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class Attachment {

    private final int key;
    private final String command;
    private final String nickName;

}
