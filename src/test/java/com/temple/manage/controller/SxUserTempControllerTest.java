package com.temple.manage.controller;

import com.temple.manage.util.AbstractRestControllerTest;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.cp.api.WxCpService;
import me.chanjar.weixin.cp.bean.WxCpDepart;
import me.chanjar.weixin.cp.bean.WxCpUser;
import me.chanjar.weixin.cp.bean.message.WxCpMessage;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;

public class SxUserTempControllerTest extends AbstractRestControllerTest {
    @Autowired
    private WxCpService wxCpService;

    @Test
    public void getUser() throws Exception {
        System.out.println(wxCpService.getUserService().getById("2000"));
    }

    @Test
    public void getToken() throws WxErrorException {
        List<WxCpDepart> list = wxCpService.getDepartmentService().list(null);
        System.out.println(list);
//        for (WxCpDepart depart : list) {
//            List<WxCpUser> wxCpUsers = wxCpService.getUserService().listSimpleByDepartment(depart.getId(), false, null);
//            System.out.println(wxCpUsers);
//        }
        System.out.println(wxCpService.getUserService().getById("2826"));
//        System.out.println(wxCpService.getDepartmentService().list(1L));
//        wxCpService.getMessageService().sendLinkedCorpMessage();
//        WxCpMessage wxCpMessage = WxCpMessage.newMiniProgramNoticeBuilder()
//                .toUser("18676383865")
//                .title("新建议审核提醒")
//                .appId("wx002db6ba4793bb79")
//                .description("有一条建议需要审核,点击开始审核")
//                .contentItems(new HashMap<>())
//                .build();
//        wxCpService.getMessageService().send(wxCpMessage);
    }
}
