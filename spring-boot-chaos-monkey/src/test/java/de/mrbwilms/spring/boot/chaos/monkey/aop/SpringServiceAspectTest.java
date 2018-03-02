package de.mrbwilms.spring.boot.chaos.monkey.aop;

import de.mrbwilms.spring.boot.chaos.monkey.component.ChaosMonkey;
import de.mrbwilms.spring.boot.chaos.monkey.demo.controller.DemoController;
import de.mrbwilms.spring.boot.chaos.monkey.demo.restcontroller.DemoRestController;
import de.mrbwilms.spring.boot.chaos.monkey.demo.service.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.aop.aspectj.annotation.AspectJProxyFactory;

import static org.mockito.Mockito.*;

/**
 * @author Benjamin Wilms
 */
@RunWith(MockitoJUnitRunner.class)
public class SpringServiceAspectTest {

    @Mock
    private ChaosMonkey chaosMonkeyMock;

    @Test
    public void chaosMonkeyIsCalled() {
        DemoService serviceTarget = new DemoService();

        AspectJProxyFactory factory = new AspectJProxyFactory(serviceTarget);
        SpringServiceAspect serviceAspect = new SpringServiceAspect(chaosMonkeyMock);
        factory.addAspect(serviceAspect);

        DemoService proxy = factory.getProxy();
        proxy.sayHelloService();

        verify(chaosMonkeyMock, times(1)).callChaosMonkey();
        verifyNoMoreInteractions(chaosMonkeyMock);

    }

    @Test
    public void chaosMonkeyIsNotCalled() {
        DemoService target = new DemoService();

        AspectJProxyFactory factory = new AspectJProxyFactory(target);
        SpringControllerAspect controllerAspect = new SpringControllerAspect(chaosMonkeyMock);
        SpringRestControllerAspect serviceAspect = new SpringRestControllerAspect(chaosMonkeyMock);
        SpringRepositoryAspect repositoryAspect = new SpringRepositoryAspect(chaosMonkeyMock);
        factory.addAspect(controllerAspect);
        factory.addAspect(serviceAspect);
        factory.addAspect(repositoryAspect);


        DemoService proxy = factory.getProxy();
        proxy.sayHelloService();

        verify(chaosMonkeyMock, times(0)).callChaosMonkey();
        verifyNoMoreInteractions(chaosMonkeyMock);

    }
}