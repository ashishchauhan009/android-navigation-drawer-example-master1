package net.simplifiedcoding.navigationdrawerexample.Constant;

/**
 * Created by vibes on 1/3/17.
 */

public class Constant {
    public static String TASK_ID;
    public static String check_internet_connection = "Check internet connection";
    public static String check_name = "Please enter name";
    public static String check_email = "Please enter email";
    public static String check_designation = "Please enter designation";
    public static final String MULTIPART_FILE_FORM_DATA = "multipart/form-data";


    //permissions
    public static class Fields {
        public static final int ACTION_CALL_REQUEST_CODE = 3;
        public static final int WRITE_FILE_PERMISSION = 3;
    }

    //    https://cleanmoney.in/beta-pwc-v/cleanmoneyapi.php?action=searchbydesignation&id=4&function=&level=&region=&page=1
    public static class WebUrl {

        /*CLEAN MONEY server base URL.*/
        public static final String BASE_URL = "https://cleanmoney.in/beta-pwc-v/";
        public static final String INDEX_URL = "cleanmoneyapi.php";

        /*API URL for Home Page.*/
        public static final String HOME_SCREEN = INDEX_URL + "?action=homepage";
        public static final String FAQ_PMGKY = INDEX_URL + "?action=faq_pmgky";
        public static final String FAQ_Everification = INDEX_URL + "?action=faqverification";
        public static final String VISION = INDEX_URL + "?action=vision";
        public static final String ABOUTPMGKY = INDEX_URL + "?action=pmgky";
        public static final String ABOUTVERFICATION = INDEX_URL + "?action=aboutverification";
        public static final String DOWNLOADS = INDEX_URL + "?action=downloads";
        public static final String IMPORTANTLINKS = INDEX_URL + "?action=importantlink";
        public static final String CIRCULARS = INDEX_URL + "?action=circular_services";
        public static final String FAQSRESOURCE = INDEX_URL + "?action=faqresource";
        public static final String TRAINING = INDEX_URL + "?action=training";
        public static final String CAMPAIGNS = INDEX_URL + "?action=campaign";
        public static final String CONTENTFAQ = INDEX_URL + "?action=faqcontent";
        public static final String HELPDESK = INDEX_URL + "?action=helpdesk";
        public static final String JOURNEYSOFAR = INDEX_URL + "?action=journey";
        public static final String LATESTNEWS = INDEX_URL + "?action=latestnews";
        public static final String BANNER = INDEX_URL + "?action=bannercampaign";

        public static final String OUT_REACH = INDEX_URL + "?action=outreachlist&id=1";

        //login api
        public static final String LOGINDONTHAVEPASSWORD = INDEX_URL + "?action=sendotp";
        public static final String LOGINWITHOTP = INDEX_URL + "?action=loginwithotp";
        public static final String UPDATEPASSWORD = INDEX_URL + "?action=updatepassword";
        public static final String LOGINITD = INDEX_URL + "?action=login";
        public static final String MIS = INDEX_URL + "?action=mis";

        //task managment
        public static final String USERLIST = INDEX_URL + "?action=userlist";
        public static final String ALLUSERLIST = INDEX_URL + "?action=alluserlist";

        public static final String MIS_LEVEL = INDEX_URL + "?action=level";
        public static final String CREATETASK = INDEX_URL + "?action=createtask";


        //New Task api
        public static final String NEW_TASK_LIST = INDEX_URL + "?action=tasklist";
        //Task Assigned api
        public static final String ASSIGNED_TASK_LIST = INDEX_URL + "?action=taskassignedforme";
        public static final String TASK_DETAIL = INDEX_URL + "?action=taskdetail";
        public static final String ACTION_TASK = INDEX_URL + "?action=actionontask";
        public static final String CLOSED_TASK = INDEX_URL + "?action=closetask";

        // vivek api
        public static final String SHOW_MEMBER = INDEX_URL + "?action=SHOWMEMBERS";
        // vivek api
        public static final String COMPLETETASK = INDEX_URL + "?action=completetask";
        public static final String SHOW_PRCCIT = INDEX_URL + "?action=SHOWPRCCIT";
        public static final String SHOW_CCIT = INDEX_URL + "?action=SHOWCCIT";
        public static final String SHOW_CIT = INDEX_URL + "?action=SHOWCIT";
        public static final String SHOW_MISREPORT = INDEX_URL + "?action=SHOWMISREPORT";
        public static final String APP_INFO = "https://cleanmoney.in/beta-pwc-v/app.json";
        //Dashboard
        public static final String DASHBOARD_URL = INDEX_URL + "?action=taskdashboard";

        public static final String CREATE_SUCCESS_STORIES = INDEX_URL + "?action=createsuccessstories";
        public static final String EDIT_SUCCESS_STORIES = INDEX_URL + "?action=editsuccessstories";
        public static final String SUCCESS_STORIES = INDEX_URL + "?action=successstorieslist";

        public static final String ADD_TO_FAV_URL = INDEX_URL + "?action=addtofavourite";
        public static final String MY_FAV_URL = INDEX_URL + "?action=myfavouritelist";
        public static final String REMOVE_FAV_URL = INDEX_URL + "?action=removefromfavourite";
        public static final String EDIT_OUTREACH = INDEX_URL + "?action=editoutreach";
        public static final String SHOW_MISDATE = INDEX_URL + "?action=allmisdate";

        //search by designation
        public static final String SEARCH_DESIGNATION = INDEX_URL + "?action=alllist";
        public static final String SUBMIT_DESIGNATION = INDEX_URL + "?action=searchbydesignation";

        //outreach
        public static final String OUTREACHSTATE = INDEX_URL + "?action=outreachpage";

        //engageandsupportlist
        public static final String ENGAGEANDSUPPORT = INDEX_URL + "?action=engageandsupportlist";
        public static final String ENGAGEFACEBOOK = INDEX_URL + "action=addengageandsupport";


        public static final String CREATE_OUTREACH = INDEX_URL + "?action=createoutreach";

        public static final String MESSAGE_CHAIN = INDEX_URL + "?action=messagechain";
        public static final String CONTRIBUTION = INDEX_URL + "?action=contributedashboard";
        public static final String CONTRIBUTION_SUBMIT = INDEX_URL + "?action=contributedashboard";
        public static final String DIALOG_TASK = INDEX_URL + "?action=SHOWELEMENTWISEMISREPORT";
        public static final String SUCCESS_STORY_DELETE = INDEX_URL + "?action=deletesuccessstories";
        public static final String OUT_REACH_DELETE = INDEX_URL + "?action=deleteoutreach";


    }


    public static class StatusCode {
        public static final int CLEANMONEY_CODE_MISSING_PARAMETER = 0;
        public static final int CLEANMONEY_CODE_SUCCESS = 1;
        public static final int CLEANMONEY_CODE_FAILURE = 2;
        public static final int CLEANMONEY_CODE_BLOCK = 3;
        public static final int CLEANMONEY_CODE_UNBLOCK = 4;
    }
}