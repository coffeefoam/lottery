package com.boying.cpapi;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/*@RunWith(SpringRunner.class)
@SpringBootTest*/
public class CpapiApplicationTests {

	@Test
	public void contextLoads() {
		int i=103;
		if (i%2==1) {
			System.err.println("单数");
		}else {
			System.err.println("偶数");
		}
	}

}
