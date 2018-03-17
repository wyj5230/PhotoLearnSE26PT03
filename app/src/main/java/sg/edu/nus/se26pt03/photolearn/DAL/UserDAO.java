package sg.edu.nus.se26pt03.photolearn.DAL;

import java.util.Date;

import sg.edu.nus.se26pt03.photolearn.utility.DateConversionHelper;

/**
 * Created by chen ping on 3/10/2018.
 * Restructured by MyatMin on 12/3/2018.
 */

public class UserDAO extends BaseDAO {
    private String LoginId;
    private String LoginSource;
    private Date LastLoginDate;
    private String Timestamp;

    public String getLoginId() {
        return LoginId;
    }

    public void setLoginId(String loginId) {
        LoginId = loginId;
    }

    public String getLoginSource() {
        return LoginSource;
    }

    public void setLoginSource(String loginSource) {
        LoginSource = loginSource;
    }

    public Date getLastLoginDate() {
        return LastLoginDate;
    }

    public void setLastLoginDate(Date lastLoginDate) {
        LastLoginDate = lastLoginDate;
    }

    public Date getTimestamp() {
        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        return dateConversionHelper.convertStringToDate(Timestamp);
    }

    public void setTimestamp(Date timestamp) {
        DateConversionHelper dateConversionHelper = new DateConversionHelper();
        Timestamp = dateConversionHelper.convertDateToString(timestamp);
    }
}
