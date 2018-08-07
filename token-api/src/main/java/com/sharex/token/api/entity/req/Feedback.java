package com.sharex.token.api.entity.req;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.NotBlank;

public class Feedback {

    @NotBlank(message = "内容不能为空")
    @Length(max = 200, message = "内容长度不超过200个字符")
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
