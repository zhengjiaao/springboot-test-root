//package com.dist.service.Impl;
//
//import com.dist.entity.UserEntity;
//import com.dist.service.ProcedureService;
//import dist.common.procedure.define.ProcedureCaller;
//import dist.common.procedure.define.ProcedureModel;
//import dist.common.procedure.define.ProcedureRepository;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Map;
//
///**存储过程测试实现类
// * @author zhengja@dist.com.cn
// * @data 2019/6/24 14:23
// */
//@Service
//public class ProcedureServiceImpl implements ProcedureService {
//    /**
//     * 存储过程获取所有的用户信息
//     * @return
//     */
//    @Override
//    public List<UserEntity> getUsers(){
//        ProcedureModel model = ProcedureRepository.getProcedure("getUserProcedure");
//        Map<String,Object> resultMap = (Map<String, Object>) ProcedureCaller.call(model);
//        List<UserEntity> result = (List<UserEntity>) resultMap.get("P_DATA");
//        System.out.println("result========== "+result);
//        return result;
//    }
//
//    /**
//     * 根据年龄获取用户信息
//     * @param age
//     * @return
//     */
//    @Override
//    public List<UserEntity> getUsersByAge(String age){
//        ProcedureModel model = ProcedureRepository.getProcedure("getUsersByAge");
//        Map<String,Object> resultMap = (Map<String, Object>) ProcedureCaller.call(model,age);
//        List<UserEntity> result = (List<UserEntity>) resultMap.get("P_DATA");
//        System.out.println("result========== "+result);
//        return result;
//    }
//}
