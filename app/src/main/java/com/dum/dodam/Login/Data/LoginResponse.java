package com.dum.dodam.Login.Data;

import com.dum.dodam.httpConnection.BaseResponse;

import java.io.Serializable;

public class LoginResponse extends BaseResponse implements Serializable {
    public UserJson body;
}
