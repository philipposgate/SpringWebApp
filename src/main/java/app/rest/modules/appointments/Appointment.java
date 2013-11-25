package app.rest.modules.appointments;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import app.AbstractEntity;

@Entity
@Table(name = "appointment")
public class Appointment extends AbstractEntity
{
	@Column(nullable = false)
	private Date dateCreated;

	@Column(unique = true, nullable = false)
	private String confirmationCode;

	@Column(nullable = false)
	private String customerName;

	@Column
	private String customerPhone;

	@Column
	private String customerEmail;

	@Column
	private Date apptStart;

	@Column
	private Date apptEnd;

	@Column
	private int unitAmount;

	@Column
	private String locationCode;

	@Column
	private String locAddress;

	@Column
	private String locCity;

	@Column(columnDefinition = "TEXT")
	private String customerMessage;

	@Column
	private String googleEventId;

	public String getGoogleEventId()
	{
		return googleEventId;
	}

	public void setGoogleEventId(String googleEventId)
	{
		this.googleEventId = googleEventId;
	}

	public String getConfirmationCode() {
		return confirmationCode;
	}

	public void setConfirmationCode(String confirmationCode) {
		this.confirmationCode = confirmationCode;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public String getCustomerPhone() {
		return customerPhone;
	}

	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	public String getCustomerEmail() {
		return customerEmail;
	}

	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	public Date getApptStart() {
		return apptStart;
	}

	public void setApptStart(Date apptStart) {
		this.apptStart = apptStart;
	}

	public Date getApptEnd() {
		return apptEnd;
	}

	public void setApptEnd(Date apptEnd) {
		this.apptEnd = apptEnd;
	}

	public int getUnitAmount() {
		return unitAmount;
	}

	public void setUnitAmount(int unitAmount) {
		this.unitAmount = unitAmount;
	}

	public String getLocationCode() {
		return locationCode;
	}

	public void setLocationCode(String locationCode) {
		this.locationCode = locationCode;
	}

	public String getLocAddress() {
		return locAddress;
	}

	public void setLocAddress(String locAddress) {
		this.locAddress = locAddress;
	}

	public String getLocCity() {
		return locCity;
	}

	public void setLocCity(String locCity) {
		this.locCity = locCity;
	}

	public Date getDateCreated() {
		return dateCreated;
	}

	public void setDateCreated(Date dateCreated) {
		this.dateCreated = dateCreated;
	}

	public String getCustomerMessage() {
		return customerMessage;
	}

	public void setCustomerMessage(String customerMessage) {
		this.customerMessage = customerMessage;
	}

}
