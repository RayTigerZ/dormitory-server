package com.ray.dormitory.service.impl;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ray.dormitory.bean.po.Cost;
import com.ray.dormitory.bean.po.Notice;
import com.ray.dormitory.bean.po.User;
import com.ray.dormitory.exception.CustomException;
import com.ray.dormitory.mapper.CostMapper;
import com.ray.dormitory.mapper.NoticeMapper;
import com.ray.dormitory.service.NoticeService;
import com.ray.dormitory.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 通知 服务实现类
 * </p>
 *
 * @author Ray
 * @date 2020-2-28 0:19:18
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
    @Autowired
    private CostMapper costMapper;
    @Autowired
    private UserService userService;
//    @Autowired
//    private FreeMarkerConfigurer configuration;

    private static final String costNoticeTemplate = "%s %s 的 %s";


    @Override
    public boolean costNotice(int[] costIds) {
        List<Notice> noticeList = new ArrayList<>();

        for (int costId : costIds) {
            Cost cost = costMapper.selectById(costId);
            Wrapper<User> wrapper = Wrappers.<User>lambdaQuery()
                    .select(User::getAccount)
                    .eq(User::getRoomNum, cost.getRoomNum());
            List<Object> accounts = userService.listObjs(wrapper);
            accounts.forEach(account -> {
                String title = String.format("%s的%s通知", cost.getCycle(), cost.getChargeName());
//                try {
//                    configuration.getConfiguration().getTemplate("");
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
                String content = String.format(costNoticeTemplate, cost.getRoomNum(), cost.getCycle(), cost.getChargeName());
                Notice notice = new Notice((String) account, title, content);
                noticeList.add(notice);
            });


        }
        return saveBatch(noticeList);
    }

    @Override
    public boolean removeById(Serializable id) {
        Notice notice = baseMapper.selectById(id);
        if (!notice.getIsRead()) {
            throw new CustomException(204, "未读的通知无法删除！");
        }
        return super.removeById(id);
    }
}
