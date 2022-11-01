package com.zja.remote;

import com.zja.api.FeignTestService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class RemoteConsumerApplicationTests {

	@Autowired
	FeignTestService service;

	@Test
	public void contextLoads() {
		/*Object userEntityList = service.getFileRetrieveFromFtps("root");
		Map<String,Object> map = (Map<String, Object>) userEntityList;
		List<FoldersDTO> foldersDTOS = (List<FoldersDTO>) map.get("data");
		System.out.println("userEntityList"+userEntityList);
		System.out.println("foldersDTOS"+foldersDTOS);*/
	}

}
