package cs310.trojancheckinout.models;


import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class BuildingTest {

    private Building building;
    private static final String DEFAULT_MAX_CAPACITY = "100";

    @Before
    public void setUp() throws Exception {
        building = new Building();
        building.setMaxCapacity(DEFAULT_MAX_CAPACITY);
    }

    @Test
    public void checkOverrideMaxCapacity(){
        String newCap = "200";
        building.setMaxCapacity(newCap);
        assertEquals(newCap, building.getMaxCapacity());
    }
}
