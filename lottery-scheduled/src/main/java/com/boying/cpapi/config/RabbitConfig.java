package com.boying.cpapi.config;

import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.BindingBuilder.DestinationConfigurer;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {
	
	String Fanout_Exchange="kjzb_Exchange";
	
	@Autowired
    private RabbitTemplate rabbitTemplate;
    
	/**
     * 声明交互器  订阅模式，给platdata_Exchange交换机发送消息，绑定了这个交换机的所有队列都收到这个消息。
     */
    @Bean
    FanoutExchange fanoutExchange() {
        return new FanoutExchange(Fanout_Exchange);
    }
    
    /**
     * 绑定交换器
    * @author ms
    * @date 2018年10月1日 下午3:59:03
     */
    @Bean
    public DestinationConfigurer binding() {
    	return BindingBuilder.bind(fanoutExchange());
    }
    
    /**
     * 推送消息到MQ,类型为广播模式
     * Fanout : 消息广播模式 不管路由键或者是路由模式 会把消息发给绑定给它的全部队列  如果配置了routingkey会被忽略
     */
    public void rabbitMQ_Send(String msg) {
		this.rabbitTemplate.convertAndSend(Fanout_Exchange, "", msg);
    }
    
}

