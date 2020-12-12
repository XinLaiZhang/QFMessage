package top.fluffcotton.service;

import top.fluffcotton.pojo.Folder;
import top.fluffcotton.pojo.GroupFolder;
import top.fluffcotton.pojo.QqGroup;
import top.fluffcotton.pojo.UserSelect;

import java.util.List;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupFileControl
 * @Description: 群与文件夹控制Service
 * @date 2020.08.23 22:15
 */
public interface GroupFileService {

    /**
     * 创建文件夹
     *
     * @param father 父文件夹允许为null
     * @param folder 要创建的文件夹
     * @return 数据库处理行数
     * @Title createFolder
     * @Description 创建文件夹
     * @author 张逸辰
     * @Date 2020/8/24 17:05
     */
    int createFolder(Folder father, Folder folder);

    /**
     * 将文件夹，群组添加到父文件夹内
     * children与qqGroup 仅允许一个为null
     *
     * @param father   父文件夹
     * @param children 子文件夹
     * @param qqGroup  子群
     * @return 修改数据库行数
     * @Title createFile
     * @Description 将文件夹，群组添加到父文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 16:06
     */
    int insertFolder(Folder father, Folder children, QqGroup qqGroup);

    /**
     * {@link GroupFileService#insertFolder(Folder, Folder, QqGroup)}
     *
     * @return 修改数据库行数
     * @Title createFile
     * @Description 将文件夹，群组添加到父文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 16:06
     */
    default int insertFolder(Folder father, Folder children) {
        return this.insertFolder(father, children, null);
    }

    /**
     * {@link GroupFileService#insertFolder(Folder, Folder, QqGroup)}
     *
     * @return 修改数据库行数
     * @Title createFile
     * @Description 将文件夹，群组添加到父文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 16:06
     */
    default int insertFolder(Folder father, QqGroup qqGroup) {
        return this.insertFolder(father, null, qqGroup);
    }


    /**
     * 删除文件夹，并删除其中全部内容
     *
     * @param folder   父文件夹
     * @return 修改数据库行数
     * @Title deleteFile
     * @Description 删除文件夹内子文件夹
     * @author 张逸辰
     * @Date 2020/8/24 16:13
     */
    int deleteFile(Folder folder);

    /**
     * 更新文件夹名称，传入文件夹，并且该 对象 名称为新名称，fileId为需要修改的文件夹id
     *
     * @param folder 文件夹
     * @return 成功返回修改行数
     * @Title updateFolderName
     * @Description 更新文件夹名称
     * @author 张逸辰
     * @Date 2020/8/23 23:08
     */
    int updateFolderName(Folder folder);

    /**
     * 重新获取群名称，存储到数据库
     *
     * @param qqGroup 要更新的群
     * @return 更改数据库行数
     * @Title updateGroupName
     * @Description 重新获取群名称，存储到数据库
     * @author 张逸辰
     * @Date 2020/8/24 16:16
     */
    int updateGroupName(QqGroup qqGroup);

    /**
     * 将文件夹，群组移动到新的文件夹内
     * 若newFolder为null 则移动到根目录
     *
     * @param newFolder 目标文件夹
     * @param folder 要移动的文件夹
     * @param qqGroup 要移动的群组
     * @return 数据库修改次数
     * @Title removeFile
     * @Description 将文件夹，群组移动到新的文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 17:46
     */
    int removeFile(Folder newFolder, Folder folder, QqGroup qqGroup);

    /**
     * {@link GroupFileService#removeFile(Folder, Folder, QqGroup)}
     * @Description 移动文件夹到文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 17:58
     */
    default int removeFile(Folder newFolder, Folder folder){
        return this.removeFile(newFolder, folder, null);
    }

    /**
     * {@link GroupFileService#removeFile(Folder, Folder, QqGroup)}
     * @Description 移动群组到文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 17:58
     */
    default int removeFile(Folder newFolder,QqGroup qqGroup){
        return this.removeFile(newFolder,null, qqGroup);
    }

    /**
     * {@link GroupFileService#removeFile(Folder, Folder, QqGroup)}
     * @Description 移动群组到根目录
     * @author 张逸辰
     * @Date 2020/8/24 17:58
     */
    default int removeFile(QqGroup qqGroup){
        return this.removeFile(null,qqGroup);
    }

    /**
     * {@link GroupFileService#removeFile(Folder, Folder, QqGroup)}
     * @Description 移动文件夹到根目录
     * @author 张逸辰
     * @Date 2020/8/24 17:58
     */
    default int removeFile(Folder folder){
        return this.removeFile(null,folder);
    }

    /**
     * 根据用户查 询群组和文件夹
     * 如果user属性 isRoot为true，则查询根目录
     * 否则查询全部
     * @param user 用户
     * @return 群组文件夹列表
     * @Title selectGroupFile
     * @Description 根据用户查询根目录下的群组和文件夹
     * @author 张逸辰
     * @Date 2020/8/23 23:05
     */
    List<GroupFolder> selectGroupFolder(UserSelect user);

    /**
     * 根据文件夹查询目录下的群组和文件夹
     * @param folder 文件夹
     * @return 群组文件夹列表
     * @Title selectGroupFile
     * @Description 根据文件夹查询目录下的群组和文件夹
     * @author 张逸辰
     * @Date 2020/8/23 23:05
     */
    List<GroupFolder> selectGroupFolder(Folder folder);

    /**
     * 根据文件夹id获取文件夹
     * @Title selectGroupFolder
     * @Description 根据文件夹id获取文件夹
     * @param id 文件夹id
     * @param all 全部包括删除文件
     * @return 文件夹
     * @author 张逸辰
     * @Date 2020/8/29 14:29
     */
    Folder selectGroupFolder(String id, boolean all);
}
