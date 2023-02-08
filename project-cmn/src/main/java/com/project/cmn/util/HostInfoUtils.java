package com.project.cmn.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import lombok.Getter;

/**
 * 현재 Host의 주소를 가져온다.
 */
public enum HostInfoUtils {
	INSTANCE;

	@Getter
	private String hostName;

	@Getter
	private String hostAddr;

	private HostInfoUtils() {
		Logger log = LoggerFactory.getLogger(HostInfoUtils.class);

		try {
			hostName = InetAddress.getLocalHost().getHostName();
			hostAddr = InetAddress.getLocalHost().getHostAddress();
		} catch (UnknownHostException e) {
			log.warn(e.getLocalizedMessage());
		}
	}

	public static HostInfoUtils getInstance() {
		return INSTANCE;
	}
}
