package com.leyunone.disk.model.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * :)
 *
 * @Author LeYunnoe
 * @Date 2024/5/7 14:37
 */
@Getter
@Setter
public class SelectTreeVO {

    private String value;
    
    private String label;
    
    private List<SelectTreeVO> children;
}

