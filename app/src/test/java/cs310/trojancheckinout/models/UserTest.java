package cs310.trojancheckinout.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class UserTest {

    // Manager User Constructor setup
    private static final String DEFAULT_FNAME_MANAGER = "Ashna";
    private static final String DEFAULT_LNAME_MANAGER = "Chandra";
    private static final String DEFAULT_EMAIL_MANAGER = "ashnacha@usc.edu";
    private static final String DEFAULT_PSWD_MANAGER = "test";
    private User userManager;

    // Student User Constructor setup
    private static final String DEFAULT_FNAME_STUDENT = "Shania";
    private static final String DEFAULT_LNAME_STUDENT = "Jain";
    private static final String DEFAULT_EMAIL_STUDENT = "shaniaja@usc.edu";
    private static final String DEFAULT_PSWD_STUDENT = "test";
    private static final String DEFAULT_MAJOR_STUDENT = "Computer Science";
    private static final String DEFAULT_ID_STUDENT= "123456789";

    private User userStudent;


    @Before
    public void setup() {
        userManager = new User(DEFAULT_FNAME_MANAGER, DEFAULT_LNAME_MANAGER,DEFAULT_EMAIL_MANAGER,DEFAULT_PSWD_MANAGER);
        userStudent = new User(DEFAULT_FNAME_STUDENT, DEFAULT_LNAME_STUDENT,DEFAULT_EMAIL_STUDENT,DEFAULT_PSWD_STUDENT, DEFAULT_ID_STUDENT, DEFAULT_MAJOR_STUDENT);

    }


    @Test
    public void checkConstructorSetsManagerUserProfile() {
        assertEquals(DEFAULT_FNAME_MANAGER, userManager.getFirstName());
        assertEquals(DEFAULT_LNAME_MANAGER, userManager.getLastName());
        assertEquals(DEFAULT_EMAIL_MANAGER, userManager.getEmail());
        assertEquals(DEFAULT_PSWD_MANAGER, userManager.getPassword());
        assertEquals("Manager", userManager.getOccupation());

    }

    @Test
    public void checkConstructorSetStudentUserProfile() {
        assertEquals(DEFAULT_FNAME_STUDENT, userStudent.getFirstName());
        assertEquals(DEFAULT_LNAME_STUDENT, userStudent.getLastName());
        assertEquals(DEFAULT_EMAIL_STUDENT, userStudent.getEmail());
        assertEquals(DEFAULT_PSWD_STUDENT, userStudent.getPassword());
        assertEquals(DEFAULT_ID_STUDENT, userStudent.getStudentID());
        assertEquals(DEFAULT_MAJOR_STUDENT, userStudent.getOccupation());

    }

    @Test
    public void checkSetOverridePassword() {
        String newPassword = "test123";
        userManager.setPassword(newPassword);
        assertEquals(newPassword, userManager.getPassword());


    }
}