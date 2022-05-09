package com.nowcoder.community.controller;

import com.nowcoder.community.entity.DiscussPost;
import com.nowcoder.community.entity.Page;
import com.nowcoder.community.entity.User;
import com.nowcoder.community.service.CommentService;
import com.nowcoder.community.service.DiscussPostService;
import com.nowcoder.community.service.LikeService;
import com.nowcoder.community.service.UserService;
import com.nowcoder.community.util.CommunityUtil;
import com.nowcoder.community.util.HostHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

import static com.nowcoder.community.util.CommunityConstant.ENTITY_TYPE_POST;

@Controller
@RequestMapping("/discuss")
public class DiscussPostController {

    private static final Logger logger = LoggerFactory.getLogger(DiscussPostController.class);

    @Autowired
    private DiscussPostService discussPostService;

    @Autowired
    private HostHolder hostHolder;

    @Autowired
    private UserService userService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private CommentService commentService;

    // 添加一条帖子
    @RequestMapping(path = "/add", method = RequestMethod.POST)
    @ResponseBody
    public String addDiscussPost(String title, String content) {
        User user = hostHolder.getUser();
        if (user == null) {
            // 异步的返回，其中403代表没有权限访问
            return CommunityUtil.getJSONString(403, "你还没有登录哦!");
        }
        DiscussPost post = new DiscussPost();
        post.setUserId(user.getId());
        post.setTitle(title);
        post.setContent(content);
        post.setCreateTime(new Date());
        // discuss_post表中的type和status默认为0，不用添加
        discussPostService.addDiscussPost(post);
        // 如果报错，将来统一异常处理
        return CommunityUtil.getJSONString(0, "发布成功!");
    }

    // controller层中：查询帖子详情
    @RequestMapping(path = "/detail/{discussPostId}", method = RequestMethod.GET)
    public String getDiscussPost(@PathVariable("discussPostId") int discussPostId, Model model, Page page) {
        // 帖子
         DiscussPost post = discussPostService.findDiscussPostById(discussPostId);
         // 将数据封装成model发送给模板引擎
         model.addAttribute("post", post);
         // 查帖子的作者 因为不希望显示帖子的id，而是希望显示用户的名字和头像这样的信息，所以先获得discussPost表中的userId，再用userId去user表查，找出user对象，从而找出user的名字和头像的信息
        User user = userService.findUserById(post.getUserId());
        model.addAttribute("user", user);
//        // 点赞数量
//        long likeCount = likeService.findEntityLikeCount(ENTITY_TYPE_POST, discussPostId);
//        model.addAttribute("likeCount", likeCount);
//        // 点赞状态
//        int likeStatus = hostHolder.getUser() == null ? 0 :
//                likeService.findEntityLikeStatus(hostHolder.getUser().getId(), ENTITY_TYPE_POST, discussPostId);
//        model.addAttribute("likeStatus", likeStatus);
//        // 评论分页信息
//        page.setLimit(5);
//        page.setPath("/discuss/detail/" + discussPostId);
//        page.setRows(post.getCommentCount());
        // 评论列表,分为下面两种：
        // 1. 评论: 给帖子的评论
        // 2. 回复: 给评论的评论

        return "/site/discuss-detail";
    }
}
