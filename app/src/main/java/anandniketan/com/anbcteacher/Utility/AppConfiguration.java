package anandniketan.com.anbcteacher.Utility;

import java.util.ArrayList;

import anandniketan.com.anbcteacher.Models.ClassDetailModel;
import anandniketan.com.anbcteacher.Models.UserProfileModel;
import anandniketan.com.anbcteacher.base.BaseApp;

/**
 * Created by Megha on 15-Sep-17.
 */
public class AppConfiguration {

    //Local
    public static String DOMAIN_LOCAL = "http://192.168.1.6:8086/MobileApp_Service.asmx/";
    public static String GET_API_URL = "http://anandniketanbhadaj.org/appService/5b9a72856992e144c74fc836ed6e76a2/appsUrl";

    public static String LIVE_BASE_URL = "http://192.168.1.6:8086/";

//    public static String BASEURL = GET_API_URL + "MobileApp_Service.asmx/";

    public static String BASEURL = LIVE_BASE_URL + "MobileApp_Service.asmx/";

    // public static String DOMAIN_LIVE = "http://192.168.1.11:8086/MobileApp_Service.asmx/";//use for office only
//   public static String DOMAIN_LIVE = "http://192.168.1.187:8089/MobileApp_Service.asmx/";//client for office only
    //public static String DOMAIN_LIVE = "http://103.24.183.28:8085/MobileApp_Service.asmx/";//use for client

    //public static String LIVE_BASE_URL = Utility.getPref(MyApp.getAppContext(), "live_base_url");

    public static String DOMAIN_LIVE = "";//use for client

    //Image Url
    //Local
    //public static String DOMAIN_LIVE_IMAGES = "http://192.168.1.19:8086/skool360-Category-Images/Teacher/";
    //Live
    public static String DOMAIN_LIVE_IMAGES = LIVE_BASE_URL+"skool360-Category-Images/Teacher/";

    //ICONS URL
    //Local
    //public static String DOMAIN_LIVE_ICONS = "http://192.168.1.19:8086/skool360-Design-Icons/Teacher/";
    //Live
    public static String DOMAIN_LIVE_ICONS = LIVE_BASE_URL+"skool360-Design-Icons/Teacher/";


    public enum Domain {
        LIVE, LOCAL
    }

    static Domain domain = Domain.LOCAL ;//only Change this for changing environment

    public static String getBaseUrl() {
        String url = "";
        switch (domain) {
            case LIVE:
                url = DOMAIN_LIVE ;
                break;
            case LOCAL:
                url = DOMAIN_LOCAL;
                break;
            default:
                break;
        }
        return url;
    }

    public static String getUrl(String methodName) {
        String url = "";
        switch (domain) {
            case LIVE:
                LIVE_BASE_URL = Utility.getPref(BaseApp.getAppContext(), "live_base_url");

                DOMAIN_LIVE_IMAGES = LIVE_BASE_URL + "SKOOL360-Category-Images/Teacher/";

//                GALLARY_LIVE = LIVE_BASE_URL;

                AppConfiguration.DOMAIN_LIVE = LIVE_BASE_URL + "MobileApp_Service.asmx/";

                url = DOMAIN_LIVE + methodName;

                break;

            case LOCAL:
                url = DOMAIN_LOCAL + methodName;

                DOMAIN_LIVE_IMAGES = "http://192.168.1.6:8086/SKOOL360-Category-Images/Teacher/";
//
//                GALLARY_LIVE = "http://192.168.1.6:8086/";

                break;

            default:
                break;
        }
        return url;
    }

    public static String GetStaffLogin = "StaffLogin";
    public static String GetStaffAttendence = "StaffAttendence";
    public static String GetInsertAttendance = "InsertAttendance";
    public static String GetStaffProfile = "StaffProfile";
    public static String GetTeacherTodaySchedule = "TeacherTodaySchedule";
    public static String GetTeacherAssignedSubject = "TeacherAssignedSubject";
    public static String GetTeacherGetAssignStudentSubject = "TeacherGetAssignStudentSubject";
    public static String GetTeacherGetTimetable = "TeacherGetTimetable";
    public static String GetTeacherGetTestSyllabus = "TeacherGetTestSyllabus";
    public static String GetTeacherGetTestMarks = "TeacherGetTestMarks";
    public static String GetTeacherInsertTestDetail = "TeacherInsertTestDetail";
    public static String GetTeacherGetTestNameGradeWise = "TeacherGetTestNameGradeWise";
    public static String GetTeacherDailyWork = "TeacherDailyWork";
    public static String TeacherGetClassSubjectWiseStudent = "TeacherGetClassSubjectWiseStudent";
    public static String PTMTeacherStudentInsertDetail = "PTMTeacherStudentInsertDetail";
    public static String PTMTeacherStudentGetDetail = "PTMTeacherStudentGetDetail";
    public static String PTMDeleteMeeting = "PTMDeleteMeeting";
    public static String TeacherInsertAssignStudentSubject = "TeacherInsertAssignStudentSubject";
    public static String DeviceVersion = "DeviceVersion";
    public static String InsertTimetable = "InsertTimetable";
    public static String DeleteTimetable = "DeleteTimetable";
    public static String GetStandardSection = "GetStandardSection";
    public static String TeacherStudentHomeworkStatus = "TeacherStudentHomeworkStatus";
    public static String TeacherStudentHomeworkStatusInsertUpdate = "TeacherStudentHomeworkStatusInsertUpdate";
    public static String GetTeacherWorkPlan = "TeacherWorkPlan";
    public static String TeacherUpdateWorkPlanCompletion = "TeacherUpdateWorkPlanCompletion";
    public static String GetAssignedSubjectForTimeTable = "GetAssignedSubject";
    public static String GetLectureDetails = "GetLectureDetails";
    public static String GetStudentLeaveRequest = "GetStudentLeaveRequest";
    public static String GetHead = "GetHead";
    public static String GetLeaveEndDate = "GetLeaveEndDate";
    public static String InsertStaffLeaveRequest = "InsertStaffLeaveRequest";
    public static String GetStaffLeaveRequest = "GetStaffLeaveRequest";
    public static String DeleteStaffLeave = "DeleteStaffLeave";
    public static String GetStandardSectionForMarks = "GetStandardSectionForMarks";
    public static String GetTestForMarks = "GetTestForMarks";
    public static String GetTerm = "GetTerm";
    public static String GetGrade = "GetTeacherStandardList";
    public static String GetSection = "GetTeacherClassList";
    public static String GetSubject = "GetTeacherSubjectList";
    public static String InsertDailyEntry = "InsertDailyEntry";
    public static String GetMarks = "GetMarks";
    public static String InsertMarks = "InsertMarks";
    public static String ForgetIDPasswordStaff = "ForgetIDPasswordStaff";
    public static String StaffChangePassword = "StaffChangePassword";
    public static String AddDeviceDetailStaff = "AddDeviceDetailStaff";
    public static String DeleteDeviceDetailStaff = "DeleteDeviceDetailStaff";
    public static String UpdateHWCW = "UpdateHWCW";
    public static String StaffProfile = "StaffProfile";
    public static String BirthdayWish = "BirthdayWish";
    public static ArrayList<ClassDetailModel> rows = new ArrayList<ClassDetailModel>();
    public static boolean firsttimeback;
    public static int position;

}
