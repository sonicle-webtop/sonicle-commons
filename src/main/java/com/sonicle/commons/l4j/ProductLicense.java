/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.sonicle.commons.l4j;

import com.license4j.ActivationStatus;
import com.license4j.HardwareID;
import com.license4j.License;
import com.license4j.LicenseText;
import com.license4j.LicenseValidator;
import com.license4j.ModificationStatus;
import com.license4j.ValidationStatus;
import com.sonicle.commons.URIUtils;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.HashMap;
import net.sf.qualitycheck.Check;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.utils.URIBuilder;
import org.joda.time.LocalDate;

/**
 *
 * @author malbinola
 */
public class ProductLicense {
	private final LicenseType licenseType;
	private final ActivationLicenseType activationReturnType;
	private final String productCode;
	private final String publicKey;
	private final String internalHiddenString; // only for basic and crypto (maybe a l4j bug?) key
	private final String customHardwareId;
	private String activationCustomHardwareId;
	private String licenseServer;
	private String trialLicense; // license-key or license-text
	private String licenseString; // license-key or license-text
	private String activatedLicenseString; // license-text
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final String productCode, final String publicKey, final String customHardwareId, final String internalHiddenString, final String licenseServer, final String trialLicense, final File licenseFile) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productCode = productCode;
		this.customHardwareId = customHardwareId;
		this.internalHiddenString = internalHiddenString;
		this.licenseServer = licenseServer;
		this.trialLicense = trialLicense;
		this.publicKey = publicKey;
	}
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final String productCode, final String publicKey, final String customHardwareId, final String internalHiddenString, final String licenseServer, final String trialLicense, final String licenseString) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productCode = productCode;
		this.customHardwareId = customHardwareId;
		this.internalHiddenString = internalHiddenString;
		this.licenseServer = licenseServer;
		this.trialLicense = trialLicense;
		this.publicKey = publicKey;
		this.licenseString = licenseString;
	}
	
	public ProductLicense(final AbstractProduct product) {
		this.licenseType = product.getLicenseType();
		this.activationReturnType = product.getActivationReturnType();
		this.productCode = product.getProductCode();
		this.publicKey = product.getPublicKey();
		this.customHardwareId = product.getHardwareId();
		this.internalHiddenString = product.getInternalHiddenString();
		this.licenseServer = product.getLicenseServer();
	}
	
	private String getLicenseServerBaseUrl() {
		return StringUtils.isBlank(licenseServer) ? ONLINE_SERVER_URL : licenseServer;
	}
	
	private static final String ONLINE_SERVER_URL = "https://online.license4j.com";
	private static final String ACTIVATION_URL_PATH = "/d/manualactivation";
	private static final String DEACTIVATION_URL_PATH = "/d/manualdeactivation";
	private static final String MODIFICATION_URL_PATH = "/d/manualmodification";
	
	private String buildLicenseServerPathUrl(String path) {
		try {
			URIBuilder builder = new URIBuilder(getLicenseServerBaseUrl());
			URIUtils.appendPath(builder, path);
			return builder.build().toASCIIString();
		} catch(URISyntaxException ex) {
			return null;
		}	
	}
	
	public LicenseType getLicenseType() {
		return licenseType;
	}
	
	public ActivationLicenseType getActivationReturnType() {
		return activationReturnType;
	}
	
	public String getProductCode() {
		return productCode;
	}
	
	public String getPublicKey() {
		return publicKey;
	}
	
	public String getInternalHiddenString() {
		return internalHiddenString;
	}
	
	public String getLicenseServer() {
		return licenseServer;
	}
	
	public String getHardwareID() {
		return customHardwareId != null ? customHardwareId : HardwareID.getHardwareIDFromVolumeSerialNumber();
	}
	
	public boolean isTrialLicense() {
		return StringUtils.equals(licenseString, trialLicense);
	}
	
	public boolean isTrialLicenseAvailable() {
		return trialLicense != null;
	}
	
	public String getTrialLicense() {
		return trialLicense;
	}
	
	public void setLicenseString(String licenseString) {
		this.licenseString = licenseString;
		setActivatedLicenseString(null);
	}
	
	public String getLicenseString() {
		return this.licenseString;
	}
	
	public String getActivatedLicenseString() {
		return this.activatedLicenseString;
	}
	
	public void setActivatedLicenseString(String activatedLicenseString) {
		this.activatedLicenseString = activatedLicenseString;
	}
	
	public void setActivationCustomHardwareId(String activationCustomHardwareId) {
		this.activationCustomHardwareId = activationCustomHardwareId;
	}
	
	public LicenseInfo validate(final boolean beforeActivation) {
		LicenseInfo li = null;
		if (activatedLicenseString != null && !beforeActivation) {
			// After activation, validate the latest activated license string.
			li = internalValidate(activationReturnType.name(), activatedLicenseString, activationCustomHardwareId);
		} else {
			// Before activation, it will be activate, so validate the original not activated license string.
			li = internalValidate(licenseType.name(), Check.notNull(licenseString, "licenseString"), customHardwareId);
		}
		return li;
	}
	
	public LicenseInfo autoActivate() throws IOException {
		LicenseInfo li = validate(true);
		if (li.isValid() && li.isActivationRequired()) {
			LicenseInfo ali = internalActivate(li.getLicense());
			if (ali.isValid() && ali.isActivationCompleted()) {
				activatedLicenseString = ali.getLicenseString();
				li = ali;
			} else {
				activatedLicenseString = null;
			}
		}
		return li;
	}
	
	public LicenseInfo manualActivate(final String activatedLicenseString) throws IOException {
		LicenseInfo li = validate(true);
		if (li.isValid() && li.isActivationRequired()) {
			this.activatedLicenseString = activatedLicenseString;
			li = validate(false);
		}
		return li;
	}
	
	public LicenseInfo autoDeactivate() throws IOException {
		LicenseInfo li = validate(false);
		if (!li.isInvalid() && li.isActivationCompleted()) {
			LicenseInfo dli = internalDeactivate(li.getLicense());
			if (dli.isDeactivationCompleted()) {
				activatedLicenseString = null;
				li = dli;
			} else {
				activatedLicenseString = null;
			}
		}
		return li;
	}
	
	public LicenseInfo manualDeactivate() throws IOException {
		LicenseInfo li = validate(false);
		if (!li.isInvalid() && li.isActivationCompleted()) {
			activatedLicenseString = null;
			li = validate(true);
		}
		return li;
	}
	
	public LicenseInfo modifyLicense(final String modificationKey) throws IOException {
		LicenseInfo li = validate(false);
		if (!li.isInvalid()) {
			LicenseInfo mli = internalModify(li.getLicense(), modificationKey);
			if (mli.isModified()) {
				activatedLicenseString = mli.getLicenseString();
				li = mli;
			}
		}
		return li;
	}
	
	public LicenseInfo manualModify(final String activatedLicenseString) throws IOException {
		LicenseInfo li = validate(true);
		if (!li.isInvalid()) {
			this.activatedLicenseString = activatedLicenseString;
			li = validate(false);
		}
		return li;
	}
	
	public RequestInfo getManualActivationRequestInfo() {
		LicenseInfo li = validate(true);
		if (li != null && li.isActivationRequired()) {
			if (activationCustomHardwareId == null) {
				String hwid = HardwareID.getHardwareIDFromHostName() + "&&" + HardwareID.getHardwareIDFromEthernetAddress();
				return new RequestInfo(buildLicenseServerPathUrl(ACTIVATION_URL_PATH), li.getManualActivationRequestString(), hwid);
			} else {
				return new RequestInfo(buildLicenseServerPathUrl(ACTIVATION_URL_PATH), li.getLicense().getManualActivationRequestStringWithCustomHardwareID(activationCustomHardwareId), activationCustomHardwareId);
			}
		}
		return null;
	}
	
	public RequestInfo getManualDeactivationRequestInfo() {
		LicenseInfo li = validate(false);
		if (li != null && li.isActivationCompleted()) {
			return new RequestInfo(buildLicenseServerPathUrl(DEACTIVATION_URL_PATH), li.getManualDeactivationRequestString(), li.getHardwareID());
		}
		return null;
	}
	
	public RequestInfo getManualModificationRequestInfo(String modificationKey) {
		LicenseInfo li = validate(false);
		if (li != null && li.isValid()) {
			String hwid = HardwareID.getHardwareIDFromHostName() + "&&" + HardwareID.getHardwareIDFromEthernetAddress();
			return new RequestInfo(buildLicenseServerPathUrl(MODIFICATION_URL_PATH), li.getManualModificationRequestString(modificationKey), hwid);
		}
		return null;
	}
	
	public int updateUseInfo(final int timeout) {
		LicenseInfo li = validate(true);
		if (StringUtils.isBlank(licenseServer)) {
			return LicenseValidator.updateLicenseUseInfo(publicKey, li.getLicense(), timeout);
		} else {
			return LicenseValidator.updateLicenseUseInfo(publicKey, li.getLicense(), licenseServer, timeout);
		}
	}
	
	public int updateTrackingInfo(final HashMap<String, String> map, final int timeout) {
		LicenseInfo li = validate(true);
		if (StringUtils.isBlank(licenseServer)) {
			return LicenseValidator.updateLicenseUseTrackingInfo(publicKey, li.getLicense(), map, timeout);
		} else {
			return LicenseValidator.updateLicenseUseTrackingInfo(publicKey, li.getLicense(), map, licenseServer, timeout);
		}
	}
	
	public int queryTrackingInfo(HashMap<String, String> map, final int timeout) {
		LicenseInfo li = validate(true);
		if (StringUtils.isBlank(licenseServer)) {
			return LicenseValidator.queryLicenseUseTrackingInfo(publicKey, li.getLicense(), map, timeout);
		} else {
			return LicenseValidator.queryLicenseUseTrackingInfo(publicKey, li.getLicense(), map, licenseServer, timeout);
		}
	}
	
	public int checkOnlineAvailability(final int timeout) {
		LicenseInfo li = validate(true);
		if (StringUtils.isBlank(licenseServer)) {
			return LicenseValidator.checkOnlineAvailability(publicKey, li.getLicense(),  timeout);
		} else {
			return LicenseValidator.checkOnlineAvailability(publicKey, li.getLicense(), licenseServer, timeout);
		}
	}
	
	/*
	public LicenseInfo modify(final String modificationKey) throws IOException {
		LicenseInfo alic = internalValidate2(activationReturnType.name(), licenseActivationString, activationCustomHardwareId);
		//LicenseInfo alic = internalValidate(activationReturnType.name(), licenseActivationString);
		LicenseInfo mlic = null;
		if ((mlic != null) && mlic.isValid()) {
			mlic = createLicenseInfo(LicenseValidator.modifyLicense(alic.getLicense(), licenseServer, modificationKey));
			if (ModificationStatus.MODIFICATION_COMPLETED == alic.getLicense().getModificationStatus()) {
				licenseActivationString = mlic.getLicenseString();
				//save();
			}
		}
		return mlic;
	}
	
	public boolean loadProperties() throws IOException {
		if (licenseFile.exists() && licenseFile.canRead()) {
			properties.load(new FileInputStream(licenseFile));
			licenseString = properties.getProperty("license");
			licenseActivationString = properties.getProperty("activationString");
			return true;
		} else {
			return false;
		}
	}
	
	public boolean loadLicense() throws IOException {
		if (licenseFile.exists() && licenseFile.canRead()) {
			licenseString = FileUtils.readFile(licenseFile.getAbsolutePath());
			return true;
		} else {
			return false;
		}
	}
	
	public boolean save() throws IOException {
		if (!licenseFile.exists()) licenseFile.createNewFile();
		if (licenseFile.canWrite()) {
			if (licenseString != null) {
				properties.setProperty("license", licenseString);
			} else {
				properties.remove("license");
			}
			if (licenseActivationString != null) {
				properties.setProperty("activationString", licenseActivationString);
			} else {
				properties.remove("activationString");
			}
			properties.store(new FileOutputStream(this.licenseFile, false), null);
			return true;
		} else {
			return false;
		}	
	}
	*/
	
	private LicenseInfo internalValidate(String typeName, String licenseString, String hardwareId) {
		if (licenseString == null) return null; // Is still useful?
		License license = null;
		if (LicenseType.LICENSE_TEXT.name().equals(typeName)) {
			if (hardwareId == null) {
				license = LicenseValidator.validate(licenseString, publicKey, productCode, (String)null, (String)null, (Date)null, (Date)null);
			} else {
				license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, productCode, (String)null, (String)null, hardwareId, (Date)null, (Date)null);
			}
		} else if (LicenseType.BASIC_KEY.name().equals(typeName) || LicenseType.CRYPTO_KEY.name().equals(typeName)) {
			if (hardwareId == null) {
				license = LicenseValidator.validate(licenseString, publicKey, internalHiddenString, (String)null, (String)null, 0);
			} else {
				license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, internalHiddenString, (String)null, (String)null, hardwareId);
			}
		}
		return createLicenseInfo(license);
	}
	
	public LicenseInfo internalActivate(License license) {
		License alic = null;
		if (activationCustomHardwareId == null) {
			if (StringUtils.isBlank(licenseServer)) {
				alic = LicenseValidator.autoActivate(license);
			} else {
				alic = LicenseValidator.autoActivate(license, licenseServer);
			}
		} else {
			if (StringUtils.isBlank(licenseServer)) {
				alic = LicenseValidator.autoActivateWithCustomHardwareID(license, activationCustomHardwareId);
			} else {
				alic = LicenseValidator.autoActivateWithCustomHardwareID(license, licenseServer, activationCustomHardwareId);
			}
		}
		return createLicenseInfo(alic);
	}
	
	public LicenseInfo internalDeactivate(License license) {
		License dlic = null;
		if (StringUtils.isBlank(licenseServer)) {
			dlic = LicenseValidator.autoDeactivate(license);
		} else {
			dlic = LicenseValidator.autoDeactivate(license, licenseServer);
		}
		return createLicenseInfo(dlic);
	}
	
	public LicenseInfo internalModify(License license, String modificationKey) {
		License mlic = null;
		if (StringUtils.isBlank(licenseServer)) {
			mlic = LicenseValidator.modifyLicense(license, modificationKey);
		} else {
			mlic = LicenseValidator.modifyLicense(license, licenseServer, modificationKey);
		}
		return createLicenseInfo(mlic);
	}
	
	private LicenseInfo createLicenseInfo(License license) {
		return (license != null) ? new LicenseInfo(productCode, license) : null;
	}
	
	public static enum LicenseType {
		LICENSE_TEXT, BASIC_KEY, CRYPTO_KEY, DUMMY
	}
	
	public static enum ActivationLicenseType {
		OFF_NO_ACTIVATION, LICENSE_TEXT, CODE
	}
	
	public static class RequestInfo {
		public final String url;
		public final String request;
		public final String hardwareId;
		
		public RequestInfo(final String url, final String request, final String hardwareId) {
			this.url = url;
			this.request = request;
			this.hardwareId = hardwareId;
		}
	}
	
	public static class LicenseInfo {
		private final String productCode;
		private final License license;
		
		public LicenseInfo(final String productCode, final License license) {
			this.productCode = productCode;
			this.license = license;
		}
		
		public String getProductCode() {
			return this.productCode;
		}
		
		public License getLicense() {
			return this.license;
		}
		
		public long getLicenseID() {
			LicenseText lt = license.getLicenseText();
			return (lt != null) ? lt.getLicenseID() : -1;
		}
		
		public String getLicenseString() {
			return license.getLicenseString();
		}
		
		public ValidationStatus getValidationStatus() {
			return license.getValidationStatus();
		}
		
		public ModificationStatus getModificationStatus() {
			return license.getModificationStatus();
		}
		
		public ActivationStatus getActivationStatus() {
			return license.getActivationStatus();
		}
		
		public String getManualActivationRequestString() {
			return license.getManualActivationRequestString();
		}
		
		public String getManualDeactivationRequestString() {
			return license.getManualDeactivationRequestString();
		}
		
		public String getManualModificationRequestString(String modificationKey) {
			return license.getManualModificationRequestString(modificationKey);
		}
		
		public int getActivationDaysRemaining() {
			return license.getLicenseActivationDaysRemaining(null);
		}
		
		public Integer getQuantity() {
			LicenseText lt = license.getLicenseText();
			Integer i =(lt != null) ? lt.getLicenseQuantity() :null;
			return (i == 999999) ? null : i;
		}
		
		public Integer getExpireDaysRemaining() {
			LicenseText lt = license.getLicenseText();
			return (lt != null) ? lt.getLicenseExpireDaysRemaining(null) : null;
		}
		
		public LocalDate getExpirationDate() {
			LicenseText lt = license.getLicenseText();
			if (lt == null) return null;
			Date expDate = lt.getLicenseExpireDate();
			return expDate != null ? new LocalDate(expDate) : null;
		}
		
		public String getHardwareID() {
			LicenseText lt = license.getLicenseText();
			return (lt != null) ? lt.getLicenseHardwareID() : null;
		}
		
		public boolean isInvalid() {
			return ValidationStatus.LICENSE_INVALID == license.getValidationStatus();
		}
		
		public boolean isValid() {
			return ValidationStatus.LICENSE_VALID == license.getValidationStatus();
		}
		
		public boolean isExpired() {
			return ValidationStatus.LICENSE_EXPIRED == license.getValidationStatus();
		}
		
		public boolean isActivationCompleted() {
			return license.isActivationCompleted();
		}
		
		public boolean isActivationNotFound() {
			return ActivationStatus.ACTIVATION_NOT_FOUND_ON_SERVER == license.getActivationStatus();
		}
		
		public boolean isDeactivationCompleted() {
			return ActivationStatus.DEACTIVATION_COMPLETED == license.getActivationStatus();
		}
		
		public boolean isModified() {
			return ModificationStatus.MODIFICATION_COMPLETED == license.getModificationStatus()
					|| ModificationStatus.MODIFICATION_COMPLETED_PREVIOUSLY == license.getModificationStatus();
		}
		
		public boolean isValidAndDeactivated2() {
			return isValid() && isDeactivationCompleted();
		}

		public boolean isActivationRequired() {
			return license.isActivationRequired();
		}

		public boolean isActivationRequiredNow() {
			return license.isActivationRequired() && (license.getLicenseActivationDaysRemaining(null) == 0);
		}

		public boolean isExpiringSoon() {
			return (license.getLicenseText() != null) && (license.getLicenseText().getLicenseExpireDaysRemaining(null) > 0) && (license.getLicenseText().getLicenseExpireDaysRemaining(null) < 30);
		}
		
		public String getUserRegisteredTo() {
			LicenseText lt = license.getLicenseText();
			return lt != null ? lt.getUserRegisteredTo() : null;
		}
		
		public String getCustomSignedFeature(String key) {
			LicenseText lt = license.getLicenseText();
			return lt != null ? lt.getCustomSignedFeature(key) : null;
		}
	}
}
