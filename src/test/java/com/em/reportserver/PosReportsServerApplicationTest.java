package com.em.reportserver;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.util.Assert;

@SpringBootTest
@ExtendWith(SpringExtension.class)
@Execution(ExecutionMode.CONCURRENT)
@RequiredArgsConstructor
@ActiveProfiles(value = "test")
class PosReportsServerApplicationTest {

    private final ApplicationContext applicationContext;

    @Test
    public void contextLoads() {
        Assert.notNull(this.applicationContext,
                "[Assertion failed] - applicationContext argument is required; it must not be null");
    }

    @Test
    public void applicationStarts() {
        PosReportsServerApplication.main(new String[] {});
    }
}