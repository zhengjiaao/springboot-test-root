package com.dist.test;

import com.dist.base.BaseTest;
import com.dist.utils.IGenerator;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * Dozer是什么?

 Dozer是一个JavaBean映射工具库。

 它支持简单的属性映射，复杂类型映射，双向映射，隐式显式的映射，以及递归映射。

 它支持三种映射方式：注解、API、XML。

 它是开源的，遵从Apache 2.0 协议

 * @author zhengja@dist.com.cn
 * @data 2019/5/30 14:36
 */
public class DozerTest extends BaseTest {

    @Autowired
    private Mapper mapper;

    /**
     * 封装了Mapper工具接口：实现了list、set，数组，对象等深度复制！
     */
    @Autowired
    IGenerator iGenerator;

    /**
     * 单个对象的深度复制及类型转换，vo/domain , po
     * 输出结果： UserVO(userCode=123, loginName=null, loginPassword=null)
     */
    /*@Test
    public void testMapper(){
        UserDTO userDTO =new UserDTO();
        userDTO.setUserCode("123");
        UserVO userVO = mapper.map(userDTO, UserVO.class);
        System.out.println(userVO);
    }*/

    /**
     *单个对象的深度复制及类型转换，vo/domain , po
     * 输出结果： UserVO(userCode=123, loginName=null, loginPassword=null)
     */
   /* @Test
    public void test(){
        UserDTO userDTO =new UserDTO();
        userDTO.setUserCode("123");
        UserVO userVO = iGenerator.convert(userDTO, UserVO.class);
        //UserVO userVO = mapper.map(userDTO, UserVO.class);
        System.out.println(userVO);
    }*/

    //list深度复制
   /* @Test
    public void test1(){
        List<UserDTO> dtoList = new ArrayList<>();
        UserDTO userDTO =new UserDTO();
        userDTO.setUserCode("123");
        dtoList.add(userDTO);
        List<UserVO> userVOS = iGenerator.convert(dtoList, UserVO.class);
        System.out.println(userVOS);
    }*/

    //set深度复制
   /* @Test
    public void test2(){
        Set<UserDTO> dtoSet = new HashSet<>();
        UserDTO userDTO =new UserDTO();
        userDTO.setUserCode("123");
        dtoSet.add(userDTO);
        Set<UserVO> userVOS = iGenerator.convert(dtoSet, UserVO.class);
        System.out.println(userVOS);
    }*/


    //数组深度复制
    /*@Test
    public void test13(){
        UserDTO[] dtos = new UserDTO[3];
        UserDTO userDTO =new UserDTO();
        userDTO.setUserCode("123");
        dtos[0] = userDTO;
        dtos[1] = userDTO;
        dtos[2] = userDTO;
        UserVO[] userVOS = iGenerator.convert(dtos, UserVO.class);
        for (int i=0; i<userVOS.length;i++){
            System.out.println(userVOS[i]);
        }
    }*/
}
