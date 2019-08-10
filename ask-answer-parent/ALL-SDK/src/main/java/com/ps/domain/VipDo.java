package com.ps.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @author：ZLRWJSAN
 * @date 2019/7/18 21:12
 */
@Data
public class VipDo implements Serializable{
    private Integer id;
    private String account;
    private String askNickname;
    private String answerNickname;
    private String password;
    private Integer integral;
    private String inviteLink;
    private Integer inviterId;
}
