package org.example.pojo;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.sql.Timestamp;
@Data
@TableName("visitor")
public class Visitor {
    private int ID;
    private String Block;
    private Timestamp Start;
    private  Timestamp End;
    private String session;
    private String token;
    private String Agent;
    private String fro;
}
