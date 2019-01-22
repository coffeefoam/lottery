package Test;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import io.lottery.WebApplication;
import io.lottery.controller.ApiController;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration 
@SpringBootTest(classes = WebApplication.class)
public class HelloTests {

	@Autowired
    private MockMvc mvc;

    @Before
    public void setUp() throws Exception {
        mvc = MockMvcBuilders.standaloneSetup(new ApiController()).build();
    }

    @Test
    public void getHello() throws Exception {
        String result=mvc.perform(
        		MockMvcRequestBuilders.post("/api/list")
        		.accept(MediaType.APPLICATION_JSON)
        		.param("code", "bjsc_hmyl")
                ).andExpect(status().isOk())    //返回的状态是200
        		.andDo(print())         //打印出请求和相应的内容
        		.andReturn().getResponse().getContentAsString();   //将相应的数据转换为字符串;
        System.err.println(result);
      
    }

}