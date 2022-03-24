package com.temple.manage.controller;

import com.temple.manage.util.AbstractRestControllerTest;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;

public class SxUserTempControllerTest extends AbstractRestControllerTest {
    @Autowired
    private WxCpService wxCpService;

    @Test
    public void getUser() throws Exception {
        System.out.println(wxCpService.getUserService().getById("2000"));
    }

    @Test
    public void getToken() throws WxErrorException {
        //List<WxCpDepart> list = wxCpService.getDepartmentService().list(null);
        //System.out.println(list);
        //List<WxCpUser> wxCpUsers = wxCpService.getUserService().listSimpleByDepartment(135L, false, null);
        //System.out.println(wxCpService.getDepartmentService().list(0L));
        //System.out.println(wxCpUsers);
        //wxCpService.getMessageService().sendLinkedCorpMessage();
        WxCpMessage wxCpMessage = WxCpMessage.newMiniProgramNoticeBuilder()
                .toUser("18676383865")
                .title("新建议审核提醒")
                .appId("wx002db6ba4793bb79")
                .description("有一条建议需要审核,点击开始审核")
                .contentItems(new HashMap<>())
                .build();
        wxCpService.getMessageService().send(wxCpMessage);
    }
}
