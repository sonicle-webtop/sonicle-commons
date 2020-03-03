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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import org.apache.commons.lang3.StringUtils;

/**
 *
 * @author malbinola
 */
public class ProductLicense {
	private LicenseType licenseType;
	private ActivationLicenseType activationReturnType;
	private String productId;
	private String publicKey;
	private String customHardwareId;
	private String internalHiddenString;
	private String licenseServer;
	private String trialLicense; // license-key or license-text
	private String licenseString; // license-key or license-text
	private String licenseActivationString; // license-text
	private File licenseFile;
	private Properties properties;
	private LicenseObject validatedLicenseObject;
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final String productId, final String publicKey, final String customHardwareId, final String internalHiddenString, final String licenseServer, final String trialLicense, final File licenseFile) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productId = productId;
		this.customHardwareId = customHardwareId;
		this.internalHiddenString = internalHiddenString;
		this.licenseServer = licenseServer;
		this.trialLicense = trialLicense;
		this.publicKey = publicKey;
		this.licenseFile = licenseFile;
		this.properties = new Properties();
	}
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final String productId, final String publicKey, final String customHardwareId, final String internalHiddenString, final String licenseServer, final String trialLicense, final String licenseString) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productId = productId;
		this.customHardwareId = customHardwareId;
		this.internalHiddenString = internalHiddenString;
		this.licenseServer = licenseServer;
		this.trialLicense = trialLicense;
		this.publicKey = publicKey;
		this.licenseString = licenseString;
		this.properties = new Properties();
	}
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final AbstractProduct product, final String internalHiddenString, final String licenseServer, final String trialLicense, final String licenseString) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productId = product.getProductId();
		this.publicKey = product.getPublicKey();
		this.customHardwareId = product.getHardwareId();
		this.internalHiddenString = internalHiddenString;
		this.licenseServer = licenseServer;
		this.trialLicense = trialLicense;
		this.licenseString = licenseString;
		this.properties = new Properties();
	}
	
	public ProductLicense(final LicenseType licenseType, final ActivationLicenseType activationReturnType, final AbstractProduct product, final String licenseString) {
		this.licenseType = licenseType;
		this.activationReturnType = activationReturnType;
		this.productId = product.getProductId();
		this.publicKey = product.getPublicKey();
		this.customHardwareId = product.getHardwareId();
		this.licenseString = licenseString;
		this.properties = new Properties();
	}
	
	public String getHardwareID() {
		return HardwareID.getHardwareIDFromVolumeSerialNumber();
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
	
	public void setLicenseActivationString(String licenseActivationString) {
		this.licenseActivationString = licenseActivationString;
	}
	
	public LicenseObject getObject() {
		LicenseObject lic = internalValidate(licenseType.name(), licenseString);
		if ((lic != null) && lic.isValid() && lic.isActivationRequired()) {
			return internalValidate(activationReturnType.name(), licenseActivationString);
		}
		return lic;
	}
	
	public LicenseObject validate() {
		LicenseObject vlic = internalValidate(activationReturnType.name(), licenseActivationString);
		if (vlic == null) vlic = internalValidate(licenseType.name(), licenseString);
		return validatedLicenseObject=vlic;
	}
	
	public LicenseObject getValidatedLicenseObject() {
		return validatedLicenseObject;
	}
	
	public LicenseObject manualActivate(final String licenseActivationString) throws IOException {
		LicenseObject lic = internalValidate(licenseType.name(), licenseString);
		LicenseObject alic = null;
		if ((lic != null) && lic.isValid() && lic.isActivationRequired()) {
			this.licenseActivationString = licenseActivationString;
			alic = internalValidate(activationReturnType.name(), licenseActivationString);
			if ((alic != null) && alic.isActivated()) {
				save();
			}
		}
		return alic;
	}
	
	public LicenseObject autoActivate() throws IOException {
		LicenseObject lic = internalValidate(licenseType.name(), licenseString);
		LicenseObject alic = null;
		if ((lic != null) && lic.isValid() && lic.isActivationRequired()) {
			alic = createLicenseObject(LicenseValidator.autoActivate(lic.getLicense(), licenseServer));
			if ((alic != null) && alic.isActivated()) {
				licenseActivationString = alic.getLicenseString();
				save();
			}
		}
		return alic;
	}
	
	public LicenseObject manualDeactivate(final String licenseDeactivationString) throws IOException {
		LicenseObject alic = internalValidate(activationReturnType.name(), licenseActivationString);
		LicenseObject dlic = null;
		if ((alic != null) && alic.isValid()) {
			dlic = internalValidate(activationReturnType.name(), licenseDeactivationString);
			if ((dlic != null) && (ActivationStatus.DEACTIVATION_COMPLETED == dlic.getLicense().getActivationStatus())) {
				licenseActivationString = null;
				save();
			}
		}
		return dlic;
	}
	
	public LicenseObject autoDeactivate() throws IOException {
		LicenseObject alic = internalValidate(activationReturnType.name(), licenseActivationString);
		LicenseObject dlic = null;
		if ((alic != null) && alic.isValid()) {
			dlic = createLicenseObject(LicenseValidator.autoDeactivate(alic.getLicense(), licenseServer));
			if ((dlic != null) && (ActivationStatus.DEACTIVATION_COMPLETED == dlic.getLicense().getActivationStatus())) {
				licenseActivationString = null;
				save();
			}
		}
		return dlic;
	}
	
	public LicenseObject modify(final String modificationKey) throws IOException {
		LicenseObject alic = internalValidate(activationReturnType.name(), licenseActivationString);
		LicenseObject mlic = null;
		if ((mlic != null) && mlic.isValid()) {
			mlic = createLicenseObject(LicenseValidator.modifyLicense(alic.getLicense(), licenseServer, modificationKey));
			if (ModificationStatus.MODIFICATION_COMPLETED == alic.getLicense().getModificationStatus()) {
				licenseActivationString = mlic.getLicenseString();
				save();
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
	
	private LicenseObject internalValidate(String typeName, String licenseString) {
		if (licenseString == null) return null;
		if (customHardwareId==null) {
			License license = null;
			if (LicenseType.LICENSE_TEXT.name().equals(typeName)) {
				license = LicenseValidator.validate(licenseString, publicKey, productId, (String)null, (String)null, (Date)null, (Date)null);
			} else if (LicenseType.BASIC_KEY.name().equals(typeName)) {
				license = LicenseValidator.validate(licenseString, publicKey, internalHiddenString, (String)null, (String)null, 0);
			} else if (LicenseType.CRYPTO_KEY.name().equals(typeName)) {
				license = LicenseValidator.validate(licenseString, publicKey, productId, (String)null, (String)null, (Date)null, (Date)null);
			} 
			return createLicenseObject(license);
		}
		else return internalValidateWithCustomHardwareID(typeName, licenseString);
	}
	
	private LicenseObject internalValidateWithCustomHardwareID(String typeName, String licenseString) {
		if (licenseString == null) return null;
		License license = null;
		if (LicenseType.LICENSE_TEXT.name().equals(typeName)) {
			license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, productId, (String)null, (String)null, customHardwareId, (Date)null, (Date)null);
		} else if (LicenseType.BASIC_KEY.name().equals(typeName)) {
			license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, internalHiddenString, (String)null, (String)null, customHardwareId);
		} else if (LicenseType.CRYPTO_KEY.name().equals(typeName)) {
			license = LicenseValidator.validateWithCustomHardwareID(licenseString, publicKey, productId, (String)null, (String)null, customHardwareId, (Date)null, (Date)null);
		} 
		return createLicenseObject(license);
	}
	
	private LicenseObject createLicenseObject(License license) {
		return (license != null) ? new LicenseObject(license) : null;
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
		OFF_NO_ACTIVATION, LICENSE_TEXT, BASIC_KEY
	}
	
	public static class LicenseObject {
		private final License license;
		
		public LicenseObject(final License license) {
			this.license = license;
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
		
		public String getManualActivationRequestString() {
			return license.getManualActivationRequestString();
		}
		
		public String getManualDeactivationRequestString() {
			return license.getManualDeactivationRequestString();
		}
		
		public int getLicenseActivationDaysRemaining() {
			return license.getLicenseActivationDaysRemaining(null);
		}
		
		public int getLicenseExpireDaysRemaining() {
			LicenseText lt = license.getLicenseText();
			return (lt != null) ? lt.getLicenseExpireDaysRemaining(null) : -1;
		}
		
		public boolean isValid() {
			return ValidationStatus.LICENSE_VALID == license.getValidationStatus();
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
	}
	
	public static class Builder {
		
	}
}
