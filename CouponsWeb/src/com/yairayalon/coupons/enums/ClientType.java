package com.yairayalon.coupons.enums;

import com.yairayalon.couponsservercommon.exceptions.ApplicationException;

// enum used for client types
public enum ClientType {

	ADMIN(1),
	COMPANY(2),
	CUSTOMER(3);

	private int clientTypeId;

	ClientType(int clientTypeId){
		this.clientTypeId = clientTypeId;
	}

	public int getClientTypeId() {
		return clientTypeId;
	}

	public static ClientType getClientTypeByClientTypeId(int clientTypeId) throws ApplicationException {
		for(ClientType clientType : ClientType.values()) {
			if (clientType.getClientTypeId() == clientTypeId) {
				return clientType;
			}
		}
		return null;
	}

}