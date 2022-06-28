package org.example;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.mapper.VisitorMapper;
import org.example.pojo.Show;
import org.example.pojo.Visitor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import java.sql.Timestamp;
import java.util.*;


@SuppressWarnings("all")
@Slf4j
@RestController
@CrossOrigin(origins = {"*","null"})
public class Controller {
//    @Autowired
//    private StudentMapper studentMapper;
    private Gson gson = new GsonBuilder().serializeSpecialFloatingPointValues().create();
    @Autowired
    private VisitorMapper visitorMapper;
//    @GetMapping("/student")
//    public String getStudents(){
//        List<Student> students = studentMapper.selectList(null);
//        return gson.toJson(students);
//    }
//    @PostMapping("/add")
//    public void addStudent(@RequestBody Student student)
//    {
//        studentMapper.insert(student);
//    }
//    @PostMapping("/delete")
//    public void removeStudent(@RequestBody Student student)
//    {
//        studentMapper.deleteById(student);
//    }
//    @PostMapping("/update")
//    public void updateStudent(@RequestBody Student student)
//    {
//        studentMapper.updateById(student);
//    }


    @GetMapping("/api/report")
    public ResultData<String> report(@RequestHeader Map<String,String> headers,@RequestParam("block") String block,
                                         @RequestParam("lasttime") String lasttime,@RequestParam("from") String from,
                                         @RequestParam("session") String session){

        QueryWrapper<Visitor> queryWrapper = new QueryWrapper<Visitor>();
        queryWrapper.eq("session",session);
        queryWrapper.eq("token",headers.get("token"));
        List<Visitor> visitors = visitorMapper.selectList(queryWrapper);
        if(visitors.size() == 0)
        {
            Visitor visitor = new Visitor();
            visitor.setBlock(block);
            visitor.setStart(new Timestamp(System.currentTimeMillis()-10000));//往前推10秒
            visitor.setEnd(Timestamp.valueOf(lasttime));
            visitor.setSession(session);
            visitor.setToken(headers.get("token"));
            visitor.setAgent(headers.get("user-agent"));
            visitor.setFro(from);
            visitorMapper.insert(visitor);
        }
        else{
            visitors.get(0).setEnd(Timestamp.valueOf(lasttime));
            visitorMapper.updateById(visitors.get(0));
        }
        return ResultData.success("OK!");
    }
    @GetMapping("/api/test")
    public ResultData<String> get(){
        return ResultData.success("test CI@4");
    }
    @GetMapping("/api/yoydod")
    public ResultData<String> yoydod(@RequestParam("block") String block){
        //-----------变量名----------------------------
        int lastdayFlow,todayFlow,lastMonthFlow,nowMonthFlow;
        float totFlow,yoyFlow;
        int lastdayVisit,todayVisit,lastMonthVisit,nowMonthVisit;
        float totVisit,yoyVisit;
        float todayStayTime=1,lastdayStayTime=1,lastMonthStayTime=1,nowMonthStayTime=1;
        float totStayTime,yoyStayTime;
        float todayInvalidFlow=0,lastdayInvalidFlow=0,nowMonthInvalidFlow=0,lastMonthInvaliedFlow=0;
        float totInvalidFlow,yoyInvalidFlow;
        float todayDeepUser=0,lastdayDeepUser=0,nowMonthDeepUser=0,lastMonthDeepUser=0;
        float totDeepUser,yoyDeepUser;
        //--------------------客流量--------------------------------
        QueryWrapper<Visitor> lastDayQueryWrapper = new QueryWrapper<Visitor>();
        QueryWrapper<Visitor> todayQueryWrapper = new QueryWrapper<Visitor>();
        QueryWrapper<Visitor> lastMonthQueryWrapper = new QueryWrapper<Visitor>();
        QueryWrapper<Visitor> nowMonthQueryWrapper = new QueryWrapper<Visitor>();
        //获取前天、当天、明天零点
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long todayZero = calendar.getTimeInMillis();
        long lastdayZero =todayZero-86400000;
        long tomorrowZero = todayZero+86400000;
        //获取前月、当月，下月时间
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, -1);
        long lastMonth = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        long nowMonth = calendar.getTimeInMillis();
        calendar.add(Calendar.MONTH, 1);
        long nextMonth = calendar.getTimeInMillis();
        //当天、昨天、当月、上月
        lastDayQueryWrapper.lambda().ge(Visitor::getStart,new Timestamp(lastdayZero)).le(Visitor::getStart,new Timestamp(todayZero)).eq(Visitor::getBlock,block);
        todayQueryWrapper.lambda().ge(Visitor::getStart,new Timestamp(todayZero)).le(Visitor::getStart,new Timestamp(tomorrowZero)).eq(Visitor::getBlock,block);
        lastMonthQueryWrapper.lambda().ge(Visitor::getStart,new Timestamp(lastMonth)).le(Visitor::getStart,new Timestamp(nowMonth)).eq(Visitor::getBlock,block);
        nowMonthQueryWrapper.lambda().ge(Visitor::getStart,new Timestamp(nowMonth)).le(Visitor::getStart,new Timestamp(nextMonth)).eq(Visitor::getBlock,block);
        //查询
        List<Visitor> lastdayVisitors = visitorMapper.selectList(lastDayQueryWrapper);
        List<Visitor> todayVisitors = visitorMapper.selectList(todayQueryWrapper);
        List<Visitor> lastMonthVisitors = visitorMapper.selectList(lastMonthQueryWrapper);
        List<Visitor> nowMonthVisitors = visitorMapper.selectList(nowMonthQueryWrapper);

        //构造集合确认客流量
        Set<String> lastdayPeopleSet = new HashSet<>();
        for(Visitor item:lastdayVisitors){
            lastdayPeopleSet.add(item.getToken());
            if(item.getEnd().getTime()-item.getStart().getTime()<3000)
                lastdayInvalidFlow+=1;
            else if(item.getEnd().getTime()-item.getStart().getTime()>300000)
                lastdayDeepUser+=1;
            lastdayStayTime += (float) (item.getEnd().getTime()-item.getStart().getTime())/1000;
        }
        Set<String> todayPeopleSet = new HashSet<>();
        for(Visitor item:todayVisitors){
            todayPeopleSet.add(item.getToken());
            if(item.getEnd().getTime()-item.getStart().getTime()<3000)
                todayInvalidFlow+=1;
            else if(item.getEnd().getTime()-item.getStart().getTime()>300000)
                todayDeepUser+=1;
            todayStayTime+=(float) (item.getEnd().getTime()-item.getStart().getTime())/1000;
        }
        Set<String> lastMonthPeopleSet = new HashSet<>();
        for(Visitor item:lastMonthVisitors){
            lastMonthPeopleSet.add(item.getToken());
            if(item.getEnd().getTime()-item.getStart().getTime()<3000)
                lastMonthInvaliedFlow+=1;
            else if(item.getEnd().getTime()-item.getStart().getTime()>300000)
                lastMonthDeepUser+=1;
            lastMonthStayTime+=(float) (item.getEnd().getTime()-item.getStart().getTime())/1000;
        }
        Set<String> nowMonthPeopleSet = new HashSet<>();
        for(Visitor item:nowMonthVisitors){
            nowMonthPeopleSet.add(item.getToken());
            if(item.getEnd().getTime()-item.getStart().getTime()<3000)
                nowMonthInvalidFlow+=1;
            else if(item.getEnd().getTime()-item.getStart().getTime()>300000)
                nowMonthDeepUser+=1;
            nowMonthStayTime+=(float) (item.getEnd().getTime()-item.getStart().getTime())/1000;
        }
        //客流量
        lastdayFlow = lastdayPeopleSet.size()==0?1:lastdayPeopleSet.size();
        todayFlow = todayPeopleSet.size()==0?1:todayPeopleSet.size();
        lastMonthFlow = lastMonthPeopleSet.size()==0?1:lastMonthPeopleSet.size();
        nowMonthFlow = nowMonthPeopleSet.size()==0?1:nowMonthPeopleSet.size();
        totFlow = (float)todayFlow/(float) lastdayFlow;
        yoyFlow = (float) nowMonthFlow/(float) lastMonthFlow;
        //访问量
        lastdayVisit = lastdayVisitors.size()==0?1:lastdayVisitors.size();
        todayVisit = todayVisitors.size()==0?1:todayVisitors.size();
        lastMonthVisit = lastMonthVisitors.size()==0?1:lastMonthVisitors.size();
        nowMonthVisit = nowMonthVisitors.size()==0?1:nowMonthVisitors.size();
        totVisit = (float)todayVisit/(float) lastdayVisit;
        yoyVisit = (float) nowMonthVisit/(float) lastMonthVisit;
        //平均停留时长
        lastdayStayTime = lastdayStayTime/lastdayVisit;
        todayStayTime = todayStayTime/todayVisit;
        lastMonthStayTime = lastMonthStayTime/lastMonthVisit;
        nowMonthStayTime  = nowMonthStayTime/nowMonthVisit;

        totStayTime = todayStayTime/lastdayStayTime;
        yoyStayTime = nowMonthStayTime/lastMonthStayTime;

        //无效客流比 停留时间小于三秒视为无效访问
        todayInvalidFlow = todayInvalidFlow/todayFlow;
        lastdayInvalidFlow = lastdayInvalidFlow/lastdayFlow;
        nowMonthInvalidFlow = nowMonthInvalidFlow/nowMonthFlow;
        lastMonthInvaliedFlow = lastMonthInvaliedFlow/lastMonthFlow;
        totInvalidFlow = lastdayInvalidFlow==0?todayInvalidFlow/1:todayInvalidFlow/lastdayInvalidFlow;
        yoyInvalidFlow = lastMonthInvaliedFlow==0?nowMonthInvalidFlow/1:nowMonthInvalidFlow/lastMonthInvaliedFlow;

        //深度用户比
        todayDeepUser = todayDeepUser/todayFlow;
        lastdayDeepUser = lastdayDeepUser/lastdayFlow;
        nowMonthDeepUser = nowMonthDeepUser/nowMonthFlow;
        lastMonthDeepUser = lastMonthDeepUser/lastMonthFlow;
        totDeepUser = lastdayDeepUser==0?todayDeepUser/1:todayDeepUser/lastdayDeepUser;
        yoyDeepUser = lastMonthDeepUser==0?nowMonthDeepUser/1:nowMonthDeepUser/lastMonthDeepUser;

        Show show = new Show(todayFlow,totFlow,yoyFlow,todayVisit,totVisit,
                yoyVisit,todayStayTime,totStayTime,yoyStayTime,todayInvalidFlow,
                totInvalidFlow,yoyInvalidFlow,todayDeepUser,totDeepUser,yoyDeepUser);

        return ResultData.success(gson.toJson(show));
    }
    @GetMapping("/api/getLists")
    public ResultData<String> getStudents(){
        List<Visitor> visitors = visitorMapper.selectList(null);
        return ResultData.success(gson.toJson(visitors));
    }
}
