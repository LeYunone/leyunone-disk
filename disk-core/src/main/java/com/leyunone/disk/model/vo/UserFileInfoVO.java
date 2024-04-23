package com.leyunone.disk.model.vo;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author LeYunone
 * @email 365627310@qq.com
 * @create 2022-03-22 09:33
 */
@Getter
@Setter
@ToString
public class UserFileInfoVO {

    /**
     * 用户内存总量
     */
    private String fileTotal;

    /**
     * 用户文件
     */
    private List<FileInfoVO> fileinfos;
}
