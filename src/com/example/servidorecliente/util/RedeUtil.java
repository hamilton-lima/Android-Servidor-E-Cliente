package com.example.servidorecliente.util;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

import android.util.Log;

public class RedeUtil {

	private static final String TAG = RedeUtil.class.getName();

	public static String getLocalIpAddress() {
		String result = null;

		try {
			Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces();

			while (en.hasMoreElements()) {
				NetworkInterface intf = en.nextElement();
				Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
				
				Log.i(TAG, "NI=" + intf);

				while (enumIpAddr.hasMoreElements()) {

					InetAddress inetAddress = enumIpAddr.nextElement();

					Log.i(TAG, "ADDR=" + inetAddress);

					if (!inetAddress.isLoopbackAddress()
							&& (inetAddress instanceof Inet4Address)) {
						result = inetAddress.getHostAddress().toString();
						Log.i(TAG, "RSLT=" + result);

					}
				}
			}
		} catch (SocketException ex) {
			Log.e(TAG, "erro recuperando IP atual", ex);
		}

		return result;
	}

}
