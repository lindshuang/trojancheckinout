package cs310.trojancheckinout.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class HistoryTest {

    private History history;

    public static final String timeInDate = "03/26/2021";
    public static final String timeInTime = "03:30";
    public static final  String timeOutDate = null;
    public static final  String timeOutTime = null;
    public static final  double totalTime = 0;
    public static final  String buildingName = "SAL";


    @Before
    public void setUp() throws Exception {
        history = new History(timeInDate, timeInTime, timeOutDate, timeOutTime, totalTime, buildingName);
    }

    @Test
    public void setTimeOutAndTotalTime(){
        String newTimeOutDate = "03/16/2021";
        String newTimeOutTime = "11:30";
        double newTotalTime = 8;
        history.setTimeOutDate(newTimeOutDate);
        history.setTimeOutTime(newTimeOutTime);
        history.setTotalTime(newTotalTime);

        assertEquals(newTimeOutDate, history.getTimeOutDate());
        assertEquals(newTimeOutTime, history.getTimeOutTime());
        assertEquals(newTotalTime, history.getTotalTime(), 0);


    }
}