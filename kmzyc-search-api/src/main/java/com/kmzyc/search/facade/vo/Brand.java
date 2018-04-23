package com.kmzyc.search.facade.vo;

import java.io.Serializable;
import java.util.List;

public class Brand implements Serializable
{

	public int getBrandId()
	{
		return brandId;
	}

	public void setBrandId(int brandId)
	{
		this.brandId = brandId;
	}

	public String getBrandName()
	{
		return brandName;
	}

	public void setBrandName(String brandName)
	{
		this.brandName = brandName;
	}

	public String getNation()
	{
		return nation;
	}

	public void setNation(String nation)
	{
		this.nation = nation;
	}

	public String getLogoPath()
	{
		return logoPath;
	}

	public void setLogoPath(String logoPath)
	{
		this.logoPath = logoPath;
	}

	public String getEngName()
	{
		return engName;
	}

	public void setEngName(String engName)
	{
		this.engName = engName;
	}

	public String getChnSpell()
	{
		return chnSpell;
	}

	public void setChnSpell(String chnSpell)
	{
		this.chnSpell = chnSpell;
	}

	public String getHomePage()
	{
		return homePage;
	}

	public void setHomePage(String homePage)
	{
		this.homePage = homePage;
	}

	public String getDes()
	{
		return des;
	}

	public void setDes(String des)
	{
		this.des = des;
	}

	public boolean isValid()
	{
		return valid;
	}

	public void setValid(boolean valid)
	{
		this.valid = valid;
	}

	public String getContactInfo()
	{
		return contactInfo;
	}

	public void setContactInfo(String contactInfo)
	{
		this.contactInfo = contactInfo;
	}

	public String getPavilionPicPath()
	{
		return pavilionPicPath;
	}

	public void setPavilionPicPath(String pavilionPicPath)
	{
		this.pavilionPicPath = pavilionPicPath;
	}

	public String getIntroduceFilePath()
	{
		return introduceFilePath;
	}

	public void setIntroduceFilePath(String introduceFilePath)
	{
		this.introduceFilePath = introduceFilePath;
	}

	public String getCertificateHonor()
	{
		return certificateHonor;
	}

	public void setCertificateHonor(String certificateHonor)
	{
		this.certificateHonor = certificateHonor;
	}

	public List<String> getContactList()
	{
		return contactList;
	}

	public void setContactList(List<String> contactList)
	{
		this.contactList = contactList;
	}

	public List<String> getIntroducePics()
	{
		return introducePics;
	}

	public void setIntroducePics(List<String> introducePics)
	{
		this.introducePics = introducePics;
	}

	public List<String> getHonorList()
	{
		return honorList;
	}

	public void setHonorList(List<String> honorList)
	{
		this.honorList = honorList;
	}

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 8256378814994676711L;

	private int					brandId;
	private String				brandName;
	private String				nation;
	private String				logoPath;
	private String				engName;
	private String				chnSpell;
	private String				homePage;
	private String				des;
	private boolean				valid;
	private String				contactInfo;
	private String				pavilionPicPath;
	private String				introduceFilePath;
	private String				certificateHonor;

	private List<String>		contactList;
	private List<String>		introducePics;
	private List<String>		honorList;

}
