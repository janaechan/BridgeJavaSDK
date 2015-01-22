package org.sagebionetworks.bridge;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.RandomStringUtils;
import org.joda.time.Period;
import org.sagebionetworks.bridge.sdk.models.schedules.ABTestScheduleStrategy;
import org.sagebionetworks.bridge.sdk.models.schedules.Activity;
import org.sagebionetworks.bridge.sdk.models.schedules.Schedule;
import org.sagebionetworks.bridge.sdk.models.schedules.SchedulePlan;

public class Tests {

    public static final String TEST_KEY = "api";
    
    public static final String ADMIN_ROLE = "admin";

    public static final String RESEARCHER_ROLE = TEST_KEY + "_researcher";

    public static final Properties getApplicationProperties() {
        Properties properties = new Properties();
        File file = new File("src/main/resources/bridge-sdk.properties");
        try (InputStream in = new FileInputStream(file)) {
            properties.load(in);
        } catch(IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
    
    public static final String randomIdentifier() {
        return ("sdk-" + RandomStringUtils.randomAlphabetic(5)).toLowerCase();
    }
    
    // This seems like something that should be added to schedule.
    private static void setTaskActivity(Schedule schedule, String taskIdentifier) {
        checkNotNull(taskIdentifier);
        schedule.addActivity(new Activity("Task activity", taskIdentifier));
    }
    
    public static SchedulePlan getABTestSchedulePlan() {
        SchedulePlan plan = new SchedulePlan();
        Schedule schedule1 = new Schedule() {
            {
                setCronTrigger("* * *");
                setTaskActivity(this, "task:AAA");
                setExpires(Period.parse("PT60S"));
                setLabel("Test label for the user");
            }
        };
        Schedule schedule2 = new Schedule() {
            {
                setCronTrigger("* * *");
                setTaskActivity(this, "task:BBB");
                setExpires(Period.parse("PT60S"));
                setLabel("Test label for the user");
            }
        };
        Schedule schedule3 = new Schedule() {
            {
                setCronTrigger("* * *");
                setTaskActivity(this, "task:CCC");
                setExpires(Period.parse("PT60S"));
                setLabel("Test label for the user");
            }
        };
        ABTestScheduleStrategy strategy = new ABTestScheduleStrategy();
        strategy.addGroup(40, schedule1);
        strategy.addGroup(40, schedule2);
        strategy.addGroup(20, schedule3);
        plan.setStrategy(strategy);
        return plan;
    }
    
    public static SchedulePlan getSimpleSchedulePlan() {
        SchedulePlan plan = new SchedulePlan();
        Schedule schedule = new Schedule() {
            {
                setCronTrigger("* * *");
                setTaskActivity(this, "task:CCC");
                setExpires(Period.parse("PT60S"));
                setLabel("Test label for the user");
            }
        };
        plan.setSchedule(schedule);
        return plan;
    }

}
