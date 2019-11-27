/*
* Copyright (c) 2014 Yodlee, Inc. All Rights Reserved.
*
* This software is the confidential and proprietary information of Yodlee, Inc.
* Use is subject to license terms.
*/
package com.yodlee.ycc.dapi.webapp;

import java.util.Locale;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpSession;

public class CommonDefs {

	public static final String OFF = "0";

	public static final String ON = "1";

	public static final long TIME_MULTIPLER = 1000l;

	public static final String REQUEST = ServletRequest.class.getName();

	public static final String RESPONSE = ServletResponse.class.getName();

	public static final String BILL_PAY_ELIGIBLE="personalInfo.BILL_PAY_ELIGIBLE";

	public static final String BILL_PAY_ENROLLED="personalInfo.BILL_PAY_ENROLLED";

	public static final String EXTERNAL_TRANSFER_ELIGIBLE="personalInfo.EXTERNAL_TRANSFER_ELIGIBLE";

	public static final String EXTERNAL_TRANSFER_ENROLLED="personalInfo.EXTERNAL_TRANSFER_ENROLLED";

	public static final String CONTEXT = "context";

	public static final String CONTEXT_TYPE = "contextSource";

	public static final String DIRECT_CONTEXT = "directContext";

	public static final String AJAX_CONTEXT = "ajaxContext";

	public static final String ADVISORY_URL = "ADVISORY_URL";

	public static final String VERSION_INFO = "versionInfo";

	public static final String SSO_AUTHENTICATION_STATUS = "SSO_AUTHENTICATION_STATUS";

	public static final String COB_STRINGS_BUNDLE = "prop.apps.CobrandableStrings";

	public static final String BASE_PARAMS_BUNDLE = "prop.apps.base.BaseApplicationParams";

	public static final String COB_PARAMS_BUNDLE = "prop.apps.CobrandableParams";

	public static final String BASE_STRINGS_BUNDLE = "prop.apps.base.BaseApplicationStrings";

	public static final String TNC_REV = "feature.param.login.TNC_REV";

	public static final String PROFILE_LOCALE_NAME = "personalinfo.LOCALE";

	public static final String USER_TOKEN = "USER_TOKEN";

	public static final String UNCERT_USER_TOKEN = "UNCERT_USER_TOKEN";

	public static final String PWS_USER_TOKEN = "PWS_USER_TOKEN";

	public static final String SESSION = HttpSession.class.getName();

	public static final String USER_TOKEN_INVALID = "123321";

	public static final String ITEM_SUMMARY_DATA = "ITEM_SUMMARY_DATA";

	public static final String APPS_COMMON_NUMBERFORMATTER = "com.yodlee.apps.common.util.numberformatter";

	public static final String DEFAULT_ENCODE_SCHEME = "com.yodlee.apps.default_encode_scheme";

	public static final String DEFAULT_MIN_FRACTION_DIGITS = "DefaultMinFractionDigits";

	public static final String DEFAULT_CURRENCY = "feature.param.currency.default";
	
	public static final String DEFAULT_CURRENCY_FORMAT = "feature.param.currency.format.default";

	public static final String DEFAULT_BASE_CURRENCY = "USD";

	public static final String SHORT_DATE = "com.yodlee.apps.base.datetime.format.SHORT_DATE";

	public static final String NORMAL_DATE = "com.yodlee.apps.base.datetime.format.NORMAL_DATE";

	public static final String SHORTEST_DATE = "com.yodlee.apps.base.datetime.format.SHORTEST_DATE";
	
	public static final String SHORTEST_DATE1 = "com.yodlee.apps.base.datetime.format.SHORTEST_DATE1";

	public static final String CFS_DISPLAY_MANUAL__TRANSACTIONS_IN_CALENDAR = "feature.switch.CFS_DISPLAY_MANUAL__TRANSACTIONS_IN_CALENDAR";

	// Used to store itemSummariesWithoutData for for the user
	public static final String ITEM_SUMMARIES_WITHOUT_ITEM_DATA = "com.yodlee.core.autologin.dataservice.DataService.ItemSummariesWithoutItemData";

	public static final String ITEM_SUMMARIES_BILLPAY_LIST = "ItemSummaryBillpayList";

	public static final String ITEM_SUMMARIES_BILLPAY_MAP = "ItemSummaryBillpayMap";

	public static final String BILLPAY_ACCOUNT_CACHE_MAP = "BillpayAccountCacheMap";

	public static final String ACCESS_NOT_VERIFIED_ITEM_SUMMARIES_BILLPAY_MAP = "accessNotVerifiedItemSummaryBillPayMap";

	public static final String SITES_NOT_VERIFIED_WITH_ERRORS_BEANLIST = "sitesNotVerifiedWithErrorsBeanList";

	public static final String SITES_NOT_VERIFIED_WITHOUT_ERRORS_BEANLIST = "sitesNotVerifiedWithoutErrorsBeanList";

	public static final String SITES_NOT_VERIFIED_BEAN_MAP = "sitesNotVerifiedBeanMap";
	public static final String AUTO_REGISTRATION_INCOMPLETE_SITES = "autoRegIncompleteSites";

	public static final String BILLPAY_ACCOUNT_MAP = "BillpayAccountMap";

	public static final String PAYMENT_ACCOUNT_MAP = "PaymentAccountMap";

	public static final String PAYMENT_ACCOUNT_ARRAY = "PaymentAccountArray";
	
	public static final String SUSPENDED_ACCOUNT_ARRAY = "suspendedPaymentsAccounts";

	public static final String PAYMENT_ACCOUNTS_MAP = "PaymentAccountsMap";

	public static final String COB_VALID_CARD_TYPE_SET = "CobValidCardTypeSet";

	public static final String PAYEE_ACCOUNT = "PayeeAccount";

	public static final String CACHED_PAYEE_ACCOUNT = "CachedPayeeAccount";

	public static final String CACHED_PAYEE_ARRAY = "CachedPayeeArray";
	
	public static final String CACHED_USL_BY_CATEGORY_LIST = "USLByCategoryList";

	public static final String CACHED_PAYEE_MAP = "CachedPayeeMap";

	public static final String CACHED_PAYEE_ITEM_MAP = "CachedPayeeItemMap";

	public static final String CACHED_PAYEE_ITEMS = "CachedPayeeItems";
	
	public static final String DELETED_PAYEES_ARRAY = "DeletedPayeesArray";
	
	public static final String DELETED_PAYEES_LIST = "DeletedPayeesList";

	public static final String BILL_ACCOUNTS_ITEM_MAP = "BillAccountsItemMap";

	public static final String BILL_ACCOUNT_ITEM_ACCOUNT_MAP = "BillAccountItemAccountMap";

	public static final String PAYMENT_REQUESTS = "PaymentRequests";

	public static final String PAYMENT_REQUESTS_MAP = "PaymentRequestsMap";

	public static final String CACHED_PAYMENTS_MAP = "CachedPaymentsMap";

	public static final String CACHED_RECURRING_PAYMENTS_MAP = "CachedRecurringPaymentsMap";

	public static final String CACHED_AUTOPAY_SETUPS_MAP = "CachedAutopaySetupsMap";

	public static final String BILLS_OVERVIEW_DATA = "BillsOverviewData";

	public static final String FEDERAL_HOLIDAY_INFO = "DederalHolidayInfo";

	public static final String HOLIDAY_INFO_HOLIDAYS_MAP = "HolidayInfoHolidaysMap";

	public static final String BASE_CURRENCY_SYMBOL = "_BASE_CURRENCY_SYMBOL_";

	public static final String CURRENT_DATE = "_CURRENT_DATE_";

	public static final String RESPONSE_TYPE = "RESPONSE_TYPE";

	public static final String RESPONSE_TYPE_JSP = "JSP";

	public static final String RESPONSE_TYPE_XML = "XML";

	public static final String PRINTABLE = "PRINTABLE";
	
	public static final String CUSTOMIZE = "CUSTOMIZE";
	
	public static final String PROGRESSINDICATOR = "PROGRESSINDICATOR";

	public static final String PRINTABLE_NO = "no";

	public static final String PRINTABLE_YES = "yes";

	public static final String HELP = "HELP";

	public static final String SERVICE_REQUEST = "SERVICE_REQUEST";

	public static final String MESSAGES = "MESSAGES";

	public static final String PAGE_NAME = "PAGE_NAME";

	public static final String EMPTY_STR = "";

	public static final String AT_THE_RATE_OF = "@";

	public static final String CFS_MASK_ACCOUNT_NUMBER = "feature.switch.MASK_ACCOUNT_NUMBER";

	public static final String CFS_BILL_AUTOPAY = "feature.switch.CFS_BILL_AUTOPAY";

	public static final String CFS_ENABLE_SCHEDULED_PAYMENTS = "feature.switch.ENABLE_SCHEDULED_PAYMENTS";

	public static final String CFS_SHOW_SCHEDULED_PAYMENTS = "feature.switch.SHOW_SCHEDULED_PAYMENTS";

	public static final String CFS_ENABLE_CHECKDIRECT_PAYMENTS = "feature.switch.ENABLE_CHECKDIRECT_PAYMENTS";

	public static final String CFS_CARD_DIRECT_ONLY = "feature.switch.CFS_CARD_DIRECT_ONLY";
	
	public static final String MANAGE_ACCOUNTS = "TRUE";
	
	public static final String EXPEDITED_ACL_KEY = "BILLPAY_FEES_EXPEDITED";
	
	public static final String PAYTODAY_ENABLED_ACL_KEY = "BILLPAY_RISKMODEL_HELDAWAY_EXPEDITED";
	
	public static final String PAYITALL_ENABLED_ACL_KEY = "BILLPAY_PAYMENTRAIL_HELDAWAY_PAYANYONE";
	
	public static final String IS_STANDALONE_PAYTODAY = "isStandalonePayTodayEnv";
	
	public static final String EXPEDITED_PAYMENT_FEE="expeditedFee";
	
	public static final String PAYTODAY_EXPEDITED_PAYMENT_FEE="payTodayExpeditedFee";	

	public static final String SHOW_PROCESSINGPAYMENT_PAGE = "feature.switch.SHOW_INTERSTITIAL_WHILE_PROCESSINGPAYMENT";
	
	public static final String PAYMENT_REQUEST_STATUS_ID="4";
	
	public static final String EBILL_AGGREGATION = "aggregation";
		
	public static final String EBILL_ENROLLMENT = "enrollment";	
	
	public static final String NO_CHANNEL_TYPE_FOUND = "unknown";
	
	public static final String IS_EBILL_ENROLLMENT_ENABLED = "isEbillEnrollmentEnabled";
	
	public static final String HAS_PAYNOW_PAYMENT = "hasPayNowPayment";
	
	public static final String HAS_SCHEDULED_PAYMENT = "hasScheduledPayment";
	
	public static final String HAS_EXPEDITED_PAYMEMT = "hasExpeditedPayment";
	
	public static final String CONFIRM_SINGLE_PAYMENT_PAGE = "ConfirmSinglePayment";
	
	public static final String EDIT_SINGLE_PAYMENT = "editPayment";
	
	public static final String URL_PARAM_FROM = "from";
		
	public static final long TIME_OUT=20000;	

	public static final String SUCCESS= "success";
	
	public static final String ERROR = "error";
	
	public static final String ERROR_VIEW= "error-view";
	/**
	 * URL Encoder/Decoder Scheme
	 */
	public static final String URL_ENCODER_SCHEME = "UTF-8";

	public static final String APP_ID = "AppId";

	public static final String APP_NAME = "WEB_USER";

	public static final String USER_PERSONAL_INFO = "USER_PERSONAL_INFO";

	public static final String CFS_PASSWDRESET = "CFS_PASSWDRESET";

	public static final String CHALLENGE_URL = "ANSWER_CHALLENGE_QUESTIONS_URL";

	public static final String BASE_URL = "baseurl";

	public static final String INVITE_CLIENT_SIGNON = "INVITE_CLIENT_SIGNON";

	public static final String INVITE_SIGNON = "INVITE_SIGNON";

	public static final String ADVISOR_NAMESPACE_ID = "AdvisorNs";

	public static final String ADVISOR_TK = "AdvisorTk";

	public static final String ADVISOR_CLIENTCOBRAND = "com.yodlee.advisor.clientcobrand";

	public static final String ADVISOR_CLIENTCOBRAND_URL = "com.yodlee.advisor.clientcobrand.url";

	public static final String ADVISOR_CLIENTCOBRAND_APPID = "com.yodlee.advisor.clientcobrand.appid";

	public static final String ADVISOR_CLIENTCOBRAND_TNC = "com.yodlee.advisor.clientcobrand.tncversion";

	public static final String CFS_REFRESH_AT_LOGIN = "feature.switch.CFS_REFRESH_AT_LOGIN";

	public static final String CFS_REFRESH_ALL = "feature.switch.CFS_REFRESH_ALL";

	public static final String CFS_FINANCIAL_SNAPSHOT = "feature.switch.CFS_FINANCIAL_SNAPSHOT";

	public static final String CFS_ACCOUNT_ADD_ADDITIONAL = "feature.switch.CFS_ACCOUNT_ADD_ADDITIONAL";

	public static final String CFS_ACCOUNT_ADD_RELATED = "feature.switch.CFS_ACCOUNT_ADD_RELATED";

	public static final String CFS_PASSWD_RESET = "feature.switch.CFS_PASSWD_RESET";

	public static final String CFS_CHANGE_PASSWD = "feature.switch.CFS_CHANGE_PASSWD";

	public static final String CFS_CS_NOTIFY = "feature.switch.CFS_CS_NOTIFY";

	public static final String CFS_CS_SUGGEST_SITE = "feature.switch.CFS_CS_SUGGEST_SITE";

	public static final String CFS_CS_USE_INFO = "feature.switch.CFS_CS_USE_INFO";

	public static final String CFS_ACCOUNT_LOCK = "feature.switch.CFS_ACCOUNT_LOCK";

	public static final String CFS_ACTION_PULLDOWN = "feature.switch.CFS_ACTION_PULLDOWN";

	public static final String CFS_SHOW_PASSWORD = "feature.switch.CFS_SHOW_PASSWORD";

	public static final String CFS_SHOW_CUSTOM_CAT_ON_SITE_LIST = "feature.switch.CFS_SHOW_CUSTOM_CAT_ON_SITE_LIST";

	public static final String CFS_CURRENCY_DISPLAY = "feature.switch.CFS_CURRENCY_DISPLAY";

	public static final String CFS_TNC_UPDATE = "feature.switch.CFS_TNC_UPDATE";

	public static final String CFS_CASE_INSENSITIVE_PASSWORD = "feature.switch.CFS_CASE_INSENSITIVE_PASSWORD";

	public static final String CFS_ACCOUNT_INSURANCE = "feature.switch.CFS_ACCOUNT_INSURANCE";

	public static final String CFS_AUTO_LOGIN = "feature.switch.CFS_AUTO_LOGIN";

	public static final String CFS_AUTO_LOGIN_PROMPT_PASSWORD = "feature.switch.CFS_AUTO_LOGIN_PROMPT_PASSWORD";

	public static final String CFS_CUSTOM_SITE = "feature.switch.CFS_CUSTOM_SITE";

	public static final String CFS_ADVISOR_SHARE_SECURITY_QUESTION = "feature.switch.CFS_ADVISOR_SHARE_SECURITY_QUESTION";

	public static final String CFS_ADVISOR_SHARE = "feature.switch.CFS_ADVISOR_SHARE";

	public static final String CFS_ENABLE_PORTFOLIO_EXPANDED_VIEW = "feature.switch.CFS_ENABLE_PORTFOLIO_EXPANDED_VIEW";

	public static final String CFS_ENABLE_NETSCALER = "feature.switch.CFS_ENABLE_NETSCALER";

	public static final String CFS_ENABLE_MULTIPLE_ADD_ACCOUNT = "feature.switch.CFS_ENABLE_MULTIPLE_ADD_ACCOUNT";

	public static final String CFS_FAIL_REGISTRATION_ON_ALERT_FAILURE = "feature.switch.CFS_FAIL_REGISTRATION_ON_ALERT_FAILURE";

	public static final String CFS_ENABLE_ACCOUNTDETAILS_CROSSLINKING = "feature.switch.CFS_ENABLE_ACCOUNTDETAILS_CROSSLINKING";

	public static final String CFS_DEEPLINKCLIENT = "feature.switch.CFS_CLIENT_SIDE_DOWNLOAD";

	public static final String CFS_SERVICE_CANCELLATION = "feature.switch.CFS_SERVICE_CANCELLATION";

	public static final String CFS_BILL_PAYMENT = "feature.switch.CFS_BILL_PAYMENT";

	public static final String CFS_SUPPRESS_CONTINUE_ON_VERIFYING_PAGE = "feature.switch.CFS_SUPPRESS_CONTINUE_ON_VERIFYING_PAGE";

	public static final String CFS_SUPPRESS_REFRESH_LINKS_AND_ANIMATION = "feature.switch.CFS_SUPPRESS_REFRESH_LINKS_AND_ANIMATION";

	public static final String CFS_DISPLAY_PROMOTIONAL_PAGE = "feature.switch.CFS_DISPLAY_PROMOTIONAL_PAGE";

	public static final String CFS_DISPLAY_BROWSE_ALL_ACCOUNTS_LINK = "feature.switch.CFS_DISPLAY_BROWSE_ALL_ACCOUNTS_LINK";

	public static final String CFS_DISPLAY_SAVE_AND_CONTINUE_BUTTONS = "feature.switch.CFS_DISPLAY_SAVE_AND_CONTINUE_BUTTONS";

	public static final String CFS_UPDATE_USERPROFILE_DURING_AUTOREG = "feature.switch.CFS_UPDATE_USERPROFILE_DURING_AUTOREG";

	public static final String CFS_ENABLE_AUTO_LOGIN_LINK = "feature.switch.ENABLE_AUTO_LOGIN_LINK";

	public static final String CFS_ENABLE_CHART_LINK = "feature.switch.ENABLE_CHART_LINK";

	// *****This is added for checking of Advisor at (RAVE) by Santhosh*****
	public static final String CFS_SHARED_MODE = "feature.switch.CFS_SHARED_MODE";

	public static final String CFS_ADVISOR_MODE = "1";

	public static final String SSO_ADVISOR_MEMBER_ID = "CLIENTID";

	public static final String ADVISOR_PERMISSION_INFO = "PERMISSION_INFO";

	// *****END*****

	// BillDirect online bank payment switch
	public static final String CFS_ONLINE_BANK_PAYMENT = "feature.switch.CFS_ONLINE_BANK_PAYMENT";

	// Addaccount sort by container switch
	public static final String CFS_ADDACCOUNT_SORT_BY_CONTAINER = "feature.switch.CFS_ADDACCOUNT_SORT_BY_CONTAINER";

	public static final String CFS_ADDACCOUNT_POPULAR_ONLY = "feature.switch.CFS_ADDACCOUNT_POPULAR_ONLY";

	public static final String CFS_CUSTOM_ACCOUNT_ON_POPULAR = "feature.switch.CFS_CUSTOM_ACCOUNT_ON_POPULAR";

	public static final String CFS_ENABLE_ZIPCODE_FILTERING_FOR_PAYMENT_SITES = "feature.switch.CFS_ENABLE_ZIPCODE_FILTERING_FOR_PAYMENT_SITES";

	public static final String CFS_ENABLE_FEE_CHARGING_BILLS = "feature.switch.CFS_ENABLE_FEE_CHARGING_BILLS";

	// Content Service Extensio stuff
	public static final String CFS_CS_EXTN = "feature.switch.CFS_CS_EXTN";

	public static final String CS_EXTN_GROUP_ID = "com.yodlee.apps.base.csext.group";

	public static final String CS_EXTN_TARGET_KEY = "feature.param.CS_EXTN_PARTNER_TARGET_KEY";

	public static final String AUTO_LOGIN_OFF_VALUE = "0";

	public static final String CFS_DISPLAY_CREDENTIALS = "feature.switch.CFS_DISPLAY_CREDENTIALS";

	// AutoRegistration FeatureSwitches
	public static final String CFS_AUTO_REG_ENABLED = "feature.switch.CFS_AUTO_REG_ENABLED";

	public static final String CFS_AUTO_REG_GENERATE_USERNAME = "feature.switch.CFS_AUTO_REG_GENERATE_USERNAME";

	public static final String CFS_AUTO_REG_GENERATE_PASSWORD = "feature.switch.CFS_AUTO_REG_GENERATE_PASSWORD";

	public static final String LOCALE_DEFAULT = "feature.param.locale.default";

	public static final String LOCALE_SUPPORTED = "feature.param.locale.supported";

	public static final String PASSWORD_LCASE = "feature.param.PASSWORD_LCASE";

	public static final String RAL_SUPPRESSION_INTERVAL = "feature.param.RAL_SUPPRESSION_INTERVAL";

	public static final String FORGOT_PASSWORD_SUPPORT_EMAIL_ADDRESS = "feature.param.forgotpassword.SUPPORT_EMAIL_ADDRESS";

	public static final String FORGOT_PASSWORD_MIN_QUESTIONS = "feature.param.forgotpassword.MIN_QUESTIONS_REQUIRED";

	public static final String NUMBER_OF_LOGIN_FIELDS = "feature.param.customaccount.NUMBER_OF_LOGIN_FIELDS";

	public static final String NUMBER_OF_PASSWD_FIELDS = "feature.param.customaccount.NUMBER_OF_PASSWD_FIELDS";

	public static final String CUSTOMQ = "com.yodlee.core.db.passwordrecovery.CustomQ";

	public static final String CUSTOMA = "com.yodlee.core.db.passwordrecovery.CustomA";

	public static final String CUSTOMQ_DISPLAYSTRING = "passwordrecovery.question.custom.displaystring";

	public static final String USER_CONTEXT = "UserContext";

	public static final String NORMAL_USER_CONTEXT = "NormalUserContext";

	public static final String ADVISOR_CONTEXT = "AdvisorContext";

	public static final String COBRAND_CONTEXT = "CobrandContext";

	public static final String SESSION_TOKEN = "SESSIONTOKEN";
	
	public static final String YCC_OBO_SESSION_TOKEN = "YCC_OBO_SESSION_TOKEN";

	public static final String DATA_CONTEXT = "DataContext";

	public static final String USER_NAME = "UserName";

	public static final String OBO_USER_NAME_MEM_PREF = "OBO_USER_NAME";

	public static final String USER_ID = "userId";

	public static final String COMMON_ERROR_IMAGE_URL = "common.image.error.url";

	public static final String AUTO_CHECK_JAVASCRIPT = "AutoCheckJavascript";

	public static final String ACTION_NEXTPAGE_REGISTRATIONFINISH = "feature.param.registration.ACTION_NEXTPAGE_REGISTRATIONFINISH";

	public static final String NUM_OF_PERSONAL_INFO_COLS = "registration.user.registrationForm.NUM_OF_PERSONAL_INFO_COLS";

	public static final String TITLE_POSITION_DEFAULT = "common.display.TITLE_POSITION_DEFAULT";

	public static final String TITLE_POSITION_FOR_PERSONAL_INFO = "passwordreset.passwordResetForm.TITLE_POSITION_FOR_PERSONAL_INFO";

	public static final String PASSWORD_RESET_QUESTIONS_FIELD_TYPE = "passwordreset.passwordResetForm.PASSWORD_RESET_QUESTIONS_FIELD_TYPE";

	// Location IDs
	public static final long APP_EXT_IMPL_ITEM_TRAVERSAL_GETALLITEMS = 10000;

	public static final long APP_EXT_IMPL_ITEM_TRAVERSAL_TUSERCTX = 10001;

	public static final String PROFILE_FIELD_GROUP_NAME = "personalinfo";

	public static final String PROFILE_EMAIL_FORMAT_NAME = "EMAIL_FORMAT";

	public static final String SESSION_VARIABLE_REFRESH_ALL = "isRefreshAll";

	public static final String SESSION_VARIABLE_REFRESH_ALL_VALUE = "1";

	public static final String SESSION_VARIABLE_JUST_LOGGED_IN = "JustLoggedIn";

	public static final String SESSION_VARIABLE_EMAIL_FORMAT = "EMAIL_FORMAT";

	public static final String REQUEST_VARIABLE_REFRESH_ALL = "RefreshInProgress";

	public static final String REQUEST_VARIABLE_REFRESH_ALL_VALUE = "1";

	public static final String REQUEST_ATTRIBUTE_PAGE_RERENDERED = "pageRerendered";

	public static final String REQUEST_ATTRIBUTE_PAGE_RERENDERED_VALUE = "1";

	public static final String REQUEST_ATTRIBUTE_EMAIL_FIELD_LABEL = "emailLabel";

	public static final String REQUEST_ATTRIBUTE_PERSONAL_INFO_TEXT_MAP = "personalInfoTextMap";

	public static final String REQUEST_ATTRIBUTE_STEP_REGISTRATION = "registration";

	public static final String REQUEST_ATTRIBUTE_STEP_FIND = "find";

	public static final String REQUEST_ATTRIBUTE_STEP_CREDENTIAL = "credential";

	public static final String REQUEST_ATTRIBUTE_STEP_TOOLBAR = "toolbar";

	public static final String REQUEST_ATTRIBUTE_STEP_DEEPLINKCLIENT = "deeplinkclient";

	public static final String REQUEST_ATTRIBUTE_STEP_VERIFY_ACCOUNTS = "verifyAccounts";

	public static final String REQUEST_ATTRIBUTE_STEP_ADD_PAYMENT_ACCOUNT = "addPaymentAccount";

	public static final String REQUEST_ATTRIBUTE_STEP_ONLINE_BANK_PAYMENT_ACCOUNT = "addOnlineBankPaymentAccount";

	public static final String REQUEST_ATTRIBUTE_INCOMPLETE_CSINFOS = "IncompleteCSINFOS";

	public static final String REQUEST_ATTRIBUTE_INCOMPLETE_CSINFOS_SIZE_FLAG = "size_of_incomplete_csinfos";

	public static final String REQUEST_ATTRIBUTE_BROWSE_LINKS = "AccountBrowseLinks";

	public static final String REQUEST_ATTRIBUTE_CONTAINERS = "containers";

	public static final String REQUEST_ATTRIBUTE_CONTAINER_POPULAR_ACCOUNTS = "ContainerPopularAccounts";

	public static final String REQUEST_ATTRIBUTE_POPULAR_ACCOUNTS_ZIPCODE = "zipcode";

	public static final String REQUEST_ATTRIBUTE_POPULAR_ACCOUNTS_INVALID_ZIPCODE = "invalidZipcode";

	public static final String REQUEST_ATTRIBUTE_CONTAINER_ACCOUNTS = "ContainerAccounts";

	public static final String REQUEST_ATTRIBUTE_CONTAINER_JUMPTO = "JumptoLinks";

	public static final String REQUEST_ATTRIBUTE_CONTAINER_INCOMPLETE = "ContainerIncomplete";

	public static final String REQUEST_ATTRIBUTE_SEARCH_ACCOUNTS = "SearchAccounts";

	public static final String REQUEST_ATTRIBUTE_SEARCH_TOTAL = "SearchTotal";

	public static final String REQUEST_ATTRIBUTE_SEARCH_KEYWORDS = "SearchKeywords";

	public static final String REQUEST_ATTRIBUTE_SEARCH_DISPLAY_ALL = "AllSearchResults";

	public static final String REQUEST_ATTRIBUTE_SEARCH_DISPLAY_SIZE = "SearchDisplaySize";

	public static final String REQUEST_ATTRIBUTE_INCOMPLETE_CONTENT_SERVICE_IDS = "com.yodlee.apps.base.addaccount.container.incompleteContentServiceIds";

	public static final String REQUEST_ATTRIBUTE_FIRST_USER_EXPERIENCE = "FirstUserExperience";
	
	public static final String NUMBER_OF_UNREAD_MSG = "numberOfUnreadMsg";

	public static final int ITEM_NOT_REFRESHING = 0;

	public static final int STOP_REFRESH_ERROR_CODE = 405;

	public static final String REQUEST_VARIABLE_NO_ITEM_PRESENT = "noItemPresent";

	public static final String REQUEST_VARIABLE_NO_ITEM_PRESENT_VALUE = "true";

	public static final String REQUEST_VARIABLE_NON_CUSTOM_ITEM_PRESENT = "nonCustomItemPresent";

	public static final String REQUEST_VARIABLE_FINANCIAL_ITEM_PRESENT = "financialItemPresent";

	public static final String FEATURE_SWITCH_OFF = OFF;

	public static final String REQUEST_VARIABLE_RAL_OFF = "ralSupportedforCobrand";

	public static final String hiddenFieldPrefix = "hidden";

	public static final String REQUEST_ATTRIBUTE_FINANCIAL_ITEMLIST_JAVASCRIPT = "financialItemListArray";

	public static final String LAST_REFRESH_TIME_PREF = "LAST_REFRESH_TIME";

	public static final String FEATURE_SWICH_ON = ON;

	public static final String REQUEST_ATTRIBUTE_CUSTOM_CURRENCY = "ShowCurrency";

	public static final String WELCOME_MAIL_TEMPLATE = "welcome";

	public static final String TNC_FILE = "tnc";

	public static final String TNC_FILE_HTML_SUFFIX = "html";

	public static final String TNC_FILE_TEXT_SUFFIX = "text";

	public static final String WELCOME_MAIL_TEMPLATE_HTML_SUFFIX = "html";

	public static final String WELCOME_MAIL_TEMPLATE_TEXT_SUFFIX = "text";

	public static final String NUM_OF_PFILES_FOR_WELCOME_HTML_MAIL = "feature.param.welcomemail.html.NUMBER_OF_PFILES";

	public static final String NUM_OF_PFILES_FOR_WELCOME_TEXT_MAIL = "feature.param.welcomemail.text.NUMBER_OF_PFILES";

	public static final String WELCOME_PFILE_NAME_DELIMITER = ".";

	public static final String EMAIL_FORMAT_HTML = "0";

	public static final String EMAIL_FORMAT_TEXT = "1";

	public static final String EMAIL_FORMAT_ALERT_DEFAULT = "com.yodlee.apps.base.registration.email.format.default";

	public static final String WELCOME_MAIL_PRIVACY_TITLE_KEY_WITH_INSURANCE = "welcome.email.privacyTitleWithInsurance";

	public static final String WELCOME_MAIL_PRIVACY_TITLE_KEY_WITHOUT_INSURANCE = "welcome.email.privacyTitleWithoutInsurance";

	public static final String WELCOME_MAIL_INSURANCE_TEXT = "welcome.email.insuranceText";

	public static final String CSIT_FAILURE = "CSIT_FAILURE";

	public static final String PAGE_CSIT_VALIDATION = "PAGE_CSIT_VALIDATION";

	public static final String PAGE_CSIT_VALIDATION_ENABLED = "enabled";

	public static final String PAGE_CSIT_VALIDATION_DISABLED = "disabled";

	public static final String INPUT_TYPE_URL = "URL";

	public static final String INPUT_TYPE_TEXT = "TEXT";

	public static final String INPUT_TYPE_PASSWORD = "PASSWORD";

	/**
	 * The name of the request attribute which has the toolbar session based
	 * user context.
	 */
	public static final String TOOLBAR_USER_CONTEXT = "ToolbarUserContext";

	/**
	 * The name of the session attribute which has the toolbar source cobrand
	 * context.
	 */
	public static final String TOOLBAR_COBRAND_CONTEXT = "ToolbarCobrandContext";

	/**
	 * The Application GUID for the Oncenter Application
	 */
	public static final String ONCENTER_APP_GUID = "94AAC319-CF5E-4733-AA42-CA88C79C656D";

	/**
	 * The Application GUID for the Advisor Pro Application
	 */
	public static final String ADVISOR_APP_GUID = "0A6D76D6-15E2-44c0-8CA9-236E898F6996";

	/**
	 * The application GUID for the Toolbar Application.
	 */
	public static final String TOOLBAR_APP_GUID = "6121C98D-9E1F-48eb-A426-39B76C17B105";

	/**
	 * The key for the modules
	 */
	public static final String MODULE = "com.yodlee.apps.base.module_";

	// Module Views
	public static final String MODULE_VIEW_EXPANDED = "expanded";

	// Used to store cached preferences in request.
	public static final String USER_PREFS = "com.yodlee.apps.common.util.UserPreferenceCache";

	public static final String ITEM_PREFS = "com.yodlee.apps.common.util.ItemPreferenceCache";

	public static final String SUM_INFO_CACHE = "com.yodlee.apps.common.util.SumInfoCache";

	// Used to store cached autologin for item
	public static final String AUTO_LOGIN_TYPE_MAP = "com.yodlee.core.autologin.AutoLoginManagement.AutoLoginTypeMap";

	//Used to store SessionID in bps_payee Table
	public static final String SESSIONID = "personalInfo.SESSIONID";

	public static final String PAYMENT_ACCOUNT_NUMBER = "com.yodlee.userprofile.PAYMENT_ACCOUNT_NUMBER";

	public static final String MEMO = "memo";

	// Used to store cached login URL for a content service
	public static final String CONTENT_SERVICE_LOGIN_URL_MAP = "com.yodlee.core.autologin.AutoLoginManagement.LoginUrlForContentService";

	// Used to store login Forms for memItems for a user
	public static final String MEM_LOGIN_FORM_MAP = "com.yodlee.core.autologin.AutoLoginManagement.LoginFormsForMem";

	// Used to store cached login URL for a content service
	public static final String USER_INFO = "com.yodlee.common.UserContext.UserInfo";

	// used for Reporting_Logging

	public static final String REPORTS_USERLOGIN = "COMMON.REPORTS.EVENTS.USER_LOGIN";

	public static final String REPORTS_SIGNON = "COMMON.REPORTS.EVENTS.SIGNON";

	public static final String REPORTS_REGISTER = "COMMON.REPORTS.EVENTS.REGISTER";

	public static final String REPORTS_ADDACCOUNTPOPULAR = "COMMON.REPORTS.EVENTS.ADD_ACCOUNT_POPULAR";

	public static final String REPORTS_ADDACCOUNTBROWSE = "COMMON.REPORTS.EVENTS.ADD_ACCOUNT_BROWSE";

	public static final String REPORTS_ADDACCOUNTSEARCH = "COMMON.REPORTS.EVENTS.ADD_ACCOUNT_SEARCH";

	public static final String REPORTS_TNC = "COMMON.REPORTS.EVENTS.TNC";

	public static final String REPORTS_CLASSIFYACCOUNT = "COMMON.REPORTS.EVENTS.CLASSIFY_ACCOUNT";

	public static final String REPORTS_CREDENTIALINFO = "COMMON.REPORTS.EVENTS.CREDENTIAL_INFO";

	public static final String REPORTS_GETTOOLBAR = "COMMON.REPORTS.EVENTS.GET_TOOLBAR";

	public static final String REPORTS_CLASSIFIEDITEMADDED = "COMMON.REPORTS.EVENTS.CLASSIFIED_ITEM_ADDED";

	public static final String REPORTS_NOOFITEMSADDED = "COMMON.REPORTS.EVENTS.NO_OF_ITEMS_ADDED";

	public static final String REPORTS_NOOFITEMSWITHKNOWNPASSWORD = "COMMON.REPORTS.EVENTS.NO_OF_ITEMS_WITH_KNOWNPASSWORD";

	public static final String REPORTS_NOOFPARTIALITEMSADDED = "COMMON.REPORTS.EVENTS.NO_OF_PARTIAL_ITEMS_ADDED";

	public static final String REPORTS_LOGOUT = "COMMON.REPORTS.EVENTS.LOGOUT";

	public static final String REPORTS_DASHBOARDVIEW = "COMMON.REPORTS.EVENTS.DASHBOARD_VIEW";

	public static final String REPORTS_DASHBOARDPRINTVIEW = "COMMON.REPORTS.EVENTS.DASHBOARD_PRINTVIEW";

	public static final String REPORTS_ADDACCOUNTHOME = "COMMON.REPORTS.EVENTS.ADD_ACCOUNT_HOME";

	public static final String REPORTS_CUSTOMIZEPAGEVIEW = "COMMON.REPORTS.EVENTS.CUSTOMIZE_PAGE_VIEW";

	public static final String REPORTS_CHANGEPROFILE = "COMMON.REPORTS.EVENTS.CHANGE_PROFILE";

	public static final String REPORTS_CHANGEPASSWORD = "COMMON.REPORTS.EVENTS.CHANGE_PASSWORD";

	public static final String REPORTS_CHANGEQUESTIONS = "COMMON.REPORTS.EVENTS.CHANGE_QUESTIONS";

	public static final String REPORTS_ADDREMOVEMODULES = "COMMON.REPORTS.EVENTS.ADD_REMOVE_MODULES";

	public static final String REPORTS_CHANGELAYOUT = "COMMON.REPORTS.EVENTS.CHANGE_LAYOUT";

	public static final String REPORTS_CHANGELOCALE = "COMMON.REPORTS.EVENTS.CHANGE_LOCALE";

	public static final String REPORTS_AUTOLOGINSETTING = "COMMON.REPORTS.EVENTS.AUTO_LOGIN_SETTING";

	public static final String REPORTS_SHOWPASSWORD = "COMMON.REPORTS.EVENTS.SHOW_PASSWORD";

	public static final String REPORTS_MODULEEXPANDEDVIEW = "COMMON.REPORTS.EVENTS.MODULE_EXPANDED_VIEW";

	public static final String REPORTS_MODULEEXPANDCOLLAPSEARRORWVIEW = "COMMON.REPORTS.EVENTS.MODULE_EXPAND_COLLAPSE_ARRORW_VIEW";

	public static final String REPORTS_EDITMODULEVIEW = "COMMON.REPORTS.EVENTS.EDIT_MODULE_VIEW";

	public static final String REPORTS_MODULEHELP = "COMMON.REPORTS.EVENTS.MODULE_HELP";

	public static final String REPORTS_MODULECHART = "COMMON.REPORTS.EVENTS.MODULE_CHART";

	public static final String REPORTS_MODULESORT = "COMMON.REPORTS.EVENTS.MODULE_SORT";

	public static final String REPORTS_REFRESHALL = "COMMON.REPORTS.EVENTS.REFRESH_ALL";

	public static final String REPORTS_REFRESHMODULE = "COMMON.REPORTS.EVENTS.REFRESH_MODULE";

	public static final String REPORTS_REFRESHITEMS = "COMMON.REPORTS.EVENTS.REFRESH_ITEMS";

	public static final String REPORTS_REMOVEACCOUNTVIEW = "COMMON.REPORTS.EVENTS.REMOVE_ACCOUNT_VIEW";

	public static final String REPORTS_REMOVEACCOUNTBUTTON = "COMMON.REPORTS.EVENTS.REMOVE_ACCOUNT_BUTTON";

	public static final String REPORTS_MODULECHANGEVIEW = "COMMON.REPORTS.EVENTS.MODULE_CHANGE_VIEW";

	public static final String REPORTS_ACCOUNTDETAILS = "COMMON.REPORTS.EVENTS.ACCOUNT_DETAILS";

	public static final String REPORTS_USERLOGINLASTREFRESHTIME = "COMMON.REPORTS.EVENTS.USER_LOGIN_LAST_REFRESHTIME";

	public static final String REPORTS_LOGINREFRESHEDITEMS = "COMMON.REPORTS.EVENTS.LOGIN_REFRESHED_ITEMS";

	public static final String REPORTS_MOREINFO = "COMMON.REPORTS.EVENTS.MORE_INFO";

	public static final String REPORTS_REMOVEINCOMPLETEACCOUNT = "COMMON.REPORTS.EVENTS.REMOVE_INCOMPLETE_ACCOUNT";

	public static final String REPORTS_AUTOLOGIN = "COMMON.REPORTS.EVENTS.AUTO_LOGIN";

	public static final String REPORTS_SECURITYPOLICY = "COMMON.REPORTS.EVENTS.SECURITY_POLICY";

	public static final String REPORTS_PAGE_VIEW = "COMMON.REPORTS.EVENTS.PAGE_VIEW";

	public static final String REPORTS_APPLICATION_NAME = "common.reports.events.applicationName";

	public static final String REPORTS_SIGNON_LOG_MSG = "common.reports.events.signon.log_msg";

	public static final String REPORTS_REGISTER_LOG_MSG = "common.reports.events.register.log_msg";

	public static final String REPORTS_TNC_LOG_MSG = "common.reports.events.tnc.log_msg";

	public static final String REPORTS_USERLOGIN_LOG_MSG = "common.reports.events.userlogin.log_msg";

	public static final String REPORTS_ADDACCOUNTPOPULAR_LOG_MSG = "common.reports.events.addaccountpopular.log_msg";

	public static final String REPORTS_ADDACCOUNTBROWSE_LOG_MSG = "common.reports.events.addaccountbrowse.log_msg";

	public static final String REPORTS_ADDACCOUNTSEARCH_LOG_MSG = "common.reports.events.addaccountsearch.log_msg";

	public static final String REPORTS_CLASSIFYACCOUNT_LOG_MSG = "common.reports.events.classifyaccount.log_msg";

	public static final String REPORTS_CREDENTIALINFO_LOG_MSG = "common.reports.events.credentialinfo.log_msg";

	public static final String REPORTS_GETTOOLBAR_LOG_MSG = "common.reports.events.gettoolbar.log_msg";

	public static final String REPORTS_CLASSIFIEDITEMADDED_LOG_MSG = "common.reports.events.classifieditemadded.log_msg";

	public static final String REPORTS_NOOFITEMSADDED_LOG_MSG = "common.reports.events.noofitemsadded.log_msg";

	public static final String REPORTS_NOOFITEMSWITHKNOWNPASSWORD_LOG_MSG = "common.reports.events.noofitemswithknownpassword.log_msg";

	public static final String REPORTS_NOOFPARTIALITEMSADDED_LOG_MSG = "common.reports.events.noofpartialitemsadded.log_msg";

	public static final String REPORTS_LOGOUT_LOG_MSG = "common.reports.events.logout.log_msg";

	public static final String REPORTS_DASHBOARDVIEW_LOG_MSG = "common.reports.events.dashboardview.log_msg";

	public static final String REPORTS_DASHBOARDPRINTVIEW_LOG_MSG = "common.reports.events.dashboardprintview.log_msg";

	public static final String REPORTS_ADDACCOUNTHOME_LOG_MSG = "common.reports.events.addaccounthome.log_msg";

	public static final String REPORTS_CUSTOMIZEPAGEVIEW_LOG_MSG = "common.reports.events.customizepageview.log_msg";

	public static final String REPORTS_CHANGEPROFILE_LOG_MSG = "common.reports.events.changeprofile.log_msg";

	public static final String REPORTS_CHANGEPASSWORD_LOG_MSG = "common.reports.events.changepassword.log_msg";

	public static final String REPORTS_CHANGEQUESTIONS_LOG_MSG = "common.reports.events.changequestions.log_msg";

	public static final String REPORTS_ADDREMOVEMODULES_LOG_MSG = "common.reports.events.addremovemodules.log_msg";

	public static final String REPORTS_CHANGELAYOUT_LOG_MSG = "common.reports.events.changelayout.log_msg";

	public static final String REPORTS_CHANGELOCALE_LOG_MSG = "common.reports.events.changelocale.log_msg";

	public static final String REPORTS_AUTOLOGINSETTING_LOG_MSG = "common.reports.events.autologinsetting.log_msg";

	public static final String REPORTS_SHOWPASSWORD_LOG_MSG = "common.reports.events.showpassword.log_msg";

	public static final String REPORTS_SECURITYPOLICY_LOG_MSG = "common.reports.events.securitypolicy.log_msg";

	public static final String REPORTS_SESSION_TOKEN_KEY = "common.reports.events.session_token_key";

	public static final String REPORTS_APP_ID = "common.reports.events.app_id";

	public static final String REPORTS_LOGIN_NAME = "common.reports.events.login_name";

	public static final String REPORTS_MEM_ID = "common.reports.events.mem_id";

	public static final String REPORTS_GOTOURL = "COMMON.REPORTS.EVENTS.GOTO_URL";

	public static final String REPORTS_DEEPLINK = "COMMON.REPORTS.EVENTS.DEEPLINK";

	public static final String REPORTS_GOTOURL_WITH_CSID = "COMMON.REPORTS.EVENTS.GOTO_URL_WITH_CSID";

	public static final String REPORTS_INTERSTITIAL_PAGE = "COMMON.REPORTS.EVENTS.GOTO_INTERSTITIAL_PAGE";

	public static final String REPORTS_INTERSTITIAL_PAGE_WITH_CSID = "COMMON.REPORTS.EVENTS.GOTO_INTERSTITIAL_PAGE_WITH_CSID";

	public static final String REPORTS_PASSWORD_SAVER = "COMMON.REPORTS.EVENTS.PASSWORD_SAVER";

	public static final String REPORTS_LOGIN_HELPER_DOWNLOAD = "COMMON.REPORTS.EVENTS.LOGIN_HELPER_DOWNLOAD";

	public static final String REPORTS_PASSWORD_SAVER_STATE = "COMMON.REPORTS.EVENTS.PASSWORD_SAVER_STATE";

	// Add Popular Account
	public static final String POPULAR_ACCOUNT_COLUMN_SIZE = "com.yodlee.apps.base.addaccount.popular.columns";

	// On center Help
	public static final String HELP_LOCATIONTYPE = "com.yodlee.apps.help.locationtype";

	public static final String HELP_LOCATIONTYPE_DEFAULT = "0";

	public static final String HELP_LOCATIONTYPE_UPLOAD = "1";

	public static final String HELP_LOCATIONTYPE_REDIRECT = "2";

	public static final String HELP_MODULEID = "moduleId";

	public static final String HELP_SECTION = "section";

	public static final String HELP_BUNDLE = "pfile.apps.base.help";

	public static final String HELP_INDEX = "index";

	public static final String MODULE_FATAL_COMMON_ERROR = "common.errors.module.fatal.general";

	// Migrated user
	public static final String USER_MIGRATION = "com.yodlee.apps.migrated_user";

	public static final String LIST_OF_MIGRATED_ACCOUNTS = "listOfMigratedAccounts";

	public static final String ADDITIONAL_ACCOUNT_DISPLAY_MSG = "addaccount.additionalaccounts.display.msg";

	public static final String REGISTER = "register";

	public static final String ADD_ACCOUNT = "add_account";

	// Alert Deep Links
	public static final String REQUEST_ATTRIBUTE_DEEPLINK_URL = "deepLinkUrl";

	public static final String REQUEST_ATTRIBUTE_DEEPLINK_REDIRECT_URL = "deepLinkRedirectUrl";
	
	public static final String SAML_CALLBACK_URL = "samlCallbackUrl";

	public static final String HEADER_NON_CLICKABLE = "HeaderNonClickable";

	// To set the flag for removing the incomplete account
	public static final String DELETE_FLAG = "delete_flag_from_addaccount";

	public static final String LOGOUT_VISITED = "com.yodlee.apps.logout.LOGOUT_VISITED";

	public static final String PRINTVIEW_TRUNCATE_SWITCH = "com.yodlee.apps.base.print.truncate.feature.switch";

	public static final String PRINTVIEW_TRUNCATE_SIZE = "com.yodlee.apps.base.print.truncate.size";

	public static final String PRINTVIEW_TRUNCATE_INITIAL_LENGTH = "com.yodlee.apps.base.print.truncate.initiallength";

	public static final String PORTFOLIO_PRINTVIEW_TRUNCATE_SWITCH = "com.yodlee.apps.base.portfolio.print.truncate.feature.switch";

	public static final String PORTFOLIO_PRINTVIEW_TRUNCATE_SIZE = "com.yodlee.apps.base.portfolio.print.truncate.size";

	public static final String PORTFOLIO_PRINTVIEW_TRUNCATE_INITIAL_LENGTH = "com.yodlee.apps.base.portfolio.print.truncate.initiallength";

	public static final String EMAIL_PRINTVIEW_TRUNCATE_SWITCH = "com.yodlee.apps.base.email.truncate.feature.switch";

	public static final String EMAIL_PRINTVIEW_TRUNCATE_SIZE_ONE_COLUMN = "com.yodlee.apps.base.email.truncate.size.onecolumn";

	public static final String EMAIL_PRINTVIEW_TRUNCATE_SIZE_TWO_COLUMN = "com.yodlee.apps.base.email.truncate.size.twocolumn";

	public static final String EMAIL_PRINTVIEW_TRUNCATE_INITIAL_LENGTH = "com.yodlee.apps.base.email.truncate.initiallength";

	public static final String ADD_REMOVE_SUCCESS_MSG = "com.yodlee.apps.base.customize.Strings.changeContentSuccessMsg";

	public static final String USER_PREFERNCE_CSD_DO_NOT_PROMPT_AGAIN = "csd_download_do_not_prompt_again";

	public static final String SESSION_ATTRIBUTE_CSD_FROM_GOPAYBILL = "csd_from_gopaybill";

	public static final String SESSION_ATTRIBUTE_ALERTDEEPLINK_GOPAYBILL = "ALERTDEEPLINK_GOPAYBILL";

	public static final String SESSION_ATTRIBUTE_ALERTDEEPLINK_GOFUNDTRANSFER = "ALERTDEEPLINK_GOFUNDTRANSFER";

	public static final String SESSION_ATTRIBUTE_ADD_ACCOUNT_PROCESS_COMPLETE = "ADD_ACCOUNT_PROCESS_COMPLETE";

	public static final String SESSION_ATTRIBUTE_PREPOPULATE_ALERTS_FROM_SAML_RESPONSE = "PREPOPULATE_ALERTS_FROM_SAML_RESPONSE";

	public static final String SESSION_ATTRIBUTE_NETWORTH_REPORT_DATA = "NETWORTH_REPORT_DATA";

	public static final String REQUEST_ATTRIBUTE_CSD_DETECTED = "CSD_DETECTED";

	public static final String REQUEST_ATTRIBUTE_CSD_DETECT_REDIRECT_URL = "detectDeepLinkClientRedirectUrl";

	public static final String USER_PREF_CSD_ADD_NEW_PAYEES_ENABLED = "csd_prompt_add_new_payees";

	public static final String PORTFOLIOENABLED = "portfolioEnabled";

	// Added By K.Loganathan on 13-5-2004 For SSO Event Notification
	public static final String SSO_ENABLED = "feature.switch.CFS_SSO";

	public static final String SSO_SAML_ENABLED = "feature.switch.CFS_SSO_SAML";

	public static final String SSO_EVENT_NOTIFICATION = "feature.param.CFS_SSO_EVENT_NOTIFICATION";

	public static final String FROM_DEEPLINK_URL = "FROM_DEEPLINK_URL";

	// Constants used in inbound deeplinking
	public static final String WEB_HISTORY = "WEB_HISTORY";

	public static final String IDL_ENABLED = "IDL_ENABLED";

	public static final String IDL_RESOLVED = "IDL_RESOLVED";

	public static final String DYNAMIC_HOMEPAGE_URL = "DYNAMIC_HOMEPAGE_URL";

	public static final String DYNAMIC_HOMEPAGE_NAME = "DYNAMIC_HOMEPAGE_NAME";

	public static final String DHP_PAGE_ID = "DHP_PAGE_ID";

	public static final String IS_DYNAMIC_HOMEPAGE = "IS_DYNAMIC_HOMEPAGE";

	public static final String CUSTOMER_REFERRAL_URL = "CUSTOMER_REFERRAL_URL";

	public static final String URL_REFERER = "Referer";

	public static final String IDL_TYPE = "IDL_TYPE";

	public static final int IDL_TYPE_SINGLE_PAGE = 1;

	public static final int IDL_TYPE_SINGLE_PAGE_PROCESS = 2;

	public static final int IDL_TYPE_MULTI_PAGE_PROCESS = 3;

	// added for portfolio & expense manager (investment container)
	public static final String CFS_ENABLE_INVESTMENTS_IN_PORTFOLIO = "feature.switch.CFS_ENABLE_INVESTMENTS_IN_PORTFOLIO";

	public static final String CFS_ENABLE_INVESTMENTS_IN_TRANSACTIONS = "feature.switch.CFS_ENABLE_INVESTMENTS_IN_TRANSACTIONS";

	public static final String MEDIUM_DATE_MEDIUM_TIME = "com.yodlee.apps.base.datetime.format.MEDIUM_DATE_MEDIUM_TIME";

	public static final String SHORT_DATE_SHORT_TIME = "com.yodlee.apps.base.datetime.format.SHORT_DATE_SHORT_TIME";

	public static final String MEDIUM_DATE_SHORT_TIME = "com.yodlee.apps.base.datetime.format.MEDIUM_DATE_SHORT_TIME";

	public static final String MEDIUM_DATE_MEDIUM_TIME_ZONE = "com.yodlee.apps.base.datetime.format.MEDIUM_DATE_MEDIUM_TIME_ZONE";

	public static final String MEDIUM_DATE_SHORT_TIME_ZONE = "com.yodlee.apps.base.datetime.format.MEDIUM_DATE_SHORT_TIME_ZONE";

	public static final String TIME_FORMAT = "com.yodlee.apps.base.time.format";

	// switch to enable & disable the International date format
	public static final String CFS_ENABLE_INTERNATIONAL_DATE_FORMAT = "feature.switch.CFS_ENABLE_INTERNATIONAL_DATE_FORMAT";

	public static final String CFS_ENABLE_CURRENCY_CONVERSION = "feature.switch.CFS_ENABLE_CURRENCY_CONVERSION";

	// Added to suppress AutoLogin feature
	public static final String CFS_SUPPRESS_AUTOLOGIN_LINK = "feature.switch.CFS_SUPPRESS_AUTOLOGIN_LINK";

	// Constants related to Goto Interstitial Page feature
	public static final String CFS_ENABLE_GIP = "feature.switch.CFS_ENABLE_GIP";

	public static final String CFS_ENABLE_GIP_TIME_DELAY = "feature.switch.CFS_ENABLE_GIP_TIME_DELAY";

	public static final String GIP_TIME_DELAY = "com.yodlee.apps.oncenter.gip.time_delay";

	public static final String CFS_ENABLE_GIP_USER_SUPPRESS_OPTION = "feature.switch.CFS_ENABLE_GIP_USER_SUPPRESS_OPTION";

	public static final String HIDE_GIP_USER_SUPPRESS_OPTION = "HIDE_GIP_USER_SUPPRESS_OPTION";

	public static final String SUPPRESS_GIP = "SUPPRESS_GIP";

	// Added for Advisor Alert Refactoring 02/17/05
	public static final String _TEXT = "_text";

	public static final String _SELECT = "_select";

	public static final String ADVISOR_ALERT_PARAM_BUNDLE = "com.yodlee.apps.base.advisor.alertsetup";

	public static final String ADVISOR_ALERT_STRING_BUNDLE = "com.yodlee.apps.base.advisor.alertsetup.alert";

	public static final String ALERT_APP = "Application only";

	public static final String ALERT_EMAIL = "Email only";

	public static final String ALERT_APP_AND_EMAIL = "Application and Email";

	public static final String ALERT_DELIVERY_PREFERENCE = "alertDeliveryPreference";

	// Switch for UAR messages
	public static final String CFS_UAR_MESSAGES = "feature.switch.CFS_UAR_MESSAGES";

	public static final String UAR_ADDACCOUNT_REFRESH_SUPPRESS_MESSAGES_FOR_DAYS = "com.yodlee.apps.base.errorMessages.addaccountRefresh.Params.suppressMessagesForDays";

	public static final String UAR_CACHE_REFRESH_SUPPRESS_MESSAGES_FOR_DAYS_UAR = "com.yodlee.apps.base.errorMessages.cacheRefresh.Params.suppressMessagesForDays.UAR";

	public static final String UAR_CACHE_REFRESH_SUPPRESS_MESSAGES_FOR_DAYS_NON_UAR = "com.yodlee.apps.base.errorMessages.cacheRefresh.Params.suppressMessagesForDays.nonUAR";

	public static final String CFS_DISPLAY_PAYMENT_ACCOUNT = "feature.switch.CFS_DISPLAY_PAYMENT_ACCOUNT";

	public static final String CFS_DISPLAY_CVV2_INPUT_FIELD = "feature.switch.CFS_DISPLAY_CVV2_INPUT_FIELD";

	// add for advisor add account process
	public static final String CFS_ADVISOR_ADD_CLIENT_ACCOUNT = "feature.switch.CFS_ADVISOR_ADD_CLIENT_ACCOUNT";

	public static final String CFS_ADVISOR_IFRAMES = "feature.switch.CFS_ADVISOR_IFRAMES";

	public static final String CFS_SCHEDULE_SEARCH = "feature.switch.CFS_SCHEDULE_SEARCH";

	// For reporting in login helper/password saver
	public static final String ITEM_PREF_PS_ADDED = "ADDED_THROUGH_PASSWORD_SAVER";

	public static final String USER_PREF_DOWNLOADED_LOGIN_HELPER = "DOWNLOADED_LOGIN_HELPER";

	public static final String USER_PREF_PASSWORD_SAVER_ACTIVATED = "PASSWORD_SAVER_ACTIVATED";

	public static final String ACCOUNT_HEADER_TITLE = "_ACCOUNT_HEADER_TITLE_";

	public static final String PORTFOLIO_HEADER_TITLE = "_PORTFOLIO_HEADER_TITLE_";

	public static final String REQUEST_ATTRIBUTE_SNIPPETS_ENABLED = "SNIPPETS_ENABLED";

	// Parameters for Networth Change
	public static final String CFS_NETWORTH_CHANGE = "feature.switch.CFS_NETWORTH_CHANGE";

	public static final String NW_CHANGE_DEFAULT_PRESENCE = "1";

	public static final String NW_PRESENCE_VALUE = "1";

	// patterns for view all transactions, networth change, portfolio change and charting
	public static final String COMMON_DATE_PATTERN = "com.yodlee.app.common.date_pattern";

	public static final String DEFAULT_COMMON_DATE_PATTERN = "MM/dd/yyyy";

	public static final String COMMON_DECIMAL_PATTERN = "com.yodlee.app.common.decimal_pattern";

	public static final String DEFAULT_COMMON_DECIMAL_PATTERN = "#,##0.00";

	public static final String COMMON_PERCENT_PATTERN = "com.yodlee.app.common.percent_pattern";

	public static final String DEFAULT_COMMON_PERCENT_PATTERN = "#,##0.00%";

	public static final String SHOW_ADD_CUSTOM_ACCOUNT = "ShowAddCustomAccount";

	public static final String SHOW_ADD_ONLINE_BANK_PAYMENT_ACCOUNT = "ShowAddOnlineBankPaymentAccount";

	// tnc and coppa default values
	public static final String ACCEPT_TNC_VALUE = "com.yodlee.apps.base.registration.tnc.accept";

	public static final String DEFAULT_ACCEPT_TNC_VALUE = "1";

	public static final String COPPA_VALUE = "com.yodlee.apps.base.registration.coppa";

	public static final String DEFAULT_COPPA_VALUE = "1";

	// COPPA feature switch
	public static final String CFS_COPPA = "feature.switch.CFS_COPPA";

	public static final String DEFAULT_COPPA_PRESENCE = "1";

	public static final String REQUEST_ERROR_MSG = "ErrorMsg";

	// on behalf of access
	public static final String ON_BEHALF_OF_SESSION = "ON_BEHALF_OF_SESSION";

	public static final String CFS_SUPPRESS_OBO_AUTOLOGIN = "feature.switch.CFS_SUPPRESS_OBO_AUTOLOGIN";

	public static final String CFS_SUPPRESS_OBO_PASSWORD = "feature.switch.CFS_SUPPRESS_OBO_PASSWORD";

	public static final String CFS_SUPPRESS_OBO_ONE_TIME_PAYMENTS = "feature.switch.CFS_SUPPRESS_OBO_ONE_TIME_PAYMENTS";

	public static final String CFS_SUPPRESS_OBO_AUTOPAY = "feature.switch.CFS_SUPPRESS_OBO_AUTOPAY";

	public static final String CFS_DISPLAY_POPULAR_ACCT_RELATED_CONTAINER = "feature.switch.CFS_DISPLAY_POPULAR_ACCT_RELATED_CONTAINER";

	/**
	 *
	 */
	public static final String PROP_KEY_SEPARATOR = ".";

	public static final String CONTAINERS = ".containers";

	public static final String APP_CONFIG = "com.yodlee.apps.config";

	public static final String MENU_CONFIG = "com.yodlee.apps.menu.config";

	public static final String HELP_CONFIG = "com.yodlee.apps.help.config";

	public static final String SIDEBAR_HELP_CONFIG = "com.yodlee.apps.sidebarhelp.config";

	public static final String APPLICATION_TYPES = "com.yodlee.apps.applicationTypes";

	public static final String COMPONENT_NAME = "com.yodlee.apps.application.name";
	
	public static final String USER_GROUPS = "com.yodlee.apps.userGroups";

	public static final String APPLICATION_NAME = "common.strings.cobrand.name";

	public static final String USER_HOME_PAGE = "com.yodlee.user.home.urlKey";

	public static final String APPLICATION_EXTENSION = "applicationExtension";

	public static final String TIMEOUT_INFO = "timeoutInfo";

	public static final String LOGOUT = "LOGOUT";
	
	public static final String LOGOUT_CHECK = "LOGOUT_CHECK";
	
	public static final String USER_DND_KEY = "logoutPrePopAccFlag";
	
	public static final String LOGOUT_EXIT ="LOGOUT_EXIT";

	public static final String FORUM = "FORUM";

	public static final String ERROR_CODE = "ERROR_CODE";

	public static final String CSIT = "feature.switch.CFS_CSIT";

	public static final String URL_TOKEN_PARAMETER = "com.yodlee.apps.urlToken.parameter";

	public static final String URL_TOKEN_LIST_PARAMETER = "com.yodlee.apps.urlToken.list.parameter";

	public static final String USE_SECURE_COOKIES = "com.yodlee.apps.cookies.secure";

	public static final String SCRIPTABLE = "SCRIPTABLE";

	public static final String USER_PREF_BILLPAY_DEFAULT_PAYMENT_ACCOUNT = "billpay.defaultPaymentAccount";

	public static final String MEMO_KEYWORDS_ENABLED = "com.yodlee.apps.memo.keywords.enabled";

	public static final String SKIP_CHILD_VIEWS = "SKIP_CHILD_VIEWS";

	public static final String CFS_DISPLAY_INCLUDE_IN_NETWORTH_CALCULATION = "feature.switch.CFS_DISPLAY_INCLUDE_IN_NETWORTH_CALCULATION";

	public static final String CFS_DISPLAY_CLASSIFICATIONS = "feature.switch.CFS_DISPLAY_CLASSIFICATIONS";

	public static final String CFS_DISABLE_YODLEE_RECURRING = "CFS_DISABLE_YODLEE_RECURRING";

	/*
	 * This feature switch is used for enabling Sorting and Suppression of Card
	 * Numbers
	 */
	public static final String CFS_ENABLE_SORTING_CARD_NUMBER = "feature.switch.CFS_SORT_CARD_NUMBER";
	
	/* This switch is used for user preferred paym account */
	public static final String USER_PREFERRED_PAYM_ACCOUNT_ENABLED="com.yodlee.apps.billPay.userPreferredPaymAccountEnabled";	

	public static final String USER_PREF_BILLPAY_PERSONALINFO_CARD_ORDER = "personalInfo.CARD_ORDER";

	public static final String CD = "CD";

	public static final String TARGET_URL_CONFIG = "TARGET_URL_CONFIG";
	
	public static final String BILLPAY_IDVERIFICATION_REQ = "billPayIDVRequired";

	/* This feature switch is used for Suppression of Budgeting Alerts */
	public static final String BUDGET_REPORT_STATUS_ALERT = "com.yodlee.apps.budgeting.suppress.BudgetReportStatusAlerts";

	public static final String BUDGET_THRESHOLD_ALERT = "com.yodlee.apps.budgeting.suppress.ThresholdAlerts";

	/* To know which step the user should be taken when user logs in again */
	public static final String BPAA_USED = "1";

	public static final String BPAA_NOT_USED = "0";

	public static final String BPAA_FLOW_STEP1 = "step1";

	public static final String BPAA_FLOW_STEP4 = "step4";

	public static final String BPAA_FLOW_STEP6 = "step6";

	public static final String BPAA_ACCOUNT_TYPE = "com.yodlee.app.bpaa.source";

	public static final String ALL_CURRENCIES = "com.yodlee.apps.base.common.currencies";

	public static final String CURRENCY_EXCHANGE = "com.yodlee.apps.base.common.currenyExchange";

	public static final String SITE_NICKNAME = "site_nickname";

	public static final String MEM_PREF_KEY_USER_LOCALE = "COM.YODLEE.USER.LOCALE";

	public static final String PARAM_KEY_SUPPORTED_LOCALES = "COM.YODLEE.COBRAND.LOCALE.SUPPORTED";

	public static final String REQUEST_ATTRIBUTE_LOCALES = "Locales";

	public static final String PARAM_KEY_DEFAULT_COBRAND_LOCALE = "COM.YODLEE.COBRAND.LOCALE.DEFAULT";

	public static final Locale DEFAULT_COBRAND_LOCALE = Locale.US;

	public static final String ATTRIBUTE_LOCALE = "Locale";

	public static final String REQUEST_ATTRIBUTE_LOCALE = "Locale";
	
	public static final String REQUEST_ATTRIBUTE_USER_LOCALE = "UserLocale";

	public static final String ATTRIBUTE_TNC_TARGET_URL = "tncTargetURL";

	public static final String ATTRIBUTE_EMAIL_CONFIRMATION_CODE = "emailConfirmationCode";

	public static final String ATTRIBUTE_URL_KEY = "urlKey";

	public static final String ATTRIBUTE_URL_PARAM_NAME_LIST = "urlParamNameList";

	public static final String ATTRIBUTE_URL_PARAM_VALUE_LIST = "urlParamValueList";

	public static final String ATTRIBUTE_SHARE_ACCOUNT_ID = "shareAccountId";

	public static final String ATTRIBUTE_REQUEST_ID="requestId";

	public static final String ATTRIBUTE_DEEPLINKING_REDIRECT_URL = "redirectUrl";
	
	public static final String LOGINURL = "loginUrl";

	public static final String PARAM_KEY_LOGIN_URL = "com.yodlee.user.login.urlKey";

	public static final String SHOW_TRANSACTION_SIDEBAR_SEARCH="com.yodlee.apps.transaction.show.sideBarSearch";
	
	public static final String SELECT_STATE ="Select state";
	
	public static final String FIRST_LIST_VALUE = "firstList";
	
	public static final String BOLD_LETTER="boldLetter";

//  phone number

	public static final String PHONE1 = "com.yodlee.userprofile.DAYTIME_PHONE_1";

	public static final String PHONE2 = "com.yodlee.userprofile.DAYTIME_PHONE_2";

	public static final String PHONE3 = "com.yodlee.userprofile.DAYTIME_PHONE_3";

	//Evening Phone number
    public static final String EPHONE1 = "com.yodlee.userprofile.EVENING_PHONE_1";

	public static final String EPHONE2 = "com.yodlee.userprofile.EVENING_PHONE_2";

	public static final String EPHONE3 = "com.yodlee.userprofile.EVENING_PHONE_3";

//  Email
	public static final String EMAIL = "com.yodlee.userprofile.EMAIL_ADDRESS";

//	TNC
	public static final String TNC_PARAM = "com.yodlee.userprofile.BPAA.TNC_VIEW";

	public static final String TNC_CAPONE = "tnc_capone";

	public static final String CFS_CHECK_REFRESHING_ITEMS_ATLOGIN="feature.switch.CFS_CHECK_REFRESHING_ITEMS_ATLOGIN";

	public static final String ENTITLMENTS_AND = "AND";

	public static final String ENTITLMENTS_OR = "OR";

	public static final String FILTER_ID_ALL_SHARED_ACCOUNTS = "-5";

	public static final String FILTER_ID_MY_ACCOUNTS = "-6";

	public static final String FILTER_ID_ALL_ACCOUNTS = "-1";

	public static final String ITEM_SUMMARIES_INCLUDE_DELETED_ACCOUNTS = "itemSummariesWithDeltedAccounts";

	public static final String ITEM_SUMMARIES_EXCLUDE_DELETED_ACCOUNTS = "itemSummariesWithOutDeltedAccounts";

	public static final String FORGOT_YODLEE_ID = "ForgotYodleeID";
    
    public static final String ALL_ACCOUNTS_GROUP_ID = "-1"; 
    
    public static final String URL_TOKEN_VALIDATION_FAILURE = "urlTokenValidationFailure";
    
    public static final String MENU_NAME = "MENU_NAME";
    
    public static final String I18N_PARAM_KEY = "COM.YODLEE.CORE.INTERNATIONALIZATION_ENABLED";
    // For I18N Bean
    public static final String I18N_BEAN = "I18N_BEAN";
    
    // User preference for I18N
    public static final String PREFERRED_CURRENCY = "PREFERRED_CURRENCY";
    
    public static final String PREFERRED_DATE_FORMAT = "PREFERRED_DATE_FORMAT";

    public static final String PREFERRED_CURRENCY_FORMAT  = "PREFERRED_CURRENCY_FORMAT";
    
    public static final String PREFERRED_NUMBER_DECIMAL_SEPARATOR  = "PREFERRED_NUMBER_DECIMAL_SEPARATOR";

    public static final String PREFERRED_NUMBER_FORMAT  = "PREFERRED_NUMBER_FORMAT";
    
    public static final String PREFERRED_NUMBER_GROUPING_SEPARATOR = "PREFERRED_NUMBER_GROUPING_SEPARATOR";
    
    public static final String USER_CONTEXT_POOL = "USER_CONTEXT_POOL";
    
    public static final String PARTIAL_LOGIN = "PARTIAL_LOGIN";
    
    public static final String COBRANDS_NOT_LOGGED_IN = "COBRANDS_NOT_LOGGED_IN";
    
    public static final String CONFIGURED_COBRANDS="CONFIGURED_COBRANDS";
    
    // constant added for stand alone pay today
    public static final String STANDALONE_PAYTODAY_ENABLED = "standAlonePayTodayEnabled";
    
    // default cobrand values for I18N
    
    public static String COBRAND_CURRENCY = "com.yodlee.core.currency.default";
    
    public static String COBRAND_DATEFORMAT = "com.yodlee.core.dateFormat.default";
    
    public static String COBRAND_CURRENCY_NOTATION = "com.yodlee.core.currencyNotation.default";

    public static String COBRAND_DECIMAL_SEPARATOR = "com.yodlee.core.decimalSeparator.default";

    public static String COBRAND_GROUPING_SEPARATOR = "com.yodlee.core.groupingSeparator.default";

    public static String COBRAND_GROUP_PATTERN = "com.yodlee.core.groupPattern.default";   
    
    public static String INVALID = "INVALID:";
    
    public static String WHITE_SPACE = "WHITE_SPACE";
    
    public static final String GET = "GET";
    
    public static final String PAYMENT_ACCOUNTS_IDENTIFIERS_FROM_PROFILE = "PaymentAccountsIdentifiersFromProfile";
    
    //------------Constant for IDVerification Implementation------------------
    
    public static final String IDV_PHONE3 = "EVENING_PHONE_3";
    
    public static final String IDV_PHONE2 = "EVENING_PHONE_2";
    	
    public static final String IDV_PHONE1 = "EVENING_PHONE_1";	
    
    public static final String IDV_ADDRESS1 = "ADDRESS_1";
    
    public static final String IDV_ADDRESS2 = "ADDRESS_2";
    
    public static final String IDV_CITY = "CITY";
    
    public static final String IDV_STATE = "STATE";
    
    public static final String IDV_COUNTRY = "COUNTRY";
    
    public static final String IDV_EMAIL = "EMAIL_ADDRESS";
    
    public static final String IDV_ZIP1 = "ZIP_CODE_1";
    
   // public static final String IDV_ZIP2 = "";
    
    public static final String IDV_FIRST_NAME = "FIRST_NAME";
    
    public static final String IDV_MIDDLE_NAME = "MIDDLE_INITIAL";
    
    public static final String IDV_LAST_NAME = "LAST_NAME";
    
    /** The default values for the Beta * */
    public static final String BETA = "BETA";
    
    public static final String APP = "APP";
    
    public static final String PRODUCTION = "PRODUCTION";
    
    public static final String MONEYCENTER = "MONEYCENTER";
    
    public static final String BETA_LOGOUT = "BetaLogout";
    
    public static final String USER_TOKEN_NULL = "null";
    
    public static final String BETA_PROMOTION_ENABLED = "BETA_PROMOTION_ENABLED";
    
    public static final String MONCENTER_PROD_URL = "MoncenterProdURL";

    public static final String IMAGE_ = "Image_";
    
    public static final String USER_SURVEY_VOTED="userSurveyVoted";
    
    public static final String REDIRECT_URL_KEY = "REDIRECT_URL_KEY";
    
    /*
     * Param key is used to decide whether the survey floater popup show or not for the user.
     */
   
	public static final String BETA_PROMOTION_SHOW_SURVEY = "com.yodlee.app.betaPromotion.showSurvey";
	
	 /*
	  * Param key is used to decide whether the user has rated in the session or not.
	  */
	public static final String BETA_PROMOTION_USER_SURVEY_VOTED = "com.yodlee.app.betaPromotion.userSurveyVoted";

	public static final String WHAT_IS_NEW = "whatIsNew";
	
	public static final String BACK_TO_MONEYCENTER_ID = "backToMoneyCenter";
	
	/*
	 * constants for type of Survey Question.
	 */
	public static final String TEXT = "TEXT";

	public static final String IMAGE = "IMAGE";

	/*
	 * Param key is used to check whether the application is beta or not.
	 */
	public static final String BETA_APPLICATION_KEY = "com.yodlee.apps.betapromotion.applicationRequestType";
	
	public static final String DEFAULT_USER_HOME_KEY = "com.yodlee.app.default.user.home.application";

	/*
	 * Param key used to check if the Beta user session is active before making
	 * the production app logout.
	 */
	public static final String BETA_SESSION_STATUS_KEY = "com.yodlee.app.betaPromotion.betaSession.logout.url";
	
	public static final String OBO_APPS_CENTER_SESSION_STATUS_KEY_PREFIX = "com.yodlee.app.appscenter.obosession.";
	
	public static final String OBO_APPS_CENTER_SESSION_STATUS_KEY_SUFFIX = ".logout.url";
	
	public static final String OBO_SESSION_STATUS_KEY_PREFIX = "com.yodlee.app.timemachine.obosession.";
	
	public static final String OBO_SESSION_STATUS_KEY_SUFFIX = ".logout.url";
	
	public static final String USER_HOME_KEY = "com.yodlee.app.user.home.application";
	
	public static final String PROD_URL_KEY = "com.yodlee.app.betaPromotion.prod.url";
	
	public static final String SYSTEM_TIME = "SystemTime";
	
	/*
	 * constant for ycc application type.
	 */
	public static final String YCC = "YCC";

	public static final String TRY_YODLEE_BETA = "tryYodleeBeta";
	
	public static final String BACK_TO_MONEYCENTER = "BACK_TO_MONEYCENTER";
	
	public static final String BETA_PROMOTION_FLOATER = "BETA_PROMOTION_FLOATER";
	
	public static final String BETA_PROMOTION_ACL = "BETA_PROMOTION_ENABLED";
	
	public static final String JSEnabled = "JSEnabled";
	
	public static final String REQUEST_FROM = "billpay";
	
	public static String PAYEE_DISABLED = "payeeDisabled";
	
	public static String PAYEE_ENABLED = "payeeEnabled";
	
	public static String PAYEE_SUSPENDED = "payeeSuspended";
	
	public static String PAYEE_UAR = "payeeUAR";

	public static final String YES = "yes";

	public static final String NO = "no";

	public static final String BILLPAY_FEATURE_NAME = "billpay";

	public static final String REQUEST_IS_FORM = "requestIsFrom";
	
	public static final String BILL_REQ_FROM = "billRequestFrom";
	
	public static final String IS_PAYTODAY_ACL_ON = "isPayToDayACLOn";
	
	public static final String IS_CARDDIRECT_ACL_ON = "isCardDirectACLOn";
	
	public static final String CARDDIRECT_ENABLED_ACL_KEY = "BILLPAY_PAYMENTRAIL_HELDAWAY_CARDDIRECT";
	
	public static final String GROUP_PREFIX = "h";

	public static final String LOGOUT_PROD_SESSION = "logoutProdSession";

	public static final String EXIT_URL_KEY= "EXIT_URL";

	public static final String UPDATE = "update";
	
	public static final String USER_SUSP_ERROR_CODE = "userSuspended";

	// list of alert destination types
	public static final String ALERT_DEST_SMS = "sms";
	
	public static final String ALERT_DEST_SMS_INT = "smsInt";
	
	public static final String ALERT_DEST_MAIL = "mail";
	
	public static final String ALERT_DEST_XML = "xml";	
	
	public static final String ALERT_DEST_EMAIL = "email";
	
	public static final String ALERT_DEST_MOBILE = "mobile";
	
	public static final String ALERT_DEST_DEVELOPER_MAIL = "developermail";
	
	public static final String ALERT_DEST_DEVELOPER_SMS = "developersms";

	// Param key used to check if emailmobleverification is enabled or not.
	public static final String EMAIL_MOBILE_VERIFICATION_PARAM_KEY = "feature.switch.alerts.emailmobileverification.enable";

	// Mem pref key used to update basic html edition is on or not.
	public static final String BASIC_HTML_EDITION_MEM_PREF_KEY = "com.yodlee.userprofile.BASIC_HTML_EDITION";

	// list of html edition types.
	public static final String REGULAR = "regular";

	public static final String BASIC = "basic";
	
	public static final String USER_ENTERED_EXACTMATCH_USPS_ADDRESS = "userEnteredExactMatchUspsAddress";

	public static final String AUTH_FAILURE = "authFailure";
	
	public static final String MEM_ACTIVE_ACCOUNT = "MEM_ACTIVE_ACCOUNT";
	
	public static final String USER_SESSION_INFO = "userSessionInfo";

	public static final String USER_PROFILE_PREFIX = "com.yodlee.userprofile.";
	
	public static final String USER_CATEGORIES = "USER_CATEGORIES";
	
	public static final String BASIC_HTML_EDITION_PARAM_KEY = "feature.switch.basichtmledition.enable";
	
	public static final String PAYEE_OBJECT = "payee_object";

	public static final String EMAIL_MOBILE_VERIFY_VIEW_STATUS = "emailMobileVerifyViewStatus";
	
    public static final String DISPLAY_BP_ACCT_SUSP_NOTIFICATION="bpAcctSuspNotification";
    
    public static final String IAM_DEVELOPER_PARAM_KEY = "feature.switch.iamdeveloper.enable";
    
    public static final String PAYMENT_NETWORK_PARAM_KEY = "feature.switch.PAYMENT_NETWORK_ENABLED";
    
    public static final String IAM_DEVELOPER_ACL_KEY = "I_AM_DEVELOPER_ENABLED";
    
	public static final String ENTITY_DEVELOPER_ACL_KEY = "ENTITY_DEVELOPER_ACCOUNT";
    
    public static final String DEVELOPER_TNC_VERSION_KEY = "com.yodlee.apps.moneycenter.developer.registration.tncversion";
    
    public static final String BRIDGET_REGISTRATION_TNC_VERSION_KEY = "com.yodlee.apps.moneycenter.developer.bridgetregistration.tncversion";
    
    public static final String ACTIVE_BRIDGET_KEY = "active.bridgets";
    
	public static final String BP_SUSP_PAYMACCT_LIST = "bpSuspPaymAccountList";
	
	public static final String DISPLAY_FT_ACCT_SUSPENSION_FLOATER = "ftAccountSuspensionNotification";
	
	public static final String FT_SUSPENDED_ACCOUNTS = "ftSuspendeAccounts";
	
	public static final String TRUE="TRUE";
	
	public static final String FALSE="FALSE";

	public static final String SUPPORTED_APPLICATION_TYPES = "com.yodlee.apps.moneycenter.developer.bridgetapplication.supportedapptypes";
	
	public static final String SUPPORTED_MODULE_TYPES = "com.yodlee.apps.moneycenter.developer.bridgetapplication.supportedmoduletypes";
	
	public static final String OAUTH_AUTHORIZATION_PARAM_KEY = "feature.switch.oauthauthorize.enable";

    public static final String REQUEST_FROM_PAYEE_SUCCESS= "fromPayeeSuccess";
	
	public static final String PAYEE_SUCCESS_RECURRING_PAYMENT = "payeeSuccessRecurringPaym";
	
	public static final String PAYEE_SUCCESS_SINGLE_PAYMENT = "payeeSuccessSinglePaym";
	
	public static final String PAYEE_SUCCESS_AUTOPAY = "payeeSuccessAutopay";
	
	public static final String PAYEE_SUCCESS_EBILL_PAYMENT = "payeeSuccessEbillPaym";
	
	public static final String PAYEE_SUCCESS_INFO = "payeeSuccessInfo";
	
	public static final String PAYEE_SUCCESS_EBILL_ENROLLMENT = "payeeSuccessEbillEnrollment";
	
	public static final String PAYEE_SUCCESS_ENABLEONLINE_PAYMENT = "payeeSuccessEnableCardPayment";
	
	public static final String UPDATE_PAYEE_SUCCESS_PAYMENT_INFO = "updatePayeeSuccessPaymentInfos"; 
	
	public static final String PAYEE_ACTION_COMPLETED = "completed";
	
	public static final String PAYEE_SUCCESS_PAYMENT_FLOW = "toPaymentFlow";
	
	public static final String PAYEE_SUCCESS_EBILLENROLLMENT_FLOW = "toEbillEnrollmentFlow";
	
	public static final String PAYEE_SUCCESS_CARDPAYMENT_FLOW = "toEnableCardPaymentFlow";
	
	public static final String PAYEE_PAYMENT_SUCCESSFUL = "paymSuccessful";
	
	public static final String PAYEE_RECURRING_PAYMENT_SUCCESSFUL = "recurringPaymSuccessful";
	
	public static final String ENROLLMENT_SUCCESSFUL = "enrollmentSuccessful";
	
	public static final String ENROLLMENT_FAILED = "enrollmentFailed";
	
	public static final String PROCESS_ON = "Process On";
		
	public static final String DELIVER_ON = "Deliver On";
	
	public static final String SAMLLOGOUT = "SAMLLOGOUT";
		
	// Added for BofA Pay Today Calypso Project
	public static final String BILLPAY_PAYTODAY_CALYPSO_ENABLE = "feature.switch.billpay.paytoday.calypso.enable";

	public static final String BILLPAY_PAYTODAY_CALYPSO_EPROFILE_TIMEOUT = "com.yodlee.apps.quickpay.eprofile.timeout";

	public static final String BILLPAY_PAYTODAY_CALYPSO_EPROFILE_RETRYCOUNT = "com.yodlee.apps.quickpay.eprofile.retryCount";

	public static final String BILLPAY_PAYTODAY_CALYPSO_EPROFILE_WEBSERVICE_URL = "com.yodlee.quickpay.calypso.eprofile.webservice.url";

	public static final String BILLPAY_PAYTODAY_CALYPSO_MOCK_WS_ENABLE="com.yodlee.quickpay.calypso.mockwebservice.enable";
    
    public static final String BILLPAY_PAYTODAY_CALYPSO_TIMESTAMPS="businessEventTimestampMap";

    public static final String BILLPAY_PAYTODAY_CALYPSO_PARTITION_ID = "com.yodlee.apps.quickpay.eprofile.personnelInfo.PartitionId";

	public static final String BILLPAY_PAYTODAY_CALYPSO_OLB_SESSION_ID = "com.yodlee.apps.quickpay.eprofile.personnelInfo.OLBSessionID";


	public static final String PAYMENT_REQUEST_FAILED = "6";

	public static final String PAYMENT_REQUEST_DELAYED = "7";
	
	public static final String BILLPAY_PAYTODAY_CALYPSO_PROXY_SERVER = "com.yodlee.quickpay.calypso.proxy.server";

	public static final String BILLPAY_PAYTODAY_CALYPSO_PROXY_PORT = "com.yodlee.quickpay.calypso.proxy.port";

	public static final String BILLPAY_PAYTODAY_CALYPSO_EPROFILE_DEFAULT_ACCOUNTBALANCE = "com.yodlee.quickpay.calypso.default.account.balance";
	
	public static final String BRIDGET_FLASH_TOKEN_PREFIX = "BRIDGET_FLASH_TOKEN_";
	
	public static final String BRIDGET_IMAGE_TOKEN_PREFIX = "BRIDGET_IMAGE_TOKEN_";
	
	public static final String PROCESSING_PAYMENT_STATE = "1";

	public static final String SENDING_PAYMENT_STATE = "2";

	public static final String FINALIZING_PAYMENT_STATE = "3";

	public static final String PAYMENT_REQ_INPROGRESS = "3";
	
	public static final String PAYMENT_READY_TO_INPROGRESS = "2";
	
	public static final String AUTH_PASSED = "4";

	public static final String DASHBOARD_BETA = "dashboardBeta";
	
	public static final String SANDBOX = "sandbox";
	
	public static final String DASHBOARD = "dashboard";
	
	public static final String ACCT_LEVEL_PRIVILEGE_ENABLED = "COM.YODLEE.CORE.FUNDSTRANSFER.ACCT_PRIVILEGE_ENABLED";

	public static final String WIDGET_CSS_LOCATION = "com.yodlee.apps.widget.cob.styles.location";

	public static final String WIDGET_STRINGS_LOCATION = "com.yodlee.apps.widget.cob.strings.location";

	public static final String WIDGET_PROPERTIES_LOCATION = "com.yodlee.apps.widget.cob.params.location";
	
	public static final String UTIL_CURRENCY = "util.currency.";
	
	public static final String COUNTRY_ISO_CODE = ".country.iso.code";

	public static final String FINAPP_STORE = "finappStore";

	public static final String PUBLISHED_BRIDGET = "PUBLISHED_BRIDGET";

	/*
	 * cache key for oAuth bridget applications
	 */
	public static final String OAUTH_BRIDGET_APPLICATIONS_CACHE_KEY = "OAUTH_BRIDGET_APPLICATIONS_CACHE_KEY";

	/*
	 * cache key for bridget applications
	 */
	public static final String DEVELOPER_BRIDGET_APPLICATIONS_CACHE_KEY = "DEVELOPER_BRIDGET_APPLICATIONS_CACHE_KEY";

	public static final String TRAY_WIDGETS_MAP = "TRAY_WIDGETS_MAP";

	public static final String TRAY_WIDGETS_DESCRIPTION_MAP = "TRAY_WIDGETS_DESCRIPTION_MAP";
	/*
	 * key for Finapp Tray Customization 
	 */
	public static final String FINAPP_DETAIL_PAGE = "feature.switch.FINAPP_DETAIL_PAGE";

	public static final String CERTIFICATION_ADMIN_ACL_KEY = "";
	
	public static final String WIDGET_REVIEWS_ENABLE_KEY = "feature.switch.widgetreviews.enable";

	public static final String FLAG_GET_ALL_PAYEES = "FLAG_GET_ALL_PAYEES"; // gnarang - Indicate if getAllPayees() should be called for performance reasons..

	public static final String FINAPP_ADMIN_ACL_KEY = "FINAPP_ADMIN_ENABLED";
	
	/* Payment Network Enromment Status */
	public static final String Enrollment_SUCCESS = "SUCCESS";
	public static final String Enrollment_FAILURE = "FAILURE";
	public static final String Enrollment_PENDING = "PENDING";
	public static final String Enrollment_IN_PROGRESS = "IN_PROGRESS";
	
	public static final String PUBLISH_OPERATION = "publish";
	
	public static final String CERTIFY_OPERATION = "certify";
	
	public static final String REJECT_OPERATION = "reject";
	
	public static final String TEST_IN_DASHBOARD_OPERATION = "testInDashboard";

	//From parameter for Yodlee PaymentCenter/ Payment Network Settlement Account Page
	public static final String FROM_PAYMENT_NETWORK_FLOW = "paymentNetworkFlow";
	//From parameter for Yodlee PaymentCenter/ Payment Network Settlement Account Page
	public static final String INITIATED_FROM_PAYMENT_NETWORK_FLOW = "initFromPaymNetworkFlow";

	public static final String FINAPP_CERTIFICATION_ENABLED_KEY= "FINAPP_CERTIFICATION_ENABLED";
	
	public static final String REG_TYPE_BUSINESS = "business";
	
	public static final String REG_TYPE_INDIVIDUAL = "individual";
	
	public static final String FINANCIAL_CALENDAR_FIRST_DAY_OF_WEEK = "com.yodlee.apps.billpay.billsOverview.financialCalendar.FIRST_DAY_OF_WEEK";
	
	public static final String PAYMENTCENTER_ACL_KEY = "APPLICATION_PAYMENT_CENTER";
	
	public static final String EMBEDDED_FINAPPS = "EMBEDDED_FINAPPS";

	public static final String BILLPAY_PAYTODAY_CALYPSO2_SUPPORT_USER="com.yodlee.calypso2.user.isSupportUser";

	public static final String USER_GROUP_NAMES= "USER_GROUP_NAMES";
 	public  static final String REFRESH_AT_LOGIN_HELD_ACCOUNTS_ACL_NAME ="REFRESH_AT_LOGIN_HELD_ACCOUNTS";
 	public static final String REFRESH_AT_LOGIN_HELDAWAY_ACCOUNTS_ACL_NAME = "REFRESH_AT_LOGIN_HELDAWAY_ACCOUNTS";
	public static final String CCC_USER_STATUS = "CCC_USER_STATUS";
	
	public static final String ZILLOW_SUP_TAG_REGEXP = "<sup[\\W\\w\\s\\S]+</sup>";
	
	public static final String CCC_MAP = "CCC_MAP";
	
	public static final String CCC = "CCC";
	
	public static final String BILLPAY_PAYTODAY_CALYPSO2_REQUESTPARAMS_ENCRYPTED = "com.yodlee.calypso2.requestParams.encrypted";
	
	public static final String BILLPAY_PAYTODAY_CALYPSO2_KEY_ALIAS_NAME = "com.yodlee.calypso2.key.alias.name";
	
	public static final String BILLPAY_PAYTODAY_CALYPSO2_TNC_ACCEPTED = "TNC_ACCEPTED";
	
	public static final String BILLPAY_PAYTODAY_CALYPSO2_LOGIN_NAME = "com.yodlee.calypso2.user.LoginName";
	
	public static final String BILLPAY_PAYTODAY_CALYPSO2_REQUESTPARAMS_SEPARATOR= "com.yodlee.calypso2.requestParams.separator";
		
	public static final String TIME_MACHINE_APPS_KEY = "com.yodlee.apps.timemachine.applications.version";

	public static final String BETAPROMOTION_PAST_APP_VERSION= "com.yodlee.apps.timemachine.applications.version";
	
	public static final String BETAPROMOTION_PROD_APP_VERSION= "com.yodlee.apps.timemachine.production.application.version";
	
	public static final String BETAPROMOTION_PROD_BETA_VERSION= "com.yodlee.apps.timemachine.beta.application.version";
	
    public static final String TIME_MACHINE_APP_URL_KEY_PREFIX = "com.yodlee.apps.timemachine.application.";
	
	public static final String TIME_MACHINE_APP_URL_KEY_SUFFIX = ".home.url";
	
	public static final String BETAPROMOTION_BETA_HOME_URL = "com.yodlee.apps.betapromotion.beta.home.url";
	
	public static final String PROD = "PROD";

	public static final String BILLPAY_ACL_PHONE_CHANNEL_PREMIUM_FEE= "BILLPAY_PHONE_CHANNEL_PREMIUM";
	
	public static final String BILLPAY_PHONE_CHANNEL_PREMIUM_FEE_ENABLE = "feature.switch.billpay.phonechannel.premium.enable";
	
	public static final String USER_WIDGETS_SET = "USER_WIDGETS_SET";
	
	public static final String DEVELOPER_WIDGETS_SET = "DEVELOPER_WIDGETS_SET";
	
	public static final String RELOAD_USER_ACL_VALUES = "reloadUserAclValues";

	public static final String OAUTH_DEEP_LINK = "oauthdeeplink";
	
    public static final String OAUTH_ERROR_MSG = "oauthErrorMsg";	

	public static final String USER_OAUTH_TOKEN = "USER_OAUTH_TOKEN";	
	
	public static final String OAUTH_ERROR_CODE = "oauthErrorCode";
	
	public static final String OAUTH_ACCESS_TOKEN = "oauthAccessToken";
	
	public static final String OAUTH_REGISTRATION = "oauthregistration";

	public static final String OAUTH_CONSUMER_KEY = "oauthConsumerKey";

	public static final String OAUTH_ACCESS_TYPE = "oauthAccessType";

	public static final String OAUTH_CALLBACK_URL = "oauthCallbackUrl";
	
	public static final String OAUTH_DISPLAY_MODE = "oauthDisplayMode";
	
	public static final String OAUTH_TIMEOUT_INFO = "oAuthTimeOutInfo";
	
	public static final String DISPLAY_MODE = "displayMode";	
	
	public static final String ACCESS_TYPE = "access_type";
	
	public static final String COMMON_FEATURES_MAP = "COMMON_FEATURES_MAP";
	
	public static final String OAUTH_USER_NAME = "OAUTH_USER_NAME";
	
	public static final String SHOW_SITE_CREDENTAILS = "SHOW_SITE_CREDENTAILS";	

	public static final String REQUEST_FOR_STANDALONE_FT = "requestFromFT";
	
	public static final String PAYPAL_TOEKN = "token";
	
	public static final String PAYPAL_PAYER_ID = "PayerID";
	
	public static final String YODLEE_TXN_ID = "yodleeTxnId";
	
	public static final String SHOW_COBRAND_PASSWORD = "showCobrandPassword";

	public static final String FINAPP_ID = "finappId";

	public static final String SSO_REAUTH_MEM_ID = "SSO_REAUTH_MEM_ID";
	
	public static final String IS_PRIVATE_SUBBRAND = "isPrivateSubbrand";
	
	public static final String USER_PREF_BILLPAY_PAYMENT_ACCOUNT_VISIBILITY = "billpay.accountVisible";
	
	public static final String DESKTOP_DISPLAYMODE = "desktop";
	
	public static final String STANDALONE_DIRECT_PAY = "standaloneDirectPay";
	
	public static final String OAUTH_TIMEOUT_FLAG = "com.yodlee.apps.oauth.timeout.enable";
	
	public static final String OAUTH_DEEPLINK_COBRAND = "com.yodlee.cobrand.type.oauth.deeplink"; 
	
	public static final String OAUTH_CALLBACK_REQUEST_TYPE = "oauthCallbackRequestType";
	
	public static final String ADD_PARAMS_TO_SAML_LOGOUT_URL_LIST = "feature.switch.ADD_PARAMS_TO_SAML_LOGOUT_URL_LIST";

	public static final String ADD_PARAMS_TO_SAML_LOGOUT_URL = "feature.switch.ADD_PARAMS_TO_SAML_LOGOUT_URL";

	public static final String SAML_LOGOUT_URL_PARAMS_MAP = "SAML_LOGOUT_URL_PARAMS_MAP";
	
	public static final String SAML_LOGOUT_PARAMS_IS_NEW_SESSION = "SAML_LOGOUT_PARAMS_IS_NEW_SESSION";

	public static final String SAML_LOGOUT_PARAMS_ERROR_CODE= "SAML_LOGOUT_PARAMS_ERROR_CODE";
	
	public static final String IS_COB_CUSTOM_SOURCE_SET = "IS_COB_CUSTOM_SOURCE_SET";
	
	public static final String SAML_LOGOUT_RESPONSE = "SAMLResponse";
	
	public static final String SAML_SLO_REQUEST = "SAMLRequest";
	
	public static final String SWITCH_SHOW_TOTAL_BALANCE_FOR_INVESTMENT = "feature.switch.show_total_balance_for_investment_account";
	
	public static final String SHOW_TOTAL_FOR_ACCT_TYPES_INVESTMENT = "feature.switch.show_total_balance_for_acct_types.investment_account";
	
	public static final String SWITCH_INVESTMENT_SPS_PLAN_ENABLED = "feature.switch.investment.sps_plan.enabled";
	
	public static final String SWITCH_INVESTMENT_SPS_PLAN_MARKER_DISCLAIMER_ENABLED = "feature.switch.show_SPS_plan_disclaimer_marker_for_investment_account";
	
	public static final String SHOW_DISCLAIMER_FOR_INVESTMENT_SPS_PLAN_ACCT_TYPES_ACCOUNT_SUMMARY = "feature.switch.account_summary.investment.sps_plan.acct_types.disclaimer";
	
	public static final String SHOW_DISCLAIMER_FOR_INVESTMENT_SPS_PLAN_ACCT_TYPES_NETWORTH_STATEMENT = "feature.switch.networth_statement.investment.sps_plan.acct_types.disclaimer";
	
	public static final String SHOW_DISCLAIMER_FOR_INVESTMENT_SPS_PLAN_ACCT_TYPES_PORTFOLIO_MANAGER = "feature.switch.portfolio_manager.investment.sps_plan.acct_types.disclaimer";
	
	public static final String SWITCH_INVESTMENT_SPS_PLAN_DO_NOT_MERGE_ACCOUNT_TYPES = "feature.switch.portfolio_manager.investment.do_not_merge.acct_types";
	
	public static final String SWITCH_SHOW_TOTAL_BALANCE_FOR_HELD_ACCOUNT_INVESTMENT = "feature.switch.show_total_balance_for_held_investment_account";
	
	public static final String SWITCH_SHOW_UNVESTED_BALANCE_ENABLED = "feature.switch.show_unvested_balance_enabled";
	
	public static final String CROSS_SELL_DETAILS = "crossSellDetails";
	
	public static final String CROSS_SELL_ENABLED_ACL = "CROSSSELL_FINAPP_ENABLED";
	
	public static final String SWITCH_INCLUDE_INTRADAY_AND_ACCRUED_INTEREST = "feature.switch.include_intraday_and_accrued_interest";
	
	public static final String SWITCH_INCLUDE_INTRADAY_AND_ACCRUED_INTEREST_FOR_HELDACCTS = "feature.switch.include_intraday_and_accrued_interest_held_accounts_only";

	public static final String CROSS_SELL_ENABLED = "crossSellEnabled";

	public static final String FINAPP_LAST_ACCESSED_DATE = "FINAPP_LAST_ACCESSED_DATE";

	public static final String OLB_FINAPPCAROUSEL_PRECONFIG_FINAPPS_FOR_PAGE = "com.yodlee.apps.olb.finAppcarousel.preconfigFinApps.page.";  // will get appended with finapp identifier list.

	public static final String OLB_WIDGET_PAGE = "OLB_WIDGET_PAGE";
	
	public static final String OLB_TRAY_WIDGETS_MAP = "OLB_TRAY_WIDGETS_MAP";
	public static final String FINAPP_EXCLUDE_LIST_FOR_DOMAIN_VALIDATION= "com.yodlee.apps.finapp.exclude.validate.domain";
	
	public static final String FINAPP_EXCLUDE_LIST_FOR_REST_VALIDATION= "com.yodlee.apps.finapp.exclude.validate.sdk.rest.url";

	public static final String OAUTH_CALLBACK="OAUTH_CALLBACK";
	
	public static final String NEW_LINK_ACCOUNT_ENABLED = "NEW_LINK_ACCOUNT_ENABLED";
	
	public static final String ITEM_ACCOUNT_HIDDEN_STATUS_ID = "2";
	
	public static final String NICKNAME = "nickname";
	
	public static final String NEW_LINK_ACCOUNT_ACL_NAME = "NEW_LINK_AN_ACCOUNT_ENABLED";

	public static final String EDIT_FINAPP = "10003106";
	
	public static final String ADVANCED_FINAPP = "10003108";

	public static final String APPS_CENTER_PARAM_ACL_KEY = "APPS_CENTER_ENABLED";

	public static final String APPS_CENTER = "APPS_CENTER";

	public static final String WIDGET_URL_KEY = "widgetUrlKey";
	
	public static final String ACCOUNT_DETAILS_PAGE_NAME = "AccountDetails";	
	
	public static final String JSESSIONID = "JSESSIONID";

	public static final String APPS_CENTER_CALLBACK_URL = "appsCenterCallbackURL";	
    
    public static final String APPS_CENTER_APPLCIATION_PARAM_KEY = "com.yodlee.apps.appsCenter.enabled";
    public static final String APPS_CENTER_DEEPLINK_COBRAND = "com.yodlee.cobrand.type.appsCenter.deeplink";
    
    public static final String APPS_CENTER_HOME_URL = "com.yodlee.apps.appsCenter.home.url";
    
    public static final String APPS_CENTER_CLONE_SESSION_PARAM_KEY = "COM.YODLEE.APPS.OBO.CLONE_MC_USER_SESSION";
    
	public static final String SWITCH_ACCOUNT_DETAILS_SHOW_TRANSACTION_FINAPP_SUPPORTED_CONTAINERS = "feature.switch.accountDetails.show_transaction_finapp.container";
	
	public static final long TRANSACTION_FINAPP = 10003107;
	
	public static final String DASHBOARD_DEFAULT_PAGE_NAME = "feature.switch.dashboard.default.pagename";
	public static final String WIDGET_URL_PARAM_KEY = "widgetUrlKey";
	public static final String CONTAINER_INFO = "containerInfo";
	
	public static final String SITE_ACCOUNT_ID = "siteAccountId";
	
	public static final String SITE_ID = "siteId";
	
	public static final String ACCOUNT_INFO_LIST = "accountInfoList";
	
	public static final String FLOW_ID = "flowId";

	public static final String BASE_APP_REQUEST = "BASE_APP_REQUEST";
	
	public static final String COMMON_ERROR = "com.yodlee.apps.common.error";

	public static final String REST_TEMPLATE_ALLOWED_CHARS_REGEX = "A-Za-z0-9}/{.@:\\s_";
	
	public static final String P3P_POLICY_ENABLED_KEY="com.yodlee.app.p3p.isEnabled";
	  
	public static final String P3P_POLICY_NAME="com.yodlee.app.p3p.policyName";

	public static final String P3P_POLICY_VALUE="com.yodlee.app.p3p.policyValue";	

	public static final String SWITCH_TO_ADD_ACCT_TYPES_TO_SYMBOLS = "feature.switch.portfolio_manager.append_acct_type_to_symbol_for_sps_accounts";

	public static final String FORCE_SEPERATOR_CHANGE="feature.switch.force_change_in_decimal_seperator.as_per_locale";

	public static final String SHA_256 = "SHA-256";

	public static final String SHA_512 = "SHA-512";

	public static final String MIN_FRACTION_DIGIT = "com.yodlee.apps.common.util.numberformatter.MinFractionDigits";

	public static final String DEEPLINK_STATUS_PARAMETERS = "deeplinkStatusParameters";	
	
	public static final String NOT_REFRESHABLE = "NOT_REFRESHABLE";
	
	public static final String EDIT_SITE = "EDIT_SITE";

	public static final String SITE_REFRESH = "SITE_REFRESH";
	
	public static final String ITEM_REFRESH = "ITEM_REFRESH";
	
	public static final String FEATURE_SWITCH_SCRAPED_TOTAL_BALANCE_ENABLED = "COM.YODLEE.CORE.INVESTMENT_ACCOUNT.SCRAPED.TOTAL_BALANCE.ENABLE";
	
	public static final String FEATURE_SWITCH_SITE_REFRESH_ENABLED = "com.yodlee.apps.rest.siteRefresh.enabled";
	
	public static final String UNIQUE_REFERENCE_TRACKER_ID = "yodlee-ref-id";
	
	public static final String REQ_ATTR_COBRAND_ID = "cobrandId";
}