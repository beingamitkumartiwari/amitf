package com.mtechno.freerechargeswipe.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by DEVEN SINGH on 4/27/2015.
 */
public class SharedPrefs {

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor spEditor;

    private final String DATABASE_NAME = "FreeRechargeSwipeDataDevBase";
    private final String MOBILE_NUMBER = "mobileNumber";
    private final String REGISTERED = "registered";
    private final String TIME = "time";
    private final String SERVICE_RUNNING = "serviceRunning";
    private final String ANOLOG_CLOCK = "anologClock";
    private final String POLICY_READ = "policyRead";
    private final String LOCKSCREEN_AD = "lockScreenAd";
    private final String FRS_BALANCE = "frsBalance";
    private final String AD_MOB_OFFER = "admobOffer";
    private final String WOOBI_ID = "woobiId";
    private final String SUPERSONIC_ID = "supersonicId";
    private final String AD_MOB_ID = "admobId";
    private final String AD_MOB_ID_VIDEO = "admobIdVideo";
    private final String ADBUDDIZ_ID = "adbuddizId";
    private final String STARTAPP_ID1 = "startappId1";
    private final String STARTAPP_ID2 = "startappId12";
    private final String NATIVEX_ID = "nativexId";
    private final String IN_MOBI_OFFER = "inMobiOffer";
    private final String CURRENT_TIME_STAMP = "currentTimeStamp";
    private final String AD_MOB_MONEY_VIDEO = "adMobMoneyvideo";
    private final String AD_MOB_MONEY = "adMobMoney";
    private final String IN_MOBI_MONEY = "inMobiMoney";
    private final String AD_MOB_FULLPAGE_MONEY = "adMobFullPageMoney";
    private final String AD_CLICKED = "adClicked";
    private final String AD_MOB_MONEY_VALUE = "adMobMoneyValue";
    private final String IN_MOBI_MONEY_VALUE = "inMobiMoneyValue";
    private final String USER_GCM_REG_ID = "gcmRegId";
    private final String GCM_REG_ID_SERVER = "gcmRegIdSavedInServer";
    private final String OLD_CREDITS = "oldCredits";
    private final String USER_AVATAR = "userAvatar";
    private final String USER_NAME = "userName";
    private final String USER_EMAIL = "userEmail";
    private final String USER_GENDER = "userGender";
    private final String USER_DOB = "userDob";
    private final String USER_UNIQUE_KEY = "userUniqueKey";
    private final String USER_INCOME = "userIncome";
    private final String USER_OCCUPATION = "userOccupation";
    private final String USER_M_S = "userMS";
    private final String USER_ID = "userId";
    private final String FRS_CREDITS = "frsCredit";
    private final String START_APP_MONEY = "startAppMoney";
    private final String ADBUDDIZ_FULSCREEN_MONEY = "adBuddizFullscreenMoney";
    private final String ADBUDDIZ_VIDEO_MONEY = "adBuddizVideoMoney";
    private final String SHOW_CAMPAIGN = "showCampaign";
    private final String CAMPAIGN_TYPE = "campaignType";
    private final String CAMPAIGN_IMGURL = "campaignImgurl";
    private final String CAMPAIGN_APPANAME = "campaignAppname";
    private final String CAMPAIGN_DESCRICPTION = "campaignDescription";
    private final String CAMPAIGN_PLAYSTORELINK = "campaignPlaystoreLink";
    private final String CAMPAIGN_PAYOUT = "campaignPayout";
    private final String CAMPAIGN_PACKAGENAME = "campaignPackagename";
    private final String MINIMOB_CONVERSSIONRATE = "minimobConversionRate";
    private final String SURVEY_AMOUNT = "surveyAmount";
    private final String SURVEY_SHOW = "surveyShow";
    private final String SURVEY_DISTRIBUTIONID = "surveyDistributioId";
    private final String SURVEY_APPID = "surveyAppId";
    private final String EXPLETUSOFFERNATIVEPACKAGE = "expletusOfferNativePackage";



   String surveyShow;
   String surveyDistributioId;
   String expletusOfferNativePackage;
    public SharedPrefs(Context context) {
        sharedPreferences = context.getSharedPreferences(DATABASE_NAME, Context.MODE_PRIVATE);
    }

    public String getMobileNumber() {
        return sharedPreferences.getString(MOBILE_NUMBER, "");
    }

    public void setMobileNumber(String mobileNumber) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(MOBILE_NUMBER, mobileNumber);
        spEditor.commit();
    }

    public boolean isRegistered() {
        return sharedPreferences.getBoolean(REGISTERED, false);
    }

    public void setRegistered(boolean registered) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(REGISTERED, registered);
        spEditor.commit();
    }

    public long getTime() {
        return sharedPreferences.getLong(TIME, 0);
    }

    public void setTime(long time) {
        spEditor = sharedPreferences.edit();
        spEditor.putLong(TIME, time);
        spEditor.commit();
    }

    public boolean isServiceRunning() {
        return sharedPreferences.getBoolean(SERVICE_RUNNING, true);
    }

    public void setServiceRunning(boolean serviceRunning) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(SERVICE_RUNNING, serviceRunning);
        spEditor.commit();
    }

    public boolean isAnologClock() {
        return sharedPreferences.getBoolean(ANOLOG_CLOCK, false);
    }

    public void setAnologClock(boolean anologClock) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(ANOLOG_CLOCK, anologClock);
        spEditor.commit();
    }

    public boolean isPolicyRead() {
        return sharedPreferences.getBoolean(POLICY_READ, false);
    }

    public void setPolicyRead(boolean policyRead) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(POLICY_READ, policyRead);
        spEditor.commit();
    }

    public int getLockScreenAd() {
        return sharedPreferences.getInt(LOCKSCREEN_AD, 1);
    }

    public void setLockScreenAd(int lockScreenAd) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(LOCKSCREEN_AD, lockScreenAd);
        spEditor.commit();
    }

    public float getFrsBalance() {
        return sharedPreferences.getFloat(FRS_BALANCE, 0);
    }

    public void setFrsBalance(float frsBalance) {
        spEditor = sharedPreferences.edit();
        spEditor.putFloat(FRS_BALANCE, frsBalance);
        spEditor.commit();
    }

    public int getInMobiOffer() {
        return sharedPreferences.getInt(IN_MOBI_OFFER, 1);
    }

    public void setInMobiOffer(int inMobiOffer) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(IN_MOBI_OFFER, inMobiOffer);
        spEditor.commit();
    }

    public int getAdmobOffer() {
        return sharedPreferences.getInt(AD_MOB_OFFER, 1);
    }

    public void setAdmobOffer(int admobOffer) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AD_MOB_OFFER, admobOffer);
        spEditor.commit();
    }

    public long getCurrentTimeStamp() {
        return sharedPreferences.getLong(CURRENT_TIME_STAMP, 0);
    }

    public void setCurrentTimeStamp(long currentTimeStamp) {
        spEditor = sharedPreferences.edit();
        spEditor.putLong(CURRENT_TIME_STAMP, currentTimeStamp);
        spEditor.commit();
    }

    public float getInMobiMoney() {
        return sharedPreferences.getFloat(IN_MOBI_MONEY, 0);
    }

    public void setInMobiMoney(float inMobiMoney) {
        spEditor = sharedPreferences.edit();
        spEditor.putFloat(IN_MOBI_MONEY, inMobiMoney);
        spEditor.commit();
    }



    public int getInMobiMoneyValue() {
        return sharedPreferences.getInt(IN_MOBI_MONEY_VALUE, 1);
    }

    public void setInMobiMoneyValue(int inMobiMoneyValue) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(IN_MOBI_MONEY_VALUE, inMobiMoneyValue);
        spEditor.commit();
    }

    public int getAdMobMoneyValue() {
        return sharedPreferences.getInt(AD_MOB_MONEY_VALUE, 3);
    }

    public void setAdMobMoneyValue(int adMobMoneyValue) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AD_MOB_MONEY_VALUE, adMobMoneyValue);
        spEditor.commit();
    }

    public int getAdMobFullPageMoney() {
        return sharedPreferences.getInt(AD_MOB_FULLPAGE_MONEY, 50);
    }

    public void setAdMobFullPageMoney(int adMobFullPageMoney) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AD_MOB_FULLPAGE_MONEY, adMobFullPageMoney);
        spEditor.commit();
    }

    public int getAdMobMoneyvideo() {
        return sharedPreferences.getInt(AD_MOB_MONEY_VIDEO, 70);
    }

    public void setAdMobMoneyvideo(int adMobMoneyvideo) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(AD_MOB_MONEY_VIDEO, adMobMoneyvideo);
        spEditor.commit();
    }

    public boolean isAdClicked() {
        return sharedPreferences.getBoolean(AD_CLICKED, false);
    }

    public void setAdClicked(boolean adClicked) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(AD_CLICKED, adClicked);
        spEditor.commit();
    }

    public String getGcmRegId() {
        return sharedPreferences.getString(USER_GCM_REG_ID, "");
    }

    public void setGcmRegId(String gcmRegId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_GCM_REG_ID, gcmRegId);
        spEditor.commit();
    }

    public boolean isGcmRegIdSavedInServer() {
        return sharedPreferences.getBoolean(GCM_REG_ID_SERVER, false);
    }

    public void setGcmRegIdSavedInServer(boolean gcmRegIdSavedInServer) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(GCM_REG_ID_SERVER, gcmRegIdSavedInServer);
        spEditor.commit();
    }

    public String getUserAvatar() {
        return sharedPreferences.getString(USER_AVATAR, "");
    }

    public void setUserAvatar(String userAvatar) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_AVATAR, userAvatar);
        spEditor.commit();
    }

    public String getUserName() {
        return sharedPreferences.getString(USER_NAME, "");
    }

    public void setUserName(String userName) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_NAME, userName);
        spEditor.commit();
    }

    public String getUserEmail() {
        return sharedPreferences.getString(USER_EMAIL, "");
    }

    public void setUserEmail(String userEmail) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_EMAIL, userEmail);
        spEditor.commit();
    }

    public String getUserDob() {
        return sharedPreferences.getString(USER_DOB, "Date Of Birth");
    }

    public void setUserDob(String userDob) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_DOB, userDob);
        spEditor.commit();
    }

    public String getUserGender() {
        return sharedPreferences.getString(USER_GENDER, "Gender");
    }

    public void setUserGender(String userGender) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_GENDER, userGender);
        spEditor.commit();
    }

    public String getUserIncome() {
        return sharedPreferences.getString(USER_INCOME, "Income");
    }

    public void setUserIncome(String userIncome) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_INCOME, userIncome);
        spEditor.commit();
    }

    public String getUserOccupation() {
        return sharedPreferences.getString(USER_OCCUPATION, "Occupation");
    }

    public void setUserOccupation(String userOccupation) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_OCCUPATION, userOccupation);
        spEditor.commit();
    }

    public String getUserMaritalStatus() {
        return sharedPreferences.getString(USER_M_S, "Marital Status");
    }

    public void setUserMaritalStatus(String userMaritalStatus) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_M_S, userMaritalStatus);
        spEditor.commit();
    }

    public String getUserKey() {
        return sharedPreferences.getString(USER_UNIQUE_KEY, "");
    }

    public void setUserKey(String userKey) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_UNIQUE_KEY, userKey);
        spEditor.commit();
    }

    public String getUserDev() {
        return sharedPreferences.getString(USER_ID, "");
    }

    public void setUserId(String userId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(USER_ID, userId);
        spEditor.commit();
    }

    public int getFrsCredits() {
        return sharedPreferences.getInt(FRS_CREDITS, 0);
    }

    public void setFrsCredits(int frsCredits) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(FRS_CREDITS, frsCredits);
        spEditor.commit();
    }

    public int getStartAppMoney() {
        return sharedPreferences.getInt(START_APP_MONEY, 0);
    }

    public void setStartAppMoney(int startAppMoney) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(START_APP_MONEY, startAppMoney);
        spEditor.commit();
    }

    public int getAdBuddizFullscreenMoney() {
        return sharedPreferences.getInt(ADBUDDIZ_FULSCREEN_MONEY, 50);
    }

    public void setAdBuddizFullscreenMoney(int adBuddizFullscreenMoney) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(ADBUDDIZ_FULSCREEN_MONEY, adBuddizFullscreenMoney);
        spEditor.commit();
    }

    public int getAdBuddizVideoMoney() {
        return sharedPreferences.getInt(ADBUDDIZ_VIDEO_MONEY, 50);
    }

    public void setAdBuddizVideoMoney(int adBuddizVideoMoney) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(ADBUDDIZ_VIDEO_MONEY, adBuddizVideoMoney);
        spEditor.commit();
    }

    public String getWoobiId() {
        return sharedPreferences.getString(WOOBI_ID, "17598");
    }

    public void setWoobiId(String woobiId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(WOOBI_ID, woobiId);
        spEditor.commit();
    }

    public String getSupersonicId() {
        return sharedPreferences.getString(SUPERSONIC_ID, "3b4b9911");
    }

    public void setSupersonicId(String supersonicId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SUPERSONIC_ID, supersonicId);
        spEditor.commit();
    }

    public String getAdmobId() {
        return sharedPreferences.getString(AD_MOB_ID, "ca-app-pub-8785790318965048/7735114012");
    }

    public void setAdmobId(String admobId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(AD_MOB_ID, admobId);
        spEditor.commit();
    }

    public String getAdmobIdVideo() {
        return sharedPreferences.getString(AD_MOB_ID_VIDEO, "ca-app-pub-8785790318965048/7735114012");
    }

    public void setAdmobIdVideo(String admobIdVideo) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(AD_MOB_ID_VIDEO, admobIdVideo);
        spEditor.commit();
    }

    public String getAdbuddizId() {
        return sharedPreferences.getString(ADBUDDIZ_ID, "36f07e62-be14-48d4-85e7-e1e9e9844ebc");
    }

    public void setAdbuddizId(String adbuddizId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(ADBUDDIZ_ID, adbuddizId);
        spEditor.commit();
    }

    public String getStartappId1() {
        return sharedPreferences.getString(STARTAPP_ID1, "203036330");
    }

    public void setStartappId1(String startappId1) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(STARTAPP_ID1, startappId1);
        spEditor.commit();
    }

    public String getStartappId2() {
        return sharedPreferences.getString(STARTAPP_ID2, "209862007");
    }

    public void setStartappId2(String startappId2) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(STARTAPP_ID2, startappId2);
        spEditor.commit();
    }

    public String getNativexId() {
        return sharedPreferences.getString(NATIVEX_ID, "35756");
    }

    public void setNativexId(String nativexId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(NATIVEX_ID, nativexId);
        spEditor.commit();
    }

    public String getCampaignImgurl() {
        return sharedPreferences.getString(CAMPAIGN_IMGURL, "novalue");
    }

    public void setCampaignImgurl(String campaignImgurl) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_IMGURL, campaignImgurl);
        spEditor.commit();
    }

    public String getCampaignAppname() {
        return sharedPreferences.getString(CAMPAIGN_APPANAME, "novalue");
    }

    public void setCampaignAppname(String campaignAppname) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_APPANAME, campaignAppname);
        spEditor.commit();
    }

    public String getCampaignDescription() {
        return sharedPreferences.getString(CAMPAIGN_DESCRICPTION, "novalue");
    }

    public void setCampaignDescription(String campaignDescription) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_DESCRICPTION, campaignDescription);
        spEditor.commit();
    }

    public String getCampaignPlaystoreLink() {
        return sharedPreferences.getString(CAMPAIGN_PLAYSTORELINK, "novalue");
    }

    public void setCampaignPlaystoreLink(String campaignPlaystoreLink) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_PLAYSTORELINK, campaignPlaystoreLink);
        spEditor.commit();
    }

    public String getCampaignPayout() {
        return sharedPreferences.getString(CAMPAIGN_PAYOUT, "novalue");
    }

    public void setCampaignPayout(String campaignPayout) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_PAYOUT, campaignPayout);
        spEditor.commit();
    }

    public String getCampaignPackagename() {
        return sharedPreferences.getString(CAMPAIGN_PACKAGENAME, "novalue");
    }

    public void setCampaignPackagename(String campaignPackagename) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_PACKAGENAME, campaignPackagename);
        spEditor.commit();
    }

    public boolean isShowCampaign() {
        return sharedPreferences.getBoolean(SHOW_CAMPAIGN, false);
    }

    public void setShowCampaign(boolean showCampaign) {
        spEditor = sharedPreferences.edit();
        spEditor.putBoolean(SHOW_CAMPAIGN, showCampaign);
        spEditor.commit();
    }

    public int getMinimobConversionRate() {
        return sharedPreferences.getInt(MINIMOB_CONVERSSIONRATE, 20);
    }

    public void setMinimobConversionRate(int minimobConversionRate) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(MINIMOB_CONVERSSIONRATE, minimobConversionRate);
        spEditor.commit();
    }

    public String getSurveyShow() {
        return sharedPreferences.getString(SURVEY_SHOW,"1");
    }

    public void setSurveyShow(String surveyShow) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SURVEY_SHOW, surveyShow);
        spEditor.commit();
    }

    public int getSurveyAmount() {
        return sharedPreferences.getInt(SURVEY_AMOUNT,500);
    }

    public void setSurveyAmount(int surveyAmount) {
        spEditor = sharedPreferences.edit();
        spEditor.putInt(SURVEY_AMOUNT, surveyAmount);
        spEditor.commit();
    }

    public String getSurveyDistributioId() {
        return sharedPreferences.getString(SURVEY_DISTRIBUTIONID,"55db14886ae151de6f77d674");
    }

    public void setSurveyDistributioId(String surveyDistributioId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SURVEY_DISTRIBUTIONID, surveyDistributioId);
        spEditor.commit();
    }

    public String getSurveyAppId() {
        return sharedPreferences.getString(SURVEY_APPID,"563895e4ad27817e310784e4");
    }

    public void setSurveyAppId(String surveyAppId) {
        spEditor = sharedPreferences.edit();
        spEditor.putString(SURVEY_APPID, surveyAppId);
        spEditor.commit();
    }

    public String getCampaignType() {
        return sharedPreferences.getString(CAMPAIGN_TYPE,"no value");
    }

    public void setCampaignType(String campaignType) {
       spEditor=sharedPreferences.edit();
        spEditor.putString(CAMPAIGN_TYPE,campaignType);
        spEditor.commit();
    }

    public String getExpletusOfferNativePackage() {
        return sharedPreferences.getString(EXPLETUSOFFERNATIVEPACKAGE,"");
    }

    public void setExpletusOfferNativePackage(String expletusOfferNativePackage) {
        spEditor=sharedPreferences.edit();
        spEditor.putString(EXPLETUSOFFERNATIVEPACKAGE,expletusOfferNativePackage);
        spEditor.commit();
    }
}
