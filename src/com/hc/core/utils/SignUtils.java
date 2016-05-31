package com.hc.core.utils;

import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

import javax.crypto.Cipher;

import org.bouncycastle.jce.provider.BouncyCastleProvider;


public class SignUtils {
	private static final String DEFAULT_CHARSET = "UTF-8";
	public static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/y8wlQ763EpSJwjtb1SeDuRuYSOoGVq2nWTJwVqr2qjfYLYenXzPMjy7eh9mtTRbdU85sxyNmnT7ehgzLakxs/k3CdwV3A4SNXLn2s+tEusmGE9UUQuVSC3vakWkpSRbjvEEnLyjXUUPDXgGE9QmI8j/EXYjWT/OX3Walv9RVhQIDAQAB";
	public static final String PRIVATE_KEY = "MIICXAIBAAKBgQC/y8wlQ763EpSJwjtb1SeDuRuYSOoGVq2nWTJwVqr2qjfYLYenXzPMjy7eh9mtTRbdU85sxyNmnT7ehgzLakxs/k3CdwV3A4SNXLn2s+tEusmGE9UUQuVSC3vakWkpSRbjvEEnLyjXUUPDXgGE9QmI8j/EXYjWT/OX3Walv9RVhQIDAQABAoGANIo+jULGnt4PJMG5gwmcPoBZ59ipizz5ayuLHq0PPm0YyTaHv7jbtsDOhRVP8yJ4Df9kP/Y5Y2XWEB1Gqf6fmo1wTNy23wZzEj68WTA667mtuzty5ENN5SDuR5t8tMZxAtTpd7u2buVCiM+/hLjXqy2aozH+mm1w4iNqQHBJCMECQQD+H+GzPJmc8ILosHsVDBh235vAoej1x8+Tfq9RXugtB7rwc5UPJYRem4i71ok7H/bAqNYrbx74zG90pSOQoOlVAkEAwTYoj5lMjG+OJGARQEXiaDKRiwYdBlQ+2lSg9xzb82/kQIMrhpQxACBOkGyn2NJBrlHU/YAM3jbOdNBXJ+v7cQJAFtu5So0N6D7+F34gZCFFZhPxB5/Iu6rwcfz4OiKGUd6bIxvScZ1XTcdBwlum1mmmoWfOc/O33ntNHznnfA/tTQJAYkjklTfMYtwUmJht7h4esBaZRvlTJncQigvm3g3GLa3LlK2lJI6Z5pCIpps/PuKmGsDx8gqDgT6sA2xACcTXYQJBAIifQoGJcoVYf8ppGemGF8OjVh7gI28WG253KedhOKISMtVSnznn1NR88CE+u9Sg+SBp6Br/l0r0IiHs4vLSOb0=";

	
	public static String sign(String content, String privateKey) {
		try {
			PKCS8EncodedKeySpec priPKCS8 = new PKCS8EncodedKeySpec(Base64.decode(privateKey));
			KeyFactory keyf = KeyFactory.getInstance("RSA", "BC");
			PrivateKey priKey = keyf.generatePrivate(priPKCS8);
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
			cipher.init(Cipher.ENCRYPT_MODE, priKey);
			String signed = Base64.encode(cipher.doFinal(content.getBytes(DEFAULT_CHARSET)));
			return signed;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

}
