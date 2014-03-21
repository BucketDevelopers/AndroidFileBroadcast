package com.common.methods;

import org.apache.http.conn.util.InetAddressUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Collections;
import java.util.List;

public class IpAddress {


	public static String getHostIPAddress() {
		try {
			String ipv4;
			List<NetworkInterface> nilist = Collections.list(NetworkInterface
					.getNetworkInterfaces());
			if (nilist.size() > 0) {
				for (NetworkInterface ni : nilist) {
					List<InetAddress> ialist = Collections.list(ni
							.getInetAddresses());
					if (ialist.size() > 0) {
						for (InetAddress address : ialist) {
							if (!address.isLoopbackAddress()
									&& InetAddressUtils
											.isIPv4Address(ipv4 = address
													.getHostAddress())) {
								return ipv4;
							}
						}
					}

				}
			}

		} catch (SocketException ex) {
			return "IP generation Exception";
		}
		return "Not Connected to Any Network";
	}


	
	
}
