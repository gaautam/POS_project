package com.increff.employee.service;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.AnnotationConfigWebContextLoader;
import org.springframework.test.context.web.WebAppConfiguration;

import com.increff.pos.spring.SpringConfig;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = SpringConfig.class,loader = AnnotationConfigWebContextLoader.class)
@WebAppConfiguration("employee-spring-master/src/test/webapp")
public abstract class AbstractUnitTest {

}
