package top.fluffcotton.mapper;

import top.fluffcotton.pojo.Folder;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.pojo.UserSelect;

import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupFileMapper
 * @Description: 用于操作通过User获取Group 和 File
 * @date
 */
public interface GroupFolderMapper {

    /**
     * 通过user获取Group
     * 若isRoot 为true 则查询根目录，否则查询全部
     * @Title selectQqGroupByUser
     * @Description 通过user获取Group
     * @param user 用户
     * @return group列表
     * @author 张逸辰
     * @Date 2020/8/23 22:06
     */
    List<QqGroup> selectQqGroupByUser(UserSelect user);

    /**
     * 获取文件夹内的群组
     * @Title selectQqGroupByFolder
     * @Description 获取文件夹内的群组
     * @param folder 文件夹
     * @return group列表
     * @author 张逸辰
     * @Date  22:25
     */
    List<QqGroup> selectQqGroupByFolder(Folder folder);

    /**
     * 获取文件夹内的文件夹
     * @Title selectFolderByFolder
     * @Description 获取文件夹内的文件夹
     * @param folder 文件夹
     * @return 文件夹列表
     * @author 张逸辰
     * @Date 2020/8/23 23:01
     */
    List<Folder> selectFolderByFolder(Folder folder);
}
