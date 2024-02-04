package top.cmarco.lightlogin.network;

import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.net.InetSocketAddress;

public final class NetworkUtilities {

    private NetworkUtilities() {
        throw new RuntimeException("Cannot instantiate utility class.");
    }

    public static long convertInetSocketAddressToLong(@NotNull InetSocketAddress socketAddress) {
        final InetAddress inetAddress = socketAddress.getAddress();
        final byte[] ipAddressBytes = inetAddress.getAddress();

        long ipv4Value = 0;
        for (final byte b : ipAddressBytes) {
            ipv4Value = (ipv4Value << 8) | (b & 0xFFL);
        }

        return ipv4Value;
    }
}
