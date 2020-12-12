package top.fluffcotton.service.impl;

import net.mamoe.mirai.contact.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.fluffcotton.mapper.FolderGroupMapper;
import top.fluffcotton.mapper.FolderMapper;
import top.fluffcotton.mapper.GroupFolderMapper;
import top.fluffcotton.mapper.QqGroupMapper;
import top.fluffcotton.pojo.*;
import top.fluffcotton.service.BotService;
import top.fluffcotton.service.GroupFileService;
import top.fluffcotton.service.GroupSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author 张逸辰
 * @version V1.0
 * @Title: GroupFileControlImpl
 * @Description: 文件夹处理类
 * @date 2020.08.24 16:17
 */
@Service
public class GroupFileServiceImpl implements GroupFileService {
    /**
     * 日志
     */
    private static final Logger logger = LoggerFactory.getLogger(GroupFileServiceImpl.class);

    /**
     * 直接获取用户联系的Group 和 File
     */
    @Autowired
    private GroupFolderMapper groupFolderMapper;
    /**
     * 修改文件夹名称
     * 添加文件夹
     */
    @Autowired
    private FolderMapper folderMapper;

    /**
     * 修改群名称
     * 添加群
     */
    @Autowired
    private QqGroupMapper qqGroupMapper;

    /**
     * 添加父文件夹子文件的关系
     */
    @Autowired
    private FolderGroupMapper folderGroupMapper;

    @Autowired
    private BotService botService;

    /**
     * 群设置
     */
    @Autowired
    private GroupSetting groupSetting;

    @Override
    public int createFolder(Folder father, Folder folder) {
        FolderGroup folderGroup = null;
        //判断是否有父文件夹
        if (father != null) {
            folder.setIsroot(false);
            folderGroup = new FolderGroup();
            folderGroup.setFileid(UUID.randomUUID().toString());
            folderGroup.setFolderid(father.getFileid());
            folderGroup.setChildrenid(folder.getFileid());
        }
        //创建文件夹
        int result = folderMapper.insertSelective(folder);
        //若存在父文件夹则添加记录
        if (folderGroup != null) {
            result += folderGroupMapper.insertSelective(folderGroup);
        }
        return result;
    }

    @Override
    public int insertFolder(Folder father, Folder children, QqGroup qqGroup) {
        FolderGroup folderGroup = new FolderGroup();
        folderGroup.setFileid(UUID.randomUUID().toString());
        folderGroup.setFolderid(father.getFileid());
        int result = 0;
        if (qqGroup != null) {
            folderGroup.setGroupid(qqGroup.getGroupid());
            //将根目录的群移走
            if (qqGroup.getIsroot()) {
                qqGroup.setIsroot(false);
                result += qqGroupMapper.updateByPrimaryKey(qqGroup);
            }
        }
        if (children != null) {
            folderGroup.setChildrenid(children.getFileid());
            //将根目录的文件夹移走
            if (children.getIsroot()) {
                children.setIsroot(false);
                result += folderMapper.updateByPrimaryKey(children);
            }
        }
        result += folderGroupMapper.insert(folderGroup);
        return result;
    }

    @Override
    public int deleteFile(Folder folder) {
        int result;
        //直接更新删除时间
        folder.setDeletetime(System.currentTimeMillis());
        result = folderMapper.updateByPrimaryKey(folder);
        //查询子文件夹
        FolderGroupExample folderGroupExample = new FolderGroupExample();
        folderGroupExample.createCriteria().andFolderidEqualTo(folder.getFileid());
        List<FolderGroup> folderGroups = folderGroupMapper.selectByExample(folderGroupExample);
        for (FolderGroup folderGroup : folderGroups) {
            if (folderGroup.getGroupid() != null) {
                QqGroup qqGroup = groupSetting.selectByGroupId(folderGroup.getGroupid(), false);
                result += groupSetting.deleteGroup(qqGroup);
            } else if (folderGroup.getChildrenid() != null) {
                Folder f = selectGroupFolder(folderGroup.getChildrenid(), false);
                result += deleteFile(f);
            }
        }
        return result;
    }

    @Override
    public int updateFolderName(Folder folder) {
        return folderMapper.updateByPrimaryKey(folder);
    }

    @Override
    public int updateGroupName(QqGroup qqGroup) {
        Group group = botService.getGroup(Long.parseLong(qqGroup.getGroupid()));
        String newName = group.getName();
        if (newName.equals(qqGroup.getGroupname())) {
            return 1;
        }
        qqGroup.setGroupname(newName);
        return qqGroupMapper.updateByPrimaryKey(qqGroup);
    }

    /**
     * 将文件夹，群组移动到新的文件夹内
     *
     * @param newFolder 目标文件夹
     * @param folder    要移动的文件夹
     * @param qqGroup   要移动的群组
     * @return 数据库修改次数
     * @Title removeFile
     * @Description 将文件夹，群组移动到新的文件夹内
     * @author 张逸辰
     * @Date 2020/8/24 17:46
     */
    @Override
    public int removeFile(Folder newFolder, Folder folder, QqGroup qqGroup) {
        int result = 0;
        FolderGroupExample folderGroupExample = new FolderGroupExample();
        //进入根目录
        if (newFolder == null) {
            if (folder != null) {
                //更新为根目录
                folder.setIsroot(true);
                result += folderMapper.updateByPrimaryKey(folder);
                //删除文件的记录
                folderGroupExample.or().andChildrenidEqualTo(folder.getFileid());
            }
            if (qqGroup != null) {
                //更新根目录
                qqGroup.setIsroot(true);
                result += qqGroupMapper.updateByPrimaryKey(qqGroup);
                //删除文件夹记录
                folderGroupExample.or().andGroupidEqualTo(qqGroup.getGroupid());
            }
            //若都不为空则执行删除
            if (qqGroup != null || folder != null) {
                result += folderGroupMapper.deleteByExample(folderGroupExample);
            }
        } else {
            FolderGroup folderGroup = new FolderGroup();
            folderGroup.setFileid(UUID.randomUUID().toString());
            folderGroup.setFolderid(newFolder.getFileid());
            if (folder != null) {
                folderGroupExample.or().andChildrenidEqualTo(folder.getFileid());
            }
            if (qqGroup != null) {
                folderGroupExample.or().andGroupidEqualTo(qqGroup.getGroupid());
            }
            //若都不为空则执行更新
            if (qqGroup != null || folder != null) {
                result += folderGroupMapper.updateByExampleSelective(folderGroup, folderGroupExample);
            }
        }
        return result;
    }

    @Override
    public List<GroupFolder> selectGroupFolder(UserSelect user) {
        List<GroupFolder> groupFolders = new ArrayList<>();
        List<QqGroup> qqGroups = groupFolderMapper.selectQqGroupByUser(user);
        //查询用户的文件夹
        FolderExample folderExample = new FolderExample();
        FolderExample.Criteria criteria = folderExample.createCriteria().andUseridEqualTo(user.getId()).andDeletetimeEqualTo(0L);
        if (user.isRoot()) {
            criteria.andIsrootEqualTo(user.isRoot());
        }
        List<Folder> folders = folderMapper.selectByExample(folderExample);

        if (qqGroups != null) {
            for (QqGroup qqGroup : qqGroups) {
                groupFolders.add(new GroupFolder(qqGroup));
            }

        }
        if (folders != null) {
            for (Folder f : folders) {
                groupFolders.add(new GroupFolder(f));
            }
        }
        return groupFolders;
    }

    @Override
    public List<GroupFolder> selectGroupFolder(Folder folder) {
        List<GroupFolder> groupFolders = new ArrayList<>();
        List<Folder> folders = groupFolderMapper.selectFolderByFolder(folder);
        List<QqGroup> qqGroups = groupFolderMapper.selectQqGroupByFolder(folder);
        if (folders != null) {
            for (Folder f : folders) {
                groupFolders.add(new GroupFolder(f));
            }
        }
        if (qqGroups != null) {
            for (QqGroup qqGroup : qqGroups) {
                groupFolders.add(new GroupFolder(qqGroup));
            }
        }
        return groupFolders;
    }

    /**
     * 根据文件夹id获取文件夹
     *
     * @param id  文件夹id
     * @param all 是否为全部
     * @return 文件夹
     * @Title selectGroupFolder
     * @Description 根据文件夹id获取文件夹
     * @author 张逸辰
     * @Date 2020/8/29 14:29
     */
    @Override
    public Folder selectGroupFolder(String id, boolean all) {
        Folder folder = folderMapper.selectByPrimaryKey(id);
        if (folder != null && (all || folder.getDeletetime() == 0)) {
            return folder;
        }
        return null;
    }

}
