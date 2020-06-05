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
import com.license4j.util.FileUtils;
import com.sonicle.commons.LangUtils;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;
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
	private String licenseActivationString; // license-text
	private LicenseInfo info;
	
	private File licenseFile;
	private Properties properties;
	private LicenseInfo validatedLicenseObject;
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final String productCode, final String publicKey, final String customHardwareId, final String internalHiddenString, final String licenseServer, final String trialLicense, final File licenseFile) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productCode = productCode;
		this.customHardwareId = customHardwareId;
		this.internalHiddenString = internalHiddenString;
		this.licenseServer = licenseServer;
		this.trialLicense = trialLicense;
		this.publicKey = publicKey;
		this.licenseFile = licenseFile;
		this.properties = new Properties();
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
		this.properties = new Properties();
	}
	
	public ProductLicense(final AbstractProduct product) {
		this.licenseType = product.getLicenseType();
		this.activationReturnType = product.getActivationReturnType();
		this.productCode = product.getProductCode();
		this.publicKey = product.getPublicKey();
		this.customHardwareId = product.getHardwareId();
		this.internalHiddenString = product.getInternalHiddenString();
		this.licenseServer = product.getLicenseServer();
		this.properties = new Properties();
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
		setLicenseActivationString(null);
	}
	
	public String getLicenseActivationString() {
		return this.licenseActivationString;
	}
	
	public void setLicenseActivationString(String licenseActivationString) {
		this.licenseActivationString = licenseActivationString;
	}
	
	public void setActivationCustomHardwareId(String activationCustomHardwareId) {
		this.activationCustomHardwareId = activationCustomHardwareId;
	}
	
	/*
	public synchronized LicenseInfo validate() {
		LicenseInfo vlic = internalValidate(activationReturnType.name(), licenseActivationString);
		if (vlic == null) vlic = internalValidate(licenseType.name(), licenseString);
		return info = vlic;
	}
	*/
	
	private LicenseInfo updateLicenseInfo() {
		LicenseInfo ilic = internalValidate2(activationReturnType.name(), licenseActivationString, activationCustomHardwareId);
		if (ilic == null) ilic = internalValidate2(licenseType.name(), licenseString, customHardwareId);
		return info = ilic;
		
		//LicenseInfo ilic = internalValidate(activationReturnType.name(), licenseActivationString);
		//if (ilic == null) ilic = internalValidate(licenseType.name(), licenseString);
		//return info = ilic;
	}
	
	public synchronized LicenseInfo getLicenseInfo() {
		if (info == null) updateLicenseInfo();
		return info;
	}
	
	public synchronized LicenseInfo validate() {
		return updateLicenseInfo();
	}
	
	public synchronized LicenseInfo manualActivate(final String licenseActivationString) throws IOException {
		LicenseInfo lic = updateLicenseInfo();
		if (lic != null && lic.isValid() && lic.isActivationRequired() && !lic.isActivated()) {
			this.licenseActivationString = licenseActivationString;
			updateLicenseInfo();
			/*
			if ((info != null) && info.isActivated()) {
				save();
			}
			*/
		}
		return info;
	}
	
	public synchronized LicenseInfo autoActivate() throws IOException {
		LicenseInfo lic = updateLicenseInfo();
		if (lic != null && lic.isValid() && lic.isActivationRequired() && !lic.isActivated()) {
			LicenseInfo alic = internalActivate(lic.getLicense()); 
			if ((alic != null) && alic.isActivated()) {
				licenseActivationString = alic.getLicenseString();
				info = alic;
			} else {
				licenseActivationString = null;
				updateLicenseInfo();
			}
			//save();
		}
		return info;
	}
	
	public synchronized LicenseInfo autoDeactivate() throws IOException {
		LicenseInfo lic = updateLicenseInfo();
		if (lic != null && lic.isValid() && lic.isActivated()) {
			LicenseInfo dlic = internalDeactivate(lic.getLicense()); 
			if ((dlic != null) && (ActivationStatus.DEACTIVATION_COMPLETED == dlic.getLicense().getActivationStatus())) {
				licenseActivationString = null;
				info = dlic;
			} else {
				updateLicenseInfo();
			}
			//save();
		}
		return info;
	}
	
	public synchronized LicenseInfo manualDeactivate(final String licenseDeactivationString) throws IOException {
		LicenseInfo lic = updateLicenseInfo();
		if (lic != null && lic.isValid() && lic.isActivated()) {
			LicenseInfo dlic = internalValidate2(activationReturnType.name(), licenseDeactivationString, activationCustomHardwareId);
			//LicenseInfo dlic = internalValidate(activationReturnType.name(), licenseDeactivationString);
			if ((dlic != null) && (ActivationStatus.DEACTIVATION_COMPLETED == dlic.getLicense().getActivationStatus())) {
				licenseActivationString = null;
			}
			updateLicenseInfo();
			//save();
		}
		return info;
	}
	
	public int queryTrackingInfo(HashMap<String, String> map, final int timeout) {
		LicenseInfo licInfo = updateLicenseInfo();
		if (licInfo == null) return -3;
		return LicenseValidator.queryLicenseUseTrackingInfo(publicKey, licInfo.getLicense(), map, licenseServer, timeout);
	}
	
	
	
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
	
	private LicenseInfo internalValidate(String typeName, String licenseString) {
		if (licenseString == null) return null;
		if (customHardwareId == null) {
			return internalValidateWithDefaultHardwareID(typeName, licenseString);
		} else {
			return internalValidateWithCustomHardwareID(typeName, licenseString);
		}
	}
	
	private LicenseInfo internalValidate2(String typeName, String licenseString, String hardwareId) {
		if (licenseString == null) return null;
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
	
	private LicenseInfo internalValidateWithDefaultHardwareID(String typeName, String licenseString) {
		License license = null;
		if (LicenseType.LICENSE_TEXT.name().equals(typeName)) {
			license = LicenseValidator.validate(licenseString, publicKey, productCode, (String)null, (String)null, (Date)null, (Date)null);
		} else if (LicenseType.BASIC_KEY.name().equals(typeName) || LicenseType.CRYPTO_KEY.name().equals(typeName)) {
			license = LicenseValidator.validate(licenseString, publicKey, internalHiddenString, (String)null, (String)null, 0);
		}
		return createLicenseInfo(license);
	}
	
	private LicenseInfo internalValidateWithCustomHardwareID(String typeName, String licenseString) {
		License license = null;
		if (LicenseType.LICENSE_TEXT.name().equals(typeName)) {
			license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, productCode, (String)null, (String)null, customHardwareId, (Date)null, (Date)null);
		} else if (LicenseType.BASIC_KEY.name().equals(typeName) || LicenseType.CRYPTO_KEY.name().equals(typeName)) {
			license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, internalHiddenString, (String)null, (String)null, customHardwareId);
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
	
	private LicenseInfo createLicenseInfo(License license) {
		return (license != null) ? new LicenseInfo(productCode, license) : null;
	}
	
	/*
	private License internalValidate2(boolean activated) {
		//String targetLicenseString = activated ? activatedLicenseString : licenseString;
		String targetLicenseString = licenseString;
		if (LicenseType.LICENSE_TEXT.equals(licenseType)) {
			return LicenseValidator.validate(targetLicenseString, publicKey, productId, (String)null, (String)null, (Date)null, (Date)null);
		} else if (LicenseType.BASIC_KEY.equals(licenseType)) {
			return LicenseValidator.validate(targetLicenseString, publicKey, internalHiddenString, (String)null, (String)null, 0);
		} else if (LicenseType.CRYPTO_KEY.equals(licenseType)) {
			return LicenseValidator.validate(targetLicenseString, publicKey, productId, (String)null, (String)null, (Date)null, (Date)null);
		} else {
			return null;
		}
	}
	*/
	
	public static enum LicenseType {
		LICENSE_TEXT, BASIC_KEY, CRYPTO_KEY, DUMMY
	}
	
	public static enum ActivationLicenseType {
		OFF_NO_ACTIVATION, LICENSE_TEXT, CODE
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
		
		public int getActivationDaysRemaining() {
			return license.getLicenseActivationDaysRemaining(null);
		}
		
		public int getExpireDaysRemaining() {
			LicenseText lt = license.getLicenseText();
			return (lt != null) ? lt.getLicenseExpireDaysRemaining(null) : -1;
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
		
		public boolean isValid() {
			return ValidationStatus.LICENSE_VALID == license.getValidationStatus();
		}
		
		public boolean isExpired() {
			return ValidationStatus.LICENSE_EXPIRED == license.getValidationStatus();
		}

		public boolean isActivated() {
			return (ValidationStatus.LICENSE_VALID == license.getValidationStatus()) && license.isActivationCompleted();
		}
		
		public boolean isDeactivated() {
			return (ValidationStatus.LICENSE_VALID == license.getValidationStatus()) && (ActivationStatus.DEACTIVATION_COMPLETED == license.getActivationStatus());
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
		
		public Integer getUsersNo() {
			LicenseText lt = license.getLicenseText();
			return lt != null ? LangUtils.value(lt.getCustomSignedFeature("usersNo"), (Integer)null) : null;
		}
	}
	
	public static class Builder {
		
	}
}
