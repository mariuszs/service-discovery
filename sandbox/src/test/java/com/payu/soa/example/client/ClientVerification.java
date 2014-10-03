package com.payu.soa.example.client;

import com.payu.discovery.proxy.RemoteService;
import com.payu.order.server.model.Order;
import com.payu.order.server.service.OrderService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.assertj.core.api.BDDAssertions.then;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SandboxApp.class)
public class ClientVerification {

    @Autowired
    @Lazy
    @RemoteService
    private OrderService testBean;

    @Test
    public void shouldCreateAndGetOrder() throws Exception {

        testBean.createOrder(new Order());
        final Order order = testBean.getOrder(1L);

        then(order).isNotNull();

    }
}
